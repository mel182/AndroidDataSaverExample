package com.custom.http.client.extensions

import com.custom.http.client.annotation.http.call_properties.Path
import com.custom.http.client.utils.ParameterizedTypeImpl

fun Path.asParameterized(): ParameterizedTypeImpl = ParameterizedTypeImpl(
    ownerType = null,
    rawType = String::class.java,
    typeArguments = emptyArray()
)