package com.custom.http.client

import java.io.IOException

abstract class ParameterHandler<T> {

    @Throws(IOException::class)
    abstract fun apply(builder: RequestBuilder?, value: T?)

}