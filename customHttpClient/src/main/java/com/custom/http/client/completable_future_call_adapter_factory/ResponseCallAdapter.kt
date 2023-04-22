package com.custom.http.client.completable_future_call_adapter_factory

import android.os.Build
import androidx.annotation.RequiresApi
import com.custom.http.client.Call
import com.custom.http.client.CallAdapter
import com.custom.http.client.CallCancelCompletableFuture
import com.custom.http.client.Response
import java.lang.reflect.Type
import java.util.concurrent.CompletableFuture

@RequiresApi(Build.VERSION_CODES.N)
class ResponseCallAdapter<R>(private val responseType:Type): CallAdapter<R, CompletableFuture<Response<R>>> {

    override fun responseType(): Type = responseType

    override fun adapt(call: Call<R>): CompletableFuture<Response<R>> = CallCancelCompletableFuture<Response<R>>(call = call)
    .apply {
        call.enqueue(ResponseCallback(future = this))
    }

}