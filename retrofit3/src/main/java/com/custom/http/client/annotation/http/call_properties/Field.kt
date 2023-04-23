package com.custom.http.client.annotation.http.call_properties

import com.custom.http.client.constant.DEFAULT_BOOLEAN

/**
 * Named pair for a form-encoded request.
 *
 * <p>Values are converted to strings using {@link Retrofit#stringConverter(Type, Annotation[])} (or
 * {@link Object#toString()}, if no matching string converter is installed) and then form URL
 * encoded. {@code null} values are ignored. Passing a {@link java.util.List List} or array will
 * result in a field pair for each non-{@code null} item.
 *
 * <p>Simple Example:
 *
 * <pre><code>
 * &#64;FormUrlEncoded
 * &#64;POST("/")
 * Call&lt;ResponseBody&gt; example(
 *     &#64;Field("name") String name,
 *     &#64;Field("occupation") String occupation);
 * </code></pre>
 *
 * Calling with {@code foo.example("Bob Smith", "President")} yields a request body of {@code
 * name=Bob+Smith&occupation=President}.
 *
 * <p>Array/Varargs Example:
 *
 * <pre><code>
 * &#64;FormUrlEncoded
 * &#64;POST("/list")
 * Call&lt;ResponseBody&gt; example(@Field("name") String... names);
 * </code></pre>
 *
 * Calling with {@code foo.example("Bob Smith", "Jane Doe")} yields a request body of {@code
 * name=Bob+Smith&name=Jane+Doe}.
 *
 * @see FormUrlEncoded
 * @see FieldMap
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Field(
    val value: String,
    /** Specifies whether the [name][.value] and value are already URL encoded.  */
    val encoded: Boolean = DEFAULT_BOOLEAN
)