package com.fleetmate.lib.shared.modules.check.model


import com.fleetmate.lib.shared.modules.car.model.CarModel
import com.fleetmate.lib.shared.modules.user.model.UserModel
import com.fleetmate.lib.utils.database.BaseIntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object CheckModel : BaseIntIdTable() {
    val author = reference("author", UserModel, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val car = reference("car", CarModel, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val startTime = long("start_time")
    val finishTime = long("finish_time").nullable().default(null)
    val timeExceeded = bool("time_exceeded").default(false)
}