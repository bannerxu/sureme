package top.banner.common.utils

import java.io.ByteArrayInputStream
import java.io.InputStream
import java.security.MessageDigest
import org.apache.commons.codec.binary.Hex


object  Md5Utils {


    fun md5(plainText: String?): String {
        try {
            return Hex.encodeHexString(md5(ByteArrayInputStream(plainText?.toByteArray(charset("utf-8")))))
        } catch (ex: Exception) {
            throw RuntimeException(ex)
        }

    }

    @Throws(Exception::class)
    fun md5(input: InputStream): ByteArray {
        return digest(input, "MD5")
    }

    @Throws(Exception::class)
    private fun digest(input: InputStream, algorithm: String): ByteArray {
        val messageDigest = MessageDigest.getInstance(algorithm)
        val bufferLength = 8192
        val buffer = ByteArray(bufferLength)

        var read = input.read(buffer, 0, bufferLength)
        while (read > -1) {
            messageDigest.update(buffer, 0, read)
            read = input.read(buffer, 0, bufferLength)
        }
        return messageDigest.digest()

    }
}