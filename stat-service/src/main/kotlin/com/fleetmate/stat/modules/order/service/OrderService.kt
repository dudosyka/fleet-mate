package com.fleetmate.stat.modules.order.service


import com.fleetmate.lib.exceptions.ForbiddenException
import com.fleetmate.lib.shared.conf.AppConf
import com.fleetmate.lib.shared.dto.StatusDto
import com.fleetmate.lib.shared.modules.car.model.CarModel
import com.fleetmate.lib.shared.modules.car.model.type.CarTypeModel
import com.fleetmate.lib.shared.modules.fault.model.FaultModel
import com.fleetmate.lib.shared.modules.user.model.UserModel
import com.fleetmate.lib.shared.modules.wash.model.WashModel
import com.fleetmate.lib.utils.database.idValue
import com.fleetmate.lib.utils.kodein.KodeinService
import com.fleetmate.stat.modules.car.dao.CarDao
import com.fleetmate.stat.modules.fault.dao.FaultDao
import com.fleetmate.stat.modules.order.data.dao.OrderDao
import com.fleetmate.stat.modules.order.data.dao.WashDao
import com.fleetmate.stat.modules.order.data.dao.WorkDao
import com.fleetmate.stat.modules.order.data.dao.WorkTypeDao
import com.fleetmate.stat.modules.order.data.dao.WorkTypeDao.Companion.likeCond
import com.fleetmate.stat.modules.order.data.dao.WorkTypeDao.Companion.nullableRangeCond
import com.fleetmate.stat.modules.order.data.dao.WorkTypeDao.Companion.rangeCond
import com.fleetmate.stat.modules.order.data.dto.WashDto
import com.fleetmate.stat.modules.order.data.dto.order.*
import com.fleetmate.stat.modules.order.data.dto.work.CreateWorkDto
import com.fleetmate.stat.modules.order.data.model.OrderModel
import com.fleetmate.stat.modules.order.data.model.WorkActorsModel
import com.fleetmate.stat.modules.user.dao.UserDao
import com.fleetmate.stat.modules.user.dto.MechanicWorkListItemDto
import com.fleetmate.stat.modules.user.model.UserHoursModel
import io.ktor.util.date.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.DI

class OrderService(di: DI) : KodeinService(di) {
    fun getAllStatuses(): List<StatusDto> =
        AppConf.OrderStatus.entries.map {
            StatusDto(it.id, it.name)
        }

    fun <T> getAllAs(orderFilterDto: OrderFilterDto, columnSet: List<Column<*>>, processor: ( input: ResultRow ) -> T): List<T> {
        return OrderModel
            .join(UserModel, JoinType.INNER, UserModel.id, OrderModel.mechanic)
            .innerJoin(FaultModel)
            .innerJoin(CarModel)
            .innerJoin(CarTypeModel)
            .select(columnSet)
            .where {
                likeCond(orderFilterDto.orderNumber, OrderModel.id neq 0, OrderModel.number) and
                rangeCond(orderFilterDto.startDateRange, OrderModel.id neq 0, OrderModel.startedAt, Long.MIN_VALUE, Long.MAX_VALUE) and
                nullableRangeCond(orderFilterDto.endDateRange, OrderModel.id neq 0, OrderModel.closedAt, Long.MIN_VALUE, Long.MAX_VALUE) and
                with(orderFilterDto) { createStatusFilterCond(status) } and
                likeCond(orderFilterDto.juniorMechanicFilter?.fullName, OrderModel.id neq 0, OrderModel._juniorMechanicFilterSimplifier) and
                likeCond(orderFilterDto.mechanicFilter?.fullName, OrderModel.id neq 0, UserModel.fullName)
            }
            .map(processor)
    }

    //FIXME: Dao couldn`t handle nested where clauses (should be rewrote via Model API)
    fun getAllFiltered(orderFilterDto: OrderFilterDto): List<OrderListItemDto> = transaction {
        getAllAs(orderFilterDto, OrderModel.columns +
            listOf(CarModel.id, CarModel.name, CarModel.registrationNumber, CarTypeModel.name) +
            listOf(UserModel.id, UserModel.fullName)) {
                val orderDao = OrderDao.wrapRow(it)
                val carDao = CarDao.wrapRow(it)
                val mechanicDao = UserDao.wrapRow(it)
                OrderListItemDto(
                    orderDao.idValue,
                    orderDao.number,
                    orderDao.startedAt,
                    orderDao.closedAt,
                    mechanicDao.simpleDto,
                    orderDao.hours,
                    carDao.simpleDto(it[CarTypeModel.name])
                )
            }
    }

