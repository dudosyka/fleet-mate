package com.fleetmate.stat.modules.photo.service

import com.fleetmate.lib.shared.conf.AppConf
import com.fleetmate.lib.shared.dto.StatusDto
import com.fleetmate.lib.utils.kodein.KodeinService
import org.kodein.di.DI

class PhotoService(di: DI) : KodeinService(di) {
    fun getAllTypes(): List<StatusDto> =
        AppConf.PhotoType.entries.map {
            StatusDto(it.id, it.name)
        }
}