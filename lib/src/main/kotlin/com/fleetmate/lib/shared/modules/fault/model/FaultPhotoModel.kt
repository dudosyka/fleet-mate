package com.fleetmate.lib.shared.modules.fault.model


import com.fleetmate.lib.shared.modules.photo.data.dto.PhotoOutputDto
import com.fleetmate.lib.shared.modules.photo.data.model.PhotoModel
import com.fleetmate.lib.utils.database.BaseIntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.batchInsert

object FaultPhotoModel : BaseIntIdTable() {
    val fault = reference("fault", FaultModel, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val photo = reference("photo", PhotoModel, ReferenceOption.CASCADE, ReferenceOption.CASCADE)

    fun append(faultId: Int, photos: List<PhotoOutputDto>) {
        FaultPhotoModel.batchInsert(photos) {
            this[fault] = faultId
            this[photo] = it.id
        }
    }
}