package com.github.davidpdp.compress.api

@MustBeDocumented
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Compress(
    val algorithm: CompressAlgorithmType = CompressAlgorithmType.GZIP_BASE64,
    val minKbSizeToApply: Float = 1f
)