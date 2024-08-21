package com.fleetmate.faults.modules.photo.service

import com.fleetmate.lib.dto.photo.PhotoCreateDto
import com.fleetmate.lib.dto.photo.PhotoOutputDto
import com.fleetmate.lib.model.photo.PhotoModel
import com.fleetmate.lib.utils.kodein.KodeinService
import org.kodein.di.DI

class PhotoService(di: DI) : KodeinService(di)  {
    fun create(photoCreateDto: PhotoCreateDto): PhotoOutputDto =
        PhotoOutputDto(PhotoModel.create(photoCreateDto))
}