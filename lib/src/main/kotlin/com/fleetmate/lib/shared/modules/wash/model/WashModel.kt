package com.fleetmate.lib.shared.modules.wash.model


import com.fleetmate.lib.shared.modules.trip.model.TripModel
import com.fleetmate.lib.shared.modules.user.model.UserModel
import com.fleetmate.lib.utils.database.BaseIntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object WashModel : BaseIntIdTable() {
    val trip = reference("trip", TripModel, ReferenceOption.RESTRICT, ReferenceOption.CASCADE)
    val author = reference("author", UserModel, ReferenceOption.RESTRICT, ReferenceOption.CASCADE)
    val timestamp = long("timestamp")
}