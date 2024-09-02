package com.fleetmate.trip.modules.car.data.dao

import com.fleetmate.lib.shared.modules.car.model.CarModel
import com.fleetmate.lib.shared.modules.car.model.part.CarPartToCarPartModel
import com.fleetmate.lib.shared.modules.car.model.type.CarTypeModel
import com.fleetmate.lib.utils.database.BaseIntEntity
import com.fleetmate.lib.utils.database.BaseIntEntityClass
import com.fleetmate.lib.utils.database.idValue
import com.fleetmate.trip.modules.car.data.dto.CarDto
import com.fleetmate.trip.modules.car.data.dto.CarFullDto
import com.fleetmate.trip.modules.refuel.dto.RefuelInputDto
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SqlExpressionBuilder.plus
import org.jetbrains.exposed.sql.update

class CarDao(id: EntityID<Int>) : BaseIntEntity<CarDto>(id, CarModel) {
    companion object : BaseIntEntityClass<CarDto, CarDao>(CarModel)

    val name by CarModel.name
    val registrationNumber by CarModel.registrationNumber
    val fuelLevel by CarModel.fuelLevel
    val mileage by CarModel.mileage
    private val typeId by CarModel.type

    val licenceType: Int get() =
        CarTypeModel
            .select(CarTypeModel.licenceType)
            .where { CarTypeModel.id eq typeId }
            .first()[CarTypeModel.licenceType].value

    val partRoot: Int? get() =
        CarTypeModel
            .select(CarTypeModel.rootPart)
            .where { CarTypeModel.id eq typeId }
            .first()[CarTypeModel.rootPart]?.value

    val speedLimit: Double get() =
        CarTypeModel
            .select(CarTypeModel.speedLimit)
            .where { CarTypeModel.id eq typeId }
            .first()[CarTypeModel.speedLimit]

    val speedError: Double get() =
        CarTypeModel
            .select(CarTypeModel.speedError)
            .where { CarTypeModel.id eq typeId }
            .first()[CarTypeModel.speedError]

    val avgFuelConsumption: Double get() =
        CarTypeModel
            .select(CarTypeModel.avgFuelConsumption)
            .where { CarTypeModel.id eq typeId }
            .first()[CarTypeModel.avgFuelConsumption]

    val prettyName: String get() =
        "$name - $registrationNumber"

    val fullOutputDto: CarFullDto get() =
        CarFullDto(
            idValue, licenceType, fuelLevel, mileage, CarPartToCarPartModel.getTreeFrom(partRoot)
        )

    override fun toOutputDto(): CarDto =
        CarDto(
            idValue, licenceType, fuelLevel, mileage
        )

    fun updateByRefuel(refuelInputDto: RefuelInputDto) {
        CarModel.update({ CarModel.id eq idValue }) {
            it[fuelLevel] = fuelLevel + refuelInputDto.volume
        }
    }
}