package com.github.davidpdp.compress.internal.strategies

import com.github.davidpdp.compress.api.CompressAlgorithm
import com.github.davidpdp.compress.api.CompressAlgorithm.Companion.KB
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.*
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream


object GzipBase64: CompressAlgorithm {

    override fun marshall(property: Any, minKbSizeToApply: Float): Any {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(property)
        objectOutputStream.close()
        val uncompressedBytes = byteArrayOutputStream.toByteArray()

        val compressedBase64Stream = ByteArrayOutputStream()
        GZIPOutputStream(compressedBase64Stream).use {
            gzipOutputStream -> gzipOutputStream.write(uncompressedBytes)
        }

        return Base64.getEncoder().encodeToString(compressedBase64Stream.toByteArray())
    }

    override fun unmarshall(compressProperty: String): Any {
        val compressedData: ByteArray = Base64.getDecoder().decode(compressProperty)

        val compressedStream = ByteArrayInputStream(compressedData)
        val gzipInputStream = GZIPInputStream(compressedStream)
        val byteArrayOutputStream = ByteArrayOutputStream()

        val buffer = ByteArray(KB)
        var bytesRead: Int
        while (gzipInputStream.read(buffer).also { bytesRead = it } > 0) {
            byteArrayOutputStream.write(buffer, 0, bytesRead)
        }

        val decompressedData = byteArrayOutputStream.toByteArray()

        ObjectInputStream(ByteArrayInputStream(decompressedData)).use {
            objectInputStream -> return objectInputStream.readObject()
        }
    }
}