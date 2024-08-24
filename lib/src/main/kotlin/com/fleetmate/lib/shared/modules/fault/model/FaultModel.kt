package com.fleetmate.lib.shared.modules.fault.model


import com.fleetmate.lib.shared.modules.car.model.CarModel
import com.fleetmate.lib.shared.modules.car.model.part.CarPartModel
import com.fleetmate.lib.shared.modules.trip.model.TripModel
import com.fleetmate.lib.shared.modules.user.model.UserModel
import com.fleetmate.lib.utils.database.BaseIntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object FaultModel : BaseIntIdTable() {
    val car = reference("car", CarModel, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val carPart = reference("car_part", CarPartModel, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val trip = reference("trip", TripModel, ReferenceOption.SET_NULL, ReferenceOption.CASCADE).nullable()
    val author = reference("author", UserModel, ReferenceOption.RESTRICT, ReferenceOption.CASCADE)
    val comment = text("comment")
    val critical = bool("critical").default(true)
    val status = text("status")
}