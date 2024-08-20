package com.fleetmate.lib.dto.trip

import com.fleetmate.lib.data.dto.car.CarTypeOutputDto
import com.fleetmate.lib.data.dto.car.CheckOutputDto
import com.fleetmate.lib.data.model.car.CarModel
import com.fleetmate.lib.data.model.car.CarTypeModel
import com.fleetmate.lib.dto.car.CarOutputDto
import com.fleetmate.lib.dto.division.DepartmentOutputDto
import com.fleetmate.lib.dto.post.PositionOutputDto
import com.fleetmate.lib.dto.user.UserOutputDto
import com.fleetmate.lib.model.check.CheckModel
import com.fleetmate.lib.model.division.DepartmentModel
import com.fleetmate.lib.model.post.PositionModel
import com.fleetmate.lib.model.trip.TripModel
import com.fleetmate.lib.model.user.UserModel
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import kotlin.toString


@Serializable
class TripFullOutputDto (
    val id: Int?,
    val keyAcceptance: String?,
    val status: String?,
    val mechanicCheckBeforeTrip: CheckOutputDto?,
    val driverCheckBeforeTrip: CheckOutputDto?,
    val mechanicCheckAfterTrip: CheckOutputDto? = null,
    val driverCheckAfterTrip: CheckOutputDto? = null,
    val keyReturn: String? = null,
    val route: String? = null,
    val speedInfo: List<Float>? = null,
    val avgSpeed: Float? = null,
    val driver: UserOutputDto?,
    val car: CarOutputDto?,
    val questionable: Boolean? = null,
    val needWashing: Boolean? = null,
    val washHappen: Boolean? = null
) {
    constructor(resultRows: List<ResultRow?>):
            this(
                resultRows.last()?.get(TripModel.id)?.value,
                resultRows.last()?.get(TripModel.keyAcceptance).toString(),
                resultRows.last()?.get(TripModel.status),
                CheckOutputDto(
                    resultRows.first()?.get(CheckModel.id)?.value,
                    UserOutputDto(
                        resultRows.first()?.get(UserModel.id)?.value,
                        resultRows.first()?.get(UserModel.fullName),
                        resultRows.first()?.get(UserModel.email),
                        resultRows.first()?.get(UserModel.phoneNumber),
                        PositionOutputDto(
                            resultRows.first()?.get(PositionModel.id)?.value,
                            resultRows.first()?.get(PositionModel.name)
                        ),
                        DepartmentOutputDto(
                            resultRows.first()?.get(DepartmentModel.id)?.value,
                            resultRows.first()?.get(DepartmentModel.name)
                        )
                    ),
                    resultRows.first()?.get(CheckModel.startTime).toString().toLong(),
                    resultRows.first()?.get(CheckModel.finishTime).toString().toLong(),
                    resultRows.first()?.get(CheckModel.timeExceeded),
                ),
                CheckOutputDto(
                    resultRows[1]?.get(CheckModel.id)?.value,
                    UserOutputDto(
                        resultRows[1]?.get(UserModel.id)?.value,
                        resultRows[1]?.get(UserModel.fullName),
                        resultRows[1]?.get(UserModel.email),
                        resultRows[1]?.get(UserModel.phoneNumber),
                        PositionOutputDto(
                            resultRows[1]?.get(PositionModel.id)?.value,
                            resultRows[1]?.get(PositionModel.name)
                        ),
                        DepartmentOutputDto(
                            resultRows[1]?.get(DepartmentModel.id)?.value,
                            resultRows[1]?.get(DepartmentModel.name)
                        )
                    ),
                    resultRows[1]?.get(CheckModel.startTime).toString().toLong(),
                    resultRows[1]?.get(CheckModel.finishTime).toString().toLong(),
                    resultRows[1]?.get(CheckModel.timeExceeded),
                ),
                CheckOutputDto(
                    resultRows[2]?.get(CheckModel.id)?.value,
                    UserOutputDto(
                        resultRows[2]?.get(UserModel.id)?.value,
                        resultRows[2]?.get(UserModel.fullName),
                        resultRows[2]?.get(UserModel.email),
                        resultRows[2]?.get(UserModel.phoneNumber),
                        PositionOutputDto(
                            resultRows[2]?.get(PositionModel.id)?.value,
                            resultRows[2]?.get(PositionModel.name)
                        ),
                        DepartmentOutputDto(
                            resultRows[2]?.get(DepartmentModel.id)?.value,
                            resultRows[2]?.get(DepartmentModel.name)
                        )
                    ),
                    resultRows[2]?.get(CheckModel.startTime).toString().toLong(),
                    resultRows[2]?.get(CheckModel.finishTime).toString().toLong(),
                    resultRows[2]?.get(CheckModel.timeExceeded),
                ),
                CheckOutputDto(
                    resultRows[3]?.get(CheckModel.id)?.value,
                    UserOutputDto(
                        resultRows[3]?.get(UserModel.id)?.value,
                        resultRows[3]?.get(UserModel.fullName),
                        resultRows[3]?.get(UserModel.email),
                        resultRows[3]?.get(UserModel.phoneNumber),
                        PositionOutputDto(
                            resultRows[3]?.get(PositionModel.id)?.value,
                            resultRows[3]?.get(PositionModel.name)
                        ),
                        DepartmentOutputDto(
                            resultRows[3]?.get(DepartmentModel.id)?.value,
                            resultRows[3]?.get(DepartmentModel.name)
                        )
                    ),
                    resultRows[3]?.get(CheckModel.startTime).toString().toLong(),
                    resultRows[3]?.get(CheckModel.finishTime).toString().toLong(),
                    resultRows[3]?.get(CheckModel.timeExceeded),
                ),
                resultRows.last()?.get(TripModel.keyReturn).toString(),
                resultRows.last()?.get(TripModel.route),
                resultRows.last()?.get(TripModel.speedInfo),
                resultRows.last()?.get(TripModel.avgSpeed),

                UserOutputDto(
                    resultRows[4]?.get(UserModel.id)?.value,
                    resultRows[4]?.get(UserModel.fullName),
                    resultRows[4]?.get(UserModel.email),
                    resultRows[4]?.get(UserModel.phoneNumber),
                    PositionOutputDto(
                        resultRows[4]?.get(PositionModel.id)?.value,
                        resultRows[4]?.get(PositionModel.name)
                    ),
                    DepartmentOutputDto(
                        resultRows[4]?.get(DepartmentModel.id)?.value,
                        resultRows[4]?.get(DepartmentModel.name)
                    )
                ),
                CarOutputDto(
                    resultRows[5]?.get(CarModel.id)?.value,
                    resultRows[5]?.get(CarModel.registrationNumber),
                    resultRows[5]?.get(CarModel.fuelLevel),
                    resultRows[5]?.get(CarModel.mileage),
                    resultRows[5]?.get(CarModel.dateAdded).toString(),
                    CarTypeOutputDto(
                        resultRows[5]?.get(CarTypeModel.id)?.value,
                        resultRows[5]?.get(CarTypeModel.name),
                        resultRows[5]?.get(CarTypeModel.category),
                        resultRows[5]?.get(CarTypeModel.speedLimit),
                        resultRows[5]?.get(CarTypeModel.speedError),
                        resultRows[5]?.get(CarTypeModel.avgFuelConsumption)
                    )
                ),
                resultRows.last()?.get(TripModel.questionable),
                resultRows.last()?.get(TripModel.needWashing),
                resultRows.last()?.get(TripModel.washHappen)
            )
}