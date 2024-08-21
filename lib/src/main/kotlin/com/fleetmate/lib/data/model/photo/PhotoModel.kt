package com.fleetmate.lib.data.model.photo

import com.fleetmate.lib.data.dto.photo.PhotoCreateDto
import com.fleetmate.lib.exceptions.InternalServerException
import com.fleetmate.lib.utils.database.BaseIntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.collections.first
import kotlin.collections.firstOrNull
import kotlin.collections.toList

object PhotoModel : BaseIntIdTable() {
    val link = text("link")
    val type = integer("type")
    private val original = text("original").nullable().default(null)

    fun getOne(id: Int): ResultRow? = transaction {
        select(PhotoModel.id, link, createdAt, type).where {
            PhotoModel.id eq id
        }.firstOrNull()
    }

    fun getAll(): List<ResultRow> = transaction {
        select(PhotoModel.id, link, createdAt, type).toList()
    }

    fun create(photoCreateDto: PhotoCreateDto): ResultRow = transaction {
        (PhotoModel.insert {
            it[link] = photoCreateDto.link
            it[type] = photoCreateDto.type.id
            it[original] = photoCreateDto.original
        }.resultedValues ?: throw InternalServerException("Failed to create photo")).first()
    }

    fun delete(id: Int): Boolean = transaction {
        PhotoModel.deleteWhere{ PhotoModel.id eq id} != 0
    }
}