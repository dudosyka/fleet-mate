package com.fleetmate.lib.data.model.automobile

import com.fleetmate.lib.utils.database.BaseIntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object AutomobilePartToAutomobilePartModel: BaseIntIdTable() {
    val parent = reference("parent", AutomobilePartModel, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val child = reference("child", AutomobilePartModel, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
}