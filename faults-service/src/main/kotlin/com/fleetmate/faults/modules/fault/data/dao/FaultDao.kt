package com.fleetmate.faults.modules.fault.data.dao


import com.fleetmate.faults.modules.fault.data.dto.FaultDto
import com.fleetmate.lib.shared.modules.fault.model.FaultModel
import com.fleetmate.lib.utils.database.BaseIntEntity
import com.fleetmate.lib.utils.database.BaseIntEntityClass
import com.fleetmate.lib.utils.database.idValue
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class FaultDao(id: EntityID<Int>) : BaseIntEntity<FaultDto>(id, FaultModel) {
    companion object : BaseIntEntityClass<FaultDto, FaultDao>(FaultModel) {
        fun setNotCriticalForCar(carId: Int) = transaction {
            FaultModel.update({ FaultModel.car eq carId }) {
                it[critical] = false
            }
        }

        fun getByCar(carId: Int): List<FaultDto> =
            FaultDao.find { FaultModel.car eq carId }.map { it.toOutputDto() }
    }

    var carId by FaultModel.car
    var carPartId by FaultModel.carPart
    var tripId by FaultModel.trip
    var authorId by FaultModel.author
    var comment by FaultModel.comment
    var critical by FaultModel.critical


    override fun toOutputDto(): FaultDto =
        FaultDto(
            idValue, carId.value, carPartId.value,
            tripId?.value, authorId.value, comment, critical
        )
}