package com.custom.http.client.converters.converter_types

import com.custom.http.client.converters.Converter
import okhttp3.ResponseBody

class StreamingResponseBodyConverter: Converter<ResponseBody, ResponseBody> {

    companion object {
        val INSTANCE: StreamingResponseBodyConverter = StreamingResponseBodyConverter()
    }

    override fun convert(value: ResponseBody): ResponseBody = value

}