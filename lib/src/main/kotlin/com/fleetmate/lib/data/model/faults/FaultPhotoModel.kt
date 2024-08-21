package com.fleetmate.lib.data.model.faults

import com.fleetmate.lib.data.model.photo.PhotoModel
import com.fleetmate.lib.utils.database.BaseIntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.transactions.transaction

object FaultPhotoModel: BaseIntIdTable() {
    val photo = reference("photo", PhotoModel)
    private val fault = reference("fault", FaultsModel)

    fun appendToFault(photos: List<Int>, faultId: Int): Unit = transaction {
        batchInsert(photos) {
            this[photo] = it
            this[fault] = faultId
        }
    }

    fun getByFault(faultId: Int): List<ResultRow> = transaction {
        leftJoin(PhotoModel)
            .select(
                PhotoModel.columns
            )
            .where {
                fault eq faultId
            }
            .toList()
    }
}