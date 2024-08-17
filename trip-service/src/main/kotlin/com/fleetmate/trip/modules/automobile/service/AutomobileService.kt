package com.fleetmate.trip.modules.automobile.service

import com.fleetmate.lib.data.dto.automobile.AutomobileFullOutputDto
import com.fleetmate.lib.data.model.automobile.AutomobilePartModel
import com.fleetmate.lib.dto.automobile.AutomobileCreateDto
import com.fleetmate.lib.dto.automobile.AutomobileOutputDto
import com.fleetmate.lib.dto.automobile.AutomobileUpdateDto
import com.fleetmate.lib.dto.photo.PhotoCreateDto
import com.fleetmate.lib.exceptions.NotFoundException
import com.fleetmate.lib.model.automobile.AutomobileModel
import com.fleetmate.lib.model.trip.TripModel
import com.fleetmate.lib.utils.files.FilesUtil
import com.fleetmate.lib.utils.kodein.KodeinService
import com.fleetmate.trip.modules.automobile.data.dto.AutomobileRefuelDto
import com.fleetmate.trip.modules.photo.service.PhotoService
import com.fleetmate.trip.modules.refuel.data.dto.RefuelCreateDto
import com.fleetmate.trip.modules.refuel.data.dto.RefuelInputDto
import com.fleetmate.trip.modules.refuel.data.model.RefuelModel
import com.fleetmate.trip.modules.trip.service.trip.TripService
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.DI
import org.kodein.di.instance
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.collections.map

class AutomobileService(di: DI) : KodeinService(di) {

    private val automobilePartService: AutomobilePartService by instance()
    private val photoService: PhotoService by instance()
    private val tripService: TripService by instance()

    fun getOne(id: Int): AutomobileOutputDto? {
        return AutomobileOutputDto(AutomobileModel.getOne(id) ?: return null)
    }

    fun getAll(): List<AutomobileOutputDto> {
        return AutomobileModel.getAll().map {
            AutomobileOutputDto(it)
        }
    }

    fun create(automobileCreateDto: AutomobileCreateDto): AutomobileOutputDto =
        AutomobileOutputDto(AutomobileModel.create(automobileCreateDto))

    fun update(id: Int, automobileUpdateDto: AutomobileUpdateDto): Boolean =
        AutomobileModel.update(id, automobileUpdateDto)

    fun delete(id: Int): Boolean =
        AutomobileModel.delete(id)

    fun getFullInfo(id: Int): AutomobileFullOutputDto {
        val automobile = getOne(id)
        val root = automobilePartService.getOne(automobile?.type?.id)
        if (root != null && automobile != null){
            val parts = AutomobilePartModel.getTreeFrom(
                root
            )
            return AutomobileFullOutputDto(
                automobile,
                parts
            )
        }
        throw NotFoundException("Information about automobile or automobile parts is not found")
    }
    fun checkFuel(automobileId: Int): AutomobileRefuelDto {
        val auto = AutomobileModel.getOne(automobileId) ?: throw NotFoundException(
            "Automobile is not found"
        )
        return if (auto[AutomobileModel.fuelLevel] <= 10) {
            AutomobileRefuelDto(
                true
            )
        }else{
            AutomobileRefuelDto(
                false
            )
        }
    }
    fun refuel(refuelInputDto: RefuelInputDto) = transaction{

        val photoName = FilesUtil.buildName(refuelInputDto.automobile.toString() + LocalDateTime.now().toString())
        val compressImageName = FilesUtil.buildName("shakal" + refuelInputDto.automobile.toString() + LocalDateTime.now().toString())


        val image = photoService.create(
            PhotoCreateDto(
                link = photoName,
                date = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
                type = "refuel" //FIXME
            )
        )
        photoService.create(
            PhotoCreateDto(
                link = compressImageName,
                date = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
                type = "refuel" //FIXME
            )
        )
        FilesUtil.upload(refuelInputDto.billPhoto, photoName, compressImageName)

        val trip = tripService.getActiveTrip(refuelInputDto.automobile)

        RefuelModel.create(
            RefuelCreateDto(
                refuelInputDto.date,
                refuelInputDto.volume,
                refuelInputDto.automobile,
                trip[TripModel.id].value,
                refuelInputDto.driver,
                image.id
            )
        )
        val newFuelLevel = (getOne(refuelInputDto.automobile)?.fuelLevel!! + refuelInputDto.volume)
        update(
            refuelInputDto.automobile,
            AutomobileUpdateDto(
                fuelLevel = newFuelLevel
            )
        )
    }
}