package com.fleetmate.trip.modules.report.service

import com.fleetmate.lib.utils.kodein.KodeinService
import com.fleetmate.trip.modules.report.data.dto.ReportCreateDto
import com.fleetmate.trip.modules.report.data.dto.ReportOutputDto
import com.fleetmate.trip.modules.report.data.dto.ReportUpdateDto
import com.fleetmate.trip.modules.report.data.model.ReportModel
import org.kodein.di.DI
import kotlin.collections.map

class ReportService(di: DI) : KodeinService(di) {
    fun getOne(id: Int): ReportOutputDto? {
        return ReportOutputDto(ReportModel.getOne(id) ?: return null)
    }

    fun getAll(): List<ReportOutputDto> {
        return ReportModel.getAll().map {
            ReportOutputDto(it)
        }
    }

    fun create(reportCreateDto: ReportCreateDto): ReportOutputDto =
        ReportOutputDto(ReportModel.create(reportCreateDto))

    fun update(id: Int, reportUpdateDto: ReportUpdateDto): Boolean =
        ReportModel.update(id, reportUpdateDto)

    fun delete(id: Int): Boolean =
        ReportModel.delete(id)
}