package com.fleetmate.faults.modules.faults.service

import com.fleetmate.lib.data.dto.faults.FaultsCreateDto
import com.fleetmate.lib.data.dto.faults.FaultsFullOutputDto
import com.fleetmate.lib.data.model.faults.FaultsModel
import com.fleetmate.lib.data.dto.faults.FaultDto
import com.fleetmate.lib.data.model.faults.FaultPhotoModel
import com.fleetmate.lib.data.model.photo.PhotoModel
import com.fleetmate.lib.data.dto.auth.AuthorizedUser
import com.fleetmate.lib.exceptions.NotFoundException
import com.fleetmate.lib.utils.files.PhotoService
import com.fleetmate.lib.utils.kodein.KodeinService
import org.kodein.di.DI
import org.kodein.di.instance

class FaultsService(di: DI) : KodeinService(di) {
    private val photoService: PhotoService by instance()
    fun getOne(id: Int): FaultsFullOutputDto {
        val photos = FaultPhotoModel.getByFault(id)
        val fault = FaultsFullOutputDto(FaultsModel.getOne(id) ?: throw NotFoundException("Fault not found"))
        fault.photos = photos.map { it[PhotoModel.link] }
        return fault
    }

    fun getAll(): List<FaultDto> =
        FaultsModel.getAll().map { FaultDto(it) }

    fun create(authorizedUser: AuthorizedUser, faultsCreateDto: FaultsCreateDto): FaultDto {
        val faultPhotos = faultsCreateDto.photos.map {
            photoService.create(it).id
        }

        val fault = FaultDto(FaultsModel.create(authorizedUser, faultsCreateDto))

        FaultPhotoModel.appendToFault(faultPhotos, fault.id)

        return fault
    }

    fun getCriticalByCar(carId: Int): List<FaultDto> =
        FaultsModel.getAllCriticalByCar(carId).map { FaultDto(it) }
}