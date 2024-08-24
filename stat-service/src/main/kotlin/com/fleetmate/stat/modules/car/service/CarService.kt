package com.fleetmate.stat.modules.car.service

import com.fleetmate.lib.utils.kodein.KodeinService
import com.fleetmate.stat.modules.car.dao.CarDao
import com.fleetmate.stat.modules.car.dto.CarFilterDto
import com.fleetmate.stat.modules.car.dto.CarListItemDto
import org.kodein.di.DI

class CarService(di: DI) : KodeinService(di) {
    fun getAllFiltered(carFilterDto: CarFilterDto): List<CarListItemDto> =
        CarDao.find {
            with(carFilterDto) { expressionBuilder }
        }.map {
            val output = it.listItemDto
            output.violations = CarDao.violations(output.car.id).count()
            output
        }
}