@file:Suppress("UNCHECKED_CAST")

package com.custom.http.client.http_service_method

import android.util.Log
import com.custom.http.client.call.CallAdapter
import com.custom.http.client.converters.Converter
import com.custom.http.client.extensions.await
import com.custom.http.client.extensions.awaitNonNullable
import com.custom.http.client.extensions.awaitNullable
import com.custom.http.client.extensions.suspendAndThrow
import com.custom.http.client.request.RequestFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.ResponseBody

internal class SuspendForBody<ResponseT>(requestFactory: RequestFactory, callFactory: Call.Factory, responseConverter: Converter<ResponseBody, ResponseT>, private val callAdapter: CallAdapter<ResponseT, com.custom.http.client.call.Call<ResponseT>>, private val isNullable:Boolean, private val isUnit:Boolean) : HttpServiceMethod<ResponseT, Any?>(requestFactory = requestFactory, callFactory = callFactory, responseConverter = responseConverter) {

    override fun adapt(call: com.custom.http.client.call.Call<ResponseT>, args: Array<Any>): Any? {

        val _call = callAdapter.adapt(call)

        Log.i("TAG55","adapt: ${_call}")

        //noinspection unchecked Checked by reflection inside RequestFactory.
        //val continuation = args[args.size - 1] as Continuation<ResponseT>

        // Calls to OkHttp Call.enqueue() like those inside await and awaitNullable can sometimes
        // invoke the supplied callback with an exception before the invoking stack frame can return.
        // Coroutines will intercept the subsequent invocation of the Continuation and throw the
        // exception synchronously. A Java Proxy cannot throw checked exceptions without them being
        // declared on the interface method. To avoid the synchronous checked exception being wrapped
        // in an UndeclaredThrowableException, it is intercepted and supplied to a helper which will
        // force suspension to occur so that it can be instead delivered to the continuation to
        // bypass this restriction.
        return try {
            CoroutineScope(Dispatchers.IO).launch {

                async {

                    if (isUnit) {
                        (_call as com.custom.http.client.call.Call<Unit>).await()
                    } else if (isNullable) {
                        (_call as com.custom.http.client.call.Call<Any?>).awaitNullable()
                    } else {
                        (_call as com.custom.http.client.call.Call<Any>).awaitNonNullable()
                    }
                }.await()
            }
        }catch (e:Exception) {
            CoroutineScope(Dispatchers.IO).launch {
                async { e.suspendAndThrow() }.await()
            }
        }
    }
}