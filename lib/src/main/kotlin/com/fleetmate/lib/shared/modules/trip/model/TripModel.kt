package com.fleetmate.lib.shared.modules.trip.model


import com.fleetmate.lib.shared.modules.car.model.CarModel
import com.fleetmate.lib.shared.modules.check.model.CheckModel
import com.fleetmate.lib.shared.modules.user.model.UserModel
import com.fleetmate.lib.utils.database.BaseIntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll

object TripModel : BaseIntIdTable() {
    val car = reference("car", CarModel, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val driver = reference("driver", UserModel, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val status = text("status")
    val route = text("route").default("")
    val avgSpeed = double("avg_speed").default(0.0)
    val mileage = double("mileage").default(0.0)
    val keyAcceptance = long("key_acceptance")
    val driverCheckBeforeTrip = reference("driver_check_before_trip", CheckModel, ReferenceOption.SET_NULL, ReferenceOption.CASCADE).nullable().default(null)
    val driverCheckAfterTrip = reference("driver_check_after_trip", CheckModel, ReferenceOption.SET_NULL, ReferenceOption.CASCADE).nullable().default(null)
    val mechanicCheckBeforeTrip = reference("mechanic_check_before_trip", CheckModel, ReferenceOption.SET_NULL, ReferenceOption.CASCADE).nullable().default(null)
    val mechanicCheckAfterTrip = reference("mechanic_check_after_trip", CheckModel, ReferenceOption.SET_NULL, ReferenceOption.CASCADE).nullable().default(null)
    val keyReturn = long("key_return").nullable().default(null)
    val needWashing = bool("need_washing").default(false)
    val needRefuel = bool("need_refuel").default(false)

    fun getCarActiveTrip(carId: Int): ResultRow? =
        selectAll()
            .where {
                car eq carId
            }.firstOrNull()

    fun getUserActiveTrip(driverId: Int): ResultRow? =
        selectAll()
            .where {
                driver eq driverId
            }.firstOrNull()
}