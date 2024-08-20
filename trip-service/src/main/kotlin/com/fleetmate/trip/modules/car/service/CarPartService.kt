package com.fleetmate.trip.modules.car.service

import com.fleetmate.lib.data.dto.car.CarPartOutputDto
import com.fleetmate.lib.data.model.car.CarPartModel
import com.fleetmate.lib.utils.kodein.KodeinService
import org.kodein.di.DI

class CarPartService(di: DI) : KodeinService(di) {

    fun getOne(id: Int?) : CarPartOutputDto?{
        if (id == null){
            return null
        }
        val part = CarPartModel.getOne(id)

        return CarPartOutputDto(
            part[CarPartModel.id].value,
            part[CarPartModel.name]
        )
    }
}