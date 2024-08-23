package com.fleetmate.trip.modules.report.service


import com.fleetmate.lib.shared.modules.auth.dto.AuthorizedUser
import com.fleetmate.lib.utils.kodein.KodeinService
import com.fleetmate.trip.modules.report.dto.ReportDto
import com.fleetmate.trip.modules.report.dto.ReportListItemDto
import com.fleetmate.trip.modules.trip.data.dao.TripDao
import org.kodein.di.DI

class ReportService(di: DI) : KodeinService(di) {
    fun getAllForUser(authorizedUser: AuthorizedUser): List<ReportListItemDto> =
        TripDao.driverTripsByTheLastMonth(authorizedUser.id).map {
            it.reportListItemDto
        }

    fun getOne(reportId: Int): ReportDto =
        TripDao[reportId].reportDto
}