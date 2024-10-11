package com.fleetmate.stat.modules.car.service

import com.fleetmate.lib.plugins.Logger
import com.fleetmate.lib.shared.conf.AppConf
import com.fleetmate.lib.shared.dto.StatusDto
import com.fleetmate.lib.shared.modules.car.model.CarModel
import com.fleetmate.lib.shared.modules.car.model.type.CarTypeModel
import com.fleetmate.lib.shared.modules.type.model.FuelTypeModel
import com.fleetmate.lib.shared.modules.violation.model.ViolationModel
import com.fleetmate.lib.utils.kodein.KodeinService
import com.fleetmate.stat.modules.car.dao.CarDao
import com.fleetmate.stat.modules.car.dao.FuelTypeDao
import com.fleetmate.stat.modules.car.dto.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.count
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.DI

class CarService(di: DI) : KodeinService(di) {
    fun getAllFiltered(carFilterDto: CarFilterDto): List<CarListItemDto> = transaction {
        CarModel
            .join(ViolationModel, JoinType.LEFT, ViolationModel.car, CarModel.id)
            .select(CarModel.columns + ViolationModel.id.count())
            .groupBy(CarModel.id)
            .where {
                with(carFilterDto) { expressionBuilder }
            }
            .map {
                val carDao = CarDao.wrapRow(it)
                val violationsCount = it[ViolationModel.id.count()]
                carDao.listItemDto(violationsCount)
            }
    }

    fun create(carCreateDto: CarCreateDto): CarDto = transaction {

        CarDao.new {
            name = carCreateDto.name
            registrationNumber = carCreateDto.registrationNumber
            registrationCertificateNumber = carCreateDto.registrationCertificateNumber
            typeId = EntityID(carCreateDto.type, CarTypeModel)
            fuelLevel = carCreateDto.fuelLevel
            mileage = carCreateDto.mileage
            brand = carCreateDto.brand
            model = carCreateDto.model
            vin = carCreateDto.vin
            engineHours = carCreateDto.engineHours
            fuelTypeId = EntityID(carCreateDto.fuelType, FuelTypeModel)
            compulsoryCarInsurance = carCreateDto.compulsoryCarInsurance
            comprehensiveCarInsurance = carCreateDto.comprehensiveCarInsurance
            yearManufactured = carCreateDto.yearManufactured
            lastMaintenance = carCreateDto.lastMaintenance
            antifreezeBrand = carCreateDto.antifreezeBrand
            engineOilBrand = carCreateDto.engineOilBrand
            engineOilViscosity = carCreateDto.engineOilViscosity
            adBlue = carCreateDto.adBlue
            ownership = carCreateDto.ownership
        }.toOutputDto()
    }

    fun getOne(carId: Int): CarOutputDto = transaction {
        val carDao = CarDao[carId]

        Logger.debug("Fuel type: ")
        Logger.debug(carDao.fuelType.toOutputDto())

        carDao.fullOutputDto
    }

    fun getAllFuelTypes(): List<FuelTypeDto> = transaction {
        FuelTypeDao.all().map(FuelTypeDao::toOutputDto)
    }

    fun getAllStatuses(): List<StatusDto> =
        AppConf.CarStatus.entries.map {
            StatusDto(it.id, it.name)
        }
}