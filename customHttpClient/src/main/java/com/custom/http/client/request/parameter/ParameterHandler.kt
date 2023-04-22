package com.custom.http.client.request.parameter

import com.custom.http.client.Converter
import com.custom.http.client.Utils
import com.custom.http.client.constant.BLANK_STRING
import com.custom.http.client.constant.DEFAULT_BOOLEAN
import com.custom.http.client.request.RequestBuilder
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.IOException
import java.lang.reflect.Array
import java.lang.reflect.Method
import java.util.*

abstract class ParameterHandler<T : Any> {

    @Throws(IOException::class)
    abstract fun apply(builder: RequestBuilder?, value: T?)

    fun iterable(): ParameterHandler<Iterable<T>> {

        return object : ParameterHandler<Iterable<T>>() {

            @Throws(IOException::class)
            override fun apply(builder: RequestBuilder?, value: Iterable<T>?) {

                // in case of values is null, skip null values
                value?.let {

                    for (value in it) {
                        this@ParameterHandler.apply(builder, value)
                    }
                }
            }
        }
    }

    fun array(): ParameterHandler<Any> {

        return object : ParameterHandler<Any>() {

            @Throws(IOException::class)
            override fun apply(builder: RequestBuilder?, value: Any?) {

                value?.let { _value ->
                    var i = 0
                    val size = Array.getLength(_value)
                    while (i < size) {
                        this@ParameterHandler.apply(builder, Array.get(_value, i) as T)
                        i++
                    }
                }
            }
        }
    }

