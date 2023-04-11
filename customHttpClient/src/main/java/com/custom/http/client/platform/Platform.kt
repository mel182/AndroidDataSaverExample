package com.custom.http.client.platform

import com.custom.http.client.CallAdapter
import com.custom.http.client.Converter
import java.lang.reflect.Method
import java.util.concurrent.Executor

abstract class Platform {

    private fun createPlatform(): Platform {

        return when (System.getProperty("java.vm.name")) {
            "Dalvik" -> {
                if (Android24.isSupported()) {
                    Android24()
                } else retrofit2.Platform.Android21()
            }
            "RoboVM" -> retrofit2.Platform.RoboVm()
            else -> {
                if (retrofit2.Platform.Java16.isSupported()) {
                    return retrofit2.Platform.Java16()
                }
                if (retrofit2.Platform.Java14.isSupported()) {
                    retrofit2.Platform.Java14()
                } else retrofit2.Platform.Java8()
            }
        }
    }

    abstract fun defaultCallbackExecutor(): Executor?

    abstract fun createDefaultCallAdapterFactories(callbackExecutor: Executor?): List<CallAdapter.Factory?>

    abstract fun createDefaultConverterFactories(): List<Converter.Factory?>?

    abstract fun isDefaultMethod(method: Method?): Boolean

    @Throws(Throwable::class)
    abstract fun invokeDefaultMethod(method: Method?, declaringClass: Class<*>?, proxy: Any?, vararg args: Any?): Any?


}