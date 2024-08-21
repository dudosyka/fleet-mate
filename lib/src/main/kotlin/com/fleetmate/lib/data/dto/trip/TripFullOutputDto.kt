package com.fleetmate.lib.data.dto.trip

import com.fleetmate.lib.conf.AppConf
import com.fleetmate.lib.data.dto.check.CheckOutputDto
import com.fleetmate.lib.data.dto.car.CarOutputDto
import com.fleetmate.lib.data.dto.user.UserOutputDto
import com.fleetmate.lib.data.model.trip.TripModel
import kotlinx.serialization.Serializable


@Serializable
data class TripFullOutputDto (
    val id: Int,
    val keyAcceptance: Long,
    val status: AppConf.TripStatus,
    val mechanicCheckBeforeTrip: CheckOutputDto?,
    val driverCheckBeforeTrip: CheckOutputDto?,
    val mechanicCheckAfterTrip: CheckOutputDto? = null,
    val driverCheckAfterTrip: CheckOutputDto? = null,
    val keyReturn: Long? = null,
    val route: String? = null,
    val speedInfo: List<Double> = listOf(),
    val avgSpeed: Double? = null,
    val driver: UserOutputDto,
    val car: CarOutputDto,
    val questionable: Boolean? = null,
    val needWashing: Boolean? = null,
    val washHappen: Boolean? = null
) {
    constructor(resultRow: TripModel.TripResultRow):
            this(
                resultRow.tripData[TripModel.id].value,
                resultRow.tripData[TripModel.keyAcceptance],
                AppConf.TripStatus.valueOf(resultRow.tripData[TripModel.status]),
                CheckOutputDto.constructFromNull(
                    resultRow.mechanicCheckBefore
                ),
                CheckOutputDto.constructFromNull(
                    resultRow.driverCheckBefore
                ),
                CheckOutputDto.constructFromNull(
                    resultRow.mechanicCheckAfter
                ),
                CheckOutputDto.constructFromNull(
                    resultRow.driverCheckAfter
                ),
                resultRow.tripData[TripModel.keyReturn],
                resultRow.tripData[TripModel.route],
                resultRow.tripData[TripModel.speedInfo],
                resultRow.tripData[TripModel.avgSpeed],
                UserOutputDto(
                    resultRow.tripData
                ),
                CarOutputDto(
                    resultRow.tripData
                ),
                resultRow.tripData[TripModel.questionable],
                resultRow.tripData[TripModel.needWashing],
                resultRow.tripData[TripModel.washHappen],
            )
}