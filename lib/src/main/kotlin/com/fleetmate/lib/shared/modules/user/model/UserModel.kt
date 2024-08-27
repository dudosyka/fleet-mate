package com.fleetmate.lib.shared.modules.user.model

import com.fleetmate.lib.exceptions.NotFoundException
import com.fleetmate.lib.shared.modules.car.model.licence.LicenceTypeModel
import com.fleetmate.lib.shared.modules.department.model.DepartmentModel
import com.fleetmate.lib.shared.modules.position.model.PositionModel
import com.fleetmate.lib.utils.database.BaseIntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.transactions.transaction

object UserModel: BaseIntIdTable() {
    val login = text("login")
    val email = text("email")
    val phoneNumber = text("phone_number")
    val hash = text("hash")

    val fullName = text("fullname")
    val birthday = long("birthday")
    val insuranceNumber = text("snils")
    val licenceType = reference("licence_type", LicenceTypeModel, ReferenceOption.RESTRICT, ReferenceOption.CASCADE)
    val position = reference("position", PositionModel, ReferenceOption.RESTRICT, ReferenceOption.CASCADE)
    val department = reference("department", DepartmentModel, ReferenceOption.RESTRICT, ReferenceOption.CASCADE)
    val sectorBossId = reference("sector_boss_id", UserModel, ReferenceOption.RESTRICT, ReferenceOption.CASCADE).nullable().default(null)

    fun getByLogin(login: String): ResultRow = transaction {
        select(UserModel.id, hash).where {
            (UserModel.login eq login) or (email eq login) or (phoneNumber eq login)
        }.firstOrNull() ?: throw NotFoundException("User not found")
    }
}