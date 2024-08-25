package com.fleetmate.stat.modules.car.dao


import com.fleetmate.lib.shared.modules.car.model.CarModel
import com.fleetmate.lib.shared.modules.car.model.CarPhotoModel
import com.fleetmate.lib.shared.modules.fault.model.FaultModel
import com.fleetmate.lib.shared.modules.photo.data.model.PhotoModel
import com.fleetmate.lib.shared.modules.trip.model.TripModel
import com.fleetmate.lib.shared.modules.violation.model.ViolationModel
import com.fleetmate.lib.utils.database.BaseIntEntity
import com.fleetmate.lib.utils.database.BaseIntEntityClass
import com.fleetmate.lib.utils.database.idValue
import com.fleetmate.stat.modules.car.dto.CarDto
import com.fleetmate.stat.modules.car.dto.CarListItemDto
import com.fleetmate.stat.modules.car.dto.CarOutputDto
import com.fleetmate.stat.modules.car.dto.CarSimpleDto
import com.fleetmate.stat.modules.fault.dao.FaultDao
import com.fleetmate.stat.modules.trip.dao.TripDao
import com.fleetmate.stat.modules.violation.dao.ViolationDao
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and

class CarDao(id: EntityID<Int>) : BaseIntEntity<CarDto>(id, CarModel) {
    companion object : BaseIntEntityClass<CarDto, CarDao>(CarModel)

    var name by CarModel.name
    var registrationNumber by CarModel.registrationNumber
    var typeId by CarModel.type
    var type by CarTypeDao referencedOn CarModel.type
    var fuelLevel by CarModel.fuelLevel
    var mileage by CarModel.mileage
    var brand by CarModel.brand
    var model by CarModel.model
    var vin by CarModel.vin
    var hours by CarModel.hours
    var fuelType by CarModel.fuelType
    var osago by CarModel.osago
    var casco by CarModel.casco
    var yearManufacture by CarModel.yearManufacture
    var lastMaintenance by CarModel.lastMaintenance
    var antifreezeBrand by CarModel.antifreezeBrand
    var engineOilBrand by CarModel.engineOilBrand
    var engineOilViscosity by CarModel.engineOilViscosity
    var adBlue by CarModel.adBlue
    var ownership by CarModel.ownership

    override fun toOutputDto(): CarDto =
        CarDto(idValue, name, registrationNumber, typeId.value, fuelLevel, mileage)

    val fullOutputDto: CarOutputDto get() =
        CarOutputDto(
            idValue,
            brand,
            model,
            registrationNumber,
            vin,
            fuelType,
            mileage,
            hours,
            typeId.value,
            licence,
            osago,
            casco,
            yearManufacture,
            isServiceability,
            lastMaintenance,
            antifreezeBrand,
            engineOilBrand,
            engineOilViscosity,
            adBlue,
            ownership,
            photos
        )

    val lastTrip: TripDao? get() =
        TripDao.find {
            (TripModel.car eq idValue)
        }.orderBy(TripModel.keyReturn to SortOrder.DESC).firstOrNull()

    val simpleDto: CarSimpleDto get() =
        CarSimpleDto(idValue, name, type.name, registrationNumber)

    val listItemDto: CarListItemDto get() =
        CarListItemDto(simpleDto, fuelLevel, violationsCount)

    val violationsCount: Int get() =
        ViolationDao.count(ViolationModel.car eq idValue).toInt()

    val isServiceability: Boolean get() =
        FaultDao.find {
            (FaultModel.car eq idValue) and
            (FaultModel.critical eq true)
        }.count() == 0L

    val licence: Int get() =
        CarTypeDao[typeId].licenceType.value

    val photos: List<String> get() {
        val trip = lastTrip
        return CarPhotoModel.join(
            PhotoModel, JoinType.INNER, CarPhotoModel.photo, PhotoModel.id
        ).select(
            PhotoModel.link
        ).where{
            CarPhotoModel.check inList listOf(
                trip?.driverCheckBeforeTripId, trip?.mechanicCheckBeforeTripId,
                trip?.mechanicCheckAfterTripId, trip?.driverCheckAfterTripId)
        }.map {
            it[PhotoModel.link]
        }
    }
}