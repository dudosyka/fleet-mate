package com.fleetmate.stat.modules.car.service

import com.fleetmate.lib.shared.modules.car.model.type.CarTypeModel
import com.fleetmate.lib.utils.kodein.KodeinService
import com.fleetmate.stat.modules.car.dao.CarDao
import com.fleetmate.stat.modules.car.dto.CarCreateDto
import com.fleetmate.stat.modules.car.dto.CarDto
import com.fleetmate.stat.modules.car.dto.CarFilterDto
import com.fleetmate.stat.modules.car.dto.CarListItemDto
import com.fleetmate.stat.modules.car.dto.CarOutputDto
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.DI

class CarService(di: DI) : KodeinService(di) {
    fun getAllFiltered(carFilterDto: CarFilterDto): List<CarListItemDto> =
        CarDao.find {
            with(carFilterDto) { expressionBuilder }
        }.map { it.listItemDto }

    fun create(carCreateDto: CarCreateDto): CarDto = transaction {

        CarDao.new {
            name = carCreateDto.name
            registrationNumber = carCreateDto.registerNumber
            typeId = EntityID(carCreateDto.type, CarTypeModel)
            fuelLevel = carCreateDto.fuelLevel
            mileage = carCreateDto.mileage
            brand = carCreateDto.brand
            model = carCreateDto.model
            vin = carCreateDto.vin
            hours = carCreateDto.hours
            fuelType = carCreateDto.fuelType.name
            osago = carCreateDto.osago
            casco = carCreateDto.casco
            yearManufacture = carCreateDto.yearManufacture
            lastMaintenance = carCreateDto.lastMaintenance
            antifreezeBrand = carCreateDto.antifreezeBrand
            engineOilBrand = carCreateDto.engineOilBrand
            engineOilViscosity = carCreateDto.engineOilViscosity
            adBlue = carCreateDto.adBlue
            ownership = carCreateDto.ownership

        }.toOutputDto()
    }

    fun getOne(carId: Int): CarOutputDto =
        CarDao[carId].toFullOutputDto()
}