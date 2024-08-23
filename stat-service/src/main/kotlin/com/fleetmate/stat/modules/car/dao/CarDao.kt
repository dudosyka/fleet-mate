package com.fleetmate.stat.modules.car.dao


import com.fleetmate.lib.shared.modules.car.model.CarModel
import com.fleetmate.lib.utils.database.BaseIntEntity
import com.fleetmate.lib.utils.database.BaseIntEntityClass
import com.fleetmate.lib.utils.database.idValue
import com.fleetmate.stat.modules.car.dto.CarDto
import org.jetbrains.exposed.dao.id.EntityID

class CarDao(id: EntityID<Int>) : BaseIntEntity<CarDto>(id, CarModel) {
    companion object : BaseIntEntityClass<CarDto, CarDao>(CarModel)

    val name by CarModel.name
    val registrationNumber by CarModel.registrationNumber
    val typeId by CarModel.type
    val fuelLevel by CarModel.fuelLevel
    val mileage by CarModel.mileage

    override fun toOutputDto(): CarDto =
        CarDto(idValue, name, registrationNumber, typeId.value, fuelLevel, mileage)
}