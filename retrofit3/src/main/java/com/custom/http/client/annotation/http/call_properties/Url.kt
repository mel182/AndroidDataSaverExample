package com.custom.http.client.annotation.http.call_properties

/**
 * URL resolved against the {@linkplain Retrofit#baseUrl() base URL}.
 *
 * <pre><code>
 * &#64;GET
 * Call&lt;ResponseBody&gt; list(@Url String url);
 * </code></pre>
 *
 * <p>See {@linkplain retrofit2.Retrofit.Builder#baseUrl(HttpUrl) base URL} for details of how the
 * value will be resolved against a base URL to create the full endpoint URL.
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Url
