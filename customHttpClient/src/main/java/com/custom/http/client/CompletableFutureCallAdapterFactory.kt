package com.custom.http.client

import android.annotation.TargetApi
import com.custom.http.client.Utils.Companion.getRawType
import com.custom.http.client.annotation.util.IgnoreJRERequirement
import okhttp3.Response
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.concurrent.CompletableFuture

@IgnoreJRERequirement // Only added when CompletableFuture is available (Java 8+ / Android API 24+).
@TargetApi(24)
class CompletableFutureCallAdapterFactory : CallAdapter.Factory() {

    override fun get(
        returnType: Type?,
        annotations: Array<Annotation?>?,
        retrofit: Retrofit3?
    ): CallAdapter<*, *>? {



        val rawType = getRawType(returnType)

        return getRawType(returnType)?.let { raw_type ->

            if (raw_type != CompletableFuture::class.java)
                return null

            check(returnType is ParameterizedType) {
                ("CompletableFuture return type must be parameterized as CompletableFuture<Foo> or CompletableFuture<? extends Foo>")
            }

            val innerType = getParameterUpperBound(0, returnType as ParameterizedType?)

//            if (raw_type != Response::cl)

            /*
if (getRawType(innerType) != Response::class.java) {
        // Generic type is not Response<T>. Use it for body-only adapter.
        return BodyCallAdapter(innerType)
    }

    // Generic type is Response<T>. Extract T and create the Response version of the adapter.

    // Generic type is Response<T>. Extract T and create the Response version of the adapter.
    check(innerType is ParameterizedType) { "Response must be parameterized" + " as Response<Foo> or Response<? extends Foo>" }
    val responseType: Type = getParameterUpperBound(0, innerType as ParameterizedType?)
    return ResponseCallAdapter(responseType)

            */

            null
        }

    }

    /*
    if (getRawType(returnType) != CompletableFuture.class) {
      return null;
    }
    if (!(returnType instanceof ParameterizedType)) {
      throw new IllegalStateException(
          "CompletableFuture return type must be parameterized"
              + " as CompletableFuture<Foo> or CompletableFuture<? extends Foo>");
    }
    Type innerType = getParameterUpperBound(0, (ParameterizedType) returnType);

    if (getRawType(innerType) != Response.class) {
      // Generic type is not Response<T>. Use it for body-only adapter.
      return new BodyCallAdapter<>(innerType);
    }

    // Generic type is Response<T>. Extract T and create the Response version of the adapter.
    if (!(innerType instanceof ParameterizedType)) {
      throw new IllegalStateException(
          "Response must be parameterized" + " as Response<Foo> or Response<? extends Foo>");
    }
    Type responseType = getParameterUpperBound(0, (ParameterizedType) innerType);
    return new ResponseCallAdapter<>(responseType);
    */
}