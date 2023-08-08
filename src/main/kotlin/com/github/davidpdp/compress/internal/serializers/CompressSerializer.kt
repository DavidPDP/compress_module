package com.github.davidpdp.compress.internal.serializers

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.github.davidpdp.compress.api.CompressAlgorithmType

class CompressSerializer(
    private val compressAlgorithmType: CompressAlgorithmType,
    private val minKbSizeToApply: Float
): JsonSerializer<Any>() {

    override fun serialize(value: Any, gen: JsonGenerator, serializers: SerializerProvider) {
        gen.writeObject(compressAlgorithmType.marshall(value, minKbSizeToApply))
    }
}