package com.fleetmate.trip.modules.photo.service

import com.fleetmate.lib.dto.photo.PhotoCreateDto
import com.fleetmate.lib.dto.photo.PhotoOutputDto
import com.fleetmate.lib.dto.photo.PhotoUpdateDto
import com.fleetmate.lib.model.photo.PhotoModel
import com.fleetmate.lib.utils.kodein.KodeinService
import org.kodein.di.DI
import kotlin.collections.map

class PhotoService(di: DI) : KodeinService(di) {
    fun getOne(id: Int): PhotoOutputDto? {
        return PhotoOutputDto(PhotoModel.getOne(id) ?: return null)
    }

    fun getAll(): List<PhotoOutputDto> {
        return PhotoModel.getAll().map {
            PhotoOutputDto(it)
        }
    }

    fun create(photoCreateDto: PhotoCreateDto): PhotoOutputDto =
        PhotoOutputDto(PhotoModel.create(photoCreateDto))

    fun update(id: Int, photoUpdateDto: PhotoUpdateDto): Boolean =
        PhotoModel.update(id, photoUpdateDto)

    fun delete(id: Int): Boolean =
        PhotoModel.delete(id)
}