/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.core.util

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties.*
import ru.surfstudio.android.logger.Logger
import java.nio.charset.StandardCharsets
import java.security.Key
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

/** Utils for encryption/decryption */
object SecurityUtil {

    private const val provider = "AndroidKeyStore"
    private const val IV_SIZE_IN_BYTES = 16
    private val cipher by lazy {
        Cipher.getInstance("AES/CBC/PKCS7Padding")
    }
    private val keyStore by lazy {
        KeyStore.getInstance(provider).apply {
            load(null)
        }
    }
    private val keyGenerator by lazy {
        KeyGenerator.getInstance(KEY_ALGORITHM_AES, provider)
    }

    /** Encrypt data via alias */
    fun encrypt(alias: String, data: String): String {
        val ivBytes = getRandomBytes()
        val key: Key = if (keyStore.containsAlias(alias)) {
            keyStore.getKey(alias, null)
        } else {
            generateSecretKey(alias)
        }
        cipher.init(Cipher.ENCRYPT_MODE, key, IvParameterSpec(ivBytes))
        return (ivBytes + cipher.doFinal(data.toByteArray(StandardCharsets.UTF_8)))
            .encodeStringForPreferences()
    }

    /** Decrypt data via alias */
    fun decrypt(alias: String, data: String): String? {
        return try {
            val dataBytes = data.decodeStringFromPreferences()
            val ivBytes = dataBytes.take(IV_SIZE_IN_BYTES).toByteArray()
            cipher.init(
                Cipher.DECRYPT_MODE,
                getSecretKey(alias),
                IvParameterSpec(ivBytes)
            )
            cipher.doFinal(dataBytes.drop(IV_SIZE_IN_BYTES).toByteArray())
                .toString(StandardCharsets.UTF_8)
        } catch (e: Exception) {
            Logger.e("Error during decryption", e)
            null
        }
    }

    private fun generateSecretKey(keyAlias: String): SecretKey {
        return keyGenerator.apply {
            init(
                KeyGenParameterSpec
                    .Builder(keyAlias, PURPOSE_ENCRYPT or PURPOSE_DECRYPT)
                    .setBlockModes(BLOCK_MODE_CBC)
                    .setEncryptionPaddings(ENCRYPTION_PADDING_PKCS7)
                    // provide randomly generated IV instead of saving chipher.iv after encryption
                    .setRandomizedEncryptionRequired(false)
                    .setKeySize(128)
                    .build(),
                SecureRandom()
            )
        }.generateKey()
    }

    private fun getSecretKey(keyAlias: String): SecretKey =
        (keyStore.getEntry(keyAlias, null) as KeyStore.SecretKeyEntry).secretKey

    /** Converts a String "[1, -2, -38, 41 ...]" to its [ByteArray] representation */
    private fun String.decodeStringFromPreferences(): ByteArray {
        val split = substring(1, length - 1).split(", ")
        val array = ByteArray(split.size)
        for (i in split.indices) {
            array[i] = java.lang.Byte.parseByte(split[i])
        }
        return array
    }

    /** Converts a [ByteArray] to String "[1, -2, -38, 41 ...]" */
    private fun ByteArray.encodeStringForPreferences(): String {
        return this.contentToString()
    }

    private fun getRandomBytes(size: Int = IV_SIZE_IN_BYTES): ByteArray {
        val bytes = ByteArray(size)
        SecureRandom().nextBytes(bytes)
        return bytes
    }
}