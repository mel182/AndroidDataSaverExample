package com.custom.http.client.platform

import android.os.Build
import androidx.annotation.RequiresApi
import com.custom.http.client.*
import com.custom.http.client.annotation.util.IgnoreJRERequirement
import com.custom.http.client.call.CallAdapter
import com.custom.http.client.completable_future_call_adapter_factory.CompletableFutureCallAdapterFactory
import com.custom.http.client.default_call_adapter_factory.DefaultCallAdapterFactory
import com.custom.http.client.optional_converter.OptionalConverterFactory
import java.lang.invoke.MethodHandles
import java.lang.reflect.Constructor
import java.lang.reflect.Method
import java.util.*
import java.util.concurrent.Executor

@IgnoreJRERequirement // Only used on JVM and Java 8 is the minimum-supported version.
@RequiresApi(Build.VERSION_CODES.O)
class Java8: Platform() {

    private var lookupConstructor: Constructor<MethodHandles.Lookup>? = null

    override fun defaultCallbackExecutor(): Executor? = null

    override fun createDefaultCallAdapterFactories(callbackExecutor: Executor?): List<CallAdapter.Factory?> {
        return listOf(
            CompletableFutureCallAdapterFactory(),
            DefaultCallAdapterFactory(callbackExecutor = callbackExecutor)
        )
    }

    override fun createDefaultConverterFactories(): List<Converter.Factory> = Collections.singletonList(
        OptionalConverterFactory()
    )

    override fun isDefaultMethod(method: Method?): Boolean = method?.isDefault ?: false


    override fun invokeDefaultMethod(method: Method?, declaringClass: Class<*>?, proxy: Any?, vararg args: Any?): Any? {

        if (this.lookupConstructor == null) {
            this.lookupConstructor = MethodHandles.Lookup::class.java.getDeclaredConstructor(
                Class::class.java,
                Int::class.javaPrimitiveType
            ).also {
                it.isAccessible = true
            }
        }

        return this.lookupConstructor
            ?.newInstance(declaringClass, -1)
            ?.unreflectSpecial(method,declaringClass)
            ?.bindTo(proxy)
            ?.invokeWithArguments(args)
    }

}