package com.fleetmate.trip.modules.report.service

import com.fleetmate.faults.modules.faults.data.model.FaultsModel
import com.fleetmate.lib.model.trip.TripModel
import com.fleetmate.lib.utils.kodein.KodeinService
import com.fleetmate.trip.modules.report.data.dto.ReportBaseOutputDto
import com.fleetmate.trip.modules.report.data.dto.ReportCreateDto
import com.fleetmate.trip.modules.report.data.dto.ReportFullOutputDto
import com.fleetmate.trip.modules.report.data.dto.ReportOutputDto
import com.fleetmate.trip.modules.report.data.dto.ReportUpdateDto
import com.fleetmate.trip.modules.report.data.model.ReportModel
import com.fleetmate.trip.modules.trip.service.trip.TripService
import org.kodein.di.DI
import org.kodein.di.instance

import kotlin.collections.map

class ReportService(di: DI) : KodeinService(di) {
    val tripService: TripService by instance()

    fun getOne(id: Int): ReportFullOutputDto? {
        val report =  ReportOutputDto(ReportModel.getOne(id) ?: return null)
        val trip = tripService.getOne(report.trip)
        return ReportFullOutputDto(
            id = report.id,
            mileage = report.mileage,
            avgSpeed = report.avgSpeed,
            trip = trip
        )
    }

    fun getAll(start: Long, finish: Long): List<ReportBaseOutputDto> {
        return ReportModel.getAll(start, finish).map {
            ReportBaseOutputDto(
                id = it[ReportModel.id].value,
                date = it[TripModel.keyReturn]?.epochSecond,
                faults = FaultsModel.getAllByTrip(it[TripModel.id].value).size,
                automobile = it[ReportModel.automobile].value,
                route = it[TripModel.route]
            )
        }
    }

    fun create(reportCreateDto: ReportCreateDto): ReportOutputDto =
        ReportOutputDto(ReportModel.create(reportCreateDto))

    fun update(id: Int, reportUpdateDto: ReportUpdateDto): Boolean =
        ReportModel.update(id, reportUpdateDto)

    fun delete(id: Int): Boolean =
        ReportModel.delete(id)
}