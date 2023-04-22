package com.custom.http.client.completable_future_call_adapter_factory

import android.os.Build
import androidx.annotation.RequiresApi
import com.custom.http.client.Call
import com.custom.http.client.Callback
import com.custom.http.client.Response
import com.custom.http.client.annotation.util.IgnoreJRERequirement
import java.util.concurrent.CompletableFuture

@RequiresApi(Build.VERSION_CODES.N)
@IgnoreJRERequirement
class ResponseCallback<R>(private val future: CompletableFuture<Response<R>>): Callback<R>
{
    override fun onResponse(call: Call<R>?, response: Response<R>) {
        future.complete(response)
    }

    override fun onFailure(call: Call<R>?, t: Throwable?) {
        future.completeExceptionally(t)
    }

}