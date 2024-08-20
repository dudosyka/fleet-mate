package com.fleetmate.lib.utils.security.ecdh

import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object AesUtil {
    private fun createKey(base: String): SecretKeySpec {
        val md = MessageDigest.getInstance("SHA-256")
        val bytes = base.toByteArray(StandardCharsets.UTF_8)
        md.update(bytes, 0, bytes.size)
        val key = md.digest()
        return SecretKeySpec(key, "AES")
    }

    fun encrypt(keyBase: String, value: ByteArray): ByteArray {
        val key = createKey(keyBase)
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val ivParameterSpec = IvParameterSpec(ByteArray(16))
        cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec)
        return cipher.doFinal(value)
    }

    fun decrypt(keyBase: String, value: ByteArray): ByteArray {
        val key = createKey(keyBase)
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val ivParameterSpec = IvParameterSpec(ByteArray(16))
        cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec)
        return cipher.doFinal(value)
    }

    private fun getSymmetricalKey(clientPublic: PointDto): PointDto = ECDH.getSymmetricalKey(clientPublic)

    fun snapshotSession(clientPublic: PointDto): Pair<String, PointDto> = Pair(
        getSymmetricalKey(clientPublic).createKeyBase(), ECDH.retrievePublics()
    )

    fun encrypt(data: ByteArray, clientPublic: PointDto): ByteArray {
        val symmetricalKey = getSymmetricalKey(clientPublic).createKeyBase()
        return encrypt(symmetricalKey, data)
    }

    fun decrypt(data: ByteArray, clientPublic: PointDto): ByteArray {
        val symmetricalKey = getSymmetricalKey(clientPublic).createKeyBase()
        return decrypt(symmetricalKey, data)
    }
}