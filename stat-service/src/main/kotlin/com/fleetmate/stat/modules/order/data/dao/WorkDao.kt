package com.fleetmate.stat.modules.order.data.dao


import com.fleetmate.lib.shared.modules.fault.model.FaultModel
import com.fleetmate.lib.shared.modules.user.model.UserModel
import com.fleetmate.lib.utils.database.BaseIntEntity
import com.fleetmate.lib.utils.database.BaseIntEntityClass
import com.fleetmate.lib.utils.database.idValue
import com.fleetmate.stat.modules.order.data.dto.order.OrderWorkDto
import com.fleetmate.stat.modules.order.data.dto.work.WorkActorDto
import com.fleetmate.stat.modules.order.data.dto.work.WorkDto
import com.fleetmate.stat.modules.order.data.model.OrderModel
import com.fleetmate.stat.modules.order.data.model.WorkActorsModel
import com.fleetmate.stat.modules.order.data.model.WorkModel
import com.fleetmate.lib.shared.modules.fault.model.WorkTypeModel
import com.fleetmate.stat.modules.user.dto.MechanicWorkListItemDto
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.transactions.transaction

class WorkDao(id: EntityID<Int>) : BaseIntEntity<WorkDto>(id, WorkModel) {
    companion object : BaseIntEntityClass<WorkDto, WorkDao>(WorkModel) {
        fun getByJuniorMechanic(juniorMechanicId: Int): List<MechanicWorkListItemDto> = transaction {
            WorkActorsModel
                .leftJoin(WorkModel)
                .leftJoin(WorkTypeModel)
                .join(OrderModel, JoinType.LEFT, OrderModel.id, WorkModel.order)
                .leftJoin(FaultModel)
                .join(UserModel, JoinType.LEFT, OrderModel.mechanic, UserModel.id)
                .select(
                    OrderModel.id, OrderModel.number, OrderModel.startedAt, WorkTypeModel.hours, FaultModel.id, UserModel.fullName
                )
                .where {
                    WorkActorsModel.actor eq juniorMechanicId
                }
                .map {
                    MechanicWorkListItemDto(
                        it[OrderModel.number],
                        it[OrderModel.startedAt],
                        it[WorkTypeModel.hours],
                        it[FaultModel.id].value,
                        it[OrderModel.id].value,
                        it[UserModel.fullName]
                    )
                }
        }

    }

    var type by WorkTypeDao referencedOn WorkModel.type
    val typeId by WorkModel.type
    var order by OrderDao referencedOn WorkModel.order
    val orderId by WorkModel.order

    private val actorsRows: List<ResultRow> get() =
        WorkActorsModel.leftJoin(UserModel).select(WorkActorsModel.actor, UserModel.fullName).where { WorkActorsModel.work eq idValue }.toList()

    val actorsIds: List<Int> get() =
        actorsRows.map { it[WorkActorsModel.actor].value }

    val actors: List<WorkActorDto> get() =
        actorsRows.map { WorkActorDto(it[WorkActorsModel.actor].value, it[UserModel.fullName]) }

    override fun toOutputDto(): WorkDto =
        WorkDto(idValue, typeId.value, actorsIds)

    val orderWorkDto: OrderWorkDto get() =
        OrderWorkDto(type.toOutputDto(), actors)
}