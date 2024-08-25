package com.fleetmate.stat.modules.order.service


import com.fleetmate.lib.exceptions.ForbiddenException
import com.fleetmate.lib.shared.conf.AppConf
import com.fleetmate.lib.shared.dto.StatusDto
import com.fleetmate.lib.shared.modules.wash.model.WashModel
import com.fleetmate.lib.utils.database.idValue
import com.fleetmate.lib.utils.kodein.KodeinService
import com.fleetmate.stat.modules.fault.dao.FaultDao
import com.fleetmate.stat.modules.order.data.dao.OrderDao
import com.fleetmate.stat.modules.order.data.dao.WashDao
import com.fleetmate.stat.modules.order.data.dao.WorkDao
import com.fleetmate.stat.modules.order.data.dao.WorkTypeDao
import com.fleetmate.stat.modules.order.data.dto.WashDto
import com.fleetmate.stat.modules.order.data.dto.order.*
import com.fleetmate.stat.modules.order.data.dto.work.CreateWorkDto
import com.fleetmate.stat.modules.order.data.model.WorkActorsModel
import com.fleetmate.stat.modules.user.dao.UserDao
import com.fleetmate.stat.modules.user.dto.MechanicWorkListItemDto
import com.fleetmate.stat.modules.user.model.UserHoursModel
import io.ktor.util.date.*
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.kodein.di.DI

class OrderService(di: DI) : KodeinService(di) {
    fun getAllStatuses(): List<StatusDto> =
        AppConf.OrderStatus.entries.map {
            StatusDto(it.id, it.name)
        }

    fun getAllFiltered(orderFilterDto: OrderFilterDto): List<OrderListItemDto> = transaction {
        OrderDao.find {
            with(orderFilterDto) { expressionBuilder }
        }.map { it.listItemDto }
    }

    /*
     id, Номер наряда, Дата начала, Дата завершения,
      Механик, Часы, ТС (Название, Название типа, госномер)
     */

    fun getOne(orderId: Int): OrderDto =
        OrderDao[orderId].toOutputDto()

    fun getOrderWorkList(orderId: Int): List<OrderWorkDto> =
        OrderDao[orderId].works.map { it.orderWorkDto }


    fun create(createOrderDto: CreateOrderDto): OrderDto = transaction {
        var fault = FaultDao[createOrderDto.faultId]

        if (fault.status != AppConf.FaultStatus.CREATED.name)
            throw ForbiddenException()

        val order = OrderDao.new {
            number = createOrderDto.number
            status = AppConf.OrderStatus.CREATED.name
            mechanic = UserDao[createOrderDto.mechanicId]
            fault = FaultDao[createOrderDto.faultId]
            startedAt = getTimeMillis()
        }

        fault.critical = true
        fault.status = AppConf.FaultStatus.UNDER_WORK.name
        fault.flush()

        order.toOutputDto()
    }

    fun appendWork(createWorkDto: CreateWorkDto): OrderWorkDto = transaction {
        val order = OrderDao[createWorkDto.orderId]

        val work = WorkDao.new {
            type = WorkTypeDao[createWorkDto.workType]
            this.order = order
        }

        WorkActorsModel.batchInsert(createWorkDto.actors) {
            this[WorkActorsModel.work] = work.idValue
            this[WorkActorsModel.actor] = it
            this[WorkActorsModel.order] = order.idValue
        }

        work.orderWorkDto
    }

    fun close(orderId: Int): OrderDto = transaction {
        val order = OrderDao[orderId]

        order.status = AppConf.OrderStatus.CLOSED.name
        order.fault.status = AppConf.FaultStatus.FIXED.name
        order.fault.critical = false

        order.fault.flush()
        order.flush()

        val actors = order.works.map { it.actors }.flatten().map { it.id }
        val dividedHours = order.hours / actors.size

        UserHoursModel.batchInsert(actors) {
            this[UserHoursModel.hours] = dividedHours
            this[UserHoursModel.user] = it
        }

        WorkActorsModel.update({ WorkActorsModel.order eq orderId }) {
            it[closed] = true
        }

        order.toOutputDto()
    }

    fun getWorkListByJuniorMechanic(juniorMechanicId: Int): List<MechanicWorkListItemDto> = transaction {
        WorkDao.getByJuniorMechanic(juniorMechanicId)
    }

    fun getWashListByWasher(washerId: Int): List<WashDto> = transaction {
        WashDao.find { WashModel.author eq washerId }.map { it.toOutputDto() }
    }

}