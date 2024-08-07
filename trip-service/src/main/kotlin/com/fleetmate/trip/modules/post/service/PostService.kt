package com.fleetmate.trip.modules.post.service

import com.fleetmate.lib.dto.post.PostCreateDto
import com.fleetmate.lib.dto.post.PostOutputDto
import com.fleetmate.lib.model.post.PostModel
import com.fleetmate.lib.utils.kodein.KodeinService
import org.kodein.di.DI
import kotlin.collections.map

class PostService(di: DI) : KodeinService(di) {
    fun getOne(id: Int): PostOutputDto? {
        return PostOutputDto(PostModel.getOne(id) ?: return null)
    }

    fun getAll(): List<PostOutputDto> {
        return PostModel.getAll().map {
            PostOutputDto(it)
        }
    }

    fun create(postCreateDto: PostCreateDto): PostOutputDto =
        PostOutputDto(PostModel.create(postCreateDto))

    fun update(id: Int, postUpdateDto: PostCreateDto): Boolean =
        PostModel.update(id, postUpdateDto)

    fun delete(id: Int): Boolean =
        PostModel.delete(id)
}