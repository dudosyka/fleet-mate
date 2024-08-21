package com.fleetmate.lib.data.model.car

import com.fleetmate.lib.utils.database.BaseIntIdTable

object CarToCarPhotoModel : BaseIntIdTable(){
    val carId = reference("carId", CarModel)
    val carPhotoId = reference("carPhotoId", CarPhotoModel)
}