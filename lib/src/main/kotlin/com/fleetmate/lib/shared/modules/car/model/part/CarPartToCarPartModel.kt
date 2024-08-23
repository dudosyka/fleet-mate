package com.fleetmate.lib.shared.modules.car.model.part


import com.fleetmate.lib.exceptions.NotFoundException
import com.fleetmate.lib.shared.modules.car.dto.CarPartDto
import com.fleetmate.lib.utils.database.BaseIntIdTable
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object CarPartToCarPartModel : BaseIntIdTable() {
    val parent = reference("parent", CarPartModel, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val child = reference("child", CarPartModel, ReferenceOption.CASCADE, ReferenceOption.CASCADE)

    private fun buildRecursive(carPartDto: CarPartDto): List<CarPartDto> = transaction {
        CarPartToCarPartModel
            .join(CarPartModel, JoinType.LEFT, child, CarPartModel.id)
            .select(
                CarPartModel.id,
                CarPartModel.name,
                child,

                ).where {
                    parent eq carPartDto.id
                }.orderBy(Pair(CarPartModel.name, SortOrder.ASC)).map {
                    CarPartDto(
                        id = it[CarPartModel.id].value,
                        name = it[CarPartModel.name],
                    ).apply {
                        children = buildRecursive(this)
                    }
                }
    }

    fun getTreeFrom(carPartId: Int?): List<CarPartDto> = transaction {
        if (carPartId == null)
            return@transaction listOf()

        val carPartRow = CarPartModel.selectAll().where {
            CarPartModel.id eq carPartId
        }.firstOrNull() ?: throw NotFoundException("Car part not found")

        buildRecursive(
            CarPartDto(
                carPartRow[CarPartModel.id].value,
                carPartRow[CarPartModel.name],
                children = listOf()
            )
        )
    }
}