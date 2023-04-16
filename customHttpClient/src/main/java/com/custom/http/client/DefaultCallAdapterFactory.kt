package com.custom.http.client

import okhttp3.Request
import okio.Timeout
import java.io.IOException
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

        val executor = if (Utils.isAnnotationPresent(annotations ?: emptyArray(), SkipCallbackExecutor::class.java)) null else callbackExecutor

        return object : CallAdapter<Any, Call<*>> {

            override fun responseType(): Type = responseType

            override fun adapt(call: Call<Any>): Call<*> {
                return executor?.let { ExecutorCallbackCall(it, call) } ?: call!!
            }
        }
    }

    internal class ExecutorCallbackCall<T>(val callbackExecutor: Executor, val delegate:Call<T>) : Call<T> {

        override fun enqueue(callback: Callback<T>?) {

            requireNotNull(callback){ "callback == null" }

            delegate.enqueue(object : Callback<T> {

                override fun onResponse(call: Call<T>?, response: Response<T>?) {
                    callbackExecutor.execute {
                        if (delegate.isCanceled()) {
                            // Emulate OkHttp's behavior of throwing/delivering an IOException on
                            // cancellation.
                            callback.onFailure(
                                this@ExecutorCallbackCall,
                                IOException("Canceled")
                            )
                        } else {
                            callback.onResponse(this@ExecutorCallbackCall, response)
                        }
                    }
                }

                override fun onFailure(call: Call<T>?, t: Throwable?) {
                    callbackExecutor.execute {
                        callback.onFailure(
                            this@ExecutorCallbackCall,
                            t
                        )
                    }
                }
            })
        }

        override fun execute(): Response<T> = delegate.execute()

        override fun isExecuted(): Boolean = delegate.isExecuted()

        override fun cancel() {
            delegate.cancel()
        }

        override fun isCanceled(): Boolean = delegate.isCanceled()

        override fun clone(): Call<T> {
            return ExecutorCallbackCall(
                callbackExecutor,
                delegate.clone()
            )
        }

        override fun request(): Request = delegate.request()

        override fun timeout(): Timeout? = delegate.timeout()

    }

}