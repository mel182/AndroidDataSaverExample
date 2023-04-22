package com.custom.http.client.platform

import com.custom.http.client.converters.Converter
import com.custom.http.client.call.CallAdapter
import com.custom.http.client.default_call_adapter_factory.DefaultCallAdapterFactory
import java.lang.reflect.Method
import java.util.*
import java.util.concurrent.Executor

class RoboVm : Platform() {

    override fun defaultCallbackExecutor(): Executor? = null

    override fun createDefaultCallAdapterFactories(callbackExecutor: Executor?): List<CallAdapter.Factory> {
        return Collections.singletonList(DefaultCallAdapterFactory(callbackExecutor = callbackExecutor))
    }

    override fun createDefaultConverterFactories(): List<Converter.Factory> = emptyList()

    override fun isDefaultMethod(method: Method?): Boolean = false

    override fun invokeDefaultMethod(method: Method?, declaringClass: Class<*>?, proxy: Any?, vararg args: Any?): Any? {
        throw java.lang.AssertionError()
    }

}