package com.fleetmate.trip.modules.car.service

import com.fleetmate.lib.data.dto.car.part.CarPartOutputDto
import com.fleetmate.lib.data.model.car.CarPartModel
import com.fleetmate.lib.exceptions.NotFoundException
import com.fleetmate.lib.utils.kodein.KodeinService
import org.kodein.di.DI

class CarPartService(di: DI) : KodeinService(di) {

    fun getOne(id: Int): CarPartOutputDto {
        val part = CarPartModel.getOne(id) ?: throw NotFoundException("Car part not found")

        return CarPartOutputDto(
            part[CarPartModel.id].value,
            part[CarPartModel.name]
        )
    }
}