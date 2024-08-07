package com.fleetmate.trip.modules.automobile.data.model

import com.fleetmate.lib.model.automobile.AutomobileModel
import com.fleetmate.lib.utils.database.BaseIntIdTable

object AutomobileToAutomobilePhotoModel : BaseIntIdTable(){
    val automobileId = reference("automobileId", AutomobileModel)
    val automobilePhotoId = reference("automobilePhotoId", AutomobilePhotoModel)
}