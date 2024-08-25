package com.fleetmate.stat.modules.user.dao


import com.fleetmate.lib.shared.modules.car.model.licence.LicenceTypeModel
import com.fleetmate.lib.utils.database.BaseIntEntity
import com.fleetmate.lib.utils.database.BaseIntEntityClass
import com.fleetmate.lib.utils.database.idValue
import com.fleetmate.stat.modules.user.dto.driver.LicenceTypeDto
import org.jetbrains.exposed.dao.id.EntityID

class LicenceTypeDao(id: EntityID<Int>) : BaseIntEntity<LicenceTypeDto>(id, LicenceTypeModel) {
    companion object : BaseIntEntityClass<LicenceTypeDto, LicenceTypeDao>(LicenceTypeModel)

    val name by LicenceTypeModel.name

    override fun toOutputDto(): LicenceTypeDto =
        LicenceTypeDto(idValue, name)
}