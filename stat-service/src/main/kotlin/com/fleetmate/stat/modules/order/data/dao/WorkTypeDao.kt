package com.fleetmate.stat.modules.order.data.dao


import com.fleetmate.lib.utils.database.BaseIntEntity
import com.fleetmate.lib.utils.database.BaseIntEntityClass
import com.fleetmate.lib.utils.database.idValue
import com.fleetmate.stat.modules.order.data.dto.work.WorkTypeDto
import com.fleetmate.stat.modules.order.data.model.WorkTypeModel
import org.jetbrains.exposed.dao.id.EntityID

class WorkTypeDao(id: EntityID<Int>) : BaseIntEntity<WorkTypeDto>(id, WorkTypeModel) {
    companion object : BaseIntEntityClass<WorkTypeDto, WorkTypeDao>(WorkTypeModel)

    val name by WorkTypeModel.name
    val hours by WorkTypeModel.hours

    override fun toOutputDto(): WorkTypeDto =
        WorkTypeDto(idValue, name, hours)
}