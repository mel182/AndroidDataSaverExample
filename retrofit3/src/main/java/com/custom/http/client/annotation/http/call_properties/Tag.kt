package com.custom.http.client.annotation.http.call_properties

/**
 * Adds the argument instance as a request tag using the type as the key.
 *
 * <pre><code>
 * &#64;GET("/")
 * Call&lt;ResponseBody&gt; foo(@Tag String tag);
 * </code></pre>
 *
 * Tag arguments may be {@code null} which will omit them from the request. Passing a parameterized
 * type such as {@code List<String>} will use the raw type (i.e., {@code List.class}) as the key.
 * Duplicate tag types are not allowed.
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Tag
