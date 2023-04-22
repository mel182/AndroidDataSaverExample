package com.custom.http.client.converters

import com.custom.http.client.Retrofit3
import com.custom.http.client.utils.Utils
import okhttp3.RequestBody
import okhttp3.ResponseBody
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

interface Converter<F, T> {

    @Throws(IOException::class)
    fun convert(value: F): T?

    abstract class Factory {

        open fun responseBodyConverter(type: Type, annotations: Array<Annotation>, retrofit: Retrofit3): Converter<ResponseBody, *>? = null

        open fun requestBodyConverter(type: Type, parameterAnnotations: Array<Annotation>, methodAnnotations: Array<Annotation>, retrofit: Retrofit3): Converter<*, RequestBody?>? = null

        open fun stringConverter(type: Type?, annotations: Array<Annotation?>?, retrofit: Retrofit3?): Converter<*, String?>? = null

        protected open fun getParameterUpperBound(index: Int, type: ParameterizedType?): Type? = Utils.getParameterUpperBound(index, type)

        protected open fun getRawType(type: Type?): Class<*>? = Utils.getRawType(type)

    }


}