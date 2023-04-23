package com.custom.http.client.optional_converter

import android.annotation.TargetApi
import com.custom.http.client.retrofit_3.Retrofit3
import com.custom.http.client.annotation.util.IgnoreJRERequirement
import com.custom.http.client.converters.Converter
import okhttp3.ResponseBody
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.*

@IgnoreJRERequirement // Only added when Optional is available (Java 8+ / Android API 24+).
@TargetApi(24)
class OptionalConverterFactory: Converter.Factory() {

    override fun responseBodyConverter(type: Type, annotations: Array<Annotation>, retrofit: Retrofit3): Converter<ResponseBody, *>? {

        if (getRawType(type = type) != Optional::class.java)
            return null

        val innerType = getParameterUpperBound(0, type as ParameterizedType)
        val delegate = retrofit.responseBodyConverter<Any>(innerType, annotations)
        return OptionalConverter(delegate = delegate)
    }
}