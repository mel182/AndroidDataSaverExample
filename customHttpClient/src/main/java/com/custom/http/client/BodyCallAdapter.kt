package com.custom.http.client

import android.os.Build
import androidx.annotation.RequiresApi
import com.custom.http.client.annotation.util.IgnoreJRERequirement
import java.lang.reflect.Type
import java.util.concurrent.CompletableFuture

@RequiresApi(Build.VERSION_CODES.N)
@IgnoreJRERequirement
class BodyCallAdapter<R>(private val responseType:Type): CallAdapter<R, CompletableFuture<R>> {

    override fun responseType(): Type = responseType

    override fun adapt(call: Call<R>): CompletableFuture<R> {
        val future = CallCancelCompletableFuture<R>(call = call)
        call.enqueue(BodyCallback(future = future))
        return future
    }
}