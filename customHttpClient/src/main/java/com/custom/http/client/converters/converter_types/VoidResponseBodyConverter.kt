package com.custom.http.client.converters.converter_types

import com.custom.http.client.converters.Converter
import okhttp3.ResponseBody

class VoidResponseBodyConverter: Converter<ResponseBody, Void> {

    companion object {
        val INSTANCE: VoidResponseBodyConverter = VoidResponseBodyConverter()
    }

    override fun convert(value: ResponseBody): Void? {
        value.close()
        return null
    }

}