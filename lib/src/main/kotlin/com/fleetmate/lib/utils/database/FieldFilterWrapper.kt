package com.fleetmate.lib.utils.database

import com.fleetmate.lib.exceptions.InternalServerException
import kotlinx.serialization.Serializable

@Serializable
data class FieldFilterWrapper <T> (
    val specificValue: T? = null,
    val topBound: T? = null,
    val bottomBound: T? = null
) {
    fun createRangeFromSpecificValue(rangeSize: Long): FieldFilterWrapper<Long>? {
        if (specificValue == null)
            return null
        if (specificValue is Long) {
            return FieldFilterWrapper(
                    bottomBound = specificValue,
                    topBound = specificValue + rangeSize
                )
        } else
            throw InternalServerException("Bad filter provided")
    }
}