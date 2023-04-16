package com.custom.http.client

import okhttp3.ResponseBody
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Adapts an invocation of an interface method into an HTTP call.
 */
abstract class HttpServiceMethod<ResponseT, ReturnT> : MethodService<ReturnT>() {
    /**
     * Inspects the annotations on an interface method to construct a reusable service method that
     * speaks HTTP. This requires potentially-expensive reflection so it is best to build each service
     * method only once and reuse it.
     */
    companion object {

        fun <ResponseT, ReturnT> parseAnnotations(retrofit: Retrofit3, method: Method, requestFactory: RequestFactory): HttpServiceMethod<ResponseT, ReturnT> {
            val isKotlinSuspendFunction: Boolean = requestFactory.isKotlinSuspendFunction
            var continuationWantsResponse = false
            val continuationBodyNullable = false
            var continuationIsUnit = false

            var annotations = method.annotations
            val adapterType: Type
            if (isKotlinSuspendFunction) {
                val parameterTypes = method.genericParameterTypes
                var responseType: Type = Utils.getParameterLowerBound(0, parameterTypes[parameterTypes.size - 1] as ParameterizedType)

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

                adapterType = Utils.Companion.ParameterizedTypeImpl(ownerType = null, rawType = Call::class.java, typeArguments = arrayOf(responseType))
                annotations = SkipCallbackExecutorImpl.ensurePresent(annotations)
            } else {
                adapterType = method.genericReturnType
            }
            val callAdapter: CallAdapter<ResponseT, ReturnT> = createCallAdapter(retrofit, method, adapterType, annotations)
            val responseType: Type? = callAdapter.responseType()
            if (responseType === okhttp3.Response::class.java)
                throw Utils.methodError(method = method, message = "'${Utils.getRawType(responseType).name}' is not a valid response body type. Did you mean ResponseBody?")

            if (responseType === Response::class.java)
                throw Utils.methodError(method = method, message = "Response must include generic type (e.g., Response<String>)")

            if ((requestFactory.httpMethod == "HEAD") && Void::class.java != responseType && !Utils.isUnit(responseType))
                throw Utils.methodError(method = method, message = "HEAD method must use Void or Unit as response type.")

            // TODO support Unit for Kotlin?
//            val responseConverter =


            val responseConverter: retrofit2.Converter<okhttp3.ResponseBody, ResponseT> =
                retrofit2.HttpServiceMethod.createResponseConverter<ResponseT>(
                    retrofit,
                    method,
                    responseType
                )
            val callFactory: Factory = retrofit.callFactory
            if (!isKotlinSuspendFunction) {
                return CallAdapted<ResponseT, ReturnT>(
                    requestFactory,
                    callFactory,
                    responseConverter,
                    callAdapter
                )
            } else return if (continuationWantsResponse) {
                SuspendForResponse<ResponseT>(
                    requestFactory,
                    callFactory,
                    responseConverter,
                    callAdapter as CallAdapter<ResponseT, retrofit2.Call<ResponseT>?>
                ) as retrofit2.HttpServiceMethod<ResponseT, ReturnT>?
            } else {
                SuspendForBody<ResponseT>(
                    requestFactory,
                    callFactory,
                    responseConverter,
                    callAdapter as CallAdapter<ResponseT, retrofit2.Call<ResponseT>?>,
                    continuationBodyNullable,
                    continuationIsUnit
                ) as retrofit2.HttpServiceMethod<ResponseT, ReturnT>?
            }
        }


        private fun <ResponseT, ReturnT> createCallAdapter(retrofit: Retrofit3, method: Method, returnType: Type, annotations: Array<Annotation>): CallAdapter<ResponseT, ReturnT> {
            return try {
                retrofit.callAdapter(returnType, annotations) as CallAdapter<ResponseT, ReturnT>
            } catch (e: RuntimeException) { // Wide exception range because factories are user code.
                throw Utils.methodError(method = method, cause = e, message = "Unable to create call adapter for %s", returnType)
            }
        }

        private fun <ResponseT> createResponseConverter(retrofit: Retrofit3, method: Method, responseType: Type): Converter<ResponseBody?, ResponseT>? {
            val annotations = method.annotations
            return try {
                retrofit.responseBodyConverter<ResponseT>(responseType, annotations)
            } catch (e: java.lang.RuntimeException) { // Wide exception range because factories are user code.
                throw Utils.methodError(method = method, cause = e, message = "Unable to create converter for %s", responseType)
            }
        }


    }


}