package com.fleetmate.stat.modules.fault.service


import com.fleetmate.lib.shared.conf.AppConf
import com.fleetmate.lib.shared.dto.StatusDto
import com.fleetmate.lib.shared.modules.fault.model.FaultModel
import com.fleetmate.lib.utils.kodein.KodeinService
import com.fleetmate.stat.modules.fault.dao.FaultDao
import com.fleetmate.stat.modules.fault.dto.FaultFilterDto
import com.fleetmate.stat.modules.user.dto.driver.DriverFaultListItemDto
import com.fleetmate.stat.modules.fault.dto.FaultListItemDto
import com.fleetmate.stat.modules.fault.dto.FaultOutputDto
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.DI

class FaultService(di: DI) : KodeinService(di) {
    fun getAllStatuses(): List<StatusDto> =
        AppConf.FaultStatus.entries.map {
            StatusDto(it.id, it.name)
        }

    //FIXME: Dao couldn`t handle nested where clauses (should be rewrote via Model API)
    fun getAllFiltered(faultFilterFto: FaultFilterDto): List<FaultListItemDto> = transaction {
        FaultDao.find {
            with(faultFilterFto) { expressionBuilder }
        }.map { it.listItemDto }
    }

    fun getOne(faultId: Int): FaultOutputDto = transaction {
        FaultDao[faultId].fullOutputDto
    }

    fun getByDriver(driverId: Int): List<DriverFaultListItemDto> = transaction {
        FaultDao.find {
            FaultModel.author eq driverId
        }.map { it.listDriverDto }
    }
}