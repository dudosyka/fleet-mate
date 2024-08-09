package com.fleetmate.trip.modules.report.data.model

import com.fleetmate.lib.exceptions.InternalServerException
import com.fleetmate.lib.model.automobile.AutomobileModel
import com.fleetmate.lib.model.trip.TripModel
import com.fleetmate.lib.model.user.UserModel
import com.fleetmate.lib.utils.database.BaseIntIdTable
import com.fleetmate.trip.modules.report.data.dto.ReportCreateDto
import com.fleetmate.trip.modules.report.data.dto.ReportUpdateDto
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import kotlin.collections.first
import kotlin.collections.firstOrNull
import kotlin.collections.toList

object ReportModel : BaseIntIdTable(){
    val mileage = float("mileage")
    val avgSpeed = float("avgSpeed")
    val trip = reference("trip", TripModel)
    val automobile = reference("automobile", AutomobileModel)
    val driver = reference("driver", UserModel)


    fun getOne(id: Int): ResultRow? = transaction {

        ReportModel.select(
            ReportModel.id,
            mileage,
            avgSpeed,
            trip,
            automobile,
            driver
        ).where(
            ReportModel.id eq id
        ).firstOrNull()
    }

    fun getAll(): List<ResultRow> = transaction {
        select(ReportModel.id, mileage, avgSpeed, trip, automobile, driver).toList()
    }

    fun create(reportCreateDto: ReportCreateDto): ResultRow = transaction {
        (ReportModel.insert {
            it[mileage] = reportCreateDto.mileage
            it[avgSpeed] = reportCreateDto.avgSpeed
            it[trip] = reportCreateDto.trip
            it[automobile] = reportCreateDto.automobile
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
            if (reportUpdateDto.automobile != null){
                it[automobile] = reportUpdateDto.automobile
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