package com.fleetmate.stat.modules.car.dao

import com.fleetmate.lib.shared.dto.StatusDto
import com.fleetmate.lib.shared.modules.type.model.FuelTypeModel
import com.fleetmate.lib.utils.database.BaseIntEntity
import com.fleetmate.lib.utils.database.BaseIntEntityClass
import com.fleetmate.lib.utils.database.idValue
import org.jetbrains.exposed.dao.id.EntityID

class FuelDao(id: EntityID<Int>) : BaseIntEntity<StatusDto>(id, FuelTypeModel) {
    companion object : BaseIntEntityClass<StatusDto, FuelDao>(FuelTypeModel) {
    }

    var name by FuelTypeModel.name
    override fun toOutputDto(): StatusDto =
        StatusDto(idValue, name)
}