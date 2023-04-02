package com.custom.http.client

import java.lang.reflect.Method

abstract class MethodService<T> {

    open fun <T> parseAnnotations(retrofit: HttpModified, method: Method): MethodService<T>? {
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

    abstract operator fun invoke(args: Array<Any?>?): T?



}