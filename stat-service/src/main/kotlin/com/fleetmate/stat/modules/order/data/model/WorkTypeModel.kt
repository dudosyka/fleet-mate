package com.fleetmate.stat.modules.order.data.model


import com.fleetmate.lib.utils.database.BaseIntIdTable

object WorkTypeModel : BaseIntIdTable() {
    val name = text("name")
    val hours = double("hours")
}