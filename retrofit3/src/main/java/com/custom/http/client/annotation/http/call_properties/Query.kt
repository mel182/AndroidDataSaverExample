package com.custom.http.client.annotation.http.call_properties

import com.custom.http.client.constant.DEFAULT_BOOLEAN

/**
 * Query parameter appended to the URL.
 *
 * <p>Values are converted to strings using {@link Retrofit#stringConverter(Type, Annotation[])} (or
 * {@link Object#toString()}, if no matching string converter is installed) and then URL encoded.
 * {@code null} values are ignored. Passing a {@link java.util.List List} or array will result in a
 * query parameter for each non-{@code null} item.
 *
 * <p>Simple Example:
 *
 * <pre><code>
 * &#64;GET("/friends")
 * Call&lt;ResponseBody&gt; friends(@Query("page") int page);
 * </code></pre>
 *
 * Calling with {@code foo.friends(1)} yields {@code /friends?page=1}.
 *
 * <p>Example with {@code null}:
 *
 * <pre><code>
 * &#64;GET("/friends")
 * Call&lt;ResponseBody&gt; friends(@Query("group") String group);
 * </code></pre>
 *
 * Calling with {@code foo.friends(null)} yields {@code /friends}.
 *
 * <p>Array/Varargs Example:
 *
 * <pre><code>
 * &#64;GET("/friends")
 * Call&lt;ResponseBody&gt; friends(@Query("group") String... groups);
 * </code></pre>
 *
 * Calling with {@code foo.friends("coworker", "bowling")} yields {@code
 * /friends?group=coworker&group=bowling}.
 *
 * <p>Parameter names and values are URL encoded by default. Specify {@link #encoded() encoded=true}
 * to change this behavior.
 *
 * <pre><code>
 * &#64;GET("/friends")
 * Call&lt;ResponseBody&gt; friends(@Query(value="group", encoded=true) String group);
 * </code></pre>
 *
 * Calling with {@code foo.friends("foo+bar"))} yields {@code /friends?group=foo+bar}.
 *
 * @see QueryMap
 * @see QueryName
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Query(
    /** The query parameter name.  */
    val value: String,
    /**
     * Specifies whether the parameter [name][.value] and value are already URL encoded.
     */
    val encoded: Boolean = DEFAULT_BOOLEAN
)
