package com.retrofit3.retrofit_converter.gson

import com.custom.http.client.converters.Converter
import com.custom.http.client.retrofit_3.Retrofit3
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.RequestBody
import okhttp3.ResponseBody
import java.lang.reflect.Type

/**
 * A {@linkplain Converter.Factory converter} which uses Gson for JSON.
 *
 * <p>Because Gson is so flexible in the types it supports, this converter assumes that it can
 * handle all types. If you are mixing JSON serialization with something else (such as protocol
 * buffers), you must {@linkplain Retrofit.Builder#addConverterFactory(Converter.Factory) add this
 * instance} last to allow the other converters a chance to see their types.
 */
class GsonConverterFactory(val gson:Gson): Converter.Factory() {

    override fun responseBodyConverter(type: Type, annotations: Array<Annotation>, retrofit: Retrofit3): Converter<ResponseBody, *> {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return GsonResponseBodyConverter(gson, adapter)
    }

    override fun requestBodyConverter(type: Type,
                                      parameterAnnotations: Array<Annotation>,
                                      methodAnnotations: Array<Annotation>,
                                      retrofit: Retrofit3): Converter<*, RequestBody> = GsonRequestBodyConverter(gson = gson, adapter = gson.getAdapter(TypeToken.get(type)))

}