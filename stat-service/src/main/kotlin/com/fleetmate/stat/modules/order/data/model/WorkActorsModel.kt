package com.fleetmate.stat.modules.order.data.model


import com.fleetmate.lib.shared.modules.user.model.UserModel
import com.fleetmate.lib.utils.database.BaseIntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object WorkActorsModel : BaseIntIdTable() {
    val actor = reference("actor", UserModel, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val work = reference("work", WorkModel, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
}