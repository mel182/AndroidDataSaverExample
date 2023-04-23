package com.custom.http.client.annotation.http.call_properties

/**
 * Adds headers specified in the {@link Map} or {@link okhttp3.Headers}.
 *
 * <p>Values in the map are converted to strings using {@link Retrofit#stringConverter(Type,
 * Annotation[])} (or {@link Object#toString()}, if no matching string converter is installed).
 *
 * <p>Simple Example:
 *
 * <pre>
 * &#64;GET("/search")
 * void list(@HeaderMap Map&lt;String, String&gt; headers);
 *
 * ...
 *
 * // The following call yields /search with headers
 * // Accept: text/plain and Accept-Charset: utf-8
 * foo.list(ImmutableMap.of("Accept", "text/plain", "Accept-Charset", "utf-8"));
 * </pre>
 *
 * <p>Map keys and values representing parameter values allow only ascii values by default.
 * Specify {@link #allowUnsafeNonAsciiValues() allowUnsafeNonAsciiValues=true} to change this behavior.
 *
 * <pre>
 * &#64;GET("/search")
 * void list(@HeaderMap(allowUnsafeNonAsciiValues=true) Map&lt;String, String&gt; headers);
 *
 * @see Header
 * @see Headers
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class HeaderMap(
    /** Specifies whether the parameter values are allowed with unsafe non ascii values.  */
    val allowUnsafeNonAsciiValues: Boolean = false
)
