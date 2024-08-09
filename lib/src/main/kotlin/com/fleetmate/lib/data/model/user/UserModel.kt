package com.fleetmate.lib.model.user

import com.fleetmate.lib.dto.user.UserCreateDto
import com.fleetmate.lib.dto.user.UserUpdateDto
import com.fleetmate.lib.exceptions.InternalServerException
import com.fleetmate.lib.model.division.DivisionModel
import com.fleetmate.lib.model.post.PostModel
import com.fleetmate.lib.utils.database.BaseIntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import kotlin.collections.first
import kotlin.collections.firstOrNull
import kotlin.collections.toList


object UserModel : BaseIntIdTable() {
    val login = text("login")
    val hash = text("hash")
    val fullName = text("full_name")
    val email = text("email")
    val phoneNumber = text("phone_number")
    val post = reference("post", PostModel)
    val division = reference("division", DivisionModel)

    fun getOne(id: Int?): ResultRow? = transaction {
        if (id == null){
            return@transaction null
        }
        (UserModel innerJoin PostModel)
            .innerJoin(
                DivisionModel
            )
            .select(
                UserModel.id,
                fullName,
                email,
                phoneNumber,
                PostModel.id,
                PostModel.name,
                DivisionModel.id,
                DivisionModel.name
            )
            .where(
                UserModel.id eq id
            ).firstOrNull()
    }

    fun getAll(): List<ResultRow> = transaction {

        (UserModel innerJoin PostModel)
            .innerJoin(
                DivisionModel
            )
            .select(
                UserModel.id,
                fullName,
                email,
                phoneNumber,
                PostModel.id,
                PostModel.name,
                DivisionModel.id,
                DivisionModel.name
            ).toList()
    }

    fun create(userCreateDto: UserCreateDto): ResultRow = transaction {
        (UserModel.insert {
            it[fullName] = userCreateDto.fullName
            it[email] = userCreateDto.email
            it[phoneNumber] = userCreateDto.phoneNumber
            it[post] = userCreateDto.post
            it[division] = userCreateDto.division

        }.resultedValues ?: throw InternalServerException("Failed to create user")).first()
    }

    fun update(id: Int, userUpdateDto: UserUpdateDto): Boolean = transaction {
        UserModel.update({ UserModel.id eq id })
        {
            if (userUpdateDto.fullName != null){
                it[fullName] = userUpdateDto.fullName
            }
            if (userUpdateDto.email != null){
                it[email] = userUpdateDto.email
            }
            if (userUpdateDto.phoneNumber != null){
                it[phoneNumber] = userUpdateDto.phoneNumber
            }
            if (userUpdateDto.post != null){
                it[post] = userUpdateDto.post
            }
            if (userUpdateDto.division != null){
                it[division] = userUpdateDto.division
            }
        } != 0
    }

    fun delete(id: Int): Boolean = transaction {
        UserModel.deleteWhere{ UserModel.id eq id} != 0
    }
}