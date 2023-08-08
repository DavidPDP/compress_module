package com.github.davidpdp.compress.internal.serializers

import com.fasterxml.jackson.databind.BeanDescription
import com.fasterxml.jackson.databind.SerializationConfig
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier
import com.github.davidpdp.compress.api.Compress

class CompressSerializerModifier: BeanSerializerModifier() {

    override fun changeProperties(
        config: SerializationConfig,
        beanDesc: BeanDescription,
        beanProperties: MutableList<BeanPropertyWriter>
    ): MutableList<BeanPropertyWriter> {

        for (beanProperty in beanProperties) {
            val annotation = beanProperty.getAnnotation(Compress::class.java)
            if (annotation != null) {
                beanProperty.assignSerializer(
                    CompressSerializer(annotation.algorithm, annotation.minKbSizeToApply)
                )
            }
        }

        return super.changeProperties(config, beanDesc, beanProperties)
    }

}