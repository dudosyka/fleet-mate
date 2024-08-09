package com.fleetmate.trip.modules.trip.service.trip

import com.fleetmate.lib.dto.trip.TripCreateDto
import com.fleetmate.lib.dto.trip.TripFullOutputDto
import com.fleetmate.lib.dto.trip.TripOutputDto
import com.fleetmate.lib.dto.trip.TripUpdateDto
import com.fleetmate.lib.model.trip.TripModel
import com.fleetmate.lib.utils.kodein.KodeinService
import org.kodein.di.DI
import kotlin.collections.map

class TripService(di: DI) : KodeinService(di) {
    fun getOne(id: Int): TripFullOutputDto? {
        return TripFullOutputDto(TripModel.getOne(id) ?: return null)
    }

    fun getAll(): List<TripOutputDto> {
        return TripModel.getAll().map {
            TripOutputDto(it)
        }
    }

    fun create(tripCreateDto: TripCreateDto): TripOutputDto =
        TripOutputDto(TripModel.create(tripCreateDto))

    fun update(id: Int, tripUpdateDto: TripUpdateDto): Boolean =
        TripModel.update(id, tripUpdateDto)

    fun delete(id: Int): Boolean =
        TripModel.delete(id)
}