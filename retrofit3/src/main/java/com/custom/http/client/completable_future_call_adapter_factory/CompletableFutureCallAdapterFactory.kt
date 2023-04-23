package com.custom.http.client.completable_future_call_adapter_factory

import android.annotation.TargetApi
import com.custom.http.client.call.CallAdapter
import com.custom.http.client.response.Response
import com.custom.http.client.retrofit_3.Retrofit3
import com.custom.http.client.annotation.util.IgnoreJRERequirement
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.concurrent.CompletableFuture

@IgnoreJRERequirement // Only added when CompletableFuture is available (Java 8+ / Android API 24+).
@TargetApi(24)
class CompletableFutureCallAdapterFactory : CallAdapter.Factory() {

    override fun get(
        returnType: Type?,
        annotations: Array<Annotation>?,
        retrofit: Retrofit3?
    ): CallAdapter<*, *>? = getRawType(returnType)?.let { raw_type ->

        if (raw_type != CompletableFuture::class.java)
            return null

        check(returnType !is ParameterizedType) {
            ("CompletableFuture return type must be parameterized as CompletableFuture<Foo> or CompletableFuture<? extends Foo>")
        }

        val innerType = getParameterUpperBound(0, returnType as ParameterizedType)

        if (getRawType(innerType) != Response::class.java) {
            // Generic type is not Response<T>. Use it for body-only adapter.
            return BodyCallAdapter<Any>(innerType)
        }

        // Generic type is Response<T>. Extract T and create the Response version of the adapter.
        if (innerType !is ParameterizedType)
            throw java.lang.IllegalStateException("Response must be parameterized as Response<Foo> or Response<? extends Foo>")

        val responseType: Type = getParameterUpperBound(0, innerType)
        return ResponseCallAdapter<Any>(responseType = responseType)
    }
}