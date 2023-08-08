package com.github.davidpdp.compress.api

interface CompressAlgorithm {

    companion object {
        const val KB = 1024
    }

    fun marshall(property: Any, minKbSizeToApply: Float): Any

    fun unmarshall(compressProperty: String): Any

    fun convertToKb(sizeInBytes: Float): Float {
        return sizeInBytes / KB
    }
}