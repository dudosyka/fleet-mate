package com.fleetmate.lib.data.model.role

import com.fleetmate.lib.data.dto.role.RoleInputDto
import com.fleetmate.lib.exceptions.InternalServerException
import com.fleetmate.lib.utils.database.BaseIntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

object RoleModel: BaseIntIdTable() {
    val name = text("name")
    val description = text("description").nullable().default(null)

    fun create(roleCreateDto: RoleInputDto): ResultRow = transaction {
        (RoleModel.insert {
            it[name] = roleCreateDto.name
            it[description] = roleCreateDto.description
        }.resultedValues ?: throw InternalServerException("Failed to create role")).first()
    }
}