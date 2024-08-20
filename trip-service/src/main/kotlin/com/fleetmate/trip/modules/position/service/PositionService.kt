package com.fleetmate.trip.modules.position.service

import com.fleetmate.lib.dto.post.PositionCreateDto
import com.fleetmate.lib.dto.post.PositionOutputDto
import com.fleetmate.lib.model.post.PositionModel
import com.fleetmate.lib.utils.kodein.KodeinService
import org.kodein.di.DI
import kotlin.collections.map

class PositionService(di: DI) : KodeinService(di) {
    fun getOne(id: Int): PositionOutputDto? {
        return PositionOutputDto(PositionModel.getOne(id) ?: return null)
    }

    fun getAll(): List<PositionOutputDto> {
        return PositionModel.getAll().map {
            PositionOutputDto(it)
        }
    }

    fun create(positionCreateDto: PositionCreateDto): PositionOutputDto =
        PositionOutputDto(PositionModel.create(positionCreateDto))

    fun update(id: Int, postUpdateDto: PositionCreateDto): Boolean =
        PositionModel.update(id, postUpdateDto)

    fun delete(id: Int): Boolean =
        PositionModel.delete(id)
}