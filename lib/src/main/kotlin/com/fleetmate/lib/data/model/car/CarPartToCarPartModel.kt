package com.fleetmate.lib.data.model.car

import com.fleetmate.lib.utils.database.BaseIntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object CarPartToCarPartModel: BaseIntIdTable() {
    val parent = reference("parent", CarPartModel, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val child = reference("child", CarPartModel, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
}