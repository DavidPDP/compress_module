package com.github.davidpdp.compress

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.davidpdp.compress.api.Compress
import com.github.davidpdp.compress.api.CompressModule
import com.github.davidpdp.compress.internal.deserializers.CompressDeserializer
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CompressIntegrationTest {

    private lateinit var mapper: ObjectMapper

    @BeforeAll
    fun setUp() {
        mapper = jacksonObjectMapper()
            .registerModule(CompressModule())
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
    }

    @ParameterizedTest
    @MethodSource("uncompressObjects")
    fun should_compress_object(objectToCompress: TestCompressionClass, expected: String) {
        assertEquals(
            expected,
            mapper.writeValueAsString(objectToCompress)
        )
    }

    @ParameterizedTest
    @MethodSource("compressObjects")
    fun should_uncompress_object(compressString: String, expected: TestCompressionClass) {
        assertEquals(
            expected,
            mapper.readValue(compressString, TestCompressionClass::class.java)
        )
    }

    private fun uncompressObjects(): Stream<Arguments> {
        return Stream.of(
            arguments(
                TestCompressionClass(listOf(1, 2, 3, 4, 5)),
                "{\"objectField\":\"data:application/json;Content-Encoding=gzip;base64,H4sIAAAAAAAAAFvzloG1uIhBKiuxLFGvtCQzR8+xqCixslgFTPlkFpfcXGKz72wH2yUmBsZoBsbEEgbhaB+Qav2cxLx0ff+krNTkEuuKgtIiBhGIhB5IQs8zryQ1PbXI+t/0tQsYmx9JMzEwVBQwMIBtE8RQJvRowZLvje0WQEs8GVjLEnNKUyuKGAQQ6vxKc5NSi9rWTJXlnvKgG2YYY3EhQx0DK5DFBGcxw1kscBYrAI3T89fnAAAA\"}"            )
        )
    }

    private fun compressObjects(): Stream<Arguments> {
        return Stream.of(
            arguments(
                "{\"objectField\":\"data:application/json;Content-Encoding=gzip;base64,H4sIAAAAAAAAAFvzloG1uIhBKiuxLFGvtCQzR8+xqCixslgFTPlkFpfcXGKz72wH2yUmBsZoBsbEEgbhaB+Qav2cxLx0ff+krNTkEuuKgtIiBhGIhB5IQs8zryQ1PbXI+t/0tQsYmx9JMzEwVBQwMIBtE8RQJvRowZLvje0WQEs8GVjLEnNKUyuKGAQQ6vxKc5NSi9rWTJXlnvKgG2YYY3EhQx0DK5DFBGcxw1kscBYrAI3T89fnAAAA\"}",
                TestCompressionClass(listOf(1, 2, 3, 4, 5))
            )
        )
    }

    data class TestCompressionClass(
        @JsonDeserialize(using = CompressDeserializer::class)
        @Compress(minKbSizeToApply = 0.0096f)
        var objectField: Any)
}