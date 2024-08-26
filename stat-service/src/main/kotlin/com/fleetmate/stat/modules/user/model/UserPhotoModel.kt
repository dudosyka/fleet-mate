package com.fleetmate.stat.modules.user.model

import com.fleetmate.lib.shared.modules.photo.data.model.PhotoModel
import com.fleetmate.lib.shared.modules.user.model.UserModel
import com.fleetmate.lib.utils.database.BaseIntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object UserPhotoModel: BaseIntIdTable() {
    val user = reference("user", UserModel, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val photo = reference("photo", PhotoModel, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
}