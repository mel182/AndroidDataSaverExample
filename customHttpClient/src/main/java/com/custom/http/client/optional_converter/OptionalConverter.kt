package com.custom.http.client.optional_converter

import android.os.Build
import androidx.annotation.RequiresApi
import com.custom.http.client.Converter
import com.custom.http.client.annotation.util.IgnoreJRERequirement
import okhttp3.ResponseBody
import java.util.Optional

@IgnoreJRERequirement
class OptionalConverter<T : Any>(private val delegate: Converter<ResponseBody, T>?):
    Converter<ResponseBody, Optional<T>> {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun convert(value: ResponseBody): Optional<T> = Optional.ofNullable(delegate?.convert(value))

}