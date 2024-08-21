package com.fleetmate.lib.data.model.car

import com.fleetmate.lib.data.model.faults.FaultsModel
import com.fleetmate.lib.data.dto.car.part.CarPartOutputDto
import com.fleetmate.lib.data.dto.faults.FaultDto
import com.fleetmate.lib.utils.database.BaseIntIdTable
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction


object CarPartModel : BaseIntIdTable() {
    val name = text("name")


    fun getOne(id: Int): ResultRow? = transaction {
        selectAll().where{
            CarPartModel.id eq id
        }.firstOrNull()
    }

    fun getTreeFrom(carId: Int, carPartOutputDto: CarPartOutputDto): List<CarPartOutputDto> = transaction {
             join(CarPartModel, JoinType.LEFT, CarPartToCarPartModel.child, CarPartModel.id)
            .select(
                CarPartModel.id,
                name,
                CarPartToCarPartModel.child,

            ).where{
                CarPartToCarPartModel.parent eq carPartOutputDto.id
            }.orderBy(Pair(name, SortOrder.ASC)).map {
                CarPartOutputDto(
                    id = it[CarPartModel.id].value,
                    name = it[name],
                    faults = FaultsModel.getByCarPart(carId, it[CarPartModel.id].value).map {
                        faultRow -> FaultDto(faultRow)
                    }
                ).apply {
                    children = getTreeFrom(carId, this)
                }
            }
    }
}