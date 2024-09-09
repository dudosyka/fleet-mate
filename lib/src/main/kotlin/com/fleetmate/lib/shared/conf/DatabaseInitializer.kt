package com.fleetmate.lib.shared.conf

import com.fleetmate.lib.shared.modules.car.model.CarModel
import com.fleetmate.lib.shared.modules.car.model.CarPhotoModel
import com.fleetmate.lib.shared.modules.car.model.licence.LicenceTypeModel
import com.fleetmate.lib.shared.modules.car.model.part.CarPartModel
import com.fleetmate.lib.shared.modules.car.model.part.CarPartToCarPartModel
import com.fleetmate.lib.shared.modules.car.model.type.CarTypeModel
import com.fleetmate.lib.shared.modules.check.model.CheckModel
import com.fleetmate.lib.shared.modules.department.model.DepartmentModel
import com.fleetmate.lib.shared.modules.fault.model.FaultModel
import com.fleetmate.lib.shared.modules.fault.model.FaultPhotoModel
import com.fleetmate.lib.shared.modules.fault.model.WorkTypeModel
import com.fleetmate.lib.shared.modules.photo.data.model.PhotoModel
import com.fleetmate.lib.shared.modules.position.model.PositionModel
import com.fleetmate.lib.shared.modules.trip.model.TripModel
import com.fleetmate.lib.shared.modules.type.model.FuelTypeModel
import com.fleetmate.lib.shared.modules.user.model.UserModel
import com.fleetmate.lib.shared.modules.user.model.UserRoleModel
import com.fleetmate.lib.shared.modules.violation.model.ViolationModel
import com.fleetmate.lib.shared.modules.wash.model.WashModel
import com.fleetmate.lib.utils.security.bcrypt.CryptoUtil
import io.ktor.util.date.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

object DatabaseInitializer {
    fun initUsers() = transaction {
        if (UserModel.selectAll().empty()) {
            initLicenceType()

            initPositions()
            initDepartments()

            commit()

            UserModel.insert {
                it[login] = "admin"
                it[email] = "a.hatson22@gmail.com"
                it[phoneNumber] = "89110885757"
                it[hash] = CryptoUtil.hash("admin")
                it[fullName] = "Grand Admin Admin"
                it[birthday] = getTimeMillis()
                it[insuranceNumber] = "1234-1234-143-143"
                it[licenceType] = 1
                it[position] = 1
                it[department] = 1
            }

            initDrivers()
            initMechanics()
            initJuniorMechanics()
            initWashers()

            commit()

            initRoles()

        }
    }

    private fun initCarSubTables() {
        if (CarTypeModel.selectAll().empty()) {
            CarPartModel.insert {
                it[id] = 1
                it[name] = "root-super"
            }
            CarPartModel.insert {
                it[id] = 2
                it[name] = "front"
            }
            CarPartModel.insert {
                it[id] = 3
                it[name] = "back"
            }
            CarPartModel.insert {
                it[id] = 4
                it[name] = "hood"
            }
            CarPartModel.insert {
                it[id] = 5
                it[name] = "bumper"
            }
            CarPartModel.insert {
                it[id] = 6
                it[name] = "bumper pads"
            }
            CarPartToCarPartModel.insert {
                it[parent] = 1
                it[child] = 2
            }
            CarPartToCarPartModel.insert {
                it[parent] = 1
                it[child] = 3
            }
            CarPartToCarPartModel.insert {
                it[parent] = 2
                it[child] = 4
            }
            CarPartToCarPartModel.insert {
                it[parent] = 3
                it[child] = 5
            }
            CarPartToCarPartModel.insert {
                it[parent] = 5
                it[child] = 6
            }

            initCarTypes()
        }
    }

    private fun getTrips(): List<Triple<Int, Int, Int>> =
        TripModel.select(
            TripModel.id,
            TripModel.car,
            TripModel.driver
        ).map {
            Triple(
                it[TripModel.id].value,
                it[TripModel.car].value,
                it[TripModel.driver].value
            )
        }

    fun initTripViolations() = transaction {
        val trips = getTrips()

        trips.forEach { ids ->
            ViolationModel.batchInsert(AppConf.ViolationType.entries) {
                this[ViolationModel.type] = it.name
                this[ViolationModel.registeredAt] = getTimeMillis()
                this[ViolationModel.driver] = EntityID(ids.third, UserModel)
                this[ViolationModel.trip] = EntityID(ids.first, TripModel)
                this[ViolationModel.car] = EntityID(ids.second, CarModel)
                this[ViolationModel.comment] = "comment_${it.name}"
            }
        }
    }

