package com.custom.http.client.http_service_method

import com.custom.http.client.CallAdapter
import com.custom.http.client.Converter
import com.custom.http.client.RequestFactory
import okhttp3.Call
import okhttp3.ResponseBody

class CallAdapted<ResponseT, ReturnT>(requestFactory: RequestFactory, callFactory: Call.Factory, responseConverter:Converter<ResponseBody, ResponseT>, private val callAdapter: CallAdapter<ResponseT, ReturnT>) : HttpServiceMethod<ResponseT, ReturnT>(requestFactory = requestFactory, callFactory = callFactory, responseConverter = responseConverter) {

    override fun adapt(call: com.custom.http.client.Call<ResponseT>?, args: Array<Any>): ReturnT? = callAdapter.adapt(call = call)

}