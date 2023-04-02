package com.custom.http.client.annotation.http.call_properties

import com.custom.http.client.constant.DEFAULT_BOOLEAN

/**
 * Named replacement in a URL path segment. Values are converted to strings using {@link
 * Retrofit#stringConverter(Type, Annotation[])} (or {@link Object#toString()}, if no matching
 * string converter is installed) and then URL encoded.
 *
 * <p>Simple example:
 *
 * <pre><code>
 * &#64;GET("/image/{id}")
 * Call&lt;ResponseBody&gt; example(@Path("id") int id);
 * </code></pre>
 *
 * Calling with {@code foo.example(1)} yields {@code /image/1}.
 *
 * <p>Values are URL encoded by default. Disable with {@code encoded=true}.
 *
 * <pre><code>
 * &#64;GET("/user/{name}")
 * Call&lt;ResponseBody&gt; encoded(@Path("name") String name);
 *
 * &#64;GET("/user/{name}")
 * Call&lt;ResponseBody&gt; notEncoded(@Path(value="name", encoded=true) String name);
 * </code></pre>
 *
 * Calling {@code foo.encoded("John+Doe")} yields {@code /user/John%2BDoe} whereas {@code
 * foo.notEncoded("John+Doe")} yields {@code /user/John+Doe}.
 *
 * <p>Path parameters may not be {@code null}.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Path(
    val value: String,
    /**
     * Specifies whether the argument value to the annotated method parameter is already URL encoded.
     */
    val encoded: Boolean = DEFAULT_BOOLEAN
)
