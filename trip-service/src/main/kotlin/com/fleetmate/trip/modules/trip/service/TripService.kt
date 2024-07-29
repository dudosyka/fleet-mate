package com.fleetmate.trip.modules.trip.service

import com.fleetmate.lib.utils.kodein.KodeinService
import com.fleetmate.trip.modules.trip.data.dto.TripCreateDto
import com.fleetmate.trip.modules.trip.data.dto.TripDto
import com.fleetmate.trip.modules.trip.data.dto.TripUpdateDto
import com.fleetmate.trip.modules.trip.data.model.TripModel
import org.kodein.di.DI

class TripService(di: DI) : KodeinService(di) {
    fun getOne(id: Int): TripDto? {
        return TripDto(TripModel.getOne(id) ?: return null)
    }

    fun getAll(): List<TripDto> =
        TripModel.getAll().map { TripDto(it) }

    fun create(tripCreateDto: TripCreateDto): TripDto =
        TripDto(TripModel.create(tripCreateDto))

    fun update(id: Int, tripUpdateDto: TripUpdateDto): Boolean =
        TripModel.update(id, tripUpdateDto)

    fun delete(id: Int): Boolean =
        TripModel.delete(id)
}