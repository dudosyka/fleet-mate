package com.fleetmate.lib.data.model.car

import com.fleetmate.faults.modules.faults.data.model.FaultsModel
import com.fleetmate.lib.data.dto.car.CarPartOutputDto
import com.fleetmate.lib.data.dto.faults.FaultDto
import com.fleetmate.lib.dto.photo.PhotoOutputDto
import com.fleetmate.lib.model.photo.PhotoModel
import com.fleetmate.lib.utils.database.BaseIntIdTable
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update


object CarPartModel : BaseIntIdTable() {
    val fault = reference("fault", FaultsModel).nullable().default(null)
    val name = text("name")


    fun getOne(id: Int): ResultRow = transaction {
        selectAll().where{
            CarPartModel.id eq id
        }.first()
    }

    fun getTreeFrom(carPartOutputDto: CarPartOutputDto): List<CarPartOutputDto> = transaction {


            join(CarPartModel, JoinType.LEFT, CarPartToCarPartModel.child, CarPartModel.id)
            .join(FaultsModel, JoinType.LEFT, fault, FaultsModel.id)
            .join(PhotoModel, JoinType.LEFT, FaultsModel.photo, PhotoModel.id)
            .select(
                CarPartModel.id,
                name,
                CarPartToCarPartModel.child,
                FaultsModel.id,
                PhotoModel.id,
                PhotoModel.type,
                PhotoModel.link,
                FaultsModel.critical,
                FaultsModel.status,
                FaultsModel.comment

            ).where{
                CarPartToCarPartModel.parent eq carPartOutputDto.id
            }.orderBy(Pair(name, SortOrder.ASC)).map {
                CarPartOutputDto(
                    id = it[CarPartModel.id].value,
                    name = it[name]

                ).apply {
                    if (it[FaultsModel.id] != null){
                        fault = FaultDto(
                            it[FaultsModel.id].value,
                            PhotoOutputDto(
                                it[PhotoModel.id].value,
                                it[PhotoModel.link],
                                null,
                                it[PhotoModel.type]
                            ),
                            it[FaultsModel.status],
                            it[FaultsModel.comment],
                            it[FaultsModel.critical]
                        )
                    }
                    else{
                        fault = null
                    }
                }.apply {
                    children = getTreeFrom(this)
                }
            }
    }
    fun addFault(faultId: Int, carPartId: Int){
        update(
            {id eq carPartId}
        ){
            it[fault] = faultId
        }
    }
}