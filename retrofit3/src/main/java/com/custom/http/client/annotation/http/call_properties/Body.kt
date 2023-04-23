package com.custom.http.client.annotation.http.call_properties

/**
 * Use this annotation on a service method param when you want to directly control the request body
 * of a POST/PUT request (instead of sending in as request parameters or form-style request body).
 * The object will be serialized using the {@link Retrofit Retrofit} instance {@link Converter
 * Converter} and the result will be set directly as the request body.
 *
 * <p>Body parameters may not be {@code null}.
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Body