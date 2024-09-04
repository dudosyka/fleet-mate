package com.fleetmate.file.modules.stream

import com.fleetmate.lib.exceptions.NotFoundException
import com.fleetmate.lib.shared.modules.photo.data.model.PhotoModel
import com.fleetmate.lib.utils.files.FilesUtil
import com.fleetmate.lib.utils.kodein.KodeinService
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.DI

class StreamService(di: DI) : KodeinService(di) {
    fun getPhotoById(id: Int): ByteArray = transaction {
        val photo = PhotoModel.select(PhotoModel.link).where { PhotoModel.id eq id }.firstOrNull() ?: throw NotFoundException("")

        FilesUtil.read(photo[PhotoModel.link]) ?: throw NotFoundException("")
    }

    fun getPhotoByFileName(fileName: String): ByteArray = transaction {
        FilesUtil.read(fileName) ?: throw NotFoundException("")
    }

    fun getPhotoOriginById(id: Int): ByteArray = transaction {
        val photo = PhotoModel.select(PhotoModel.original).where { PhotoModel.id eq id }.firstOrNull()?.get(PhotoModel.original) ?: (return@transaction getPhotoById(id))

        FilesUtil.read(photo) ?: throw NotFoundException("")
    }

    fun getPhotoOriginByFileName(fileName: String): ByteArray = transaction {
        val photo = PhotoModel.select(PhotoModel.original).where { PhotoModel.link eq fileName }.firstOrNull()?.get(PhotoModel.original) ?: (return@transaction getPhotoByFileName(fileName))

        FilesUtil.read(photo) ?: throw NotFoundException("")
    }

}