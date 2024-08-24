package com.fleetmate.stat.modules.trip.service


import com.fleetmate.lib.utils.kodein.KodeinService
import com.fleetmate.stat.modules.trip.dao.TripDao
import com.fleetmate.stat.modules.trip.dto.TripFilterDto
import com.fleetmate.stat.modules.trip.dto.TripListItemDto
import org.kodein.di.DI

class TripService(di: DI) : KodeinService(di) {
    fun getAllFiltered(tripFilterDto: TripFilterDto): List<TripListItemDto> =
        TripDao.find {
            with(tripFilterDto) { expressionBuilder }
        }.map {
            val output = it.listItemDto
            output.violations = TripDao.violations(output.id).count()
            output
        }
}