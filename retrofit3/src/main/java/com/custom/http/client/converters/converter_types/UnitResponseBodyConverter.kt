package com.custom.http.client.converters.converter_types

import com.custom.http.client.converters.Converter
import okhttp3.ResponseBody

class UnitResponseBodyConverter: Converter<ResponseBody, Unit> {

    companion object {
        val INSTANCE: VoidResponseBodyConverter = VoidResponseBodyConverter()
    }

    override fun convert(value: ResponseBody): Unit? {
        value.close()
        return null
    }
}