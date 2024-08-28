package com.fleetmate.stat.modules.violation.service


import com.fleetmate.lib.shared.conf.AppConf
import com.fleetmate.lib.shared.dto.StatusDto
import com.fleetmate.lib.shared.modules.violation.model.ViolationModel
import com.fleetmate.lib.utils.kodein.KodeinService
import com.fleetmate.stat.modules.violation.dao.ViolationDao
import com.fleetmate.stat.modules.trip.dto.TripViolationListItemDto
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.DI

class ViolationService(di: DI) : KodeinService(di) {

    fun getAllTypes(): List<StatusDto> =
        AppConf.ViolationType.entries.map {
            StatusDto(it.id, it.name)
        }
    fun getByTrip(tripId: Int): List<TripViolationListItemDto> = transaction {
        ViolationDao.find {
            ViolationModel.trip eq  tripId
        }.map { it.listTripDto }
    }
}