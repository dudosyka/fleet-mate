package com.fleetmate.stat.modules.user.dto.output

abstract class UserBaseOutput {
    abstract val id: Int
    abstract val fullName: String
    abstract val photo: List<String>?
}