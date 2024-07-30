package com.fleetmate.crypt.modules.access

import com.fleetmate.lib.utils.kodein.KodeinService
import com.fleetmate.lib.utils.security.ecdh.ECDH
import com.fleetmate.lib.utils.security.ecdh.PointDto
import org.kodein.di.DI

class AccessService(di: DI) : KodeinService(di) {
    fun getPublics(): PointDto = ECDH.retrievePublics()
}