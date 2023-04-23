package com.custom.http.client.annotation.http.call_properties

import com.custom.http.client.constant.DEFAULT_BOOLEAN

/**
 * Query parameter keys and values appended to the URL.
 *
 * <p>Values are converted to strings using {@link Retrofit#stringConverter(Type, Annotation[])} (or
 * {@link Object#toString()}, if no matching string converter is installed).
 *
 * <p>Simple Example:
 *
 * <pre><code>
 * &#64;GET("/friends")
 * Call&lt;ResponseBody&gt; friends(@QueryMap Map&lt;String, String&gt; filters);
 * </code></pre>
 *
 * Calling with {@code foo.friends(ImmutableMap.of("group", "coworker", "age", "42"))} yields {@code
 * /friends?group=coworker&age=42}.
 *
 * <p>Map keys and values representing parameter values are URL encoded by default. Specify {@link
 * #encoded() encoded=true} to change this behavior.
 *
 * <pre><code>
 * &#64;GET("/friends")
 * Call&lt;ResponseBody&gt; friends(@QueryMap(encoded=true) Map&lt;String, String&gt; filters);
 * </code></pre>
 *
 * Calling with {@code foo.list(ImmutableMap.of("group", "coworker+bowling"))} yields {@code
 * /friends?group=coworker+bowling}.
 *
 * <p>A {@code null} value for the map, as a key, or as a value is not allowed.
 *
 * @see Query
 * @see QueryName
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class QueryMap(
    /** Specifies whether parameter names and values are already URL encoded.  */
    val encoded: Boolean = DEFAULT_BOOLEAN
)
