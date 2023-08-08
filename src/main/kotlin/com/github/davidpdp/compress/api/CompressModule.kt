package com.github.davidpdp.compress.api

import com.fasterxml.jackson.databind.module.SimpleModule
import com.github.davidpdp.compress.internal.deserializers.CompressDeserializerModifier
import com.github.davidpdp.compress.internal.serializers.CompressSerializerModifier

class CompressModule: SimpleModule() {
    init {
        setSerializerModifier(CompressSerializerModifier())
        setDeserializerModifier(CompressDeserializerModifier())
    }
}