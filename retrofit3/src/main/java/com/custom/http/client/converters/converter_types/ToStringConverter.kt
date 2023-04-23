package com.custom.http.client.converters.converter_types

import com.custom.http.client.converters.Converter

class ToStringConverter: Converter<Any, String> {

    companion object {
        val INSTANCE: ToStringConverter = ToStringConverter()
    }

    override fun convert(value: Any): String = value.toString()

}