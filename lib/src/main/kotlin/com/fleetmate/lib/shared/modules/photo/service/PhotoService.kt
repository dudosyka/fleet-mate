package com.fleetmate.lib.shared.modules.photo.service

import com.fleetmate.lib.shared.modules.photo.data.dto.PhotoOutputDto
import com.fleetmate.lib.exceptions.InternalServerException
import com.fleetmate.lib.shared.modules.photo.data.dto.PhotoCreateDto
import com.fleetmate.lib.shared.modules.photo.data.dto.PhotoUploadDto
import com.fleetmate.lib.shared.modules.photo.data.model.PhotoModel
import com.fleetmate.lib.utils.files.FilesUtil
import com.fleetmate.lib.utils.kodein.KodeinService
import org.kodein.di.DI

class PhotoService(di: DI) : KodeinService(di) {
    fun upload(photoUploadDto: PhotoUploadDto): PhotoOutputDto {
        val photoName = FilesUtil.buildName(photoUploadDto.photoName)
        val compressedPhotoName = FilesUtil.buildName("compressed_${photoUploadDto.photoName}")

        val compressedBillPhoto = create(
            PhotoCreateDto(
                link = compressedPhotoName,
                type = photoUploadDto.type ?: throw InternalServerException("Type must be provided"),
                original = photoName
            )
        )

        FilesUtil.upload(photoUploadDto.photo, photoName, compressedPhotoName)

        return compressedBillPhoto
    }

    fun upload(photoUploadDto: List<PhotoUploadDto>): List<PhotoOutputDto> {
        return photoUploadDto.map {
            upload(it)
        }
    }
    fun create(photoCreateDto: PhotoCreateDto): PhotoOutputDto =
        PhotoOutputDto(PhotoModel.create(photoCreateDto))

    fun delete(id: Int): Boolean =
        PhotoModel.delete(id)
}