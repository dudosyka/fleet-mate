package com.fleetmate.stat.modules.user.service


import com.fleetmate.lib.exceptions.InternalServerException
import com.fleetmate.lib.plugins.Logger
import com.fleetmate.lib.shared.conf.AppConf
import com.fleetmate.lib.shared.modules.auth.dto.AuthorizedUser
import com.fleetmate.lib.shared.modules.position.model.PositionModel
import com.fleetmate.lib.shared.modules.user.model.UserModel
import com.fleetmate.lib.shared.modules.user.model.UserRoleModel
import com.fleetmate.lib.shared.modules.violation.model.ViolationModel
import com.fleetmate.lib.utils.database.idValue
import com.fleetmate.lib.utils.kodein.KodeinService
import com.fleetmate.stat.modules.order.data.model.OrderModel
import com.fleetmate.stat.modules.order.data.model.WorkActorsModel
import com.fleetmate.stat.modules.order.data.model.WorkModel
import com.fleetmate.lib.shared.modules.fault.model.WorkTypeModel
import com.fleetmate.lib.shared.modules.wash.model.WashModel
import com.fleetmate.stat.modules.user.dao.PositionDao.Companion.nullableRangeCond
import com.fleetmate.stat.modules.user.dao.PositionDao.Companion.rangeCond
import com.fleetmate.stat.modules.user.dao.UserDao
import com.fleetmate.stat.modules.user.dto.UserFilterDto
import com.fleetmate.stat.modules.user.dto.filter.StaffFilterDto
import com.fleetmate.stat.modules.user.dto.output.DriverDto
import com.fleetmate.stat.modules.user.dto.output.DriverOutputDto
import com.fleetmate.stat.modules.user.dto.output.StaffDto
import com.fleetmate.stat.modules.user.dto.output.StaffOutputDto
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.DI
import java.io.ByteArrayOutputStream

class UserService(di: DI) : KodeinService(di) {
    fun getDriversFiltered(userFilterDto: UserFilterDto): List<DriverOutputDto> = transaction {
        UserModel
            .join(UserRoleModel, JoinType.INNER, UserModel.id, UserRoleModel.user) {
                UserRoleModel.role eq AppConf.roles.driver
            }
            .join(ViolationModel, JoinType.LEFT, UserModel.id, ViolationModel.driver)
            .select(
                UserModel.id, UserModel.fullName,
                UserModel.licenceNumber,
                ViolationModel.id.count()
            )
            .groupBy(UserModel.id)
            .where {
                with (userFilterDto.staffFilter ?: StaffFilterDto()) { expressionBuilder }
            }
            .map {
                val userDao = UserDao.wrapRow(it)
                DriverOutputDto(
                    userDao.idValue,
                    userDao.fullName,
                    userDao.lastTrip?.simpleDto,
                    licenceNumber = userDao.licenceNumber ?: throw InternalServerException(""),
                    violationCount = it[ViolationModel.id.count()],
                    null
                )
            }
    }

