package com.fleetmate.lib.dto.trip

import com.fleetmate.lib.dto.automobile.AutomobileOutputDto
import com.fleetmate.lib.dto.automobile.AutomobileTypeOutputDto
import com.fleetmate.lib.dto.check.CheckOutputDto
import com.fleetmate.lib.dto.division.DivisionOutputDto
import com.fleetmate.lib.dto.post.PostOutputDto
import com.fleetmate.lib.dto.user.UserOutputDto
import com.fleetmate.lib.model.automobile.AutomobileModel
import com.fleetmate.lib.model.automobile.AutomobileTypeModel
import com.fleetmate.lib.model.check.CheckModel
import com.fleetmate.lib.model.division.DivisionModel
import com.fleetmate.lib.model.post.PostModel
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
    val automobile: AutomobileOutputDto?,
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
                        PostOutputDto(
                            resultRows.first()?.get(PostModel.id)?.value,
                            resultRows.first()?.get(PostModel.name)
                        ),
                        DivisionOutputDto(
                            resultRows.first()?.get(DivisionModel.id)?.value,
                            resultRows.first()?.get(DivisionModel.name)
                        )
                    ),
                    resultRows.first()?.get(CheckModel.startTime).toString(),
                    resultRows.first()?.get(CheckModel.finishTime).toString(),
                    resultRows.first()?.get(CheckModel.timeExceeding),
                ),
                CheckOutputDto(
                    resultRows[1]?.get(CheckModel.id)?.value,
                    UserOutputDto(
                        resultRows[1]?.get(UserModel.id)?.value,
                        resultRows[1]?.get(UserModel.fullName),
                        resultRows[1]?.get(UserModel.email),
                        resultRows[1]?.get(UserModel.phoneNumber),
                        PostOutputDto(
                            resultRows[1]?.get(PostModel.id)?.value,
                            resultRows[1]?.get(PostModel.name)
                        ),
                        DivisionOutputDto(
                            resultRows[1]?.get(DivisionModel.id)?.value,
                            resultRows[1]?.get(DivisionModel.name)
                        )
                    ),
                    resultRows[1]?.get(CheckModel.startTime).toString(),
                    resultRows[1]?.get(CheckModel.finishTime).toString(),
                    resultRows[1]?.get(CheckModel.timeExceeding),
                ),
                CheckOutputDto(
                    resultRows[2]?.get(CheckModel.id)?.value,
                    UserOutputDto(
                        resultRows[2]?.get(UserModel.id)?.value,
                        resultRows[2]?.get(UserModel.fullName),
                        resultRows[2]?.get(UserModel.email),
                        resultRows[2]?.get(UserModel.phoneNumber),
                        PostOutputDto(
                            resultRows[2]?.get(PostModel.id)?.value,
                            resultRows[2]?.get(PostModel.name)
                        ),
                        DivisionOutputDto(
                            resultRows[2]?.get(DivisionModel.id)?.value,
                            resultRows[2]?.get(DivisionModel.name)
                        )
                    ),
                    resultRows[2]?.get(CheckModel.startTime).toString(),
                    resultRows[2]?.get(CheckModel.finishTime).toString(),
                    resultRows[2]?.get(CheckModel.timeExceeding),
                ),
                CheckOutputDto(
                    resultRows[3]?.get(CheckModel.id)?.value,
                    UserOutputDto(
                        resultRows[3]?.get(UserModel.id)?.value,
                        resultRows[3]?.get(UserModel.fullName),
                        resultRows[3]?.get(UserModel.email),
                        resultRows[3]?.get(UserModel.phoneNumber),
                        PostOutputDto(
                            resultRows[3]?.get(PostModel.id)?.value,
                            resultRows[3]?.get(PostModel.name)
                        ),
                        DivisionOutputDto(
                            resultRows[3]?.get(DivisionModel.id)?.value,
                            resultRows[3]?.get(DivisionModel.name)
                        )
                    ),
                    resultRows[3]?.get(CheckModel.startTime).toString(),
                    resultRows[3]?.get(CheckModel.finishTime).toString(),
                    resultRows[3]?.get(CheckModel.timeExceeding),
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
                    PostOutputDto(
                        resultRows[4]?.get(PostModel.id)?.value,
                        resultRows[4]?.get(PostModel.name)
                    ),
                    DivisionOutputDto(
                        resultRows[4]?.get(DivisionModel.id)?.value,
                        resultRows[4]?.get(DivisionModel.name)
                    )
                ),
                AutomobileOutputDto(
                    resultRows[5]?.get(AutomobileModel.id)?.value,
                    resultRows[5]?.get(AutomobileModel.stateNumber),
                    resultRows[5]?.get(AutomobileModel.fuelLevel),
                    resultRows[5]?.get(AutomobileModel.mileage),
                    resultRows[5]?.get(AutomobileModel.additionDate).toString(),
                    AutomobileTypeOutputDto(
                        resultRows[5]?.get(AutomobileTypeModel.id)?.value,
                        resultRows[5]?.get(AutomobileTypeModel.name),
                        resultRows[5]?.get(AutomobileTypeModel.category),
                        resultRows[5]?.get(AutomobileTypeModel.speedLimit),
                        resultRows[5]?.get(AutomobileTypeModel.speedError),
                        resultRows[5]?.get(AutomobileTypeModel.avgFuelConsumption)
                    )
                ),
                resultRows.last()?.get(TripModel.questionable),
                resultRows.last()?.get(TripModel.needWashing),
                resultRows.last()?.get(TripModel.washHappen)
            )
}