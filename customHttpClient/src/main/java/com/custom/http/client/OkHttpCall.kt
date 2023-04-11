package com.custom.http.client

import androidx.annotation.GuardedBy
import com.custom.http.client.constant.DEFAULT_BOOLEAN
import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import java.io.IOException

class OkHttpCall<T>(private val requestFactory: RequestFactory, private val args: Array<Any>, private val callFactory: okhttp3.Call.Factory?, private val responseConverter: Converter<ResponseBody, T>): Call<T> {

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

    override fun request(): Request {
        try {
            if (rawCall == null)
                throw RuntimeException("raw call is null")

            return rawCall.request()
        }catch (e:IOException) {
            throw RuntimeException("Unable to create request. $e")
        }
    }

    /*


    */

    override fun timeout(): Timeout {
        try {
            return rawCall!!.timeout()
        }catch (e:IOException) {
            throw RuntimeException("Unable to create call. $e")
        }
    }

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

    @Throws(IOException::class)
    private fun createRawCall(): okhttp3.Call {
        return callFactory!!.newCall(requestFactory.create(args))
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