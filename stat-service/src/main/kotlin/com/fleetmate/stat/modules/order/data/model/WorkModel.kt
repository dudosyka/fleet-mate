package com.fleetmate.stat.modules.order.data.model


import com.fleetmate.lib.shared.modules.fault.model.WorkTypeModel
import com.fleetmate.lib.utils.database.BaseIntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object WorkModel : BaseIntIdTable() {
    val type = reference("type", WorkTypeModel, ReferenceOption.RESTRICT, ReferenceOption.CASCADE)
    val order = reference("order", OrderModel, ReferenceOption.RESTRICT, ReferenceOption.CASCADE)
    val doneAt = long("done_at").nullable().default(null)
}