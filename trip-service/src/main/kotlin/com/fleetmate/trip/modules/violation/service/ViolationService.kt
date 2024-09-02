package com.fleetmate.trip.modules.violation.service


import com.fleetmate.lib.shared.conf.AppConf
import com.fleetmate.lib.utils.kodein.KodeinService
import com.fleetmate.trip.modules.trip.data.dao.TripDao
import com.fleetmate.trip.modules.violation.dao.ViolationDao
import com.fleetmate.trip.modules.violation.dto.ViolationDto
import io.ktor.util.date.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.DI

class ViolationService(di: DI) : KodeinService(di) {
    private fun registerViolation(trip: TripDao, type: AppConf.ViolationType, comment: String?, registeredAt: Long, hidden: Boolean): ViolationDto = transaction {
        val violation = ViolationDao.new {
            this.type = type.name
            this.registeredAt = registeredAt
            this.hidden = hidden
            driverId = trip.driver.id
            tripId = trip.id
            carId = trip.car.id
            this.comment = comment
        }

        violation.toOutputDto()
    }

    fun registerRefuelViolation(trip: TripDao) {
        registerViolation(trip, AppConf.ViolationType.REFUEL, null, getTimeMillis(), false)
    }

    fun registerWashViolation(trip: TripDao) {
        registerViolation(trip, AppConf.ViolationType.WASHING, null, getTimeMillis(), false)

    }

    fun updateSpeedingViolation(trip: TripDao, speed: Double) {
        val speeding = ViolationDao.speedingRegistered(trip)
        if (speeding == null) {
            registerViolation(trip, AppConf.ViolationType.SPEEDING, "", 0, true)
            return
        }

        val currentTime = getTimeMillis()

        if (speed <= trip.car.speedLimit) {
            if (speeding.registeredAt < trip.car.speedError)
                speeding.delete()
            else {
                speeding.updateByDuration(currentTime - speeding.registeredAt)
            }
            speeding.flush()
        }
    }
}