package com.fleetmate.faults.modules.check.dao


import com.fleetmate.faults.modules.car.CarDao
import com.fleetmate.faults.modules.check.dto.CheckDto
import com.fleetmate.faults.modules.user.UserDao
import com.fleetmate.lib.exceptions.NotFoundException
import com.fleetmate.lib.shared.modules.check.model.CheckModel
import com.fleetmate.lib.utils.database.BaseIntEntity
import com.fleetmate.lib.utils.database.BaseIntEntityClass
import com.fleetmate.lib.utils.database.idValue
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.and

class CheckDao(id: EntityID<Int>) : BaseIntEntity<CheckDto>(id, CheckModel) {
    companion object : BaseIntEntityClass<CheckDto, CheckDao>(CheckModel) {
        fun findActiveCheckByUser(carId: Int, userId: Int): CheckDao = with(find {
            (CheckModel.car eq carId) and (CheckModel.author eq userId) and (CheckModel.finishTime.isNull())
        }) {
            if (empty())
                throw NotFoundException("Check not found")
            else first()
        }
    }

    var authorId by CheckModel.author
    var author by UserDao referencedOn CheckModel.author
    var carId by CheckModel.car
    var car by CarDao referencedOn CheckModel.car
    var startTime by CheckModel.startTime
    var finishTime by CheckModel.finishTime
    var timeExceeded by CheckModel.timeExceeded

    override fun toOutputDto(): CheckDto =
        CheckDto(idValue, authorId.value, carId.value, startTime, finishTime, timeExceeded)
}