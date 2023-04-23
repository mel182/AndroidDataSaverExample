package com.custom.http.client.converters.converter_types

import com.custom.http.client.converters.Converter
import okhttp3.RequestBody
import okhttp3.ResponseBody

class RequestBodyConverter: Converter<ResponseBody, RequestBody> {

    companion object {
        val INSTANCE: RequestBodyConverter = RequestBodyConverter()
    }

    override fun convert(value: ResponseBody): RequestBody? {
        value.close()
        return null
    }

}