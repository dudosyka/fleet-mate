package com.fleetmate.stat.modules.car.dao

import com.fleetmate.lib.shared.modules.type.model.FuelTypeModel
import com.fleetmate.lib.utils.database.BaseIntEntity
import com.fleetmate.lib.utils.database.BaseIntEntityClass
import com.fleetmate.lib.utils.database.idValue
import com.fleetmate.stat.modules.car.dto.FuelTypeDto
import org.jetbrains.exposed.dao.id.EntityID

class FuelTypeDao(id: EntityID<Int>) : BaseIntEntity<FuelTypeDto>(id, FuelTypeModel) {
    companion object : BaseIntEntityClass<FuelTypeDto, FuelTypeDao>(FuelTypeModel)

    var name by FuelTypeModel.name

    override fun toOutputDto(): FuelTypeDto =
        FuelTypeDto(idValue, name)
}