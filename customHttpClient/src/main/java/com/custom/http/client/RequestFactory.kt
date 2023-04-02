package com.custom.http.client

import java.lang.reflect.Method

class RequestFactory {

    companion object {
        fun parseAnnotations(httpModified: HttpModified, method:Method): RequestFactory {
            return RequestFactory()
        }
    }

}