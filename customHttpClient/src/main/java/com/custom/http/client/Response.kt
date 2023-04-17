package com.custom.http.client

import com.custom.http.client.ok_http_call.NoContentResponseBody
import okhttp3.*
import java.util.*

class Response<T>(val rawResponse: okhttp3.Response, val body: T?, val errorBody:ResponseBody?) {

    val message: String = rawResponse.message
    val headers: Headers = rawResponse.headers
    val isSuccessful: Boolean = rawResponse.isSuccessful
    val code: Int = rawResponse.code

    companion object {

        fun <T> success(body: T?): Response<T>? {
            return success<T>(
                body,
                okhttp3.Response.Builder() //
                    .code(200)
                    .message("OK")
                    .protocol(Protocol.HTTP_1_1)
                    .request(Request.Builder().url("http://localhost/").build())
                    .build()
            )
        }

        fun <T> success(body: T?, rawResponse: okhttp3.Response): Response<T> {
            Objects.requireNonNull(rawResponse, "rawResponse == null")
            require(rawResponse.isSuccessful) { "rawResponse must be successful response" }
            return Response(rawResponse, body, null)
        }


        /*
    public static <T> Response<T> error(ResponseBody body, okhttp3.Response rawResponse) {
    Objects.requireNonNull(body, "body == null");
    Objects.requireNonNull(rawResponse, "rawResponse == null");
    if (rawResponse.isSuccessful()) {
      throw new IllegalArgumentException("rawResponse should not be successful response");
    }
    return new Response<>(rawResponse, null, body);
  }
    */

        //fun error(body: ResponseBody, rawResponse: okhttp3.Response)


        fun <T> error(code: Int, body: ResponseBody): Response<T>? {
            Objects.requireNonNull(body, "body == null")
            require(code >= 400) { "code < 400: $code" }
            return error<T>(
                body,
                okhttp3.Response.Builder()
                    .body(   NoContentResponseBody(body.contentType(), body.contentLength()))
                    .code(code)
                    .message("Response.error()")
                    .protocol(Protocol.HTTP_1_1)
                    .request(Request.Builder().url("http://localhost/").build())
                    .build()
            )
            /*
            new okhttp3.Response.Builder() //
            .body(new OkHttpCall.NoContentResponseBody(body.contentType(), body.contentLength()))
            .code(code)
            .message("Response.error()")
            .protocol(Protocol.HTTP_1_1)
            .request(new Request.Builder().url("http://localhost/").build())
            .build());
            */
        }


        fun <T> error(body: ResponseBody, rawResponse: Response): com.custom.http.client.Response<T> {
            Objects.requireNonNull(body, "body == null")
            Objects.requireNonNull(rawResponse, "rawResponse == null")
            require(!rawResponse.isSuccessful) { "rawResponse should not be successful response" }
            return Response(rawResponse = rawResponse, body = null, errorBody = body)
        }

    }

    /** Create a synthetic successful response with `body` as the deserialized body.  */
//    fun <T> success(body: T?): Response<T>? {
//        return Response.success<T>(
//            body,
//            Builder() //
//                .code(200)
//                .message("OK")
//                .protocol(Protocol.HTTP_1_1)
//                .request(Builder().url("http://localhost/").build())
//                .build()
//        )
//    }

    /*
    public static <T> Response<T> success(@Nullable T body) {
    return success(
        body,
        new okhttp3.Response.Builder() //
            .code(200)
            .message("OK")
            .protocol(Protocol.HTTP_1_1)
            .request(new Request.Builder().url("http://localhost/").build())
            .build());
  }
    */

    /*
    public static <T> Response<T> error(ResponseBody body, okhttp3.Response rawResponse) {
    Objects.requireNonNull(body, "body == null");
    Objects.requireNonNull(rawResponse, "rawResponse == null");
    if (rawResponse.isSuccessful()) {
      throw new IllegalArgumentException("rawResponse should not be successful response");
    }
    return new Response<>(rawResponse, null, body);
  }
    */



    override fun toString(): String = rawResponse.toString()

}