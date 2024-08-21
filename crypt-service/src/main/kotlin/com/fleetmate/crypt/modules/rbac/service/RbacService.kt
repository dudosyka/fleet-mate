package com.fleetmate.crypt.modules.rbac.service

import com.fleetmate.lib.data.dto.role.RoleInputDto
import com.fleetmate.lib.data.dto.role.RoleOutputDto
import com.fleetmate.lib.data.model.role.RoleModel
import com.fleetmate.lib.dto.auth.AuthorizedUser
import com.fleetmate.lib.utils.kodein.KodeinService
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.DI


class RbacService(di: DI) : KodeinService(di) {

    fun getRole(roleId: Int): RoleOutputDto = transaction {
        RoleModel.select(
            RoleModel.name,
            RoleModel.description
        ).where(
            RoleModel.id eq roleId
        ).map {
            RoleOutputDto(
                roleId,
                it[RoleModel.name],
                it[RoleModel.description]
            )
        }.first()
    }


    fun createRole(authorizedUser: AuthorizedUser, roleCreateDto: RoleInputDto): RoleOutputDto = transaction {
        val role = RoleModel.create(roleCreateDto)

        commit()

        RoleOutputDto(role[RoleModel.id].value, role[RoleModel.name], role[RoleModel.description])
    }

}