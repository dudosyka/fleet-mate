package com.fleetmate.stat.modules.order.data.dao


import com.fleetmate.lib.utils.database.BaseIntEntity
import com.fleetmate.lib.utils.database.BaseIntEntityClass
import com.fleetmate.lib.utils.database.idValue
import com.fleetmate.stat.modules.fault.dao.FaultDao
import com.fleetmate.stat.modules.order.data.dto.order.OrderDto
import com.fleetmate.stat.modules.order.data.dto.order.OrderListItemDto
import com.fleetmate.stat.modules.order.data.model.OrderModel
import com.fleetmate.stat.modules.order.data.model.WorkModel
import com.fleetmate.stat.modules.order.data.model.WorkTypeModel
import com.fleetmate.stat.modules.user.dao.UserDao
import org.jetbrains.exposed.dao.id.EntityID

class OrderDao(id: EntityID<Int>) : BaseIntEntity<OrderDto>(id, OrderModel) {
    companion object : BaseIntEntityClass<OrderDto, OrderDao>(OrderModel)

    var number by OrderModel.number
    var status by OrderModel.status
    var mechanic by UserDao referencedOn OrderModel.mechanic
    val mechanicId by OrderModel.mechanic
    var fault by FaultDao referencedOn OrderModel.fault
    val faultId by OrderModel.fault
    var startedAt by OrderModel.startedAt
    val closedAt by OrderModel.closedAt

    override fun toOutputDto(): OrderDto =
        OrderDto(idValue, number, status, mechanicId.value, faultId.value, startedAt, closedAt)

    val hours: Double get() =
        WorkModel.leftJoin(WorkTypeModel).select(WorkTypeModel.hours).where { WorkModel.order eq idValue }.toList().sumOf { it[WorkTypeModel.hours] }

    val works: List<WorkDao> =
        WorkDao.find { WorkModel.order eq idValue }.toList()

    val listItemDto: OrderListItemDto get() =
        OrderListItemDto(idValue, number, startedAt, closedAt, mechanic.simpleDto, hours, fault.car.simpleDto)
}