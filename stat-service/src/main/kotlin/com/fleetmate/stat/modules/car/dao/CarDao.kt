package com.fleetmate.stat.modules.car.dao


import com.fleetmate.lib.shared.modules.car.model.CarModel
import com.fleetmate.lib.shared.modules.violation.model.ViolationModel
import com.fleetmate.lib.utils.database.BaseIntEntity
import com.fleetmate.lib.utils.database.BaseIntEntityClass
import com.fleetmate.lib.utils.database.idValue
import com.fleetmate.stat.modules.car.dto.CarDto
import com.fleetmate.stat.modules.car.dto.CarListItemDto
import com.fleetmate.stat.modules.car.dto.CarSimpleDto
import com.fleetmate.stat.modules.violation.dao.ViolationDao
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedIterable

class CarDao(id: EntityID<Int>) : BaseIntEntity<CarDto>(id, CarModel) {
    companion object : BaseIntEntityClass<CarDto, CarDao>(CarModel) {
        fun violations(
            idValue: Int,
        ): SizedIterable<ViolationDao> =
            ViolationDao.find {
                (ViolationModel.car eq idValue)
            }
    }

    val name by CarModel.name
    val registrationNumber by CarModel.registrationNumber
    val typeId by CarModel.type
    val type by CarTypeDao referencedOn CarModel.type
    val fuelLevel by CarModel.fuelLevel
    val mileage by CarModel.mileage

    override fun toOutputDto(): CarDto =
        CarDto(idValue, name, registrationNumber, typeId.value, fuelLevel, mileage)

    val simpleDto: CarSimpleDto get() =
        CarSimpleDto(idValue, name, type.name, registrationNumber)

    val listItemDto: CarListItemDto get() =
        CarListItemDto(simpleDto, fuelLevel)
}