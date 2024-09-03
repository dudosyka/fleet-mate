package com.fleetmate.lib.shared.modules.fault.model


import com.fleetmate.lib.utils.database.BaseIntIdTable

object WorkTypeModel : BaseIntIdTable() {
    val name = text("name")
    val hours = double("hours")
}