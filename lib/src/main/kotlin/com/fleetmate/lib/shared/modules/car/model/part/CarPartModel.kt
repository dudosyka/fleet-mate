package com.fleetmate.lib.shared.modules.car.model.part


import com.fleetmate.lib.utils.database.BaseIntIdTable

object CarPartModel : BaseIntIdTable() {
    val name = text("name")
}