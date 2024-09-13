package com.fleetmate.stat.modules.trip.service


import com.fleetmate.lib.exceptions.NotFoundException
import com.fleetmate.lib.shared.modules.car.model.CarPhotoModel
import com.fleetmate.lib.shared.modules.check.model.CheckModel
import com.fleetmate.lib.shared.modules.photo.data.model.PhotoModel
import com.fleetmate.lib.utils.kodein.KodeinService
import com.fleetmate.stat.modules.trip.dto.check.CheckDto
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.DI

class CheckService(di: DI) : KodeinService(di) {
    fun getOne(id: Int): CheckDto = transaction {
        val photos = CarPhotoModel
                .innerJoin(PhotoModel)
                .select( PhotoModel.link )
                .where {
                    CarPhotoModel.check eq id
                }
                .map {
                    it[PhotoModel.link]
                }
        val check = CheckModel.select(CheckModel.id, CheckModel.author).where { CheckModel.id eq id }.firstOrNull()
            ?: throw NotFoundException("Check model not found")

        CheckDto(check[CheckModel.id].value, check[CheckModel.author].value, photos)
    }
}