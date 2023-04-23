package com.custom.http.client.annotation.http

/**
 * Treat the response body on methods returning {@link ResponseBody ResponseBody} as is, i.e.
 * without converting the body to {@code byte[]}.
 */
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
annotation class Streaming
