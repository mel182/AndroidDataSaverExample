package com.custom.http.client.annotation.http.method

import com.custom.http.client.constant.BLANK_STRING

/** Make a PATCH request. */
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
annotation class PATCH(
    /**
     * A relative or absolute path, or full URL of the endpoint. This value is optional if the first
     * parameter of the method is annotated with [@Url][Url].
     *
     *
     * See [base URL][retrofit2.Retrofit.Builder.baseUrl] for details of how
     * this is resolved against a base URL to create the full endpoint URL.
     */
    val value: String = BLANK_STRING
)
