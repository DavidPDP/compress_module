package com.github.davidpdp.compress.internal.deserializers

import com.fasterxml.jackson.databind.BeanDescription
import com.fasterxml.jackson.databind.DeserializationConfig
import com.fasterxml.jackson.databind.deser.BeanDeserializerBuilder
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier
import com.github.davidpdp.compress.api.Compress

class CompressDeserializerModifier: BeanDeserializerModifier() {

    override fun updateBuilder(
        config: DeserializationConfig,
        beanDesc: BeanDescription,
        builder: BeanDeserializerBuilder
    ): BeanDeserializerBuilder {

        val beanProperties = builder.properties
        while (beanProperties.hasNext()) {
            val beanProperty = beanProperties.next()
            if (beanProperty.getAnnotation(Compress::class.java) != null) {
                builder.addOrReplaceProperty(
                    beanProperty.withValueDeserializer(CompressDeserializer()),
                    true
                )
            }
        }

        return builder
    }
}