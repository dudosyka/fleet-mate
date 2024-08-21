package com.fleetmate.trip.modules.department.service

import com.fleetmate.lib.data.dto.department.DepartmentCreateDto
import com.fleetmate.lib.data.dto.department.DepartmentOutputDto
import com.fleetmate.lib.data.model.department.DepartmentModel
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
}