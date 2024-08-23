package com.fleetmate.stat.modules.user.dao


import com.fleetmate.lib.shared.modules.department.model.DepartmentModel
import com.fleetmate.lib.utils.database.BaseIntEntity
import com.fleetmate.lib.utils.database.BaseIntEntityClass
import com.fleetmate.lib.utils.database.idValue
import com.fleetmate.stat.modules.user.dto.DepartmentDto
import org.jetbrains.exposed.dao.id.EntityID

class DepartmentDao(id: EntityID<Int>) : BaseIntEntity<DepartmentDto>(id, DepartmentModel) {
    companion object : BaseIntEntityClass<DepartmentDto, DepartmentDao>(DepartmentModel)

    val name by DepartmentModel.name

    override fun toOutputDto(): DepartmentDto =
        DepartmentDto(idValue, name)
}