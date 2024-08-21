package com.fleetmate.lib.utils.files

import com.fleetmate.lib.data.dto.photo.PhotoCreateDto
import com.fleetmate.lib.data.dto.photo.PhotoUploadDto
import com.fleetmate.lib.data.dto.photo.PhotoOutputDto
import com.fleetmate.lib.data.model.photo.PhotoModel
import com.fleetmate.lib.exceptions.InternalServerException
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
    fun create(photoCreateDto: PhotoCreateDto): PhotoOutputDto =
        PhotoOutputDto(PhotoModel.create(photoCreateDto))

    fun delete(id: Int): Boolean =
        PhotoModel.delete(id)
}