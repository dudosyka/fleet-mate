package com.fleetmate.trip.modules.department.service

import com.fleetmate.lib.dto.division.DepartmentCreateDto
import com.fleetmate.lib.dto.division.DepartmentOutputDto
import com.fleetmate.lib.model.division.DepartmentModel
import com.fleetmate.lib.utils.kodein.KodeinService
import org.kodein.di.DI
import kotlin.collections.map

class DepartmentService(di: DI) : KodeinService(di) {
    fun getOne(id: Int): DepartmentOutputDto? {
        return DepartmentOutputDto(DepartmentModel.getOne(id) ?: return null)
    }

    fun getAll(): List<DepartmentOutputDto> {
        return DepartmentModel.getAll().map {
            DepartmentOutputDto(it)
        }
    }

    fun create(departmentCreateDto: DepartmentCreateDto): DepartmentOutputDto =
        DepartmentOutputDto(DepartmentModel.create(departmentCreateDto))

    fun update(id: Int, divisionUpdateDto: DepartmentCreateDto): Boolean =
        DepartmentModel.update(id, divisionUpdateDto)

    fun delete(id: Int): Boolean =
        DepartmentModel.delete(id)
}