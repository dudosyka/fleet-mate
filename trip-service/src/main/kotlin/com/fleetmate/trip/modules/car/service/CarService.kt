package com.fleetmate.trip.modules.car.service

import com.fleetmate.lib.data.model.faults.FaultsModel
import com.fleetmate.lib.data.dto.car.CarCreateDto
import com.fleetmate.lib.data.dto.car.CarFullOutputDto
import com.fleetmate.lib.data.dto.car.CarUpdateDto
import com.fleetmate.lib.data.model.car.CarModel
import com.fleetmate.lib.data.model.car.CarPartModel
import com.fleetmate.lib.data.dto.car.CarOutputDto
import com.fleetmate.lib.exceptions.NotFoundException
import com.fleetmate.lib.utils.kodein.KodeinService
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.DI
import org.kodein.di.instance

class CarService(di: DI) : KodeinService(di) {
    private val carPartService: CarPartService by instance()

    fun getOne(id: Int): CarOutputDto {
        return CarOutputDto(CarModel.getOne(id) ?: throw NotFoundException("Car not found"))
    }

    fun create(carCreateDto: CarCreateDto): CarOutputDto =
        CarOutputDto(CarModel.create(carCreateDto))

    private fun update(id: Int, carUpdateDto: CarUpdateDto): Boolean =
        CarModel.update(id, carUpdateDto)

    fun getFullInfo(id: Int): CarFullOutputDto {
        val car = getOne(id)

        return CarFullOutputDto(
            car,
            CarPartModel.getTreeFrom(car.id, carPartService.getOne(car.type.id))
        )
    }

    fun refuel(carId: Int, volume: Double) = transaction {
        val car = getOne(carId)
        update(carId, CarUpdateDto(
            fuelLevel = car.fuelLevel + volume
        ))
    }

    fun isAvailable(carId: Int) =
        FaultsModel.getAllCriticalByCar(carId).isEmpty()
}