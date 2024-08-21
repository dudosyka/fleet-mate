package com.fleetmate.crypt.modules.user.service

import com.fleetmate.lib.data.dto.user.UserOutputDto
import com.fleetmate.lib.data.model.user.UserModel
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
}