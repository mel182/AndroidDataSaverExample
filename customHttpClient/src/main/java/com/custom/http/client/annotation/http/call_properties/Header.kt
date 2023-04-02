package com.custom.http.client.annotation.http.call_properties

import com.custom.http.client.constant.DEFAULT_BOOLEAN

/**
 * Replaces the header with the value of its target.
 *
 * <pre><code>
 * &#64;GET("/")
 * Call&lt;ResponseBody&gt; foo(@Header("Accept-Language") String lang);
 * </code></pre>
 *
 * Header parameters may be {@code null} which will omit them from the request. Passing a {@link
 * java.util.List List} or array will result in a header for each non-{@code null} item.
 *
 * <p>Parameter keys and values only allows ascii values by default. Specify {@link
 * #allowUnsafeNonAsciiValues() allowUnsafeNonAsciiValues=true} to change this behavior.
 *
 * <pre><code>
 * &#64;GET("/")
 * Call&lt;ResponseBody&gt; foo(@Header("Accept-Language", allowUnsafeNonAsciiValues=true) String lang);
 * </code></pre>
 *
 * <p><strong>Note:</strong> Headers do not overwrite each other. All headers with the same name
 * will be included in the request.
 *
 * @see Headers
 * @see HeaderMap
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Header(
    /** The query parameter name.  */
    val value: String,
    /**
     * Specifies whether the parameter [name][.value] and value are already URL encoded.
     */
    val allowUnsafeNonAsciiValues: Boolean = DEFAULT_BOOLEAN
)
