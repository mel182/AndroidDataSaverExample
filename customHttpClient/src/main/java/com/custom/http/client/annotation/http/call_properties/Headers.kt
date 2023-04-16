package com.custom.http.client.annotation.http.call_properties

import com.custom.http.client.constant.DEFAULT_BOOLEAN

/**
 * Adds headers literally supplied in the {@code value}.
 *
 * <pre><code>
 * &#64;Headers("Cache-Control: max-age=640000")
 * &#64;GET("/")
 * ...
 *
 * &#64;Headers({
 *   "X-Foo: Bar",
 *   "X-Ping: Pong"
 * })
 * &#64;GET("/")
 * ...
 * </code></pre>
 *
 * <p>Parameter keys and values only allows ascii values by default. Specify {@link
 * #allowUnsafeNonAsciiValues() allowUnsafeNonAsciiValues=true} to change this behavior.
 *
 * <p>&#64;Headers({ "X-Foo: Bar", "X-Ping: Pong" }, allowUnsafeNonAsciiValues=true) &#64;GET("/")
 *
 * <p><strong>Note:</strong> Headers do not overwrite each other. All headers with the same name
 * will be included in the request.
 *
 * @see Header
 * @see HeaderMap
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Headers(
    /** The query parameter name.  */
    val value: Array<String>,
    /**
     * Specifies whether the parameter [name][.value] and value are already URL encoded.
     */
    val allowUnsafeNonAsciiValues: Boolean = DEFAULT_BOOLEAN
)
