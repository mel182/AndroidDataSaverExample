@file:Suppress("UNCHECKED_CAST")

package com.custom.http.client.http_service_method

import com.custom.http.client.CallAdapter
import com.custom.http.client.Converter
import com.custom.http.client.RequestFactory
import okhttp3.Call
import okhttp3.ResponseBody
import kotlin.coroutines.Continuation

internal class SuspendForBody<ResponseT>(requestFactory: RequestFactory, callFactory: Call.Factory, responseConverter: Converter<ResponseBody, ResponseT>, private val callAdapter: CallAdapter<ResponseT, com.custom.http.client.Call<ResponseT>>, private val isNullable:Boolean, private val isUnit:Boolean) : HttpServiceMethod<ResponseT, Any?>(requestFactory = requestFactory, callFactory = callFactory, responseConverter = responseConverter) {

    override fun adapt(call: com.custom.http.client.Call<ResponseT>?, args: Array<Any>): Any? {

        var _call = callAdapter.adapt(call)

        //noinspection unchecked Checked by reflection inside RequestFactory.
        val continuation = args[args.size - 1] as Continuation<ResponseT>

        // Calls to OkHttp Call.enqueue() like those inside await and awaitNullable can sometimes
        // invoke the supplied callback with an exception before the invoking stack frame can return.
        // Coroutines will intercept the subsequent invocation of the Continuation and throw the
        // exception synchronously. A Java Proxy cannot throw checked exceptions without them being
        // declared on the interface method. To avoid the synchronous checked exception being wrapped
        // in an UndeclaredThrowableException, it is intercepted and supplied to a helper which will
        // force suspension to occur so that it can be instead delivered to the continuation to
        // bypass this restriction.
        TODO("Not yet implemented")
    }
}