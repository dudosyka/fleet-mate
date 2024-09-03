package com.fleetmate.trip.modules.wash.service

import com.fleetmate.lib.shared.modules.auth.dto.AuthorizedUser
import com.fleetmate.lib.shared.modules.user.model.UserModel
import com.fleetmate.lib.utils.kodein.KodeinService
import com.fleetmate.trip.modules.trip.data.dao.TripDao
import com.fleetmate.trip.modules.wash.dao.WashDao
import com.fleetmate.trip.modules.wash.dto.WashDto
import io.ktor.util.date.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.DI

class WashService(di: DI) : KodeinService(di) {
    fun registerWash(authorizedUser: AuthorizedUser, carId: Int): WashDto = transaction {
        val trip = TripDao.getCarActiveTrip(carId)

        val wash = WashDao.new {
            authorId = EntityID(authorizedUser.id, UserModel)
            tripId = trip.id
            timestamp = getTimeMillis()
        }
        wash.flush()

        wash.toOutputDto()
    }

    fun isCarWashed(carId: Int): Boolean = transaction {
        TripDao.getCarActiveTrip(carId).isWashed
    }
}