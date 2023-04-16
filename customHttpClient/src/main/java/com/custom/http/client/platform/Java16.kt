package com.custom.http.client.platform

import android.os.Build
import androidx.annotation.RequiresApi
import com.custom.http.client.*
import java.lang.reflect.InvocationHandler
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.util.*
import java.util.concurrent.Executor

@RequiresApi(Build.VERSION_CODES.O)
class Java16 : Platform() {

    private var invokeDefaultMethod: Method? = null

    companion object {

        fun isSupported():Boolean {
            return try {
                val version = Runtime::class.java.getMethod("version").invoke(null)
                val feature = version.javaClass.getMethod("feature").invoke(version) as Int
                feature >= 16
            } catch (ignored: InvocationTargetException) {
                false
            } catch (ignored: IllegalAccessException) {
                false
            } catch (ignored: NoSuchMethodException) {
                false
            }
        }
    }

    override fun defaultCallbackExecutor(): Executor? = null

    override fun createDefaultCallAdapterFactories(callbackExecutor: Executor?): List<CallAdapter.Factory?> {
        return listOf(
            CompletableFutureCallAdapterFactory(),
            DefaultCallAdapterFactory(callbackExecutor = callbackExecutor)
        )
    }

    override fun createDefaultConverterFactories(): List<Converter.Factory>? {
        return Collections.singletonList(OptionalConverterFactory())
    }

    override fun isDefaultMethod(method: Method?): Boolean = method?.isDefault ?: false

    override fun invokeDefaultMethod(method: Method?, declaringClass: Class<*>?, proxy: Any?, vararg args: Any?): Any? {

        if (this.invokeDefaultMethod == null) {
            this.invokeDefaultMethod = InvocationHandler::class.java.getMethod(
                "invokeDefault",
                Any::class.java,
                Method::class.java,
                Array<Any>::class.java
            )
        }

        return invokeDefaultMethod?.invoke(null, proxy, method, args)
    }
}