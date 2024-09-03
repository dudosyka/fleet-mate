package com.fleetmate.stat.modules.order.data.dao


import com.fleetmate.lib.shared.conf.AppConf
import com.fleetmate.lib.shared.modules.fault.model.FaultModel
import com.fleetmate.lib.utils.database.BaseIntEntity
import com.fleetmate.lib.utils.database.BaseIntEntityClass
import com.fleetmate.lib.utils.database.idValue
import com.fleetmate.stat.modules.fault.dao.FaultDao
import com.fleetmate.stat.modules.order.data.dto.order.OrderDto
import com.fleetmate.stat.modules.order.data.dto.order.OrderOutputDto
import com.fleetmate.stat.modules.order.data.dto.work.WorkListItemDto
import com.fleetmate.stat.modules.order.data.model.OrderModel
import com.fleetmate.stat.modules.order.data.model.WorkModel
import com.fleetmate.lib.shared.modules.fault.model.WorkTypeModel
import com.fleetmate.lib.shared.modules.user.model.UserModel
import com.fleetmate.stat.modules.order.data.model.WorkActorsModel
import com.fleetmate.stat.modules.user.dao.UserDao
import io.ktor.util.date.*
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
    val juniorMechanicSimplifier by OrderModel._juniorMechanicFilterSimplifier

    override fun toOutputDto(): OrderDto =
        OrderDto(idValue, number, status, mechanicId.value, faultId.value, startedAt, closedAt)

    val hours: Double get() =
        WorkModel.leftJoin(WorkTypeModel).select(WorkTypeModel.hours).where { WorkModel.order eq idValue }.toList().sumOf { it[WorkTypeModel.hours] }

    val works: List<WorkDao> get() =
        WorkDao.find { WorkModel.order eq idValue }.toList()

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

    fun updateToClosed() {
        OrderModel.update({ OrderModel.id eq idValue }) {
            it[status] = AppConf.OrderStatus.CLOSED.name
            it[closedAt] = getTimeMillis()
        }
        FaultModel.update({ FaultModel.id eq faultId.value }) {
            it[status] = AppConf.FaultStatus.FIXED.name
            it[critical] = false
        }
    }

    fun updateJuniorMechanicSimplifier(actors: List<Int>) {
        val exist = WorkActorsModel.select(WorkActorsModel.actor).where { WorkActorsModel.order eq idValue }.map { it[WorkActorsModel.actor].value }
        OrderModel.update({ OrderModel.id eq idValue }) {
            it[_juniorMechanicFilterSimplifier] = this@OrderDao.juniorMechanicSimplifier + UserModel.select(UserModel.fullName).where { UserModel.id inList actors.filter { exist.contains(it) } }.map {
                row -> row[UserModel.fullName]
            }.reduce { a,b -> "$a $b" }
        }
    }
}