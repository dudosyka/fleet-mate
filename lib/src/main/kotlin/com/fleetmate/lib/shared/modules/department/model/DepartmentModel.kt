package com.fleetmate.lib.shared.modules.department.model


import com.fleetmate.lib.utils.database.BaseIntIdTable

object DepartmentModel: BaseIntIdTable() {
    val name = text("name")
}