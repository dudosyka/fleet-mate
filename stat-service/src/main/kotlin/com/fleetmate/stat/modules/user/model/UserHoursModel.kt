package com.fleetmate.stat.modules.user.model


import com.fleetmate.lib.shared.modules.user.model.UserModel
import com.fleetmate.lib.utils.database.BaseIntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object UserHoursModel : BaseIntIdTable() {
    val user = reference("user", UserModel, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val hours = double("hours")
}