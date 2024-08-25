package com.fleetmate.stat.modules.violation.service


import com.fleetmate.lib.shared.modules.violation.model.ViolationModel
import com.fleetmate.lib.utils.kodein.KodeinService
import com.fleetmate.stat.modules.violation.dao.ViolationDao
import com.fleetmate.stat.modules.violation.dto.ViolationListTripDto
import org.kodein.di.DI

class ViolationService(di: DI) : KodeinService(di) {
    fun getByTrip(tripId: Int): List<ViolationListTripDto> =
        ViolationDao.find {
            ViolationModel.trip eq  tripId
        }.map { it.listTripDto }
}