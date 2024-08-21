package com.fleetmate.lib.data.model.car

import com.fleetmate.lib.data.dto.car.photo.CarPhotoCreateDto
import com.fleetmate.lib.data.model.trip.TripModel
import com.fleetmate.lib.data.model.user.UserModel
import com.fleetmate.lib.data.model.photo.PhotoModel
import com.fleetmate.lib.utils.database.BaseIntIdTable
import org.jetbrains.exposed.sql.*

object CarPhotoModel : BaseIntIdTable() {
    val car = reference("car", CarModel, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val trip = reference("trip", TripModel).nullable().default(null)
    val driver = reference("driver", UserModel, ReferenceOption.RESTRICT, ReferenceOption.RESTRICT)
    val photo = reference("photo", PhotoModel, ReferenceOption.CASCADE, ReferenceOption.CASCADE)

    fun create(carPhotoCreateDto: CarPhotoCreateDto, photoList: List<Int>) {
        batchInsert(photoList) {
            this[car] = carPhotoCreateDto.car
            this[trip] = carPhotoCreateDto.trip
            this[driver] = carPhotoCreateDto.driver
            this[photo] = it
        }
    }
}