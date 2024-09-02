package com.fleetmate.stat.modules.order.data.dao


import com.fleetmate.lib.shared.conf.AppConf
import com.fleetmate.lib.shared.modules.fault.model.FaultModel
import com.fleetmate.lib.utils.database.BaseIntEntity
import com.fleetmate.lib.utils.database.BaseIntEntityClass
import com.fleetmate.lib.utils.database.idValue
import com.fleetmate.stat.modules.fault.dao.FaultDao
import com.fleetmate.stat.modules.order.data.dto.order.OrderDto
import com.fleetmate.stat.modules.order.data.dto.order.OrderListItemDto
import com.fleetmate.stat.modules.order.data.dto.order.OrderOutputDto
import com.fleetmate.stat.modules.order.data.dto.work.WorkListItemDto
import com.fleetmate.stat.modules.order.data.model.OrderModel
import com.fleetmate.stat.modules.order.data.model.WorkModel
import com.fleetmate.stat.modules.order.data.model.WorkTypeModel
import com.fleetmate.stat.modules.user.dao.UserDao
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.update

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

    val fullOutputDto: OrderOutputDto get() =
        OrderOutputDto(idValue, number, mechanic.staffDto, workList)

    val workList: List<WorkListItemDto> get() =
        works.map {
            WorkListItemDto(
                it.idValue,
                it.type.name,
                it.actors.map {
                    it.fullName
                },
                it.type.hours
            )
        }.toList()

    fun updateByStatus(statusName: AppConf.OrderStatus) {
        OrderModel.update({ OrderModel.id eq idValue }) {
            it[status] = statusName.name
        }
    }
    fun updateFaultByStatus(statusName: AppConf.FaultStatus) {
        FaultModel.update({ FaultModel.id eq faultId }) {
            it[status] = statusName.name
        }
    }
    fun updateFaultByCritical(isCritical: Boolean) {
        FaultModel.update({ FaultModel.id eq faultId }) {
            it[critical] = isCritical
        }
    }
}