    fun getStaffFiltered(userFilterDto: UserFilterDto): List<StaffOutputDto> = transaction {
        // Firstly select all junior_mechanics
        val completedOrders = OrderModel.alias("completed_orders")
        val ordersInProgress = OrderModel.alias("order_in_progress")
        UserModel
            .join(UserRoleModel, JoinType.INNER, UserRoleModel.user, UserModel.id) {
                UserRoleModel.role eq AppConf.roles.juniorMechanic
            }
            .join(WorkActorsModel, JoinType.LEFT, WorkActorsModel.actor, UserModel.id)
            .join(WorkModel, JoinType.LEFT,  WorkActorsModel.work, WorkModel.id) {
                nullableRangeCond(
                    userFilterDto.dateRange,
                    WorkModel.id.isNotNull(),
                    WorkModel.doneAt,
                    Long.MIN_VALUE, Long.MAX_VALUE
                )
            }
            .join(WorkTypeModel, JoinType.LEFT, WorkModel.type, WorkTypeModel.id)
            .join(completedOrders, JoinType.LEFT, WorkModel.order, completedOrders[OrderModel.id]) {
                completedOrders[OrderModel.closedAt].isNotNull() and
                nullableRangeCond(
                    userFilterDto.dateRange,
                    completedOrders[OrderModel.closedAt].isNotNull(),
                    completedOrders[OrderModel.closedAt],
                    Long.MIN_VALUE, Long.MAX_VALUE
                )
            }
            .join(ordersInProgress, JoinType.LEFT, WorkModel.order, ordersInProgress[OrderModel.id]) {
                completedOrders[OrderModel.closedAt].isNull()
            }
            .select(
                ordersInProgress[OrderModel.id].countDistinct(),
                completedOrders[OrderModel.id].countDistinct(),
                WorkTypeModel.hours.sum(),
                UserModel.id, UserModel.fullName
            )
            .groupBy(UserModel.id)
            .where {
                with (userFilterDto.staffFilter ?: StaffFilterDto()) { expressionBuilder }
            }
            .map {
                val userDao = UserDao.wrapRow(it)
                StaffOutputDto(
                    userDao.idValue,
                    userDao.fullName,
                    AppConf.Positions.JUNIOR_MECHANIC.name,
                    orderInProgress = it[ordersInProgress[OrderModel.id].countDistinct()],
                    orderCompleted = it[completedOrders[OrderModel.id].countDistinct()],
                    hoursCompleted = it[WorkTypeModel.hours.sum()] ?: 0.0,
                    photo = listOf()
                )
            } /* Then select all washers */ + UserModel
            .innerJoin(PositionModel)
            .join(UserRoleModel, JoinType.INNER, UserModel.id, UserRoleModel.user) {
                UserRoleModel.role inList listOf(AppConf.roles.washer)
            }
            .join(WashModel, JoinType.LEFT, UserModel.id, WashModel.author) {
                rangeCond(
                    userFilterDto.dateRange,
                    WashModel.timestamp.isNotNull(),
                    WashModel.timestamp,
                    Long.MIN_VALUE, Long.MAX_VALUE
                )
            }
            .select(
                UserModel.id, UserModel.fullName,
                PositionModel.name,
                WashModel.id.count()
            )
            .groupBy(UserModel.id, PositionModel.name)
            .where {
                UserModel.position inList (listOf(AppConf.Positions.WASHER.id)) and
                with (userFilterDto.staffFilter ?: StaffFilterDto()) { expressionBuilder }
            }
            .map {
                val userDao = UserDao.wrapRow(it)
                val washCount = it[WashModel.id.count()]
                StaffOutputDto(
                    userDao.idValue,
                    userDao.fullName,
                    it[PositionModel.name],
                    0, //For washers its always zero
                    washCount,
                    washCount * AppConf.washHoursNormalized, //Every wash = 2 hours
                    listOf()
                )
            }
    }

    fun exportStaffToPdf(userFilterDto: UserFilterDto) = transaction {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Сотрудники")
        val headerRow = sheet.createRow(0)
        headerRow.createCell(0).setCellValue( "ФИО" )
        headerRow.createCell(1).setCellValue( "Должность" )
        headerRow.createCell(2).setCellValue( "Наряды в работе" )
        headerRow.createCell(3).setCellValue( "Завершенные наряды" )
        headerRow.createCell(4).setCellValue( "Часы" )

        getStaffFiltered(userFilterDto).mapIndexed {
            index, user ->
                Logger.debug(index)
                Logger.debug(user)
                val dataRow = sheet.createRow(index + 1)
                val fullNameCell = dataRow.createCell(0, CellType.STRING)
                fullNameCell.setCellValue(user.fullName)
                val positionCell = dataRow.createCell(1, CellType.STRING)
                positionCell.setCellValue(user.position)
                val ordersInProgressCell = dataRow.createCell(2, CellType.STRING)
                ordersInProgressCell.setCellValue(user.orderInProgress.toString())
                val completedOrdersCell = dataRow.createCell(3, CellType.STRING)
                completedOrdersCell.setCellValue(user.orderCompleted.toString())
                val hoursCell = dataRow.createCell(4, CellType.STRING)
                hoursCell.setCellValue(user.hoursCompleted.toString())
        }

        val outputStream = ByteArrayOutputStream()
        workbook.write(outputStream)
        workbook.close()

        outputStream.toByteArray()
    }

    fun getOneStaff(authorizedUser: AuthorizedUser, staffID: Int): StaffDto = transaction {
        if (staffID == 0)
            UserDao[authorizedUser.id].staffDto
        else
            UserDao[staffID].staffDto
    }

    fun getOneDriver(authorizedUser: AuthorizedUser, driverId: Int): DriverDto = transaction {
        if (driverId == 0)
            UserDao[authorizedUser.id].driverDto
        else
            UserDao[driverId].driverDto
    }
}