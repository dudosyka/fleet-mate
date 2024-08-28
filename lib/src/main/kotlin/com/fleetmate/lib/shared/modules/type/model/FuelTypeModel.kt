package com.fleetmate.lib.shared.modules.type.model

import com.fleetmate.lib.utils.database.BaseIntIdTable

object FuelTypeModel: BaseIntIdTable() {
    val name = text("name")
}