package com.fleetmate.lib.shared.modules.position.model


import com.fleetmate.lib.utils.database.BaseIntIdTable

object PositionModel: BaseIntIdTable() {
    val name = text("name")
}