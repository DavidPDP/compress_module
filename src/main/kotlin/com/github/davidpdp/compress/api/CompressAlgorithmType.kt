package com.github.davidpdp.compress.api

import com.github.davidpdp.compress.internal.strategies.GzipBase64
import java.util.*

private const val DATA_URI_FORMAT = "data:application/json;Content-Encoding=%s;%s,%s"
private val DATA_URI_REGEX = "^data:application/json;Content-Encoding=(\\w+);(\\w+),".toRegex()

enum class CompressAlgorithmType: CompressAlgorithm {

    GZIP_BASE64 {
        override fun marshall(property: Any, minKbSizeToApply: Float): String {
            return DATA_URI_FORMAT.format("gzip", "base64", GzipBase64.marshall(property, minKbSizeToApply))
        }

        override fun unmarshall(compressProperty: String): Any {
            return GzipBase64.unmarshall(compressProperty)
        }
    };

    companion object {
        fun resolveUnmarshall(dataUri: String): Any {
            val (isValidDataUri, algorithm, data) = cutDataUri(dataUri)

            if (isValidDataUri) {
                return CompressAlgorithmType.valueOf(algorithm).unmarshall(data)
            }

            throw IllegalArgumentException(
                "Invalid Data URI format. The correct format is: data:application/json;" +
                        "Content-Encoding=[byteEncoding];[stringEncoding], ensuring the final comma is included.")
        }

        private fun cutDataUri(dataUri: String): Triple<Boolean, String, String> {
            val matchResult = DATA_URI_REGEX.find(dataUri.take(100))

            if (matchResult != null) {
                val startIndex = matchResult.range.last + 1
                return Triple(
                    true,
                    matchResult.groupValues.drop(1).joinToString("_") {it.uppercase(Locale.ROOT)},
                    dataUri.substring(startIndex)
                )
            }

            return Triple(false, "", "")
        }
    }
}