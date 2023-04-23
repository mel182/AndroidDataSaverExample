package com.custom.http.client.annotation.parser

import android.util.Log
import com.custom.http.client.MethodService
import com.custom.http.client.http_service_method.HttpServiceMethod
import com.custom.http.client.request.RequestFactory
import com.custom.http.client.retrofit_3.Retrofit3
import com.custom.http.client.utils.Utils
import java.lang.reflect.Method

class AnnotationParser {

    companion object {
        fun <T> parseAnnotations(retrofit: Retrofit3, method: Method?): MethodService<T> {

            Log.i("TAG55","parse annotation [annotation parser] line16")
            requireNotNull(method) { "Method is null" }
            val requestFactory: RequestFactory = RequestFactory.parseAnnotations(retrofit, method)
            Log.i("TAG55","request factory: $requestFactory")
            val returnType = method.genericReturnType
            if (Utils.hasUnresolvableType(returnType)) {
                throw Utils.methodError(
                    method,
                    "Method return type must not include a type variable or wildcard: %s",
                    returnType
                )
            }
            if (returnType === Void.TYPE) {
                throw Utils.methodError(method, "Service methods cannot return void.")
            }

            Log.i("TAG55","parse annotation [annotation parser] line31 - before result")
            val result = HttpServiceMethod.parseAnnotations<Any, Any>(retrofit, method, requestFactory) as MethodService<T>


            Log.i("TAG55","parse annotation [annotation parser] line34 - result: ${result}")
            return result

        }
    }

}