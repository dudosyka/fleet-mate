package com.fleetmate.faults.modules.photo.service

import com.fleetmate.lib.data.dto.automobile.AutomobilePhotoCreateDto
import com.fleetmate.lib.data.model.automobile.AutomobilePhotoModel
import com.fleetmate.lib.data.model.automobile.AutomobileToAutomobilePhotoModel
import com.fleetmate.lib.dto.photo.PhotoCreateDto
import com.fleetmate.lib.dto.photo.PhotoOutputDto
import com.fleetmate.lib.utils.files.FilesUtil
import com.fleetmate.lib.utils.kodein.KodeinService
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.DI
import org.kodein.di.instance
import java.time.LocalDateTime
import java.time.ZoneOffset

class AutomobilePhotoService(di: DI) : KodeinService(di) {
    private val photoService: PhotoService by instance()
    fun uploadPhotos(driver: Int, automobile: Int,  images : List<String>) = transaction {

            val imageDto = mutableListOf<PhotoOutputDto>()
            images.forEach {

                val photoName = FilesUtil.buildName(automobile.toString() + LocalDateTime.now().toString())
                val compressImageName = FilesUtil.buildName("shakal" + automobile.toString() + LocalDateTime.now().toString())

                imageDto.add(
                    photoService.create(
                        PhotoCreateDto(
                            link = photoName,
                            date = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
                            type = "type" //FIXME
                        )
                    )
                )
                photoService.create(
                    PhotoCreateDto(
                        link = compressImageName,
                        date = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
                        type = "type" //FIXME
                    )
                )
                FilesUtil.upload(it, photoName, compressImageName)

            }

            commit()

        imageDto.forEach {
            val automobilePhoto = AutomobilePhotoModel.create(
                AutomobilePhotoCreateDto(
                    automobile = automobile,
                    trip = null,
                    driver = driver,
                    photo = it.id
                )
            )
            AutomobileToAutomobilePhotoModel.insert {
                it[automobileId] = automobile
                it[automobilePhotoId] = automobilePhoto[id].value
            }
            commit()
        }
    }
}