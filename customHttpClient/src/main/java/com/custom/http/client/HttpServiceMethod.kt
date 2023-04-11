package com.custom.http.client

import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

abstract class HttpServiceMethod {

    companion object {

        fun <ResponseT, ReturnT> parseAnnotations(
            retrofit: Retrofit, method: Method, requestFactory: retrofit2.RequestFactory
        ): retrofit2.HttpServiceMethod<ResponseT, ReturnT>? {
            val isKotlinSuspendFunction: Boolean = requestFactory.isKotlinSuspendFunction
            var continuationWantsResponse = false
            val continuationBodyNullable = false
            var continuationIsUnit = false
            var annotations = method.annotations
            val adapterType: Type
            if (isKotlinSuspendFunction) {
                val parameterTypes = method.genericParameterTypes
                var responseType: Type = retrofit2.Utils.getParameterLowerBound(
                    0, parameterTypes[parameterTypes.size - 1] as ParameterizedType
                )
                if (retrofit2.Utils.getRawType(responseType) == retrofit2.Response::class.java && responseType is ParameterizedType) {
                    // Unwrap the actual body type from Response<T>.
                    responseType = retrofit2.Utils.getParameterUpperBound(
                        0,
                        responseType as ParameterizedType?
                    )
                    continuationWantsResponse = true
                } else {
                    continuationIsUnit = retrofit2.Utils.isUnit(responseType)
                    // TODO figure out if type is nullable or not
                    // Metadata metadata = method.getDeclaringClass().getAnnotation(Metadata.class)
                    // Find the entry for method
                    // Determine if return type is nullable or not
                }
                adapterType = retrofit2.Utils.ParameterizedTypeImpl(
                    null,
                    retrofit2.Call::class.java, responseType
                )
                annotations = SkipCallbackExecutorImpl.ensurePresent(annotations)
            } else {
                adapterType = method.genericReturnType
            }
            val callAdapter: CallAdapter<ResponseT, ReturnT?> =
                retrofit2.HttpServiceMethod.createCallAdapter<ResponseT, ReturnT>(
                    retrofit,
                    method,
                    adapterType,
                    annotations
                )
            val responseType: Type = callAdapter.responseType()
            if (responseType === okhttp3.Response::class.java) {
                throw retrofit2.Utils.methodError(
                    method,
                    "'"
                            + retrofit2.Utils.getRawType(responseType).getName()
                            + "' is not a valid response body type. Did you mean ResponseBody?"
                )
            }
            if (responseType === retrofit2.Response::class.java) {
                throw retrofit2.Utils.methodError(
                    method,
                    "Response must include generic type (e.g., Response<String>)"
                )
            }
            // TODO support Unit for Kotlin?
            if (((requestFactory.httpMethod == "HEAD") && Void::class.java != responseType
                        && !retrofit2.Utils.isUnit(responseType))
            ) {
                throw retrofit2.Utils.methodError(
                    method,
                    "HEAD method must use Void or Unit as response type."
                )
            }
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


    }


}