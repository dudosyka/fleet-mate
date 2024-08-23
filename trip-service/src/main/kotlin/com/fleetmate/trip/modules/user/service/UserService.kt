package com.fleetmate.trip.modules.user.service

import com.fleetmate.lib.exceptions.NotFoundException
import com.fleetmate.lib.shared.modules.trip.model.TripModel
import com.fleetmate.lib.shared.modules.user.model.UserModel
import com.fleetmate.lib.utils.kodein.KodeinService
import com.fleetmate.trip.modules.car.data.dto.CarDto
import com.fleetmate.trip.modules.nobilis.service.NobilisService
import com.fleetmate.trip.modules.user.dto.UserDto
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.kodein.di.DI
import org.kodein.di.instance

class UserService(di: DI) : KodeinService(di) {
    private val nobilisService: NobilisService by instance()
    fun getOne(userId: Int) =
        UserDto(UserModel.selectAll().where {
            UserModel.id eq userId
        }.firstOrNull() ?: throw NotFoundException("User not found"))

    suspend fun isAvailableForTrip(userId: Int, car: CarDto): Boolean = newSuspendedTransaction {
        val notInTrip = TripModel.select(TripModel.id).where { TripModel.driver eq userId }.empty()
        val userDto = getOne(userId)

        val licenceCorrect = userDto.licenceType >= car.licenceType

        val checkupCompleted = nobilisService.checkupExists(userDto.fullName, userDto.birthday)

        notInTrip && licenceCorrect && checkupCompleted
    }
}