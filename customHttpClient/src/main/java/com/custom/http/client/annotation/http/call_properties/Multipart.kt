package com.custom.http.client.annotation.http.call_properties

/**
 * Denotes that the request body is multi-part. Parts should be declared as parameters and annotated
 * with {@link Part @Part}.
 */
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
annotation class Multipart
