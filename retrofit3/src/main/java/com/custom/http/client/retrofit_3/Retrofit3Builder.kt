package com.custom.http.client.retrofit_3

import com.custom.http.client.call.CallAdapter
import com.custom.http.client.constant.DEFAULT_BOOLEAN
import com.custom.http.client.converters.BuiltInConverters
import com.custom.http.client.converters.Converter
import com.custom.http.client.platform.Platform
import okhttp3.Call
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import java.net.URL
import java.util.Objects
import java.util.concurrent.Executor

class Retrofit3Builder(retrofit3: Retrofit3? = null) {

    private var callFactory: Call.Factory? = retrofit3?.callFactory
    private var baseUrl: HttpUrl? = retrofit3?.baseUrl
    private var callbackExecutor: Executor? = retrofit3?.callbackExecutor
    private var validateEagerly:Boolean = retrofit3?.validateEagerly ?: DEFAULT_BOOLEAN
    val callAdapterFactories: ArrayList<CallAdapter.Factory> = ArrayList()
    val converterFactories: ArrayList<Converter.Factory> = ArrayList()

    init {

        retrofit3?.let { retrofit ->

            // Do not add the default BuiltIntConverters and platform-aware converters added by build().
            val size = retrofit.converterFactories?.size?.minus(retrofit.defaultConverterFactoriesSize) ?: 0

            for (index in 0 until size) {
                retrofit.converterFactories?.get(index)?.let {
                    converterFactories.add(it)
                }
            }

            // Do not add the default, platform-aware call adapters added by build().
            val callAdapterSize = retrofit.callAdapterFactories?.size?.minus(retrofit.defaultCallAdapterFactoriesSize) ?: 0
            for (index in 0 until callAdapterSize) {
                retrofit.callAdapterFactories?.get(index)?.let {
                    callAdapterFactories.add(it)
                }
            }
        }
    }

    /**
     * The HTTP client used for requests.
     *
     * <p>This is a convenience method for calling {@link #callFactory}.
     */
    fun client(client: OkHttpClient): Retrofit3Builder {
        return callFactory(Objects.requireNonNull(client, "client == null"))
    }

    /**
     * Specify a custom call factory for creating {@link Call} instances.
     *
     * <p>Note: Calling {@link #client} automatically sets this value.
     */
    fun callFactory(factory: Call.Factory?): Retrofit3Builder {
        requireNotNull(factory) { "factory == null" }
        this.callFactory = factory
        return this
    }

    fun baseUrl(baseUrl: URL?): Retrofit3Builder {
        requireNotNull(baseUrl) { "baseUrl == null" }
        return baseUrl(baseUrl.toString().toHttpUrl())
    }

    /**
     * Set the API base URL.
     *
     * @see #baseUrl(HttpUrl)
     */
    fun baseUrl(baseUrl: String?): Retrofit3Builder {
        requireNotNull(baseUrl) { "baseUrl == null" }
        return baseUrl(baseUrl.toHttpUrl())
    }

    /**
     * Set the API base URL.
     *
     * <p>The specified endpoint values (such as with {@link GET @GET}) are resolved against this
     * value using {@link HttpUrl#resolve(String)}. The behavior of this matches that of an {@code
     * <a href="">} link on a website resolving on the current URL.
     *
     * <p><b>Base URLs should always end in {@code /}.</b>
     *
     * <p>A trailing {@code /} ensures that endpoints values which are relative paths will correctly
     * append themselves to a base which has path components.
     *
     * <p><b>Correct:</b><br>
     * Base URL: http://example.com/api/<br>
     * Endpoint: foo/bar/<br>
     * Result: http://example.com/api/foo/bar/
     *
     * <p><b>Incorrect:</b><br>
     * Base URL: http://example.com/api<br>
     * Endpoint: foo/bar/<br>
     * Result: http://example.com/foo/bar/
     *
     * <p>This method enforces that {@code baseUrl} has a trailing {@code /}.
     *
     * <p><b>Endpoint values which contain a leading {@code /} are absolute.</b>
     *
     * <p>Absolute values retain only the host from {@code baseUrl} and ignore any specified path
     * components.
     *
     * <p>Base URL: http://example.com/api/<br>
     * Endpoint: /foo/bar/<br>
     * Result: http://example.com/foo/bar/
     *
     * <p>Base URL: http://example.com/<br>
     * Endpoint: /foo/bar/<br>
     * Result: http://example.com/foo/bar/
     *
     * <p><b>Endpoint values may be a full URL.</b>
     *
     * <p>Values which have a host replace the host of {@code baseUrl} and values also with a scheme
     * replace the scheme of {@code baseUrl}.
     *
     * <p>Base URL: http://example.com/<br>
     * Endpoint: https://github.com/square/retrofit/<br>
     * Result: https://github.com/square/retrofit/
     *
     * <p>Base URL: http://example.com<br>
     * Endpoint: //github.com/square/retrofit/<br>
     * Result: http://github.com/square/retrofit/ (note the scheme stays 'http')
     */
    fun baseUrl(baseUrl: HttpUrl?): Retrofit3Builder {
        requireNotNull(baseUrl) { "baseUrl == null" }
        require(baseUrl.toString().endsWith("/")) { "baseUrl must end in /: $baseUrl" }
        this.baseUrl = baseUrl
        return this
    }

