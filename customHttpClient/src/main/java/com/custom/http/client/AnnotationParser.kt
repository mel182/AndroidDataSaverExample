package com.custom.http.client

import com.custom.http.client.http_service_method.HttpServiceMethod
import java.lang.reflect.Method

class AnnotationParser {

    companion object {
        fun <T> parseAnnotations(retrofit: Retrofit3, method: Method?): MethodService<T> {
            requireNotNull(method) { "Method is null" }
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
            return HttpServiceMethod.parseAnnotations<Any, Any>(retrofit, method, requestFactory)
        }
    }

}