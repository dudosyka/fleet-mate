package com.fleetmate.trip.modules.report.service

import com.fleetmate.lib.data.model.faults.FaultsModel
import com.fleetmate.lib.data.dto.auth.AuthorizedUser
import com.fleetmate.lib.exceptions.NotFoundException
import com.fleetmate.lib.data.model.trip.TripModel
import com.fleetmate.lib.utils.kodein.KodeinService
import com.fleetmate.trip.modules.report.data.dto.ReportBaseOutputDto
import com.fleetmate.lib.data.dto.report.ReportCreateDto
import com.fleetmate.trip.modules.report.data.dto.ReportFullOutputDto
import com.fleetmate.trip.modules.report.data.dto.ReportOutputDto
import com.fleetmate.lib.data.model.report.ReportModel
import com.fleetmate.trip.modules.trip.service.TripService
import org.kodein.di.DI
import org.kodein.di.instance

import kotlin.collections.map

class ReportService(di: DI) : KodeinService(di) {
    private val tripService: TripService by instance()

    fun getOne(id: Int): ReportFullOutputDto? {
        val report =  ReportOutputDto(ReportModel.getOne(id) ?: return null)
        if (report.trip == null){
            throw NotFoundException("Trip is Not Found")
        }
        val trip = tripService.getOne(report.trip)
        return ReportFullOutputDto(
            id = report.id,
            mileage = report.mileage,
            avgSpeed = report.avgSpeed,
            trip = trip
        )
    }

    fun getAll(start: Long, finish: Long, authorizedUser: AuthorizedUser): List<ReportBaseOutputDto> {
        return ReportModel.getAll(start, finish, authorizedUser.id).map {
            ReportBaseOutputDto(
                id = it[ReportModel.id].value,
                date = it[TripModel.keyReturn],
                faults = FaultsModel.getAllByTrip(it[TripModel.id].value).size,
                car = it[ReportModel.car].value,
                route = it[TripModel.route]
            )
        }
    }

    fun create(reportCreateDto: ReportCreateDto): ReportOutputDto =
        ReportOutputDto(ReportModel.create(reportCreateDto))
}