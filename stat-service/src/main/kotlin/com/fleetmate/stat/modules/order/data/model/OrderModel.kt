package com.fleetmate.stat.modules.order.data.model


import com.fleetmate.lib.shared.modules.fault.model.FaultModel
import com.fleetmate.lib.shared.modules.user.model.UserModel
import com.fleetmate.lib.utils.database.BaseIntIdTable
import io.ktor.util.date.*
import org.jetbrains.exposed.sql.ReferenceOption

object OrderModel : BaseIntIdTable() {
    val number = text("number")
    val status = text("status")
    val mechanic = reference("machanic", UserModel, ReferenceOption.RESTRICT, ReferenceOption.CASCADE)
    val fault = reference("fault", FaultModel, ReferenceOption.RESTRICT, ReferenceOption.CASCADE)
    val startedAt = long("started_at").default(getTimeMillis())
    val closedAt = long("closed_at").nullable().default(null)
    val _juniorMechanicFilterSimplifier = text("_junior_mechanic_filter_simplifier")
}