package com.custom.http.client.default_call_adapter_factory

import com.custom.http.client.*
import com.custom.http.client.call.Call
import com.custom.http.client.call.CallAdapter
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.concurrent.Executor

class DefaultCallAdapterFactory(private val callbackExecutor: Executor?): CallAdapter.Factory() {

    override fun get(returnType: Type?, annotations: Array<Annotation>?, retrofit: Retrofit3?): CallAdapter<*, *>? {

        if (getRawType(returnType) != Call::class.java) {
            return null
        }

        require(returnType is ParameterizedType) { "Call return type must be parameterized as Call<Foo> or Call<? extends Foo>" }

        val responseType: Type = Utils.getParameterUpperBound(0, returnType)

        val executor = if (Utils.isAnnotationPresent(
                annotations ?: emptyArray(),
                SkipCallbackExecutor::class.java
            )
        ) null else callbackExecutor

        return object : CallAdapter<Any, Call<*>> {

            override fun responseType(): Type = responseType

            override fun adapt(call: Call<Any>): Call<*> {
                return executor?.let { ExecutorCallbackCall(it, call) } ?: call!!
            }
        }
    }
}