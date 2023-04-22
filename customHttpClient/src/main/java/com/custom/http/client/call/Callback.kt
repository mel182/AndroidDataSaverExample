package com.custom.http.client.call

import com.custom.http.client.response.Response

/**
 * Communicates responses from a server or offline requests. One and only one method will be invoked
 * in response to a given request.
 *
 * <p>Callback methods are executed using the {@link Retrofit} callback executor. When none is
 * specified, the following defaults are used:
 *
 * <ul>
 *   <li>Android: Callbacks are executed on the application's main (UI) thread.
 *   <li>JVM: Callbacks are executed on the background thread which performed the request.
 * </ul>
 * @param T Successful response body type.
 */
interface Callback<T> {

    /**
     * Invoked for a received HTTP response.
     * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
     * Call [Response.isSuccessful] to determine if the response indicates success.
     */
    fun onResponse(call: Call<T>?, response: Response<T>)

    /**
     * Invoked when a network exception occurred talking to the server or when an unexpected exception
     * occurred creating the request or processing the response.
     */
    fun onFailure(call: Call<T>?, t: Throwable?)
}