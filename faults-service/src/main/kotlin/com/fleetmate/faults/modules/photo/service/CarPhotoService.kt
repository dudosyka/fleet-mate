package com.fleetmate.faults.modules.photo.service

import com.fleetmate.lib.data.dto.car.CarPhotoCreateDto
import com.fleetmate.lib.data.model.car.CarPhotoModel
import com.fleetmate.lib.data.model.car.CarToCarPhotoModel
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

class CarPhotoService(di: DI) : KodeinService(di) {
    private val photoService: PhotoService by instance()
    fun uploadPhotos(driver: Int, car: Int, images : List<String>) = transaction {

            val imageDto = mutableListOf<PhotoOutputDto>()
            images.forEach {

                val photoName = FilesUtil.buildName(car.toString() + LocalDateTime.now().toString())
                val compressImageName = FilesUtil.buildName("shakal" + car.toString() + LocalDateTime.now().toString())

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
            val carPhoto = CarPhotoModel.create(
                CarPhotoCreateDto(
                    car = car,
                    trip = null,
                    driver = driver,
                    photo = it.id
                )
            )
            CarToCarPhotoModel.insert {
                it[carId] = car
                it[carPhotoId] = carPhoto[id].value
            }
            commit()
        }
    }
}