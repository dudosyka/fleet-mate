package com.fleetmate.trip.modules.automobile.service

import com.fleetmate.lib.data.dto.automobile.AutomobilePartOutputDto
import com.fleetmate.lib.data.model.automobile.AutomobilePartModel
import com.fleetmate.lib.utils.kodein.KodeinService
import org.kodein.di.DI

class AutomobilePartService(di: DI) : KodeinService(di) {

    fun getOne(id: Int?) : AutomobilePartOutputDto?{
        if (id == null){
            return null
        }
        val part = AutomobilePartModel.getOne(id)

        return AutomobilePartOutputDto(
            part[AutomobilePartModel.id].value,
            part[AutomobilePartModel.name]
        )
    }
}