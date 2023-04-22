@file:Suppress("UNCHECKED_CAST")

package com.custom.http.client.converters

import com.custom.http.client.Retrofit3
import com.custom.http.client.annotation.http.Streaming
import com.custom.http.client.converters.converter_types.*
import com.custom.http.client.utils.Utils
import okhttp3.RequestBody
import okhttp3.ResponseBody
import java.lang.reflect.Type

class BuiltInConverters : Converter.Factory() {

    override fun responseBodyConverter(type: Type, annotations: Array<Annotation>, retrofit: Retrofit3): Converter<ResponseBody, *>? {

        return when {

            type === ResponseBody::class.java -> {
                if (Utils.isAnnotationPresent(annotations, Streaming::class.java)) {
                    StreamingResponseBodyConverter.INSTANCE
                } else {
                    BufferingResponseBodyConverter.INSTANCE
                }
            }

            type === Void::class.java -> VoidResponseBodyConverter.INSTANCE

            Utils.isUnit(type) -> UnitResponseBodyConverter.INSTANCE

            else -> null
        }
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit3
    ): Converter<*, RequestBody?>? {
        return if (RequestBody::class.java.isAssignableFrom(Utils.getRawType(type))) {
            RequestBodyConverter.INSTANCE as Converter<*, RequestBody?>
        } else null
    }
}