package com.custom.http.client.default_call_adapter_factory

import com.custom.http.client.Call
import com.custom.http.client.Callback
import com.custom.http.client.Response
import okhttp3.Request
import okio.Timeout
import java.io.IOException
import java.util.concurrent.Executor

class ExecutorCallbackCall<T>(private val callbackExecutor: Executor, private val delegate:Call<T>): Call<T> {

    override fun enqueue(callback: Callback<T>?) {

        requireNotNull(callback){ "callback == null" }

        delegate.enqueue(object : Callback<T> {

            override fun onResponse(call: Call<T>?, response: Response<T>) {

                callbackExecutor.execute {

                    callback.apply {

                        if (delegate.isCanceled()) {
                            // Emulate OkHttp's behavior of throwing/delivering an IOException on
                            // cancellation.
                            onFailure(
                                this@ExecutorCallbackCall,
                                IOException("Canceled")
                            )
                        } else {
                            onResponse(this@ExecutorCallbackCall, response)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<T>?, t: Throwable?) {
                callbackExecutor.execute {
                    callback.onFailure(this@ExecutorCallbackCall, t)
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

    override fun clone(): Call<T> = ExecutorCallbackCall(callbackExecutor, delegate.clone())

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout? = delegate.timeout()

}