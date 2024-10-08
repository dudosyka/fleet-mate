package com.fleetmate.stat.modules.car.dao


import com.fleetmate.lib.exceptions.InternalServerException
import com.fleetmate.lib.shared.modules.car.model.type.CarTypeModel
import com.fleetmate.lib.utils.database.BaseIntEntity
import com.fleetmate.lib.utils.database.BaseIntEntityClass
import com.fleetmate.lib.utils.database.idValue
import com.fleetmate.stat.modules.car.dto.type.CarTypeDto
import com.fleetmate.stat.modules.car.dto.type.CarTypeUpdateDto
import com.fleetmate.stat.modules.user.dao.LicenceTypeDao
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.transaction

class CarTypeDao(id: EntityID<Int>) : BaseIntEntity<CarTypeDto>(id, CarTypeModel) {
    companion object : BaseIntEntityClass<CarTypeDto, CarTypeDao>(CarTypeModel) {
    }

    var name by CarTypeModel.name
    var speedLimit by CarTypeModel.speedLimit
    var speedError by CarTypeModel.speedError
    var licenceType by CarTypeModel.licenceType
    var photo by CarTypeModel.photo

    override fun toOutputDto(): CarTypeDto =
        CarTypeDto(idValue, name, speedLimit, speedError, licenceType.value, photo)

    fun updateAndFlush(carTypeUpdateDto: CarTypeUpdateDto): CarTypeDto = transaction {
        name = carTypeUpdateDto.name ?: name
        speedLimit = carTypeUpdateDto.speedLimit ?: speedLimit
        speedError = carTypeUpdateDto.speedError ?: speedError
        licenceType = LicenceTypeDao[carTypeUpdateDto.licenceType ?: licenceType.value].id
        if (flush())
            toOutputDto()
        else
            throw InternalServerException("Failed to update CarTypeModel")
    }
}