package com.fleetmate.lib.data.model.report

import com.fleetmate.lib.data.dto.report.ReportCreateDto
import com.fleetmate.lib.data.dto.report.ReportUpdateDto
import com.fleetmate.lib.data.model.car.CarModel
import com.fleetmate.lib.exceptions.InternalServerException
import com.fleetmate.lib.data.model.trip.TripModel
import com.fleetmate.lib.data.model.user.UserModel
import com.fleetmate.lib.utils.database.BaseIntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.collections.first
import kotlin.collections.firstOrNull
import kotlin.collections.toList

object ReportModel : BaseIntIdTable(){
    val mileage = double("mileage")
    val avgSpeed = double("avgSpeed")
    val trip = reference("trip", TripModel)
    val car = reference("car", CarModel)
    val driver = reference("driver", UserModel)


    fun getOne(id: Int): ResultRow? = transaction {
        selectAll().where(ReportModel.id eq id).firstOrNull()
    }

    fun getAll(start: Long, finish: Long, userId: Int): List<ResultRow> = transaction {
        val startTime = LocalDateTime.ofEpochSecond(start, 0, ZoneOffset.UTC)
        val finishTime = LocalDateTime.ofEpochSecond(finish, 0, ZoneOffset.UTC)
        join(
            TripModel, JoinType.INNER, trip, TripModel.id
        ).select(
            ReportModel.id,
            car,
            TripModel.id,
            TripModel.route,
            TripModel.keyReturn
        ).where{
            driver eq userId
        }.andWhere{
            createdAt less finishTime
        }.andWhere {
            createdAt greater startTime
        }.toList()
    }

    fun create(reportCreateDto: ReportCreateDto): ResultRow = transaction {
        (ReportModel.insert {
            it[mileage] = reportCreateDto.mileage
            it[avgSpeed] = reportCreateDto.avgSpeed
            it[trip] = reportCreateDto.trip
            it[car] = reportCreateDto.car
            it[driver] = reportCreateDto.driver

        }.resultedValues ?: throw InternalServerException("Failed to create report")).first()
    }

    fun update(id: Int, reportUpdateDto: ReportUpdateDto): Boolean = transaction {
        ReportModel.update({ ReportModel.id eq id })
        {
            if (reportUpdateDto.mileage != null){
                it[mileage] = reportUpdateDto.mileage
            }
            if (reportUpdateDto.avgSpeed != null){
                it[avgSpeed] = reportUpdateDto.avgSpeed
            }
            if (reportUpdateDto.trip != null){
                it[trip] = reportUpdateDto.trip
            }
            if (reportUpdateDto.car != null){
                it[car] = reportUpdateDto.car
            }
            if (reportUpdateDto.driver != null){
                it[driver] = reportUpdateDto.driver
            }
        } != 0
    }

    fun delete(id: Int): Boolean = transaction {
        ReportModel.deleteWhere{ ReportModel.id eq id} != 0
    }
}