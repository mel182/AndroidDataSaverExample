package com.custom.http.client.response

import com.custom.http.client.ok_http_call.NoContentResponseBody
import okhttp3.Headers
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.ResponseBody
import java.util.*

class Response<T>(private val rawResponse: okhttp3.Response, val body: T?, val errorBody:ResponseBody?) {

    val message: String = rawResponse.message
    val isSuccessful: Boolean = rawResponse.isSuccessful
    val code: Int = rawResponse.code

    companion object {
        /** Create a synthetic successful response with {@code body} as the deserialized body. */
        fun <T> success(body: T?): Response<T> {
            return success(
                body,
                okhttp3.Response.Builder() //
                    .code(200)
                    .message("OK")
                    .protocol(Protocol.HTTP_1_1)
                    .request(Request.Builder().url("http://localhost/").build())
                    .build()
            )
        }

        /**
         * Create a synthetic successful response with an HTTP status code of {@code code} and {@code
         * body} as the deserialized body.
         */
        fun <T> success(code: Int, body: T?): Response<T> {
            require(code < 200 || code >= 300) { "code < 200 or >= 300: $code" }
            return success(
                body,
                okhttp3.Response.Builder() //
                    .code(code)
                    .message("Response.success()")
                    .protocol(Protocol.HTTP_1_1)
                    .request(Request.Builder().url("http://localhost/").build())
                    .build()
            )
        }

        /**
         * Create a synthetic successful response using `headers` with `body` as the
         * deserialized body.
         */
        fun <T> success(body: T?, headers: Headers?): Response<T> {
            requireNotNull(headers) { "headers == null" }
            return success<T>(
                body,
                okhttp3.Response.Builder() //
                    .code(200)
                    .message("OK")
                    .protocol(Protocol.HTTP_1_1)
                    .headers(headers)
                    .request(Request.Builder().url("http://localhost/").build())
                    .build()
            )
        }

        /**
         * Create a synthetic successful response with an HTTP status code of {@code code} and {@code
         * body} as the deserialized body.
         */
        fun <T> success(body: T?, rawResponse: okhttp3.Response): Response<T> {
            Objects.requireNonNull(rawResponse, "rawResponse == null")
            require(rawResponse.isSuccessful) { "rawResponse must be successful response" }
            return Response(rawResponse, body, null)
        }

        /**
         * Create a synthetic error response with an HTTP status code of {@code code} and {@code body} as
         * the error body.
         */
        fun <T> error(code: Int, body: ResponseBody?): Response<T> {
            requireNotNull(body) { "body == null" }
            require(code >= 400) { "code < 400: $code" }

            return error(
                body,
                okhttp3.Response.Builder()
                    .body(NoContentResponseBody(body.contentType(), body.contentLength()))
                    .code(code)
                    .message("Response.error()")
                    .protocol(Protocol.HTTP_1_1)
                    .request(Request.Builder().url("http://localhost/").build())
                    .build()
            )
        }

        /**
         * Create an error response from [rawResponse] with [body] as the error body.
         */
        fun <T> error(body: ResponseBody?, rawResponse: okhttp3.Response?): Response<T> {
            requireNotNull(body){ "body == null" }
            requireNotNull(rawResponse){ "rawResponse == null" }
            require(!rawResponse.isSuccessful) { "rawResponse should not be successful response" }
            return Response(rawResponse, null, body)
        }
    }

    /**
     * The raw response from the HTTP client.
     */
    fun raw(): okhttp3.Response = rawResponse

    /**
     * HTTP status code.
     */
    fun code(): Int = rawResponse.code

    /**
     * HTTP status message or null if unknown.
     */
    fun message(): String = rawResponse.message


    /**
     * HTTP headers.
     */
    fun headers(): Headers = rawResponse.headers

    /**
     * Returns true if [rawResponse.code] is in the range [200..300).
     */
    fun isSuccessful(): Boolean = rawResponse.isSuccessful

    /**
     * The deserialized response body of a [isSuccessful] response.
     */
    fun body(): T? = body

    /**
     * The raw response body of an [isSuccessful] response.
     */
    fun errorBody(): ResponseBody? = errorBody

    override fun toString(): String = rawResponse.toString()

}