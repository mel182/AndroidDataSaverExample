package com.custom.http.client.http_service_method

import com.custom.http.client.call.CallAdapter
import com.custom.http.client.Converter
import com.custom.http.client.request.RequestFactory
import com.custom.http.client.response.Response
import okhttp3.Call
import okhttp3.ResponseBody
import kotlin.coroutines.Continuation

internal class SuspendForResponse<ResponseT>(requestFactory: RequestFactory, callFactory: Call.Factory, responseConverter: Converter<ResponseBody, ResponseT>, private val callAdapter: CallAdapter<ResponseT, com.custom.http.client.call.Call<ResponseT>>) : HttpServiceMethod<ResponseT, Any?>(requestFactory = requestFactory, callFactory = callFactory, responseConverter = responseConverter) {

    override fun adapt(call: com.custom.http.client.call.Call<ResponseT>?, args: Array<Any>): Any? {

        val _call = callAdapter.adapt(call)

        //noinspection unchecked Checked by reflection inside RequestFactory.
        val continuation: Continuation<Response<ResponseT>> = args[args.size - 1] as Continuation<Response<ResponseT>?>

        try {

        }catch (e:Exception) {

        }

        TODO("Need to be implemented")

    }


}