package com.fleetmate.lib.model.post

import com.fleetmate.lib.dto.post.PostCreateDto
import com.fleetmate.lib.exceptions.InternalServerException
import com.fleetmate.lib.utils.database.BaseIntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import kotlin.collections.first
import kotlin.collections.firstOrNull
import kotlin.collections.toList

object PostModel : BaseIntIdTable() {
    val name = text("name")

    fun getOne(id: Int): ResultRow? = transaction {
        select(PostModel.id, name).where {
            PostModel.id eq id
        }.firstOrNull()
    }

    fun getAll(): List<ResultRow> = transaction {
        select(PostModel.id, name).toList()
    }

    fun create(postCreateDto: PostCreateDto): ResultRow = transaction {
        (PostModel.insert {
            it[name] = postCreateDto.name
        }.resultedValues ?: throw InternalServerException("Failed to create post")).first()
    }

    fun update(id: Int, postUpdateDto: PostCreateDto): Boolean = transaction {
        PostModel.update({ PostModel.id eq id }) { it[name] = postUpdateDto.name } != 0
    }

    fun delete(id: Int): Boolean = transaction {
        PostModel.deleteWhere{ PostModel.id eq id} != 0
    }
}