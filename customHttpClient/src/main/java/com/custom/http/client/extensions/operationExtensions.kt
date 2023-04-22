@file:OptIn(ExperimentalCoroutinesApi::class)

package com.custom.http.client.extensions

import com.custom.http.client.Invocation
import com.custom.http.client.Retrofit3
import com.custom.http.client.call.Call
import com.custom.http.client.call.Callback
import com.custom.http.client.exceptions.HttpException
import com.custom.http.client.response.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.intrinsics.COROUTINE_SUSPENDED
import kotlin.coroutines.intrinsics.intercepted
import kotlin.coroutines.intrinsics.suspendCoroutineUninterceptedOrReturn
import kotlin.coroutines.resumeWithException

inline fun <reified T: Any> Retrofit3.create(): T = create(T::class.java)

suspend fun <T : Any> Call<T>.await(): T {
    return suspendCancellableCoroutine { continuation ->
        continuation.invokeOnCancellation {
            cancel()
        }
        enqueue(object : Callback<T> {

            override fun onResponse(call: Call<T>?, response: Response<T>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body == null) {
                        val invocation = call?.request()?.tag(Invocation::class.java)!!
                        val method = invocation.method
                        val e = KotlinNullPointerException("Response from " +
                                method.declaringClass.name +
                                '.' +
                                method.name +
                                " was null but response body type was declared as non-null")
                        continuation.resumeWithException(e)
                    } else {
                        continuation.resume(body){
                            continuation.resumeWithException(it)
                        }
                    }
                } else {
                    continuation.resumeWithException(HttpException(response))
                }
            }

            override fun onFailure(call: Call<T>?, t: Throwable?) {
                continuation.resumeWithException(t ?: java.lang.Exception("request failed for unknown reason"))
            }
        })
    }
}

suspend fun <T : Any> Call<T?>.await(): T? {
    return suspendCancellableCoroutine { continuation ->
        continuation.invokeOnCancellation {
            cancel()
        }
        enqueue(object : Callback<T?> {
            override fun onResponse(call: Call<T?>?, response: Response<T?>) {
                if (response.isSuccessful) {
                    continuation.resume(response.body()){
                        continuation.resumeWithException(it)
                    }
                } else {
                    continuation.resumeWithException(HttpException(response))
                }
            }

            override fun onFailure(call: Call<T?>?, t: Throwable?) {
                continuation.resumeWithException(t ?: java.lang.Exception("request failed for unknown reason"))
            }
        })
    }
}

suspend fun Call<Unit>.await() {
    @Suppress("UNCHECKED_CAST")
    (this as Call<Unit?>).await()
}

suspend fun <T> Call<T>.awaitResponse(): Response<T> {
    return suspendCancellableCoroutine { continuation ->
        continuation.invokeOnCancellation {
            cancel()
        }
        enqueue(object : Callback<T> {

            override fun onResponse(call: Call<T>?, response: Response<T>) {
                continuation.resume(response){
                    continuation.resumeWithException(it)
                }
            }

            override fun onFailure(call: Call<T>?, t: Throwable?) {
                continuation.resumeWithException(t ?: java.lang.Exception("request failed for unknown reason"))
            }
        })
    }
}

/**
 * Force the calling coroutine to suspend before throwing [this].
 *
 * This is needed when a checked exception is synchronously caught in a [java.lang.reflect.Proxy]
 * invocation to avoid being wrapped in [java.lang.reflect.UndeclaredThrowableException].
 *
 * The implementation is derived from:
 * https://github.com/Kotlin/kotlinx.coroutines/pull/1667#issuecomment-556106349
 */
internal suspend fun Exception.suspendAndThrow(): Nothing {
    suspendCoroutineUninterceptedOrReturn<Nothing> { continuation ->
        Dispatchers.Default.dispatch(continuation.context) {
            continuation.intercepted().resumeWithException(this@suspendAndThrow)
        }
        COROUTINE_SUSPENDED
    }
}