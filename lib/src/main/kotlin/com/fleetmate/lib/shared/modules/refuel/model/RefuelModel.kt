package com.fleetmate.lib.shared.modules.refuel.model


import com.fleetmate.lib.shared.modules.photo.data.model.PhotoModel
import com.fleetmate.lib.shared.modules.trip.model.TripModel
import com.fleetmate.lib.utils.database.BaseIntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object RefuelModel : BaseIntIdTable() {
    val volume = double("volume")
    val trip = reference("trip", TripModel, ReferenceOption.RESTRICT, ReferenceOption.CASCADE)
    val billPhoto = reference("bill_photo", PhotoModel, ReferenceOption.RESTRICT, ReferenceOption.CASCADE)
}