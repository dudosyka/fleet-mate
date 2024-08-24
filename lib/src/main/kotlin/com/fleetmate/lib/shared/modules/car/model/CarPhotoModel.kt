package com.fleetmate.lib.shared.modules.car.model


import com.fleetmate.lib.shared.modules.check.model.CheckModel
import com.fleetmate.lib.shared.modules.photo.data.dto.PhotoOutputDto
import com.fleetmate.lib.shared.modules.photo.data.model.PhotoModel
import com.fleetmate.lib.utils.database.BaseIntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.batchInsert

object CarPhotoModel: BaseIntIdTable() {
    val car = reference("car", CarModel, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val check = reference("check", CheckModel, ReferenceOption.SET_NULL, ReferenceOption.CASCADE).nullable().default(null)
    val photo = reference("photo", PhotoModel, ReferenceOption.CASCADE, ReferenceOption.CASCADE)

    fun append(carId: Int, checkId: Int?, photos: List<PhotoOutputDto>) {
        batchInsert(photos) {
            this[car] = carId
            this[check] = checkId
            this[photo] = it.id
        }
    }
}