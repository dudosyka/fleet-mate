package com.fleetmate.crypt.modules.auth.service

import com.fleetmate.lib.data.dto.auth.QrTokenDto
import com.fleetmate.lib.data.model.car.CarModel
import com.fleetmate.lib.data.dto.auth.AuthorizedUser
import com.fleetmate.lib.exceptions.ForbiddenException
import com.fleetmate.lib.data.model.user.UserModel
import com.fleetmate.lib.plugins.Logger
import com.fleetmate.lib.utils.kodein.KodeinService
import com.fleetmate.lib.utils.security.jwt.JwtUtil
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.DI
import qrcode.QRCode
import qrcode.color.Colors

class AuthQrService(di: DI) : KodeinService(di) {

    private fun createQr(data: String) =
        QRCode
            .ofRoundedSquares()
            .withColor(Colors.BLACK)
            .withSize(5)
            .build(data)
            .renderToBytes()

    fun createUserQr(authorizedUser: AuthorizedUser): ByteArray = transaction {
        try {

            val user = UserModel.getOne(authorizedUser.id)
            if (user == null) {
                Logger.debugException("Exception during QR generation. User is null.", NullPointerException(), "main")
                throw ForbiddenException()
            }
            else{
                val token = JwtUtil.createMobileAuthToken(QrTokenDto(user[UserModel.id].value))
                createQr(token)
            }

        } catch (e: Exception) {
            Logger.debugException("Exception during QR generation", e, "main")
            throw ForbiddenException()
        }
    }

    fun createCarQr(authorizedUser: AuthorizedUser, carId: Int): ByteArray = transaction {
        try {
            val user = UserModel.getOne(authorizedUser.id)
            val car = CarModel.getOne(carId)
            if (user == null || car == null){
                Logger.debugException("Exception during QR generation. User or Automobile is null (maybe both).", NullPointerException(), "main")
                throw ForbiddenException()
            }
            val token = JwtUtil.createMobileAuthToken(
                QrTokenDto(
                    carId = car[CarModel.id].value
                )
            )
            createQr(token)
        } catch (e: Exception) {
            Logger.debugException("Exception during QR generation", e, "main")
            throw ForbiddenException()
        }
    }
}