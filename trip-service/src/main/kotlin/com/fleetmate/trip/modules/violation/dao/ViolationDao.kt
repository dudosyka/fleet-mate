package com.fleetmate.trip.modules.violation.dao


import com.fleetmate.lib.shared.modules.violation.model.ViolationModel
import com.fleetmate.lib.utils.database.BaseIntEntity
import com.fleetmate.lib.utils.database.BaseIntEntityClass
import com.fleetmate.lib.utils.database.idValue
import com.fleetmate.trip.modules.trip.data.dao.TripDao
import com.fleetmate.trip.modules.violation.dto.ViolationDto
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.update

class ViolationDao(id: EntityID<Int>) : BaseIntEntity<ViolationDto>(id, ViolationModel) {
    companion object : BaseIntEntityClass<ViolationDto, ViolationDao>(ViolationModel) {
        fun speedingRegistered(trip: TripDao) = find(
            (ViolationModel.driver eq trip.driver.id) and
            (ViolationModel.car eq trip.car.id) and
            (ViolationModel.trip eq trip.id) and
            (ViolationModel.hidden eq true)
        ).firstOrNull()
    }

    var type by ViolationModel.type
    var registeredAt by ViolationModel.registeredAt
    var duration by ViolationModel.duration
    var hidden by ViolationModel.hidden
    var driverId by ViolationModel.driver
    var tripId by ViolationModel.trip
    var carId by ViolationModel.car
    var comment by ViolationModel.comment

    override fun toOutputDto(): ViolationDto =
        ViolationDto(
            idValue, type, duration, driverId.value,
            tripId.value, carId?.value, comment
        )
    fun updateByDuration(newDuration: Long) {
        ViolationModel.update({ ViolationModel.id eq idValue }) {
            it[duration] = newDuration
            it[hidden] = false
        }
    }
}