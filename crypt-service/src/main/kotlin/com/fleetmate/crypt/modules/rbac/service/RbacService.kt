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

//    fun getFiltered(roleFilterDto: RoleFilterDto): List<RoleOutputDto> = transaction {
//        RoleModel
//            .select {
//                createLikeCond(roleFilterDto.name, RoleModel.id neq 0, RoleModel.name)
//            }
//            .orderBy(RoleModel.name to SortOrder.ASC)
//            .map {
//                RoleOutputDto(
//                    id = it[RoleModel.id].value,
//                    name = it[RoleModel.name],
//                    description = it[RoleModel.description],
//                    relatedUsersCount = RbacModel.getRelatedUsers(it[RoleModel.id].value).count()
//                )
//            }
//    }

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

//        val author = UserModel.getOne(authorizedUser.id)
//        logEvent(RoleCreateEvent(roleDao.idValue, roleDao.name, author.login))
        commit()

        RoleOutputDto(role[RoleModel.id].value, role[RoleModel.name], role[RoleModel.description])
    }

//    fun updateRole(authorizedUser: AuthorizedUser, roleId: Int, roleUpdateDto: RoleUpdateDto): RoleOutputDto = transaction {
//        val userDao = UserDao[authorizedUser.id]
//        val roleDao = RoleDao[roleId]
//        roleDao.loadAndFlush(userDao.login, roleUpdateDto)
//
//        commit()
//
//        roleDao.toOutputDto()
//    }

//    fun removeRole(authorizedUser: AuthorizedUser, roleId: Int): RoleRemoveResultDto = transaction {
//        val userDao = UserDao[authorizedUser.id]
//        val roleDao = RoleDao[roleId]
//        val roleName = roleDao.name
//        roleDao.delete(userDao.login)
//        commit()
//
//        RoleRemoveResultDto(true, "Role $roleName successfully removed")
//    }
}