package com.fleetmate.lib.shared.modules.user.model


import com.fleetmate.lib.shared.modules.car.model.licence.LicenceTypeModel
import com.fleetmate.lib.utils.database.BaseIntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object UserLicenceModel : BaseIntIdTable() {
    val user = reference("user", UserModel, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val licence = reference("licence", LicenceTypeModel, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
}