package com.fleetmate.lib.shared.conf

import com.fleetmate.lib.shared.modules.car.model.licence.LicenceTypeModel
import com.fleetmate.lib.shared.modules.car.model.part.CarPartModel
import com.fleetmate.lib.shared.modules.car.model.part.CarPartToCarPartModel
import com.fleetmate.lib.shared.modules.car.model.type.CarTypeModel
import com.fleetmate.lib.shared.modules.department.model.DepartmentModel
import com.fleetmate.lib.shared.modules.fault.model.WorkTypeModel
import com.fleetmate.lib.shared.modules.position.model.PositionModel
import com.fleetmate.lib.shared.modules.type.model.FuelTypeModel
import com.fleetmate.lib.shared.modules.user.model.UserModel
import com.fleetmate.lib.shared.modules.user.model.UserRoleModel
import com.fleetmate.lib.shared.modules.violation.model.ViolationModel
import com.fleetmate.lib.utils.security.bcrypt.CryptoUtil
import io.ktor.util.date.*
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseInitializer {
    fun initUsers() = transaction {
        if (UserModel.selectAll().empty()) {
            LicenceTypeModel.insert {
                it[name] = "B"
            }
            PositionModel.insert {
                it[name] = "Test position"
            }
            DepartmentModel.insert {
                it[name] = "Test department"
            }

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
            UserModel.insert {
                it[login] = "driver"
                it[email] = "test@test.com"
                it[phoneNumber] = "89110885758"
                it[hash] = CryptoUtil.hash("driver")
                it[fullName] = "Driver Driver"
                it[birthday] = getTimeMillis()
                it[insuranceNumber] = "1234-1234-143-143"
                it[licenceType] = 1
                it[position] = 1
                it[department] = 1
                it[sectorBossId] = 1
            }
            UserModel.insert {
                it[login] = "mechanic"
                it[email] = "a.hatson@ya.ru"
                it[phoneNumber] = "89110885759"
                it[hash] = CryptoUtil.hash("mechanic")
                it[fullName] = "Mechanic Mechanic"
                it[birthday] = getTimeMillis()
                it[insuranceNumber] = "1234-1234-143-143"
                it[licenceType] = 1
                it[position] = 1
                it[department] = 1
                it[sectorBossId] = 1
            }
            UserModel.insert {
                it[login] = "washer"
                it[email] = "test@test.com"
                it[phoneNumber] = "89110885761"
                it[hash] = CryptoUtil.hash("washer")
                it[fullName] = "Washer Washer"
                it[birthday] = getTimeMillis()
                it[insuranceNumber] = "1234-1234-143-143"
                it[licenceType] = 1
                it[position] = 1
                it[department] = 1
                it[sectorBossId] = 1
            }
            UserModel.insert {
                it[login] = "junior_mechanic"
                it[email] = "test@test.com"
                it[phoneNumber] = "89110885760"
                it[hash] = CryptoUtil.hash("junior_mechanic")
                it[fullName] = "Junior Mechanic"
                it[birthday] = getTimeMillis()
                it[insuranceNumber] = "1234-1234-143-143"
                it[licenceType] = 1
                it[position] = 1
                it[department] = 1
                it[sectorBossId] = 1
            }

            UserRoleModel.insert {
                it[role] = AppConf.roles.admin
                it[user] = 1
            }
            UserRoleModel.insert {
                it[role] = AppConf.roles.driver
                it[user] = 2
            }
            UserRoleModel.insert {
                it[role] = AppConf.roles.mechanic
                it[user] = 3
            }
            UserRoleModel.insert {
                it[role] = AppConf.roles.washer
                it[user] = 4
            }
            UserRoleModel.insert {
                it[user] = 5
                it[role] = AppConf.roles.juniorMechanic
            }
        }
    }

    fun initCarSubTables() {
        if (CarTypeModel.selectAll().empty()) {
            CarPartModel.insert {
                it[name] = "root-super"
            }
            CarPartModel.insert {
                it[name] = "front"
            }
            CarPartModel.insert {
                it[name] = "back"
            }
            CarPartModel.insert {
                it[name] = "hood"
            }
            CarPartModel.insert {
                it[name] = "bumper"
            }
            CarPartModel.insert {
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

            CarTypeModel.insert {
                it[licenceType] = 1
                it[name] = "type"
                it[rootPart] = 1
                it[speedLimit] = 56.4
                it[speedError] = 10.1
                it[avgFuelConsumption] = 20.2
                it[photo] = "type1.svg"
            }
        }
    }

    fun initTripViolations() {
        ViolationModel.batchInsert(AppConf.ViolationType.entries) {
            this[ViolationModel.type] = it.name
            this[ViolationModel.registeredAt] = getTimeMillis()
            this[ViolationModel.driver] = 2
            this[ViolationModel.trip] = 1
            this[ViolationModel.car] = 4
            this[ViolationModel.comment] = "sdfsfsd"
        }
    }

    fun initFuelTypes() {
        if (FuelTypeModel.selectAll().empty())
            FuelTypeModel.batchInsert(AppConf.FuelType.entries) {
                this[FuelTypeModel.id] = it.id
                this[FuelTypeModel.name] = it.name
            }
    }

    fun initWorkTypes() {
        if (WorkTypeModel.selectAll().empty())
            WorkTypeModel.batchInsert(listOf(1, 2, 3)) {
                this[WorkTypeModel.name] = "Work type #$it"
                this[WorkTypeModel.hours] = it.toDouble()
            }
    }
}