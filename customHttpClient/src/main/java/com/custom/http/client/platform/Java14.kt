package com.custom.http.client.platform

import android.os.Build
import androidx.annotation.RequiresApi
import com.custom.http.client.*
import com.custom.http.client.annotation.util.IgnoreJRERequirement
import com.custom.http.client.completable_future_call_adapter_factory.CompletableFutureCallAdapterFactory
import java.lang.invoke.MethodHandles
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.util.*
import java.util.concurrent.Executor

@IgnoreJRERequirement // Only used on JVM and Java 14.
@RequiresApi(Build.VERSION_CODES.O)
class Java14 : Platform() {

    companion object {

        fun isSupported():Boolean {
            return try {
                val version = Runtime::class.java.getMethod("version").invoke(null)
                val feature = version.javaClass.getMethod("feature").invoke(version) as Int
                feature >= 14
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

        return MethodHandles.lookup()
            .unreflectSpecial(method, declaringClass)
            .bindTo(proxy)
            .invokeWithArguments(args)
    }
}