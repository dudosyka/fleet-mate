package com.fleetmate.lib.shared.modules.photo.data.model


import com.fleetmate.lib.exceptions.InternalServerException
import com.fleetmate.lib.shared.modules.photo.data.dto.PhotoCreateDto
import com.fleetmate.lib.utils.database.BaseIntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

object PhotoModel : BaseIntIdTable() {
    val link = text("link")
    val original = text("original").nullable().default(null)
    val type = text("type")


    fun create(photoCreateDto: PhotoCreateDto) = transaction {
        (PhotoModel.insert {
            it[link] = photoCreateDto.link
            it[type] = photoCreateDto.type.name
            it[original] = photoCreateDto.original
        }.resultedValues ?: throw InternalServerException("Failed to create photo")).first()
    }

    fun delete(id: Int): Boolean = transaction {
        PhotoModel.deleteWhere{ PhotoModel.id eq id} != 0
    }
}