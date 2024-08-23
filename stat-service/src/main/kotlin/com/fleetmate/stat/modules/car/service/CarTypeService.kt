package com.fleetmate.stat.modules.car.service


import com.fleetmate.lib.utils.kodein.KodeinService
import com.fleetmate.stat.modules.car.dao.CarTypeDao
import com.fleetmate.stat.modules.car.dto.type.CarTypeDto
import com.fleetmate.stat.modules.car.dto.type.CarTypeUpdateDto
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.DI

class CarTypeService(di: DI) : KodeinService(di) {
    fun update(carTypeUpdateDto: CarTypeUpdateDto): CarTypeDto = transaction {
        CarTypeDao[carTypeUpdateDto.id].updateAndFlush(carTypeUpdateDto)
    }

    fun getOne(carTypeId: Int): CarTypeDto = transaction {
        CarTypeDao[carTypeId].toOutputDto()
    }
}