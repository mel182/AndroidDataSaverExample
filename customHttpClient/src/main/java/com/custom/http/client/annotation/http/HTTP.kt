package com.custom.http.client.annotation.http

import com.custom.http.client.constant.BLANK_STRING
import com.custom.http.client.constant.DEFAULT_BOOLEAN

/**
 * Use a custom HTTP verb for a request.
 *
 * <pre><code>
 * interface Service {
 *   &#064;HTTP(method = "CUSTOM", path = "custom/endpoint/")
 *   Call&lt;ResponseBody&gt; customEndpoint();
 * }
 * </code></pre>
 *
 * This annotation can also used for sending {@code DELETE} with a request body:
 *
 * <pre><code>
 * interface Service {
 *   &#064;HTTP(method = "DELETE", path = "remove/", hasBody = true)
 *   Call&lt;ResponseBody&gt; deleteObject(@Body RequestBody object);
 * }
 * </code></pre>
 */
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
annotation class HTTP(
    val method: String,
    /**
     * A relative or absolute path, or full URL of the endpoint. This value is optional if the first
     * parameter of the method is annotated with [@Url][Url].
     *
     *
     * See [base URL][retrofit2.Retrofit.Builder.baseUrl] for details of how
     * this is resolved against a base URL to create the full endpoint URL.
     */
    val path: String = BLANK_STRING, val hasBody: Boolean = DEFAULT_BOOLEAN
)