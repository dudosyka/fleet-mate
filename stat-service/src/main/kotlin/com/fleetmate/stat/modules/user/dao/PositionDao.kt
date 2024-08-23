package com.fleetmate.stat.modules.user.dao


import com.fleetmate.lib.shared.modules.position.model.PositionModel
import com.fleetmate.lib.utils.database.BaseIntEntity
import com.fleetmate.lib.utils.database.BaseIntEntityClass
import com.fleetmate.lib.utils.database.idValue
import com.fleetmate.stat.modules.user.dto.PositionDto
import org.jetbrains.exposed.dao.id.EntityID

class PositionDao(id: EntityID<Int>) : BaseIntEntity<PositionDto>(id, PositionModel) {
    companion object : BaseIntEntityClass<PositionDto, PositionDao>(PositionModel)

    val name by PositionModel.name

    override fun toOutputDto(): PositionDto =
        PositionDto(idValue, name)
}