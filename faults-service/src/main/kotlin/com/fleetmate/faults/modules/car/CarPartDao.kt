package com.fleetmate.faults.modules.car


import com.fleetmate.lib.shared.modules.car.dto.CarPartDto
import com.fleetmate.lib.shared.modules.car.model.part.CarPartModel
import com.fleetmate.lib.utils.database.BaseIntEntity
import com.fleetmate.lib.utils.database.BaseIntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class CarPartDao(id: EntityID<Int>) : BaseIntEntity<CarPartDto>(id, CarPartModel) {
    companion object : BaseIntEntityClass<CarPartDto, CarPartDao>(CarPartModel)

    override fun toOutputDto(): CarPartDto =
        TODO("Not yet implemented")
}