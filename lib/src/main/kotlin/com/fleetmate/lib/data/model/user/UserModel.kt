package com.fleetmate.lib.data.model.user

import com.fleetmate.lib.data.dto.user.UserCreateDto
import com.fleetmate.lib.data.dto.user.UserUpdateDto
import com.fleetmate.lib.exceptions.InternalServerException
import com.fleetmate.lib.data.model.department.DepartmentModel
import com.fleetmate.lib.data.model.position.PositionModel
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
    val position = reference("post", PositionModel)
    val department = reference("division", DepartmentModel)
    val birthday = long("birthday")

    fun getOne(id: Int?): ResultRow? = transaction {
        (UserModel innerJoin PositionModel)
            .innerJoin(
                DepartmentModel
            )
            .select(
                UserModel.id,
                fullName,
                email,
                phoneNumber,
                birthday,
                PositionModel.id,
                PositionModel.name,
                DepartmentModel.id,
                DepartmentModel.name
            )
            .where(
                UserModel.id eq id
            ).firstOrNull()
    }

    fun getAll(): List<ResultRow> = transaction {

        (UserModel innerJoin PositionModel)
            .innerJoin(
                DepartmentModel
            )
            .select(
                UserModel.id,
                fullName,
                email,
                phoneNumber,
                birthday,
                PositionModel.id,
                PositionModel.name,
                DepartmentModel.id,
                DepartmentModel.name
            ).toList()
    }

    fun create(userCreateDto: UserCreateDto): ResultRow = transaction {
        (UserModel.insert {
            it[fullName] = userCreateDto.fullName
            it[email] = userCreateDto.email
            it[phoneNumber] = userCreateDto.phoneNumber
            it[position] = userCreateDto.position
            it[department] = userCreateDto.department
            it[birthday] = userCreateDto.birthday
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
            if (userUpdateDto.position != null){
                it[position] = userUpdateDto.position
            }
            if (userUpdateDto.department != null){
                it[department] = userUpdateDto.department
            }
            if (userUpdateDto.birthday != null){
                it[birthday] = userUpdateDto.birthday
            }
        } != 0
    }

    fun delete(id: Int): Boolean = transaction {
        UserModel.deleteWhere{ UserModel.id eq id} != 0
    }
}