    companion object {
        internal class RelativeUrl(private val method:Method, private val parameter:Int) : ParameterHandler<Any>() {
            override fun apply(builder: RequestBuilder?, value: Any?) {
                value?.let {
                    builder?.setRelativeUrl(it)
                }?: kotlin.run {
                    throw Utils.parameterError(
                        method = method,
                        p = parameter,
                        message = "@Url parameter is null."
                    )
                }
            }
        }

        internal class Header<T : Any>(private val name:String?, private val valueConverter: Converter<T, String>?, private val allowUnsafeNonAsciiValues:Boolean = DEFAULT_BOOLEAN) : ParameterHandler<T>() {

            init {
                Objects.requireNonNull(name, "name == null")
            }

            override fun apply(builder: RequestBuilder?, value: T?) {
                value?.let {
                    valueConverter?.convert(value)?.let { headerValue ->
                        builder?.addHeader(name = name, value = headerValue, allowUnsafeNonAsciiValues = allowUnsafeNonAsciiValues)
                    }
                }
            }
        }

        internal class Path<T : Any>(private val method:Method, private val parameter: Int, private val name:String?, private val valueConverter: Converter<T, String>?, private val encoded:Boolean) : ParameterHandler<T>() {

            init {
                Objects.requireNonNull(name, "name == null")
            }

            override fun apply(builder: RequestBuilder?, value: T?) {

                value?.let {
                    builder?.addPathParam(name = name ?: BLANK_STRING, value = valueConverter?.convert(value) ?: BLANK_STRING, encoded)
                }?: kotlin.run {
                    throw Utils.parameterError(
                        method = method,
                        p = parameter,
                        message = "Path parameter \"$name\" value must not be null."
                    )
                }
            }
        }

        internal class Query<T : Any>(private val name:String?, private val valueConverter: Converter<T, String>?, private val encoded:Boolean) : ParameterHandler<T>() {

            init {
                Objects.requireNonNull(name, "name == null")
            }

            override fun apply(builder: RequestBuilder?, value: T?) {
                value?.let { _value ->
                    valueConverter?.convert(_value)?.let { query_value ->
                        builder?.addQueryParam(name = name ?: BLANK_STRING, value = query_value, encoded = encoded)
                    }
                }
            }
        }

        internal class QueryName<T : Any>(private val nameConverter: Converter<T, String>?, private val encoded:Boolean) : ParameterHandler<T>() {

            override fun apply(builder: RequestBuilder?, value: T?) {
                value?.let {
                    builder?.addQueryParam(name = nameConverter?.convert(it) ?: BLANK_STRING, value = null, encoded = encoded)
                }
            }
        }

        internal class QueryMap<T>(private val method:Method, private val parameter: Int, private val valueConverter: Converter<T, String>?, private val encoded:Boolean) : ParameterHandler<Map<String?, T>>() {
            override fun apply(builder: RequestBuilder?, value: Map<String?, T>?) {

                if (value == null)
                    throw Utils.parameterError(
                        method = method,
                        p = parameter,
                        message = "Query map was null"
                    )

                for (entry in value.entries) {

                    val entryKey = entry.key
                        ?: throw Utils.parameterError(
                            method = method,
                            p = parameter,
                            message = "Query map contained null key."
                        )

                    val entryValue = entry.value
                        ?: throw Utils.parameterError(
                            method = method,
                            p = parameter,
                            message = "Query map contained null value for key '$entryKey'."
                        )

                    val convertedEntryValue = valueConverter?.convert(entryValue)
                        ?: throw Utils.parameterError(
                            method = method,
                            p = parameter,
                            message = "Query map value '$entryKey' converted to null by ${valueConverter?.javaClass?.name} for key '$entryKey'."
                        )

                    builder?.addQueryParam(name = entryKey, value = convertedEntryValue, encoded = encoded)
                }
            }
        }

        internal class HeaderMap<T>(private val method:Method, private val parameter: Int, private val valueConverter: Converter<T, String>?, private val allowUnsafeNonAsciiValues:Boolean) : ParameterHandler<Map<String?, T>>() {
            override fun apply(builder: RequestBuilder?, value: Map<String?, T>?) {

                if (value == null)
                    throw Utils.parameterError(
                        method = method,
                        p = parameter,
                        message = "Header map was null."
                    )

                for (entry in value.entries) {

                    val headerName = entry.key
                        ?: throw Utils.parameterError(
                            method = method,
                            p = parameter,
                            message = "Header map contained null key."
                        )

                    val headerValue = entry.value
                        ?: throw Utils.parameterError(
                            method = method,
                            p = parameter,
                            message = "Header map contained null value for key '$headerName'."
                        )

                    builder?.addHeader(name = headerName, value = valueConverter?.convert(headerValue) ?: BLANK_STRING, allowUnsafeNonAsciiValues = allowUnsafeNonAsciiValues)
                }
            }
        }

        internal class Headers(private val method:Method, private val parameter: Int) : ParameterHandler<okhttp3.Headers>() {
            override fun apply(builder: RequestBuilder?, headers: okhttp3.Headers?) {

                if (headers == null)
                    throw Utils.parameterError(
                        method = method,
                        p = parameter,
                        message = "Headers parameter must not be null."
                    )

                builder?.addHeaders(headers)
            }
        }

        internal class Field<T : Any>(private val name: String?, private val valueConverter: Converter<T, String>?, private val encoded:Boolean) : ParameterHandler<T>() {

            init {
                Objects.requireNonNull(name, "name == null")
            }
            override fun apply(builder: RequestBuilder?, value: T?) {

                value?.let {

                    valueConverter?.convert(it)?.let { fieldValue ->
                        builder?.addFormField(name = name ?: BLANK_STRING, value = fieldValue, encoded = encoded)
                    }
                }
            }
        }

        internal class FieldMap<T>(private val method: Method, private val parameter: Int, private val valueConverter: Converter<T, String>?, private val encoded:Boolean) : ParameterHandler<Map<String?, T>>() {
            override fun apply(builder: RequestBuilder?, value: Map<String?, T>?) {

                if (value == null)
                    throw Utils.parameterError(
                        method = method,
                        p = parameter,
                        message = "Field map was null."
                    )

                for (entry in value.entries) {

                    val entryKey = entry.key ?: throw Utils.parameterError(
                        method = method,
                        p = parameter,
                        message = "Field map contained null key."
                    )

                    val entryValue = entry.value ?: throw Utils.parameterError(
                        method = method,
                        p = parameter,
                        message = "Field map contained null value for key '$entryKey'."
                    )

                    val fieldEntry = valueConverter?.convert(entryValue) ?: throw Utils.parameterError(
                        method = method,
                        p = parameter,
                        message = "Field map value '$entryKey' converted to null by ${valueConverter?.javaClass?.name} for key '$entryKey'."
                    )

                    builder?.addFormField(entryKey, fieldEntry, encoded)
                }
            }
        }

        internal class Part<T : Any>(private val method: Method, private val parameter: Int, private val converter: Converter<T, RequestBody>, private val headers:okhttp3.Headers) : ParameterHandler<T>() {
            override fun apply(builder: RequestBuilder?, value: T?) {

                value?.let {

                    val requestBody = try {
                        converter?.convert(it)
                    }catch (e:IOException) {
                        throw Utils.parameterError(
                            method = method,
                            p = parameter,
                            message = "Unable to convert $value to RequestBody",
                            e
                        )
                    }

                    builder?.addPart(headers, requestBody ?: throw Utils.parameterError(
                        method = method,
                        p = parameter,
                        message = "Request body is null"
                    )
                    )
                }
            }
        }

        internal class RawPart : ParameterHandler<MultipartBody.Part>() {

            companion object {
                val INSTANCE: RawPart = RawPart()
            }

            override fun apply(builder: RequestBuilder?, value: MultipartBody.Part?) {
                value?.let { _value ->
                    builder?.addPart(_value)
                }
            }
        }

        internal class PartMap<T>(private val method: Method, private val parameter: Int, private val valueConverter: Converter<T, RequestBody>?, private val transferEncoding:String) : ParameterHandler<Map<String?, T>>() {

            override fun apply(builder: RequestBuilder?, value: Map<String?, T>?) {

                if (value == null)
                    throw Utils.parameterError(
                        method = method,
                        p = parameter,
                        message = "Part map was null."
                    )

                for (entry in value.entries) {

                    val entryKey = entry.key ?: throw Utils.parameterError(
                        method = method,
                        p = parameter,
                        message = "Part map contained null key."
                    )

                    val entryValue = entry.value ?: throw Utils.parameterError(
                        method = method,
                        p = parameter,
                        message = "Part map contained null value for key '$entryKey'."
                    )

                    val headers = okhttp3.Headers.headersOf(
                        "Content-Disposition",
                        "form-data; name=\"$entryKey\"",
                        "Content-Transfer-Encoding",
                        transferEncoding
                    )

                    builder?.addPart(headers = headers, valueConverter?.convert(entryValue) ?: throw Utils.parameterError(
                        method = method,
                        p = parameter,
                        message = "Request body is null"
                    )
                    )
                }
            }
        }

        internal class Body<T : Any>(private val method: Method, private val parameter: Int, private val converter: Converter<T, RequestBody>?) : ParameterHandler<T>() {

            override fun apply(builder: RequestBuilder?, value: T?) {

                if (value == null)
                    throw Utils.parameterError(
                        method = method,
                        p = parameter,
                        message = "Body parameter value must not be null."
                    )

                val body = try {
                    converter?.convert(value) ?: throw IOException("Unable to convert $value to RequestBody")
                } catch (e:IOException) {
                    throw Utils.parameterError(
                        method = method,
                        cause = e,
                        p = parameter,
                        message = "Unable to convert $value to RequestBody"
                    )
                }

                builder?.setBody(body = body)
            }
        }

        internal class Tag<T : Any>(val cls: Class<T>) : ParameterHandler<T>() {

            override fun apply(builder: RequestBuilder?, value: T?) {
                builder?.addTag(cls = cls, value = value)
            }
        }
    }
}