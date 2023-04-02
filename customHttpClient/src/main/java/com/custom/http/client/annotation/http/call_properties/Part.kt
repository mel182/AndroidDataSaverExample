package com.custom.http.client.annotation.http.call_properties

import com.custom.http.client.constant.BLANK_STRING

/**
 * Denotes a single part of a multi-part request.
 *
 * <p>The parameter type on which this annotation exists will be processed in one of three ways:
 *
 * <ul>
 *   <li>If the type is {@link okhttp3.MultipartBody.Part} the contents will be used directly. Omit
 *       the name from the annotation (i.e., {@code @Part MultipartBody.Part part}).
 *   <li>If the type is {@link okhttp3.RequestBody RequestBody} the value will be used directly with
 *       its content type. Supply the part name in the annotation (e.g., {@code @Part("foo")
 *       RequestBody foo}).
 *   <li>Other object types will be converted to an appropriate representation by using {@linkplain
 *       Converter a converter}. Supply the part name in the annotation (e.g., {@code @Part("foo")
 *       Image photo}).
 * </ul>
 *
 * <p>Values may be {@code null} which will omit them from the request body.
 *
 * <p>
 *
 * <pre><code>
 * &#64;Multipart
 * &#64;POST("/")
 * Call&lt;ResponseBody&gt; example(
 *     &#64;Part("description") String description,
 *     &#64;Part(value = "image", encoding = "8-bit") RequestBody image);
 * </code></pre>
 *
 * <p>Part parameters may not be {@code null}.
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Part(
    /**
     * The name of the part. Required for all parameter types except [ ].
     */
    val value: String = BLANK_STRING,
    /** The `Content-Transfer-Encoding` of this part.  */
    val encoding: String = "binary"
)
