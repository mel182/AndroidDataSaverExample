package com.retrofit3.retrofit_converter.gson

import com.custom.http.client.converters.Converter
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.Buffer
import java.io.OutputStreamWriter
import java.io.Writer
import java.nio.charset.StandardCharsets

class GsonRequestBodyConverter<T>(private val gson:Gson, private val adapter:TypeAdapter<T>): Converter<T,RequestBody> {

    private val MEDIA_TYPE = "application/json; charset=UTF-8".toMediaType()

    override fun convert(value: T): RequestBody {
        val buffer = Buffer()
        val writer: Writer = OutputStreamWriter(buffer.outputStream(), StandardCharsets.UTF_8)
        val jsonWriter = gson.newJsonWriter(writer)
        jsonWriter.use {
            adapter.write(jsonWriter, value)
        }

        return buffer.readByteString().toRequestBody(MEDIA_TYPE)
    }


}