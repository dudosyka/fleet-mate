package com.fleetmate.faults.modules.check.service

import com.fleetmate.faults.modules.check.data.dto.CheckFinishInputDto
import com.fleetmate.faults.modules.check.data.dto.CheckStartInputDto
import com.fleetmate.faults.modules.check.data.dto.CheckStartOutputDto
import com.fleetmate.faults.modules.photo.service.AutomobilePhotoService
import com.fleetmate.lib.dto.check.CheckCreateDto
import com.fleetmate.lib.dto.check.CheckOutputDto
import com.fleetmate.lib.dto.check.CheckUpdateDto
import com.fleetmate.lib.model.check.CheckModel
import com.fleetmate.lib.utils.kodein.KodeinService
import org.kodein.di.DI
import org.kodein.di.instance
import kotlin.collections.map

class CheckService(di: DI) : KodeinService(di) {

    private val automobilePhotoService: AutomobilePhotoService by instance()

    fun getOne(id: Int): CheckOutputDto? {
        return CheckOutputDto(CheckModel.getOne(id) ?: return null)
    }

    fun getAll(): List<CheckOutputDto> {
        return CheckModel.getAll().map {
            CheckOutputDto(it)
        }
    }

    fun create(checkCreateDto: CheckCreateDto): CheckOutputDto =
        CheckOutputDto(CheckModel.create(checkCreateDto))

    fun update(id: Int, checkUpdateDto: CheckUpdateDto): Boolean =
        CheckModel.update(id, checkUpdateDto)

    fun delete(id: Int): Boolean =
        CheckModel.delete(id)

    fun checkStart(checkStartInputDto: CheckStartInputDto) =
        CheckStartOutputDto(
            CheckModel.start(
                checkStartInputDto.author,
                checkStartInputDto.automobileId
            )
        )

    fun checkFinish(checkFinishInputDto: CheckFinishInputDto): CheckOutputDto {
        val automobile = CheckModel.getAutomobile(checkFinishInputDto.checkId)
        automobilePhotoService.uploadPhotos(checkFinishInputDto.driver, automobile[CheckModel.automobileId].value, checkFinishInputDto.photos)
        return CheckOutputDto(
            CheckModel.finish(
                checkFinishInputDto.checkId
            )
        )
    }
}