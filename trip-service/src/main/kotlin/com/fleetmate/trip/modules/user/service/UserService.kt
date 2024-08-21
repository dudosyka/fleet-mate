package com.fleetmate.trip.modules.user.service

import com.fleetmate.lib.conf.AppConf
import com.fleetmate.lib.data.dto.user.UserCreateDto
import com.fleetmate.lib.data.dto.user.UserOutputDto
import com.fleetmate.lib.exceptions.NotFoundException
import com.fleetmate.lib.data.model.user.UserModel
import com.fleetmate.lib.utils.kodein.KodeinService
import com.fleetmate.trip.modules.nobilis.service.NobilisService
import org.kodein.di.DI
import org.kodein.di.instance
import java.time.LocalDateTime
import kotlin.collections.map

class UserService(di: DI) : KodeinService(di) {
    private val nobilisService: NobilisService by instance()
    fun getOne(id: Int): UserOutputDto {
        return UserOutputDto(UserModel.getOne(id) ?: throw NotFoundException("User not found"))
    }

    fun getAll(): List<UserOutputDto> {
        return UserModel.getAll().map {
            UserOutputDto(it)
        }
    }

    fun create(userCreateDto: UserCreateDto): UserOutputDto =
        UserOutputDto(UserModel.create(userCreateDto))

    suspend fun isCheckupCompleted(userId: Int): Boolean {
        val user = getOne(userId)

        val birthday = LocalDateTime.ofEpochSecond(user.birthday, 0, AppConf.defaultZoneOffset)

        return nobilisService.checkupExists(user.fullName, "${birthday.year}${birthday.monthValue}${birthday.dayOfMonth}")
    }
}