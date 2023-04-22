package com.custom.http.client.platform

import com.custom.http.client.Converter
import com.custom.http.client.call.CallAdapter
import java.lang.reflect.Method
import java.util.concurrent.Executor

abstract class Platform {

    companion object {
        fun get(): Platform = PLATFORM

        private val PLATFORM:Platform = createPlatform()

        private fun createPlatform(): Platform {

            return when (System.getProperty("java.vm.name")) {
                "Dalvik" -> {
                    if (Android24.isSupported()) {
                        Android24()
                    } else Android21()
                }
                "RoboVM" -> RoboVm()
                else -> if (Android24.isSupported()) {
                    Android24()
                } else Android21()
            }
        }
    }

    abstract fun defaultCallbackExecutor(): Executor?

    abstract fun createDefaultCallAdapterFactories(callbackExecutor: Executor?): List<CallAdapter.Factory?>

    abstract fun createDefaultConverterFactories(): List<Converter.Factory>?

    abstract fun isDefaultMethod(method: Method?): Boolean

    @Throws(Throwable::class)
    abstract fun invokeDefaultMethod(method: Method?, declaringClass: Class<*>?, proxy: Any?, vararg args: Any?): Any?

}