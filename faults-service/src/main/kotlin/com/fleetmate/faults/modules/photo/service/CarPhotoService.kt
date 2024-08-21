package com.fleetmate.faults.modules.photo.service

import com.fleetmate.lib.data.dto.car.photo.CarPhotoCreateDto
import com.fleetmate.lib.data.model.car.CarPhotoModel
import com.fleetmate.lib.utils.files.PhotoService
import com.fleetmate.lib.utils.kodein.KodeinService
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.DI
import org.kodein.di.instance

class CarPhotoService(di: DI) : KodeinService(di) {
    private val photoService: PhotoService by instance()
    fun uploadPhotos(carPhotoCreateDto: CarPhotoCreateDto) = transaction {
        val photoList = carPhotoCreateDto.photoList.map {
            photoService.upload(it).id
        }

        CarPhotoModel.create(carPhotoCreateDto, photoList)
    }
}