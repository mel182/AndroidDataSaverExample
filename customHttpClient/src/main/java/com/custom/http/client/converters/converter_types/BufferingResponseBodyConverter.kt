package com.custom.http.client.converters.converter_types

import com.custom.http.client.converters.Converter
import com.custom.http.client.utils.Utils
import okhttp3.ResponseBody
import java.io.IOException

class BufferingResponseBodyConverter: Converter<ResponseBody, ResponseBody> {

    companion object {
        val INSTANCE: BufferingResponseBodyConverter = BufferingResponseBodyConverter()
    }

    @Throws(IOException::class)
    override fun convert(value: ResponseBody): ResponseBody = value.use {
        // Buffer the entire body to avoid future I/O.
        Utils.buffer(it)
    }
}