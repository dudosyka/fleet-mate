package com.fleetmate.lib.utils.database

import kotlinx.serialization.json.Json
import org.jetbrains.exposed.dao.EntityChangeType
import org.jetbrains.exposed.dao.EntityHook
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.toEntity
import org.jetbrains.exposed.sql.*
import com.fleetmate.lib.shared.conf.AppConf
import java.time.LocalDateTime

abstract class BaseIntEntityClass<Output : SerializableAny, E : BaseIntEntity<Output>>(table: BaseIntIdTable) : IntEntityClass<E>(table) {

    val json = Json { ignoreUnknownKeys = true }

    init {
        EntityHook.subscribe { action ->
            if (action.changeType == EntityChangeType.Updated) {
                try {
                    action.toEntity(this)?.updatedAt = LocalDateTime.now()
                } catch (_: Exception) { }
            }
        }
    }

    fun wrapQuery(query: Query): List<Output> =
        wrapRows(query).map { it.toOutputDto() }

    fun <T : Number> SqlExpressionBuilder.rangeCond(fieldFilterWrapper: FieldFilterWrapper<T>?, defaultCond: Op<Boolean>, field: Column<T>, fieldMin: T, fieldMax: T): Op<Boolean> =
        if (fieldFilterWrapper == null)
            defaultCond
        else {
            if (fieldFilterWrapper.specificValue != null) {
                field eq fieldFilterWrapper.specificValue
            } else {
                val min = (fieldFilterWrapper.bottomBound ?: fieldMin).toDouble()
                val max = (fieldFilterWrapper.topBound ?: fieldMax).toDouble()
                (field lessEq max) and (field greaterEq min)
            }
        }

    fun <T: Number> SqlExpressionBuilder.nullableRangeCond(fieldFilterWrapper: FieldFilterWrapper<T>?, defaultCond: Op<Boolean>, field: Column<T?>, fieldMin: T, fieldMax: T): Op<Boolean> =
        if (fieldFilterWrapper == null)
            defaultCond
        else {
            if (fieldFilterWrapper.specificValue != null) {
                field eq fieldFilterWrapper.specificValue
            } else {
                val min = (fieldFilterWrapper.bottomBound ?: fieldMin).toDouble()
                val max = (fieldFilterWrapper.topBound ?: fieldMax).toDouble()
                (field lessEq max) and (field greaterEq min)
            }
        }


    fun SqlExpressionBuilder.nullableListCond(filter: List<Int>?, defaultCond: Op<Boolean>, field: Column<EntityID<Int>?>): Op<Boolean> =
        if (filter == null)
            defaultCond
        else
            field inList filter


    fun SqlExpressionBuilder.listCond(filter: List<Int>?, defaultCond: Op<Boolean>, field: Column<EntityID<Int>>): Op<Boolean> =
        if (filter == null)
            defaultCond
        else
            field inList filter


    fun SqlExpressionBuilder.stringListCond(filter: List<String>, defaultCond: Op<Boolean>, field: Column<String>): Op<Boolean> =
        if (filter.isEmpty())
            defaultCond
        else
            field inList filter


    fun SqlExpressionBuilder.likeCond(filter: String?, defaultCond: Op<Boolean>, field: Column<String>): Op<Boolean> =
        if (filter == null)
            defaultCond
        else
            field like "%$filter%"

    fun SqlExpressionBuilder.nullableLikeCond(filter: String?, defaultCond: Op<Boolean>, field: Column<String?>): Op<Boolean> =
        if (filter == null)
            defaultCond
        else
            field like "%$filter%"

    fun SqlExpressionBuilder.booleanCond(filter: Boolean?, defaultCond: Op<Boolean>, field: Column<Boolean>, reversed: Boolean = false): Op<Boolean> =
        if (filter == null)
            defaultCond
        else
            if (reversed)
                field neq filter
            else
                field eq filter

    fun SqlExpressionBuilder.timeCond(range: Pair<Long?, Long?>?, createdAt: Column<LocalDateTime>): Op<Boolean> {
        return if (range == null)
            createdAt.isNotNull()
        else {
            val leftBound = range.first.let {
                if (it == null)
                    LocalDateTime.ofEpochSecond(0, 0, AppConf.zoneOffset)
                else {
                    val seconds: Long = it / 1000
                    val nanos: Int = (it % 1000).toInt()
                    LocalDateTime.ofEpochSecond(seconds, nanos, AppConf.zoneOffset)
                }
            }
            val rightBound = range.second.let {
                if (it == null) //Use INT.MAX * 2 (2106 year) because Long.MAX_VALUE is too big for timestamp
                    LocalDateTime.ofEpochSecond(Int.MAX_VALUE.toLong() * 2, 0, AppConf.zoneOffset)
                else {
                    val seconds: Long = it / 1000
                    val nanos: Int = (it % 1000).toInt()
                    LocalDateTime.ofEpochSecond(seconds, nanos, AppConf.zoneOffset)
                }
            }
            (createdAt lessEq rightBound) and
                    (createdAt greaterEq leftBound)
        }
    }
}