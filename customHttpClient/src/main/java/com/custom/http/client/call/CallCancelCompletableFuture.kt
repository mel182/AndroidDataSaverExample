package com.custom.http.client.call

import android.os.Build
import androidx.annotation.RequiresApi
import com.custom.http.client.annotation.util.IgnoreJRERequirement
import java.util.concurrent.CompletableFuture

@RequiresApi(Build.VERSION_CODES.N)
@IgnoreJRERequirement
class CallCancelCompletableFuture<T>(private val call: Call<*>): CompletableFuture<T>() {

    override fun cancel(mayInterruptIfRunning: Boolean): Boolean {

        if (mayInterruptIfRunning)
            call.cancel()

        return super.cancel(mayInterruptIfRunning)
    }
}