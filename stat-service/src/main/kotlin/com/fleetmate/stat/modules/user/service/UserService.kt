package com.fleetmate.stat.modules.user.service


import com.fleetmate.lib.shared.conf.AppConf
import com.fleetmate.lib.shared.modules.user.model.UserModel
import com.fleetmate.lib.utils.database.idValue
import com.fleetmate.lib.utils.kodein.KodeinService
import com.fleetmate.stat.modules.user.dao.UserDao
import com.fleetmate.stat.modules.user.dto.UserFilterDto
import com.fleetmate.stat.modules.user.dto.output.DriverOutputDto
import com.fleetmate.stat.modules.user.dto.output.StaffOutputDto
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.DI

class UserService(di: DI) : KodeinService(di) {
    fun getDriversFiltered(userFilterDto: UserFilterDto): List<DriverOutputDto> = transaction {
        UserDao.find {
            with(userFilterDto) { expressionBuilder }
        }.map {
            val output = it.driverOutput
            output.violationCount = UserDao.driverViolations(output.id, userFilterDto.dateRange).count()
            output
        }
    }

    fun getStaffFiltered(userFilterDto: UserFilterDto): List<StaffOutputDto> = transaction {
        UserDao.find {
            UserModel.position inList (listOf(AppConf.mechanicPositionId, AppConf.washerPositionId))
            with(userFilterDto) { expressionBuilder }
        }.map { userDao ->
            if (userDao.positionId.value == AppConf.washerPositionId) {
                val washes = UserDao.washerOrders(userDao.idValue, userFilterDto.dateRange)
                userDao.toStaffOutput(
                    0,
                    washes.count(),
                    washes.count() * AppConf.washHoursNormalized
                )
            } else {
                val orders = UserDao.mechanicOrders(
                    userDao.idValue, userFilterDto.dateRange
                )
                userDao.toStaffOutput(
                    orders.count { it.closedAt == null }.toLong(),
                    orders.count { it.closedAt != null }.toLong(),
                    UserDao.hoursCompleted(
                        userDao.idValue, userFilterDto.dateRange
                    )
                )
            }
        }
    }
}