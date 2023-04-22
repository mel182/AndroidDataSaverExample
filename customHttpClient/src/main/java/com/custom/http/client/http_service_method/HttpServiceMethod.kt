package com.custom.http.client.http_service_method

import com.custom.http.client.*
import com.custom.http.client.call.Call
import com.custom.http.client.call.CallAdapter
import com.custom.http.client.ok_http_call.OkHttpCall
import com.custom.http.client.request.RequestFactory
import com.custom.http.client.response.Response
import com.custom.http.client.utils.Utils
import okhttp3.ResponseBody
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Adapts an invocation of an interface method into an HTTP call.
 */
abstract class HttpServiceMethod<ResponseT, ReturnT>(private val requestFactory: RequestFactory, private val callFactory: okhttp3.Call.Factory, private val responseConverter: Converter<ResponseBody, ResponseT>) : MethodService<ReturnT>() {
    /**
     * Inspects the annotations on an interface method to construct a reusable service method that
     * speaks HTTP. This requires potentially-expensive reflection so it is best to build each service
     * method only once and reuse it.
     */
    companion object {

        fun <ResponseT, ReturnT> parseAnnotations(retrofit: Retrofit3, method: Method, requestFactory: RequestFactory): HttpServiceMethod<ResponseT, ReturnT>? {
            val isKotlinSuspendFunction: Boolean = requestFactory.isKotlinSuspendFunction
            var continuationWantsResponse = false
            val continuationBodyNullable = false
            var continuationIsUnit = false

            var annotations = method.annotations
            val adapterType: Type
            if (isKotlinSuspendFunction) {
                val parameterTypes = method.genericParameterTypes
                var responseType: Type = Utils.getParameterLowerBound(
                    0,
                    parameterTypes[parameterTypes.size - 1] as ParameterizedType
                )

                if (Utils.getRawType(responseType) == Response::class.java && responseType is ParameterizedType) {
                    // Unwrap the actual body type from Response<T>.
                    responseType = Utils.getParameterUpperBound(0, responseType)
                    continuationWantsResponse = true
                } else {
                    continuationIsUnit = Utils.isUnit(responseType)
                    // TODO figure out if type is nullable or not
                    // Metadata metadata = method.getDeclaringClass().getAnnotation(Metadata.class)
                    // Find the entry for method
                    // Determine if return type is nullable or not
                }

                adapterType = Utils.Companion.ParameterizedTypeImpl(
                    ownerType = null,
                    rawType = Call::class.java,
                    typeArguments = arrayOf(responseType)
                )
                annotations = SkipCallbackExecutorImpl.ensurePresent(annotations)
            } else {
                adapterType = method.genericReturnType
            }
            val callAdapter: CallAdapter<ResponseT, ReturnT> = createCallAdapter(retrofit, method, adapterType, annotations)
            val responseType: Type? = callAdapter.responseType()
            if (responseType === okhttp3.Response::class.java)
                throw Utils.methodError(
                    method = method,
                    message = "'${Utils.getRawType(responseType).name}' is not a valid response body type. Did you mean ResponseBody?"
                )

            if (responseType === Response::class.java)
                throw Utils.methodError(
                    method = method,
                    message = "Response must include generic type (e.g., Response<String>)"
                )

            if ((requestFactory.httpMethod == "HEAD") && Void::class.java != responseType && !Utils.isUnit(responseType))
                throw Utils.methodError(
                    method = method,
                    message = "HEAD method must use Void or Unit as response type."
                )

            val responseConverter: Converter<ResponseBody, ResponseT> = createResponseConverter(retrofit, method, responseType)

            val callFactory: okhttp3.Call.Factory = retrofit.callFactory
            if (!isKotlinSuspendFunction) {
                //noinspection unchecked Kotlin compiler guarantees ReturnT to be Object.
                return CallAdapted<ResponseT, ReturnT>(
                    requestFactory,
                    callFactory,
                    responseConverter,
                    callAdapter
                )
            } else return if (continuationWantsResponse) {
                //noinspection unchecked Kotlin compiler guarantees ReturnT to be Object.
                SuspendForResponse(
                    requestFactory,
                    callFactory,
                    responseConverter,
                    callAdapter as CallAdapter<ResponseT, Call<ResponseT>>
                ) as HttpServiceMethod<ResponseT, ReturnT>
            } else {
                //noinspection unchecked Kotlin compiler guarantees ReturnT to be Object.
                SuspendForBody(
                    requestFactory,
                    callFactory,
                    responseConverter,
                    callAdapter as CallAdapter<ResponseT, Call<ResponseT>>,
                    continuationBodyNullable,
                    continuationIsUnit
                ) as HttpServiceMethod<ResponseT, ReturnT>?
            }
        }

        private fun <ResponseT, ReturnT> createCallAdapter(retrofit: Retrofit3, method: Method, returnType: Type, annotations: Array<Annotation>): CallAdapter<ResponseT, ReturnT> {
            return try {
                retrofit.callAdapter(returnType, annotations) as CallAdapter<ResponseT, ReturnT>
            } catch (e: RuntimeException) { // Wide exception range because factories are user code.
                throw Utils.methodError(
                    method = method,
                    cause = e,
                    message = "Unable to create call adapter for %s",
                    returnType
                )
            }
        }

        private fun <ResponseT> createResponseConverter(retrofit: Retrofit3, method: Method, responseType: Type?): Converter<ResponseBody, ResponseT> {
            val annotations = method.annotations
            return try {
                retrofit.responseBodyConverter(responseType, annotations)!!
            } catch (e: java.lang.RuntimeException) { // Wide exception range because factories are user code.
                throw Utils.methodError(
                    method = method,
                    cause = e,
                    message = "Unable to create converter for %s",
                    responseType
                )
            } catch (e: java.lang.Exception) { // Wide exception range because factories are user code.
                throw Utils.methodError(
                    method = method,
                    cause = e,
                    message = "Unable to create converter for %s",
                    responseType
                )
            }
        }
    }

    override fun invoke(args: Array<Any>?): ReturnT? {
        val call: Call<ResponseT> = OkHttpCall(requestFactory, args ?: emptyArray(), callFactory, responseConverter)
        return adapt(call, args ?: emptyArray())
    }

    protected abstract fun adapt(call: Call<ResponseT>?, args: Array<Any>): ReturnT?

}