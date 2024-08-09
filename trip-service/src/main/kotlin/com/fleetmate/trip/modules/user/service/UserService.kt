package com.fleetmate.trip.modules.user.service

import com.fleetmate.lib.dto.user.UserCreateDto
import com.fleetmate.lib.dto.user.UserOutputDto
import com.fleetmate.lib.dto.user.UserUpdateDto
import com.fleetmate.lib.model.user.UserModel
import com.fleetmate.lib.utils.kodein.KodeinService
import org.kodein.di.DI
import kotlin.collections.map

class UserService(di: DI) : KodeinService(di) {
    fun getOne(id: Int): UserOutputDto? {
        return UserOutputDto(UserModel.getOne(id) ?: return null)
    }

    fun getAll(): List<UserOutputDto> {
        return UserModel.getAll().map {
            UserOutputDto(it)
        }
    }

    fun create(userCreateDto: UserCreateDto): UserOutputDto =
        UserOutputDto(UserModel.create(userCreateDto))

    fun update(id: Int, userUpdateDto: UserUpdateDto): Boolean =
        UserModel.update(id, userUpdateDto)

    fun delete(id: Int): Boolean =
        UserModel.delete(id)
}