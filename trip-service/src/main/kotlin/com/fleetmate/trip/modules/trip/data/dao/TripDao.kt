package com.fleetmate.trip.modules.trip.data.dao

import com.fleetmate.lib.exceptions.NotFoundException
import com.fleetmate.lib.shared.conf.AppConf
import com.fleetmate.lib.shared.modules.car.model.CarModel
import com.fleetmate.lib.shared.modules.refuel.model.RefuelModel
import com.fleetmate.lib.shared.modules.trip.model.TripModel
import com.fleetmate.lib.shared.modules.user.model.UserModel
import com.fleetmate.lib.shared.modules.violation.model.ViolationModel
import com.fleetmate.lib.shared.modules.wash.model.WashModel
import com.fleetmate.lib.utils.database.BaseIntEntity
import com.fleetmate.lib.utils.database.BaseIntEntityClass
import com.fleetmate.lib.utils.database.idValue
import com.fleetmate.trip.modules.car.data.dao.CarDao
import com.fleetmate.trip.modules.refuel.dao.RefuelDao
import com.fleetmate.trip.modules.report.dto.ReportDto
import com.fleetmate.trip.modules.report.dto.ReportListItemDto
import com.fleetmate.trip.modules.trip.data.dto.TripDto
import com.fleetmate.trip.modules.user.dao.UserDao
import com.fleetmate.trip.modules.violation.dao.ViolationDao
import com.fleetmate.trip.modules.violation.dto.ViolationDto
import com.fleetmate.trip.modules.wash.dao.WashDao
import io.ktor.util.date.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.update

class TripDao(id: EntityID<Int>) : BaseIntEntity<TripDto>(id, TripModel) {
    companion object: BaseIntEntityClass<TripDto, TripDao>(TripModel) {
        fun init(carId: Int, driverId: Int, needRefuel: Boolean) =
            new {
                this.carId = EntityID(carId, CarModel)
                this.driverId = EntityID(driverId, UserModel)
                keyAcceptance = getTimeMillis()
                status = AppConf.TripStatus.INITIALIZED.name
                this.needRefuel = needRefuel
            }

        fun getUserActiveTrip(driverId: Int) =
            wrapRow(
                TripModel.getUserActiveTrip(driverId) ?: throw NotFoundException("Active trip not found")
            )

        fun getCarActiveTrip(carId: Int) =
            wrapRow(
                TripModel.getCarActiveTrip(carId) ?: throw NotFoundException("Active trip not found")
            )

        fun driverTripsByTheLastMonth(userId: Int): List<TripDao> =
            TripDao.find {
                (TripModel.driver eq userId) and
                (TripModel.keyAcceptance lessEq (getTimeMillis() - 30L*24*60*60*1000)) and
                (TripModel.keyReturn.isNotNull())
            }.toList()
    }

    private var carId by TripModel.car
    val car by CarDao referencedOn TripModel.car

    private var driverId by TripModel.driver
    var driver by UserDao referencedOn TripModel.driver

    var status by TripModel.status
    var keyAcceptance by TripModel.keyAcceptance
    var keyReturn by TripModel.keyReturn
    var needRefuel by TripModel.needRefuel
    var needWashing by TripModel.needWashing
    var mileage by TripModel.mileage
    val route by TripModel.route
    val avgSpeed by TripModel.avgSpeed
    val driverCheckBeforeTrip by TripModel.driverCheckBeforeTrip
    val driverCheckAfterTrip by TripModel.driverCheckAfterTrip
    val mechanicCheckBeforeTrip by TripModel.mechanicCheckBeforeTrip
    val mechanicCheckAfterTrip by TripModel.mechanicCheckAfterTrip

    val isWashed: Boolean get() =
        !WashDao.find { WashModel.trip eq idValue }.empty()

    val isRefueled: Boolean get() =
        !RefuelDao.find { RefuelModel.trip eq idValue }.empty()

    val canBeClosed: Boolean get() =
        status == AppConf.TripStatus.EXPLOITATION.name &&
        listOf(driverCheckBeforeTrip, driverCheckAfterTrip, mechanicCheckBeforeTrip, mechanicCheckAfterTrip).all { it != null }

    val violationsCount: Int get() =
        ViolationDao.count(ViolationModel.trip eq idValue).toInt()

    val violations: List<ViolationDto> get() =
        ViolationDao.find { ViolationModel.trip eq idValue }.toList().map { it.toOutputDto() }

    override fun toOutputDto(): TripDto =
        TripDto(
            idValue,
            createdAt.toEpochSecond(AppConf.zoneOffset),
            car.idValue,
            driver.idValue,
            status, keyAcceptance, needRefuel
        )

    val reportDto: ReportDto get() =
        ReportDto(
            idValue, keyAcceptance, keyReturn ?: 0L, route, car.prettyName, violations, avgSpeed
        )

    val reportListItemDto: ReportListItemDto get() =
        ReportListItemDto(
            idValue, keyAcceptance, keyReturn ?: 0L, route, car.prettyName, violationsCount
        )
    fun updateCarByMileageAndFuelLevel(newMileage: Double, newFuelLevel: Double) {
        CarModel.update({CarModel.id eq carId}) {
            it[mileage] = newMileage
            it[fuelLevel] = newFuelLevel
        }
    }
}
