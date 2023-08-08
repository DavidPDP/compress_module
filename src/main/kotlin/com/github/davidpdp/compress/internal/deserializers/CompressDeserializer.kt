package com.github.davidpdp.compress.internal.deserializers

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.github.davidpdp.compress.api.CompressAlgorithmType

class CompressDeserializer: JsonDeserializer<Any>(){

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Any {
       return if (p.currentToken() == JsonToken.VALUE_STRING) {
            CompressAlgorithmType.resolveUnmarshall(p.valueAsString)
        } else {
            throw IllegalArgumentException("The provided data, which is not a string, " +
                    "does not constitute valid compressed data.")
        }
    }
}