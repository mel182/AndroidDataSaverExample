package com.custom.http.client.platform

import android.annotation.TargetApi
import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import com.custom.http.client.CallAdapter
import com.custom.http.client.Converter
import com.custom.http.client.annotation.util.IgnoreJRERequirement
import java.lang.invoke.MethodHandles
import java.lang.reflect.Constructor
import java.lang.reflect.Method
import java.util.concurrent.Executor

@IgnoreJRERequirement
@TargetApi(24)
class Android24 : Platform() {

    companion object {
        @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.N)
        fun isSupported(): Boolean = Build.VERSION.SDK_INT >= 24
    }

    private val lookupConstructor: Constructor<MethodHandles.Lookup>? = null

    override fun defaultCallbackExecutor(): Executor = MainThreadExecutor.INSTANCE

    override fun createDefaultCallAdapterFactories(callbackExecutor: Executor?): List<CallAdapter.Factory?> {

        //TODO: Work in progress
        return listOf(

        )

    }

    override fun createDefaultConverterFactories(): List<Converter.Factory?>? {
        TODO("Not yet implemented")
    }

    override fun isDefaultMethod(method: Method?): Boolean {
        TODO("Not yet implemented")
    }

    override fun invokeDefaultMethod(
        method: Method?,
        declaringClass: Class<*>?,
        proxy: Any?,
        vararg args: Any?
    ): Any? {
        TODO("Not yet implemented")
    }
}