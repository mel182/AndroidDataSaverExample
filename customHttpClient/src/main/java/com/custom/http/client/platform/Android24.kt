package com.custom.http.client.platform

import android.annotation.TargetApi
import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import com.custom.http.client.*
import com.custom.http.client.annotation.util.IgnoreJRERequirement
import com.custom.http.client.call.CallAdapter
import com.custom.http.client.completable_future_call_adapter_factory.CompletableFutureCallAdapterFactory
import com.custom.http.client.converters.Converter
import com.custom.http.client.default_call_adapter_factory.DefaultCallAdapterFactory
import com.custom.http.client.optional_converter.OptionalConverterFactory
import java.lang.invoke.MethodHandles.Lookup
import java.lang.reflect.Constructor
import java.lang.reflect.Method
import java.util.*
import java.util.concurrent.Executor

@IgnoreJRERequirement
@TargetApi(24)
class Android24 : Platform() {

    companion object {
        @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.N)
        fun isSupported(): Boolean = Build.VERSION.SDK_INT >= 24
    }

    private var lookupConstructor: Constructor<Lookup>? = null

    override fun defaultCallbackExecutor(): Executor = MainThreadExecutor.INSTANCE

    override fun createDefaultCallAdapterFactories(callbackExecutor: Executor?): List<CallAdapter.Factory> {
        return listOf(
            CompletableFutureCallAdapterFactory(),
            DefaultCallAdapterFactory(callbackExecutor = callbackExecutor)
        )
    }

    override fun createDefaultConverterFactories(): List<Converter.Factory>? = Collections.singletonList(
        OptionalConverterFactory()
    )

    override fun isDefaultMethod(method: Method?): Boolean = method?.isDefault ?: false

    override fun invokeDefaultMethod(method: Method?, declaringClass: Class<*>?, proxy: Any?, vararg args: Any?): Any? {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            throw UnsupportedOperationException("Calling default methods on API 24 and 25 is not supported")

        if (this.lookupConstructor == null)
        {
            lookupConstructor = Lookup::class.java.getDeclaredConstructor(Class::class.java, Int::class.javaPrimitiveType).also {
                it.isAccessible = true
            }
        }

        return lookupConstructor
            ?.newInstance(declaringClass, -1)
            ?.unreflectSpecial(method,declaringClass)
            ?.bindTo(proxy)
            ?.invoke(args)
    }
}