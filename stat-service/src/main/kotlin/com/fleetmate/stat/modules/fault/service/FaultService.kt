package com.fleetmate.stat.modules.fault.service


import com.fleetmate.lib.shared.conf.AppConf
import com.fleetmate.lib.shared.dto.StatusDto
import com.fleetmate.lib.utils.kodein.KodeinService
import com.fleetmate.stat.modules.fault.dto.FaultDto
import com.fleetmate.stat.modules.fault.dto.FaultFilterDto
import com.fleetmate.stat.modules.fault.dto.FaultListItemDto
import org.kodein.di.DI

class FaultService(di: DI) : KodeinService(di) {
    fun getAllStatuses(): List<StatusDto> =
        AppConf.FaultStatus.entries.map {
            StatusDto(it.id, it.name)
        }

    fun getAllFiltered(faultFilterFto: FaultFilterDto): List<FaultListItemDto> {
        TODO("Not yet implemented")
    }

    fun getOne(faultId: Int): FaultDto {
        TODO("Not yet implemented")
    }
}