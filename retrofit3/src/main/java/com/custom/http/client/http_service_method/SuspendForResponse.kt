package com.custom.http.client.http_service_method

import android.util.Log
import com.custom.http.client.call.CallAdapter
import com.custom.http.client.converters.Converter
import com.custom.http.client.extensions.awaitResponse
import com.custom.http.client.extensions.suspendAndThrow
import com.custom.http.client.request.RequestFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.ResponseBody

internal class SuspendForResponse<ResponseT>(requestFactory: RequestFactory, callFactory: Call.Factory, responseConverter: Converter<ResponseBody, ResponseT>, private val callAdapter: CallAdapter<ResponseT, com.custom.http.client.call.Call<ResponseT>>) : HttpServiceMethod<ResponseT, Any?>(requestFactory = requestFactory, callFactory = callFactory, responseConverter = responseConverter) {

    override fun adapt(call: com.custom.http.client.call.Call<ResponseT>, args: Array<Any>): Any? {

        val _call = callAdapter.adapt(call)

        //noinspection unchecked Checked by reflection inside RequestFactory.
        //val continuation: Continuation<Response<ResponseT>> = args[args.size - 1] as Continuation<Response<ResponseT>>
        Log.i("TAG55","adapt: ${_call}")
        //CoroutineScope(Dispatchers.IO).launch {

        return try {
            CoroutineScope(Dispatchers.IO).launch {
                async { _call.awaitResponse() }.await()
            }
        }catch (e:Exception) {
            CoroutineScope(Dispatchers.IO).launch {
                async { e.suspendAndThrow() }.await()
            }
        }
    }


}