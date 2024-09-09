package com.fleetmate.stat.modules.fault.service


import com.fleetmate.lib.shared.conf.AppConf
import com.fleetmate.lib.shared.dto.StatusDto
import com.fleetmate.lib.shared.modules.car.model.CarModel
import com.fleetmate.lib.shared.modules.car.model.part.CarPartModel
import com.fleetmate.lib.shared.modules.car.model.type.CarTypeModel
import com.fleetmate.lib.shared.modules.fault.model.FaultModel
import com.fleetmate.lib.shared.modules.trip.model.TripModel
import com.fleetmate.lib.shared.modules.user.model.UserModel
import com.fleetmate.lib.utils.database.idValue
import com.fleetmate.lib.utils.kodein.KodeinService
import com.fleetmate.stat.modules.car.dao.CarDao
import com.fleetmate.stat.modules.car.dao.CarTypeDao.Companion.rangeCond
import com.fleetmate.stat.modules.car.dto.CarFilterDto
import com.fleetmate.stat.modules.fault.dao.FaultDao
import com.fleetmate.stat.modules.fault.dto.FaultFilterDto
import com.fleetmate.stat.modules.user.dto.driver.DriverFaultListItemDto
import com.fleetmate.stat.modules.fault.dto.FaultListItemDto
import com.fleetmate.stat.modules.fault.dto.FaultOutputDto
import com.fleetmate.stat.modules.order.data.model.OrderModel
import com.fleetmate.stat.modules.order.service.OrderService
import com.fleetmate.stat.modules.user.dto.filter.StaffFilterDto
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.DI
import org.kodein.di.instance

class FaultService(di: DI) : KodeinService(di) {
    private val orderService: OrderService by instance()

    fun getAllStatuses(): List<StatusDto> =
        AppConf.FaultStatus.entries.map {
            StatusDto(it.id, it.name)
        }

    fun getAllFiltered(faultFilterDto: FaultFilterDto): List<FaultListItemDto> = transaction {
        val correctOrders = if (faultFilterDto.orderFilter != null)
            orderService.getAllAs(faultFilterDto.orderFilter, listOf(OrderModel.id)) {
                it[OrderModel.id].value
            } else null

        //We must set INNER join when the order filter is provided to cut off the faults whose orders are not correct
        val joinType = if (faultFilterDto.orderFilter != null)
            JoinType.INNER
        else
            JoinType.LEFT

        FaultModel
            .join(OrderModel, joinType, OrderModel.fault, FaultModel.id) {
                if (faultFilterDto.orderFilter != null)
                    (OrderModel.id inList (correctOrders ?: listOf()))
                else
                    OrderModel.id neq 0
            }
            .join(CarModel, JoinType.INNER, CarModel.id, FaultModel.car) {
                with(faultFilterDto.carFilter ?: CarFilterDto()) { expressionBuilder }
            }
            .innerJoin(CarTypeModel)
            .join(UserModel, JoinType.INNER, UserModel.id, FaultModel.author) {
                with(faultFilterDto.authorFilter ?: StaffFilterDto()) { expressionBuilder }
            }
            .select(
                FaultModel.id, FaultModel.status, FaultModel.timestamp,
                OrderModel.number,
                CarModel.id, CarModel.name, CarModel.registrationNumber,
                CarTypeModel.name
            )
            .where {
                with(faultFilterDto) { statusFilterCond } and
                rangeCond(faultFilterDto.createdDateRange(), FaultModel.id neq 0, FaultModel.timestamp, Long.MIN_VALUE, Long.MAX_VALUE)
            }
            .map {
                val faultDao = FaultDao.wrapRow(it)
                val carDao = CarDao.wrapRow(it)
                FaultListItemDto(
                    faultDao.idValue,
                    it[OrderModel.number],
                    faultDao.status,
                    faultDao.timestamp,
                    carDao.simpleDto(it[CarTypeModel.name])
                )
            }
    }

    fun getOne(faultId: Int): FaultOutputDto = transaction {
        FaultDao[faultId].fullOutputDto
    }

    fun getByDriver(driverId: Int): List<DriverFaultListItemDto> = transaction {
        FaultModel
            .leftJoin(OrderModel)
            .innerJoin(CarModel)
            .join(TripModel, JoinType.LEFT, FaultModel.trip, TripModel.id)
            .join(CarPartModel, JoinType.INNER, FaultModel.carPart, CarPartModel.id)
            .select(
                FaultModel.id, FaultModel.status, OrderModel.number, CarPartModel.name, TripModel.driver
            )
            .where {
                (FaultModel.author eq driverId) or (TripModel.driver eq driverId)
            }
            .map {
                DriverFaultListItemDto(
                    it[FaultModel.id].value,
                    it[OrderModel.number],
                    it[FaultModel.status],
                    it[CarPartModel.name]
                )
            }
    }
}