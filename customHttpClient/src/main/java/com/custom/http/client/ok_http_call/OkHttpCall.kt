package com.custom.http.client.ok_http_call

import androidx.annotation.GuardedBy
import com.custom.http.client.*
import com.custom.http.client.constant.DEFAULT_BOOLEAN
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okio.Timeout
import java.io.IOException

class OkHttpCall<T>(private val requestFactory: RequestFactory, private val args: Array<Any>, private val callFactory: okhttp3.Call.Factory?, private val responseConverter: Converter<ResponseBody, T>):
    Call<T> {

    @Volatile
    private var canceled = false

    @GuardedBy("this")
    private var rawCall: okhttp3.Call? = null

    @GuardedBy("this")
    private var creationFailure: Throwable? = null

    @GuardedBy("this")
    private var executed: Boolean = DEFAULT_BOOLEAN

    override fun clone(): Call<T> = OkHttpCall(requestFactory = requestFactory,
        args = args,
        callFactory = callFactory,
        responseConverter = responseConverter
    )

    override fun request(): Request =  try {
        getRawCall().request()
    }catch (e:IOException) {
        throw RuntimeException("Unable to create request.", e)
    }

    override fun timeout(): Timeout = try {
        getRawCall().timeout()
    }catch (e:IOException) {
        throw RuntimeException("Unable to create call. $e")
    }

    /**
     * Returns the raw call, initializing it if necessary. Throws if initializing the raw call throws,
     * or has thrown in previous attempts to create it.
     */
    @Throws(IOException::class)
    private fun getRawCall(): okhttp3.Call {

        val call:okhttp3.Call? = rawCall
        if (call != null)
            return call

        // Re-throw previous failures if this isn't the first attempt.
        if (creationFailure != null) {

            when (creationFailure) {
                is IOException -> {
                    throw creationFailure as IOException
                }
                is RuntimeException -> {
                    throw creationFailure as RuntimeException
                }
                else -> {
                    throw creationFailure as Error
                }
            }
        }

        // Create and remember either the success or the failure.
        return try {
            createRawCall().also { rawCall = it }
        } catch (e: java.lang.RuntimeException) {
            Utils.throwIfFatal(e) // Do not assign a fatal error to creationFailure.
            creationFailure = e
            throw e
        } catch (e: java.lang.Error) {
            Utils.throwIfFatal(e)
            creationFailure = e
            throw e
        } catch (e: IOException) {
            Utils.throwIfFatal(e)
            creationFailure = e
            throw e
        }
    }

    override fun enqueue(callback: Callback<T>?) {

        requireNotNull(callback){ "callback == null" }

        synchronized(this) {

            check(!executed) { "Already executed." }
            executed = true

            if (rawCall == null && creationFailure == null) {
                try {
                    rawCall = createRawCall()
                }catch (throwable:Throwable) {
                    Utils.throwIfFatal(throwable)
                    creationFailure = throwable
                }
            }

            creationFailure?.let {
                callback.onFailure(this,it)
                return@synchronized
            }

            if (canceled)
                rawCall?.cancel()

            rawCall?.enqueue(object : okhttp3.Callback {

                override fun onResponse(call: okhttp3.Call, response: Response) {
                    val rawResponse: com.custom.http.client.Response<T>
                    try {
                        rawResponse = parseResponse(response)
                    } catch (e: Throwable) {
                        Utils.throwIfFatal(e)
                        callFailure(e)
                        return
                    }

                    try {
                        callback.onResponse(this@OkHttpCall, rawResponse)
                    } catch (t: Throwable) {
                        Utils.throwIfFatal(t)
                        t.printStackTrace() // TODO this is not great
                    }
                }

                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    callFailure(e)
                }

                private fun callFailure(e: Throwable) {
                    try {
                        callback.onFailure(this@OkHttpCall, e)
                    } catch (t: Throwable) {
                        Utils.throwIfFatal(t)
                        t.printStackTrace() // TODO this is not great
                    }
                }
            })
        }
    }

    override fun execute(): com.custom.http.client.Response<T> {
        var call: okhttp3.Call

        synchronized(this) {
            check(!executed) { "Already executed." }
            executed = true
            call = getRawCall()
        }

        if (canceled) {
            call.cancel()
        }

        return parseResponse(call.execute())
    }

    override fun isExecuted(): Boolean = executed

    override fun cancel() {
        canceled = true

        var call: okhttp3.Call?
        synchronized(this) { call = rawCall }
        call?.cancel()
    }

    override fun isCanceled(): Boolean {
        if (canceled) {
            return true
        }
        synchronized(this) { return rawCall != null && rawCall!!.isCanceled() }
    }

    @Throws(IOException::class)
    fun parseResponse(rawResponse: Response): com.custom.http.client.Response<T> {
        //var rawResponse = rawResponse
        val rawBody = rawResponse.body

        // Remove the body's source (the only stateful object) so we can pass the response along.
        val raw_response = rawResponse
            .newBuilder()
            .body(NoContentResponseBody(rawBody!!.contentType(), rawBody.contentLength()))
            .build()

        val code = raw_response.code

        if (code < 200 || code >= 300) {
            return rawBody.use { rawBody ->
                // Buffer the entire body to avoid future I/O.
                val bufferedBody: ResponseBody = Utils.buffer(rawBody)
                com.custom.http.client.Response.error<T>(bufferedBody, rawResponse)
            }
        }

        if (code == 204 || code == 205) {
            rawBody.close()
            return com.custom.http.client.Response.success<T>(null, raw_response)
        }

        val catchingBody = ExceptionCatchingResponseBody(rawBody)
        return try {
            val body = responseConverter.convert(catchingBody)
            com.custom.http.client.Response.success<T>(body, rawResponse)
        } catch (e: java.lang.RuntimeException) {
            // If the underlying source threw an exception, propagate that rather than indicating it was
            // a runtime exception.
            catchingBody.throwIfCaught()
            throw e
        }
    }

    @Throws(IOException::class)
    private fun createRawCall(): okhttp3.Call {
        return requestFactory.create(args)?.let { callFactory?.newCall(it) }
            ?: throw NullPointerException("Call.Factory returned null.")
    }


    /*
    @GuardedBy("this")
  private okhttp3.Call getRawCall() throws IOException {
    okhttp3.Call call = rawCall;
    if (call != null) return call;

    // Re-throw previous failures if this isn't the first attempt.
    if (creationFailure != null) {
      if (creationFailure instanceof IOException) {
        throw (IOException) creationFailure;
      } else if (creationFailure instanceof RuntimeException) {
        throw (RuntimeException) creationFailure;
      } else {
        throw (Error) creationFailure;
      }
    }

    // Create and remember either the success or the failure.
    try {
      return rawCall = createRawCall();
    } catch (RuntimeException | Error | IOException e) {
      throwIfFatal(e); // Do not assign a fatal error to creationFailure.
      creationFailure = e;
      throw e;
    }
  }

    */


    /*
    private final RequestFactory requestFactory;
  private final Object[] args;
  private final okhttp3.Call.Factory callFactory;
  private final Converter<ResponseBody, T> responseConverter;

  private volatile boolean canceled;

  @GuardedBy("this")
  private @Nullable okhttp3.Call rawCall;

  @GuardedBy("this") // Either a RuntimeException, non-fatal Error, or IOException.
  private @Nullable Throwable creationFailure;

  @GuardedBy("this")
  private boolean executed;

  OkHttpCall(
      RequestFactory requestFactory,
      Object[] args,
      okhttp3.Call.Factory callFactory,
      Converter<ResponseBody, T> responseConverter) {
    this.requestFactory = requestFactory;
    this.args = args;
    this.callFactory = callFactory;
    this.responseConverter = responseConverter;
  }

    */

}