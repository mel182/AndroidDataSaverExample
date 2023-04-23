package com.custom.http.client.annotation.http.call_properties

/**
 * Denotes that the request body will use form URL encoding. Fields should be declared as parameters
 * and annotated with {@link Field @Field}.
 *
 * <p>Requests made with this annotation will have {@code application/x-www-form-urlencoded} MIME
 * type. Field names and values will be UTF-8 encoded before being URI-encoded in accordance to <a
 * href="https://datatracker.ietf.org/doc/html/rfc3986">RFC-3986</a>.
 */

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
annotation class FormUrlEncoded
