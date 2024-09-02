package com.fleetmate.lib.shared.modules.violation.model


import com.fleetmate.lib.shared.modules.car.model.CarModel
import com.fleetmate.lib.shared.modules.trip.model.TripModel
import com.fleetmate.lib.shared.modules.user.model.UserModel
import com.fleetmate.lib.utils.database.BaseIntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

/*


    "carFilter": {
        "registrationNumber": "к027рв178",
        "carType": [],
        "status": []
    },
    "startDateRange": {
        "topBound": 13421312312,
        "bottomBound": 12312412
    },
    "endDateRange": {
        "topBound": 13421312312,
        "bottomBound": 12312412
    },
    "driverFilter": {
        "fullName": "Full name..."
    }

 */

object ViolationModel : BaseIntIdTable() {
    val type = text("type")
    val registeredAt = long("registered_at")
    val duration = long("duration").default(0L)
    val hidden = bool("hidden").default(true)
    val driver = reference("driver", UserModel, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val trip = reference("trip", TripModel, ReferenceOption.RESTRICT, ReferenceOption.RESTRICT)
    val car = reference("car", CarModel, ReferenceOption.SET_NULL).nullable()
    val comment = text("comment").nullable().default(null)
}