    /*
     id, Номер наряда, Дата начала, Дата завершения,
      Механик, Часы, ТС (Название, Название типа, госномер)
     */

    fun getOne(orderId: Int): OrderOutputDto = transaction {
        OrderDao[orderId].fullOutputDto
    }

    fun getOrderWorkList(orderId: Int): List<OrderWorkDto> = transaction {
        OrderDao[orderId].works.map { it.orderWorkDto }
    }


    //FIXME: Check that mechanicId user has mechanic role
    fun create(createOrderDto: CreateOrderDto): OrderDto = transaction {
        var fault = FaultDao[createOrderDto.faultId]

        if (fault.status != AppConf.FaultStatus.CREATED.name)
            throw ForbiddenException()

        val order = OrderDao.new {
            number = createOrderDto.number
            status = AppConf.OrderStatus.CREATED.name
            mechanic = UserDao[createOrderDto.mechanicId]
            fault = FaultDao[createOrderDto.faultId]
            startedAt = getTimeMillis()
        }

        fault.critical = true
        fault.status = AppConf.FaultStatus.UNDER_WORK.name
        fault.flush()

        order.toOutputDto()
    }

    fun appendWork(createWorkDto: CreateWorkDto): OrderWorkDto = transaction {
        val order = OrderDao[createWorkDto.orderId]

        val work = WorkDao.new {
            type = WorkTypeDao[createWorkDto.workType]
            this.order = order
        }

        WorkActorsModel.batchInsert(createWorkDto.actors) {
            this[WorkActorsModel.work] = work.idValue
            this[WorkActorsModel.actor] = it
            this[WorkActorsModel.order] = order.idValue
        }

        work.orderWorkDto
    }

    fun close(orderId: Int): OrderDto = transaction {
        val order = OrderDao[orderId]

        order.updateByStatus(AppConf.OrderStatus.CLOSED)
        order.updateFaultByStatus(AppConf.FaultStatus.FIXED)
        order.updateFaultByCritical(false)

        order.fault.flush()
        order.flush()

        val actors = order.works.map { it.actors }.flatten().map { it.id }
        val dividedHours = order.hours / actors.size

        UserHoursModel.batchInsert(actors) {
            this[UserHoursModel.hours] = dividedHours
            this[UserHoursModel.user] = it
        }

        WorkActorsModel.update({ WorkActorsModel.order eq orderId }) {
            it[closed] = true
        }

        order.toOutputDto()
    }

    //FIXME:
    // Cannot join with com.fleetmate.stat.modules.order.data.model.OrderModel@a2e7c09b as there is multiple primary key <-> foreign key references.
    // com.fleetmate.stat.modules.order.data.model.OrderModel.id -> com.fleetmate.stat.modules.order.data.model.WorkActorsModel.order, com.fleetmate.stat.modules.order.data.model.WorkModel.order
    // 22:02:18.324 [eventLoopGroupProxy-4-2] DEBUG com.fleetmate.ExceptionFilter -- Stacktrace => java.lang.IllegalStateException: Cannot join with com.fleetmate.stat.modules.order.data.model.OrderModel@a2e7c09b as there is multiple primary key <-> foreign key references.
    // com.fleetmate.stat.modules.order.data.model.OrderModel.id -> com.fleetmate.stat.modules.order.data.model.WorkActorsModel.order, com.fleetmate.stat.modules.order.data.model.WorkModel.order
    fun getWorkListByJuniorMechanic(juniorMechanicId: Int): List<MechanicWorkListItemDto> = transaction {
        WorkDao.getByJuniorMechanic(juniorMechanicId)
    }

    fun getWashListByWasher(washerId: Int): List<WashDto> = transaction {
        WashDao.find { WashModel.author eq washerId }.map { it.toOutputDto() }
    }

}