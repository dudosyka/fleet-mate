package com.fleetmate.lib.shared.modules.car.model.licence


import com.fleetmate.lib.utils.database.BaseIntIdTable

object LicenceTypeModel: BaseIntIdTable() {
    val name = text("name")
}