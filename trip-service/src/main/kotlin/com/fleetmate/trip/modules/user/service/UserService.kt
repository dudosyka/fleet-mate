package com.fleetmate.trip.modules.user.service

import com.fleetmate.lib.exceptions.NotFoundException
import com.fleetmate.lib.shared.modules.trip.model.TripModel
import com.fleetmate.lib.shared.modules.user.model.UserLicenceModel
import com.fleetmate.lib.shared.modules.user.model.UserModel
import com.fleetmate.lib.utils.kodein.KodeinService
import com.fleetmate.trip.modules.nobilis.service.NobilisService
import com.fleetmate.trip.modules.user.dto.UserDto
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.DI
import org.kodein.di.instance

class UserService(di: DI) : KodeinService(di) {
    private val nobilisService: NobilisService by instance()

    private fun getOne(userId: Int) = transaction {
        val userDto = UserDto(UserModel.selectAll().where {
            UserModel.id eq userId
        }.firstOrNull() ?: throw NotFoundException("User not found"))

        userDto.licenceTypes = UserLicenceModel.select(UserLicenceModel.licence).where { UserLicenceModel.user eq userDto.id }.map {
            it[UserLicenceModel.licence].value
        }

        userDto
    }

    suspend fun isAvailableForTrip(userId: Int, licenceType: Int): Boolean {
        val userDto = getOne(userId)
        val checkupCompleted = nobilisService.checkupExists(userDto.fullName, userDto.birthday)


        return checkupCompleted && transaction {
            val notInTrip = TripModel.select(TripModel.id).where { TripModel.driver eq userId }.empty()

            val licenceCorrect = userDto.licenceTypes.contains(licenceType)

            notInTrip && licenceCorrect
        }
    }
}