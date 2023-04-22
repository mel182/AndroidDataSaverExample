@file:Suppress("UNCHECKED_CAST")

package com.custom.http.client

import com.custom.http.client.http_service_method.HttpServiceMethod
import com.custom.http.client.request.RequestFactory
import com.custom.http.client.utils.Utils
import java.lang.reflect.Method

abstract class MethodService<T> {

    companion object {
        fun <T> parseAnnotations(retrofit: Retrofit3, method: Method): MethodService<T> {
            val requestFactory: RequestFactory = RequestFactory.parseAnnotations(retrofit, method)

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
            return HttpServiceMethod.parseAnnotations<Any, T>(retrofit, method, requestFactory) as MethodService<T>
        }
    }

    abstract operator fun invoke(args: Array<Any>?): T?

}