    private fun initFuelTypes() {
        if (FuelTypeModel.selectAll().empty())
            FuelTypeModel.batchInsert(AppConf.FuelType.entries) {
                this[FuelTypeModel.id] = it.id
                this[FuelTypeModel.name] = it.name
            }
    }

    fun initWorkTypes() = transaction {
        if (WorkTypeModel.selectAll().empty())
            WorkTypeModel.batchInsert(listOf(1, 2, 3)) {
                this[WorkTypeModel.name] = "Work type #$it"
                this[WorkTypeModel.hours] = it.toDouble()
            }
    }
    private fun initPositions() {
        if (PositionModel.selectAll().empty())
            PositionModel.batchInsert(AppConf.Positions.entries) {
                this[PositionModel.id] = it.id
                this[PositionModel.name] = it.name
            }
    }
    private fun initDepartments() {
        if (DepartmentModel.selectAll().empty())
            DepartmentModel.batchInsert(listOf(1, 2, 3)) {
                this[DepartmentModel.id] = it
                this[DepartmentModel.name] = "Department name #$it"
            }
    }
    private fun initMechanics() {
        UserModel.batchInsert(listOf(1, 2, 3)) {
            this[UserModel.login] = "mechanic$it"
            this[UserModel.email] = "mechanic$it@gmail.com"
            this[UserModel.phoneNumber] = "+790251168$it"
            this[UserModel.hash] = CryptoUtil.hash("mechanic_$it")
            this[UserModel.fullName] = "Mechanic Mechanic $it"
            this[UserModel.birthday] = getTimeMillis()
            this[UserModel.insuranceNumber] = "9876-1234-143-$it"
            this[UserModel.position] = AppConf.Positions.MECHANIC.id
            this[UserModel.department] = 1
            this[UserModel.licenceType] = 4
        }
    }
    private fun initJuniorMechanics() {
        UserModel.batchInsert(listOf(1, 2, 3, 4, 5)) {
            this[UserModel.login] = "junior_mechanic_$it"
            this[UserModel.email] = "juniorMechanic$it@gmail.com"
            this[UserModel.phoneNumber] = "+790217150$it"
            this[UserModel.hash] = CryptoUtil.hash("junior_mechanic$it")
            this[UserModel.fullName] = "Junior Junior$it"
            this[UserModel.birthday] = getTimeMillis()
            this[UserModel.insuranceNumber] = "4321-1234-143-$it"
            this[UserModel.position] = AppConf.Positions.JUNIOR_MECHANIC.id
            this[UserModel.department] = 1
            this[UserModel.licenceType] = 4
        }
    }
    private fun initDrivers() {
        UserModel.batchInsert(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)) {
            this[UserModel.login] = "driver_$it"
            this[UserModel.email] = "driver$it@gmail.com"
            this[UserModel.phoneNumber] = "+7902542620$it"
            this[UserModel.hash] = CryptoUtil.hash("driver_$it")
            this[UserModel.fullName] = "Driver Driver$it"
            this[UserModel.birthday] = getTimeMillis()
            this[UserModel.insuranceNumber] = "1234-1234-143-$it"
            this[UserModel.position] = AppConf.Positions.DRIVER.id
            this[UserModel.department] = 1
            this[UserModel.licenceType] = 1
        }
    }
    private fun initWashers() {
        UserModel.batchInsert(listOf(1, 2, 3)) {
            this[UserModel.login] = "washer_$it"
            this[UserModel.email] = "washer_$it@gmail.com"
            this[UserModel.phoneNumber] = "+790251382$it"
            this[UserModel.hash] = CryptoUtil.hash("washer_$it")
            this[UserModel.fullName] = "Washer Washer$it"
            this[UserModel.birthday] = getTimeMillis()
            this[UserModel.insuranceNumber] = "5252-1234-143-$it"
            this[UserModel.position] = AppConf.Positions.WASHER.id
            this[UserModel.department] = 1
            this[UserModel.licenceType] = 4
        }
    }
    private fun initRoles() {
        UserRoleModel.insert {
            it[role] = AppConf.roles.admin
            it[user] = 1
        }
        UserRoleModel.batchInsert(listOf(2, 3, 4, 5, 6, 7, 8, 9, 10, 11)) {
            this[UserRoleModel.role] = AppConf.roles.driver
            this[UserRoleModel.user] = it
        }
        UserRoleModel.batchInsert(listOf(12, 13, 14)) {
            this[UserRoleModel.role] = AppConf.roles.mechanic
            this[UserRoleModel.user] = it
        }
        UserRoleModel.batchInsert(listOf(15, 16, 17, 18, 19)) {
            this[UserRoleModel.role] = AppConf.roles.juniorMechanic
            this[UserRoleModel.user] = it
        }
        UserRoleModel.batchInsert(listOf(20, 21, 22)) {
            this[UserRoleModel.role] = AppConf.roles.washer
            this[UserRoleModel.user] = it
        }
    }
    fun initCars() = transaction {
        if (CarModel.selectAll().empty()) {
            initCarSubTables()
            initFuelTypes()

            commit()

            CarModel.batchInsert(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9)) {
                this[CarModel.name] = "car_name_$it"
                this[CarModel.registrationNumber] = "р68${it}рс 78"
                this[CarModel.type] = (it % 3) + 1
                this[CarModel.fuelLevel] = it.toDouble()
                this[CarModel.mileage] = 10000 + it.toDouble()
                this[CarModel.brand] = "brand_$it"
                this[CarModel.model] = "model_$it"
                this[CarModel.vin] = "JHMCM56557C40445$it"
                this[CarModel.engineHours] = 150 + it.toDouble()
                this[CarModel.fuelType] = AppConf.FuelType.PETROL_92.id
                this[CarModel.compulsoryCarInsurance] = "compulsory_car_insurance_$it"
                this[CarModel.comprehensiveCarInsurance] = "comprehensiveCarInsurance_$it"
                this[CarModel.lastMaintenance] = getTimeMillis()
                this[CarModel.yearManufactured] = 2005 + it
                this[CarModel.antifreezeBrand] = "antifreeze_brand_$it"
                this[CarModel.engineOilBrand] = "engine_oil_brand_$it"
                this[CarModel.engineOilViscosity] = "engine_oil_viscosity_$it"
                this[CarModel.adBlue] = false
                this[CarModel.ownership] = true
                this[CarModel.status] = AppConf.CarStatus.FREE.name
            }
            commit()
        }
    }
    fun initTestPhotos() = transaction {
        if (PhotoModel.selectAll().empty()) {
            PhotoModel.batchInsert(listOf(1, 2, 3)) {
                this[PhotoModel.id] = it
                this[PhotoModel.link] = "type$it.svg"
                this[PhotoModel.type] = "type_$it"
            }
            commit()
        }
    }
    private fun initCarTypes() {
        if (CarTypeModel.selectAll().empty()) {
            CarTypeModel.batchInsert(listOf(1, 2, 3)) {
                this[CarTypeModel.licenceType] = it
                this[CarTypeModel.name] = "car_type_$it"
                this[CarTypeModel.rootPart] = 1
                this[CarTypeModel.speedLimit] = 90 / it.toDouble()
                this[CarTypeModel.speedError] = 5 * it.toDouble()
                this[CarTypeModel.avgFuelConsumption] = 7.5 * it.toDouble()
                this[CarTypeModel.photo] = "type$it.svg"
            }
        }
    }

    private fun initLicenceType() {
        if (LicenceTypeModel.selectAll().empty()) {
            LicenceTypeModel.insert {
                it[id] = 1
                it[name] = "B"
            }
            LicenceTypeModel.insert {
                it[id] = 2
                it[name] = "C"
            }
            LicenceTypeModel.insert {
                it[id] = 3
                it[name] = "D"
            }
            LicenceTypeModel.insert {
                it[id] = 4
                it[name] = "NO_LICENCE"
            }
        }
    }
    fun initTrips() = transaction {
        if (TripModel.selectAll().empty()) {
            val trips = TripModel.batchInsert(AppConf.TripStatus.entries) {
                this[TripModel.car] = it.id
                this[TripModel.driver] = it.id + 1
                this[TripModel.status] = it.name
                this[TripModel.keyAcceptance] = getTimeMillis()

                if (it.name != AppConf.TripStatus.EXPLOITATION.name && it.name != AppConf.TripStatus.INITIALIZED.name) {
                    val checksIds = initAllChecksForCar(it.id, it.id + 1)
                    if (checksIds.isNotEmpty()) {
                        this[TripModel.driverCheckBeforeTrip] = checksIds[0]
                        this[TripModel.driverCheckAfterTrip] = checksIds[1]
                        this[TripModel.mechanicCheckBeforeTrip] = checksIds[2]
                        this[TripModel.mechanicCheckAfterTrip] = checksIds[3]

                        initChecksPhotos(checksIds, it.id)
                    }
                    this[TripModel.keyReturn] = getTimeMillis()

                } else {
                    val checksIds = initCheckForCarBeforeTrip(it.id, it.id + 1)
                    if (checksIds.isNotEmpty()) {
                        this[TripModel.driverCheckBeforeTrip] = checksIds[0]
                        this[TripModel.mechanicCheckBeforeTrip] = checksIds[1]

                        initChecksPhotos(checksIds, it.id)
                    }
                }
            }.map {
                Triple(
                    it[TripModel.id].value,
                    it[TripModel.status],
                    it[TripModel.keyAcceptance]
                )
            }.toList() + TripModel.batchInsert(listOf(1, 2)) {
                    this[TripModel.car] = 4 + it
                    this[TripModel.driver] = 5 + it
                    this[TripModel.status] = AppConf.TripStatus.EXPLOITATION.name
                    this[TripModel.keyAcceptance] = getTimeMillis()

                    val checksIds = initCheckForCarBeforeTrip(4 + it, 5 + it)
                    if (checksIds.isNotEmpty()) {
                        this[TripModel.driverCheckBeforeTrip] = checksIds[0]
                        this[TripModel.mechanicCheckBeforeTrip] = checksIds[1]
                    }
                }.map {
                    Triple(
                        it[TripModel.id].value,
                        it[TripModel.status],
                        it[TripModel.keyAcceptance]
                    )
                }

            WashModel.batchInsert(
                trips.filter {
                    it.second != AppConf.TripStatus.CLOSED_DUE_TO_FAULT.name
                }
            ) { trip ->
                this[WashModel.trip] = trip.first
                this[WashModel.author] = 21
                this[WashModel.timestamp] = trip.third + 1
            }


            CarModel.update({ CarModel.id lessEq 6 }) {
                it[status] = AppConf.CarStatus.IN_USE.name
            }
        }
    }

    private fun initChecksPhotos(checksIds: List<Int>, carId: Int) {
        CarPhotoModel.batchInsert(checksIds) {
            this[CarPhotoModel.photo] = 1
            this[CarPhotoModel.check] = it
            this[CarPhotoModel.car] = carId
        }
    }
    private fun initAllChecksForCar(carId: Int, driverId: Int): List<Int> {
        return CheckModel.batchInsert(listOf(driverId, 2, driverId, 2)) {
            this[CheckModel.author] = if (it == driverId) driverId else it + 9
            this[CheckModel.car] = carId
            this[CheckModel.startTime] = getTimeMillis()
            this[CheckModel.finishTime] = getTimeMillis()
        }.map {
            it[CheckModel.id].value
        }.toList()
    }
    private fun initCheckForCarBeforeTrip(carId: Int, driverId: Int): List<Int> {
        return CheckModel.batchInsert(listOf(driverId, 2)) {
                this[CheckModel.author] = if (it == driverId) driverId else it + 10
                this[CheckModel.car] = carId
                this[CheckModel.startTime] = getTimeMillis()
                this[CheckModel.finishTime] = getTimeMillis()
            }.map {
                it[CheckModel.id].value
            }.toList()
    }
    private fun getCars(): List<Int> =
        CarModel.select(
            CarModel.id
        ).map {
            it[CarModel.id].value
        }
    fun initFaults() = transaction {
        val cars = getCars()
        cars.forEach { id ->
            val faults = FaultModel.batchInsert(listOf(4, 6)) {
                this[FaultModel.car] = id
                this[FaultModel.carPart] = it
                this[FaultModel.author] = 1
                this[FaultModel.comment] = "comment_$it"
                this[FaultModel.critical] = false
                this[FaultModel.status] = AppConf.FaultStatus.CREATED.name
            }.map {
                it[FaultModel.id].value
            }.toList()

            faults.forEach { faultID ->
                FaultPhotoModel.insert {
                    it[fault] = faultID
                    it[photo] = 1
                }
            }

        }

        val criticalFaults = FaultModel.batchInsert(listOf(8, 9)) {
            this[FaultModel.car] = it
            this[FaultModel.carPart] = 6
            this[FaultModel.author] = 1
            this[FaultModel.comment] = "comment_critical$it"
            this[FaultModel.critical] = true
            this[FaultModel.status] = AppConf.FaultStatus.CREATED.name
        }.map {
            it[FaultModel.id].value
        }.toList()

        criticalFaults.forEach { faultID ->
            FaultPhotoModel.insert {
                it[fault] = faultID
                it[photo] = 1
            }
        }

        CarModel.update({ CarModel.id inList listOf(8, 9)}) {
            it[status] = AppConf.CarStatus.UNDER_REPAIR.name
        }
    }
}