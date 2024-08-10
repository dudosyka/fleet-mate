package com.fleetmate.lib.model.photo

import com.fleetmate.lib.dto.photo.PhotoCreateDto
import com.fleetmate.lib.dto.photo.PhotoUpdateDto
import com.fleetmate.lib.exceptions.InternalServerException
import com.fleetmate.lib.model.post.PostModel
import com.fleetmate.lib.utils.database.BaseIntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import kotlin.collections.first
import kotlin.collections.firstOrNull
import kotlin.collections.toList
import kotlin.text.isNullOrEmpty

object PhotoModel : BaseIntIdTable() {
    val link = text("link")
    val date = timestamp("date")
    val type = text("type")

    fun getOne(id: Int): ResultRow? = transaction {
        select(PhotoModel.id, link, date, type).where {
            PhotoModel.id eq id
        }.firstOrNull()
    }

    fun getAll(): List<ResultRow> = transaction {
        select(PhotoModel.id, link, date, type).toList()
    }

    fun create(photoCreateDto: PhotoCreateDto): ResultRow = transaction {
        (PhotoModel.insert {
            it[link] = photoCreateDto.link
            it[date] =
                LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(photoCreateDto.date),
                    ZoneId.systemDefault()
                ).toInstant(ZoneOffset.UTC)
            it[type] = photoCreateDto.type
        }.resultedValues ?: throw InternalServerException("Failed to create photo")).first()
    }

    fun update(id: Int, photoUpdateDto: PhotoUpdateDto): Boolean = transaction {
        PostModel.update({ PhotoModel.id eq id })
        {
            if (!photoUpdateDto.link.isNullOrEmpty()){
                it[link] = photoUpdateDto.link
            }
            if (photoUpdateDto.date != null){
                it[date] =
                    LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(photoUpdateDto.date),
                        ZoneId.systemDefault()
                    ).toInstant(ZoneOffset.UTC)
            }
            if (!photoUpdateDto.type.isNullOrEmpty()){
                it[type] = photoUpdateDto.type
            }
        } != 0
    }

    fun delete(id: Int): Boolean = transaction {
        PhotoModel.deleteWhere{ PhotoModel.id eq id} != 0
    }
}