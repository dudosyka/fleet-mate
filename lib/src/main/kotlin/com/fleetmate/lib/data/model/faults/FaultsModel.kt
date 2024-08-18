package com.fleetmate.faults.modules.faults.data.model

import com.fleetmate.faults.modules.faults.data.dto.FaultsCreateDto
import com.fleetmate.faults.modules.faults.data.dto.FaultsUpdateDto
import com.fleetmate.lib.data.model.automobile.AutomobilePartModel
import com.fleetmate.lib.dto.auth.AuthorizedUser
import com.fleetmate.lib.exceptions.InternalServerException
import com.fleetmate.lib.model.automobile.AutomobileModel
import com.fleetmate.lib.model.photo.PhotoModel
import com.fleetmate.lib.model.trip.TripModel
import com.fleetmate.lib.model.user.UserModel
import com.fleetmate.lib.utils.database.BaseIntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.time.LocalDateTime
import java.time.ZoneOffset

object FaultsModel: BaseIntIdTable() {
    val date = timestamp("date")
    val status = text("status")
    val trip = reference("trip", TripModel).nullable().default(null)
    val user = reference("user", UserModel)
    val automobile = reference("automobile", AutomobileModel)
    val photo = reference("photo", PhotoModel).nullable().default(null)
    val comment = text("comment").nullable().default(null)
    val critical = bool("critical")


    fun getOne(id: Int): ResultRow? = transaction {


        (FaultsModel innerJoin AutomobileModel)
            .innerJoin(PhotoModel)
            .select(
                FaultsModel.id,
                date,
                status,
                trip,
                user,
                AutomobileModel.id,
                AutomobileModel.stateNumber,
                PhotoModel.id,
                PhotoModel.link,
                PhotoModel.type
            )
            .where {
            FaultsModel.id eq id
        }.firstOrNull()
    }

    fun getAll(): List<ResultRow> = transaction {
        selectAll().toList()
    }

    fun create(authorizedUser: AuthorizedUser, faultsCreateDto: FaultsCreateDto): ResultRow = transaction {
        val photoId = if (faultsCreateDto.photo != null){
            PhotoModel.create(faultsCreateDto.photo)
        }
        else{
            null
        }

        val fault = (FaultsModel.insert {
            it[date] = LocalDateTime.now().toInstant(ZoneOffset.UTC)
            it[status] = faultsCreateDto.status.name
            if (faultsCreateDto.trip != null){
                it[trip] = faultsCreateDto.trip
            }
            it[user] = authorizedUser.id
            it[automobile] = faultsCreateDto.automobile
            if (faultsCreateDto.comment != null){
                it[comment] = faultsCreateDto.comment
            }
            it[critical] = true
            if (photoId != null){
                it[photo] = photoId[id].value
            }
        }.resultedValues ?: throw InternalServerException("Failed to create fault")).first()

        AutomobilePartModel.addFault(fault[FaultsModel.id].value, faultsCreateDto.automobilePart)

        return@transaction fault
    }

    fun update(id: Int, faultsUpdateDto: FaultsUpdateDto): Boolean = transaction {
        FaultsModel.update({FaultsModel.id eq id }) {
            if (faultsUpdateDto.status != null){
                it[status] = faultsUpdateDto.status.name
            }
            if (faultsUpdateDto.trip != null){
                it[trip] = faultsUpdateDto.trip
            }
            if (faultsUpdateDto.automobile != null){
                it[automobile] = faultsUpdateDto.automobile
            }
            if (faultsUpdateDto.comment != null){
                it[comment] = faultsUpdateDto.comment
            }
            if (faultsUpdateDto.critical != null){
                it[critical] = faultsUpdateDto.critical
            }
        } != 0
    }

    fun delete(id: Int): Boolean = transaction {
        FaultsModel.deleteWhere { FaultsModel.id eq id } != 0
    }
    fun getAllCriticalByAutomobile(automobileId: Int): List<ResultRow> = transaction{
        (FaultsModel innerJoin PhotoModel)
            .select(
                FaultsModel.id,
                date,
                status,
                trip,
                user,
                PhotoModel.id,
                PhotoModel.link,
                PhotoModel.type,
                comment,
                critical
            )
            .where {
                automobile eq automobileId
            }.andWhere {
                critical eq true
            }.toList()
    }

    fun getAllByTrip(tripId: Int): List<ResultRow?> = transaction{
        select(
            FaultsModel.id
        ).where{
            trip eq tripId
        }.toList()
    }
}