    /**
     * Add converter factory for serialization and deserialization of objects.
     */
    fun addConverterFactory(factory: Converter.Factory?): Retrofit3Builder {
        requireNotNull(factory) { "factory == null" }
        converterFactories.add(factory)
        return this
    }

    /**
     * Add a call adapter factory for supporting service method return types other than {@link
     * Call}.
     */
    fun addCallAdapterFactory(factory: CallAdapter.Factory?): Retrofit3Builder {
        requireNotNull(factory) { "factory == null" }
        callAdapterFactories.add(factory)
        return this
    }

    /**
     * The executor on which {@link Callback} methods are invoked when returning {@link Call} from
     * your service method.
     *
     * <p>Note: {@code executor} is not used for {@linkplain #addCallAdapterFactory custom method
     * return types}.
     */
    fun callbackExecutor(executor: Executor?): Retrofit3Builder {
        requireNotNull(executor)
        this.callbackExecutor = executor
        return this
    }

    /**
     * When calling {@link #create} on the resulting {@link Retrofit} instance, eagerly validate the
     * configuration of all methods in the supplied interface.
     */
    fun validateEagerly(validateEagerly: Boolean): Retrofit3Builder {
        this.validateEagerly = validateEagerly
        return this
    }

    /**
     * Create the {@link Retrofit} instance using the configured values.
     *
     * <p>Note: If neither {@link #client} nor {@link #callFactory} is called a default {@link
     * OkHttpClient} will be created and used.
     */
    fun build(): Retrofit3 {
        checkNotNull(baseUrl) { "Base URL required." }
        //checkNotNull(callFactory) { "CallFactory required." }

        val platform: Platform = Platform.get()
        val call_factory = this.callFactory ?: OkHttpClient()
        val call_back_executor = this.callbackExecutor ?: platform.defaultCallbackExecutor()

        // Make a defensive copy of the adapters and add the default Call adapter.
        val callAdapterFactories = java.util.ArrayList(this.callAdapterFactories)
        val defaultCallAdapterFactories: List<CallAdapter.Factory?> = platform.createDefaultCallAdapterFactories(callbackExecutor)
        callAdapterFactories.addAll(defaultCallAdapterFactories)

        // Make a defensive copy of the converters.
        val defaultConverterFactories: List<Converter.Factory> =
            platform.createDefaultConverterFactories() ?: emptyList()
        val defaultConverterFactoriesSize = defaultConverterFactories.size
        val converterFactories: java.util.ArrayList<Converter.Factory> = java.util.ArrayList<Converter.Factory>()
            .apply {
            // Add the built-in converter factory first. This prevents overriding its behavior but also
            // ensures correct behavior when using converters that consume all types.
            add(BuiltInConverters())
            addAll(this@Retrofit3Builder.converterFactories)
            addAll(defaultConverterFactories)
        }

        return Retrofit3(
            call_factory,
            baseUrl,
            converterFactories.toList(),
            defaultConverterFactoriesSize,
            callAdapterFactories.toList(),
            defaultCallAdapterFactories.size,
            call_back_executor,
            validateEagerly
        )
    }

}