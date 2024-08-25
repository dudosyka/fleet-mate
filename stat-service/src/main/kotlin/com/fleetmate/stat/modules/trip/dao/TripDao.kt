package com.fleetmate.stat.modules.trip.dao


import com.fleetmate.lib.shared.modules.trip.model.TripModel
import com.fleetmate.lib.shared.modules.violation.model.ViolationModel
import com.fleetmate.lib.utils.database.BaseIntEntity
import com.fleetmate.lib.utils.database.BaseIntEntityClass
import com.fleetmate.lib.utils.database.idValue
import com.fleetmate.stat.modules.car.dao.CarDao
import com.fleetmate.stat.modules.trip.dto.TripDto
import com.fleetmate.stat.modules.trip.dto.TripListCarDto
import com.fleetmate.stat.modules.trip.dto.TripListDriverDto
import com.fleetmate.stat.modules.trip.dto.TripListItemDto
import com.fleetmate.stat.modules.trip.dto.TripSimpleDto
import com.fleetmate.stat.modules.user.dao.UserDao
import com.fleetmate.stat.modules.violation.dao.ViolationDao
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedIterable

class TripDao(id: EntityID<Int>) : BaseIntEntity<TripDto>(id, TripModel) {
    companion object : BaseIntEntityClass<TripDto, TripDao>(TripModel){
        fun violations(
            idValue: Int,
        ): SizedIterable<ViolationDao> =
            ViolationDao.find {
                (ViolationModel.trip eq idValue)
            }
    }

    val carId by TripModel.car
    val car by CarDao referencedOn TripModel.car
    val driverId by TripModel.driver
    val driver by UserDao referencedOn TripModel.driver
    val status by TripModel.status
    val route by TripModel.route
    val avgSpeed by TripModel.avgSpeed
    val mileage by TripModel.mileage
    val keyAcceptance by TripModel.keyAcceptance
    val driverCheckBeforeTripId by TripModel.driverCheckBeforeTrip
    val driverCheckAfterTripId by TripModel.driverCheckAfterTrip
    val mechanicCheckBeforeTripId by TripModel.mechanicCheckBeforeTrip
    val mechanicCheckAfterTripId by TripModel.mechanicCheckAfterTrip
    val keyReturn by TripModel.keyReturn
    val needWashing by TripModel.needWashing
    val needRefuel by TripModel.needRefuel

    override fun toOutputDto(): TripDto =
        TripDto(
            idValue, carId.value, driverId.value, status,
            route, avgSpeed, mileage, keyAcceptance, driverCheckBeforeTripId?.value,
            driverCheckAfterTripId?.value, mechanicCheckBeforeTripId?.value,
            mechanicCheckAfterTripId?.value, keyReturn, needWashing, needRefuel
        )

    val simpleDto: TripSimpleDto get() =
        TripSimpleDto(idValue, route, keyAcceptance, keyReturn ?: 0L)

    val listItemDto: TripListItemDto get() =
        TripListItemDto(idValue, status, keyAcceptance, keyReturn, driver.simpleDto, car.simpleDto)

    val listDriverDto: TripListDriverDto get() =
        TripListDriverDto(idValue, keyAcceptance, keyReturn, car.simpleDto, car.typeId.value, mileage)

    val listCarDto: TripListCarDto get() =
        TripListCarDto(idValue, keyAcceptance, keyReturn, driver.fullName, mileage)
}