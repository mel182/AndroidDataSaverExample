package com.custom.http.client

import android.os.Build
import androidx.annotation.RequiresApi
import com.custom.http.client.annotation.util.IgnoreJRERequirement
import com.custom.http.client.exceptions.HttpException
import java.util.concurrent.CompletableFuture

@RequiresApi(Build.VERSION_CODES.N)
@IgnoreJRERequirement
class BodyCallback<R>(private val future: CompletableFuture<R>): Callback<R> {

    override fun onResponse(call: Call<R>?, response: Response<R>) {

        if (response.isSuccessful) {
            future.complete(response.body)
        } else {
            future.completeExceptionally(HttpException(response = response))
        }

    }

    override fun onFailure(call: Call<R>?, t: Throwable?) {
        future.completeExceptionally(t)
    }

}