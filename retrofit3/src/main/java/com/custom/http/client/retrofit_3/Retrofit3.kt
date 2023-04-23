package com.custom.http.client.retrofit_3

import android.util.Log
import com.custom.http.client.MethodService
import com.custom.http.client.annotation.parser.AnnotationParser
import com.custom.http.client.call.CallAdapter
import com.custom.http.client.constant.DEFAULT_BOOLEAN
import com.custom.http.client.constant.DEFAULT_INT
import com.custom.http.client.converters.Converter
import com.custom.http.client.converters.converter_types.ToStringConverter
import com.custom.http.client.platform.Platform
import okhttp3.*
import java.lang.reflect.*
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executor

class Retrofit3(
    val callFactory: Call.Factory,
    val baseUrl: okhttp3.HttpUrl? = null,
    val converterFactories: List<Converter.Factory>? = null,
    val defaultConverterFactoriesSize:Int = DEFAULT_INT,
    val callAdapterFactories: List<CallAdapter.Factory>? = null,
    val defaultCallAdapterFactoriesSize:Int = DEFAULT_INT,
    val callbackExecutor: Executor? = null,
    val validateEagerly:Boolean = DEFAULT_BOOLEAN
) {

    private val serviceMethodCache: ConcurrentHashMap<Method, MethodService<*>> = ConcurrentHashMap<Method, MethodService<*>>()

    fun <T> create(service: Class<T>): T {
        Log.i("TAG55","create with class: ${service}")
        validateServiceInterface(service)
        return Proxy.newProxyInstance(
            service.classLoader, arrayOf<Class<*>>(service),
            object : InvocationHandler {
                private val emptyArgs = arrayOf<Any>(0)

                @Throws(Throwable::class)
                override fun invoke(proxy: Any, method: Method, args: Array<Any>?): Any? {
                    // If the method is a method from Object then defer to normal invocation.
                    val _args = args ?: emptyArgs
                    if (method.declaringClass == Any::class.java) {
                        return method.invoke(this, *_args)
                    }
                    //args = args ?: emptyArgs
                    val platform: Platform = Platform.get()
                    return if (platform.isDefaultMethod(method)) platform.invokeDefaultMethod(
                        method,
                        service,
                        proxy,
                        *_args
                    ) else loadServiceMethod3(method)?.invoke(_args)
                }
            }) as T
    }

    private fun validateServiceInterface(service: Class<*>) {
        Log.i("TAG55","validateServiceInterface: ${service}")
        Log.i("TAG55","validateEagerly: ${validateEagerly}")
        require(service.isInterface) { "API declarations must be interfaces." }

        val check: Deque<Class<*>> = ArrayDeque(1)
        check.add(service)
        while (!check.isEmpty()) {
            val candidate = check.removeFirst()
            if (candidate.typeParameters.isNotEmpty()) {
                val message = StringBuilder("Type parameters are unsupported on ").apply {
                    append(candidate.name)

                    if (candidate != service) {
                        append(" which is an interface of ").append(service.name)
                    }
                }

                throw IllegalArgumentException(message.toString())
            }
            Collections.addAll(check, *candidate.interfaces)
        }

        if (validateEagerly) {
            val platform: Platform = Platform.get()
            for (method in service.declaredMethods) {
                if (!platform.isDefaultMethod(method) && !Modifier.isStatic(method.modifiers)) {
                    loadServiceMethod2(method)
                }
            }
        }
    }

    fun loadServiceMethod2(method: Method): MethodService<*>? {

        Log.i("TAG55","load service method 2")

        var result: MethodService<*>? = serviceMethodCache[method]
        if (result != null) return result
        synchronized(serviceMethodCache) {
            result = serviceMethodCache[method]
            if (result == null) {
                result = MethodService.parseAnnotations<Any>(this, method)
                serviceMethodCache[method] = result as MethodService<*>
            }
        }
        return result
    }

    fun <T> stringConverter(type: Type?, annotations: Array<Annotation>?): Converter<T, String>
    {
        Log.i("TAG55","stringConverter called!")
        requireNotNull(type){ "type == null" }
        requireNotNull(annotations){ "annotations == null" }

        converterFactories?.let {

            for (index in it.indices) {

                val converter = converterFactories[index].stringConverter(type = type, annotations = annotations, retrofit = this)
                if (converter != null)
                    return converter as Converter<T, String>
            }
        }

        return ToStringConverter.INSTANCE as Converter<T, String>
    }

    fun <T> requestBodyConverter(type: Type?, parameterAnnotations: Array<Annotation>?, methodAnnotations: Array<Annotation>?
    ): Converter<T, RequestBody> {
        Log.i("TAG55","requestBodyConverter called!")
        return nextRequestBodyConverter(skipPast = null, type = type, parameterAnnotations = parameterAnnotations, methodAnnotations = methodAnnotations)
    }

    fun <T> nextRequestBodyConverter(
        skipPast: Converter.Factory?,
        type: Type?,
        parameterAnnotations: Array<Annotation>?,
        methodAnnotations: Array<Annotation>?
    ): Converter<T, RequestBody> {
        Log.i("TAG55","nextRequestBodyConverter called!")
        requireNotNull(type) { "type == null" }
        requireNotNull(parameterAnnotations) { "parameterAnnotations == null" }
        requireNotNull(methodAnnotations) { "methodAnnotations == null" }

        converterFactories?.let {

            val start = it.indexOf(skipPast) + 1
            for (index in it.indices) {
                val factory = it[index]
                val converter = factory.requestBodyConverter(type = type, parameterAnnotations = parameterAnnotations, methodAnnotations = methodAnnotations, retrofit = this)
                if (converter != null)
                    return converter as Converter<T, RequestBody>
            }

            val builder = java.lang.StringBuilder("Could not locate RequestBody converter for ").apply {
                if (skipPast != null)
                    append("  Skipped:")

                for (i in 0 until start) {
                    append("\n   * ").append(converterFactories[i].javaClass.name)
                }

                append('\n')
                append("  Tried:")
                for (index in it.indices) {
                    append("\n   * ")
                    append(converterFactories[index]::class.java.name)
                }
            }

            throw IllegalArgumentException(builder.toString())
        }?: kotlin.run {
            throw IllegalArgumentException("converterFactories is null")
        }
    }

    fun callAdapter(returnType: Type?, annotations: Array<Annotation>?): CallAdapter<*, *>? {
        Log.i("TAG55","callAdapter called!")
        return nextCallAdapter(null, returnType, annotations)
    }

    fun loadServiceMethod3(method: Method?): MethodService<*>? {
        Log.i("TAG55","loadServiceMethod3 called with method: ${method}!")
        requireNotNull(method) { "Method is null" }
        var result: MethodService<*>? = serviceMethodCache[method]
        Log.i("TAG55","result1: ${result}!")
        if (result != null) return result
        synchronized(serviceMethodCache) {
            result = serviceMethodCache[method]
            Log.i("TAG55","sync result1.1: ${result}!")
            if (result == null) {
                Log.i("TAG55","sync result1.2: ${result}!")
                result = AnnotationParser.parseAnnotations<Any>(this, method)
                Log.i("TAG55","sync result1.3: ${result}!")
                serviceMethodCache[method] = result as MethodService<*>
            }
        }
        Log.i("TAG55","result: $result")
        return result
    }

    fun nextCallAdapter(skipPast: CallAdapter.Factory?, returnType: Type?, annotations: Array<Annotation>?): CallAdapter<*, *>? {
        Log.i("TAG55","nextCallAdapter called!")
        requireNotNull(returnType) { "returnType == null" }
        requireNotNull(annotations) { "annotations == null" }

        var resultingAdapter: CallAdapter<*, *>? = null

        callAdapterFactories?.apply {
            val start = callAdapterFactories.indexOf(skipPast) + 1

            for (index in start until size) {

                val adapter = get(index).get(returnType = returnType, annotations = annotations, retrofit = this@Retrofit3)

                if (adapter != null && resultingAdapter == null)
                    resultingAdapter = adapter
            }

            if (resultingAdapter == null) {

                val builder = StringBuilder("Could not locate call adapter for ").append(returnType).append(".\n")
                if (skipPast != null) {
                    builder.append("  Skipped")
                    for (index in 0..start) {
                        builder.append("\n   * ").append(this[index].javaClass.name)
                    }
                    builder.append('\n')
                }
                builder.append("   Tried")
                for (index in start until size) {
                    builder.append("\n   * ").append(this[index].javaClass.name)
                }

                throw IllegalArgumentException(builder.toString())
            }
        }

        return resultingAdapter
    }

    /**
     * Returns a [Converter] for [ResponseBody] to `type` from the available
     * [factories][.converterFactories].
     *
     * @throws IllegalArgumentException if no converter available for `type`.
     */
    fun <T> responseBodyConverter(type: Type?, annotations: Array<Annotation>?): Converter<ResponseBody, T>? {
        return nextResponseBodyConverter<T>(null, type, annotations)
    }

    fun newBuilder(): Retrofit3Builder = Retrofit3Builder(this)

    /**
     * Returns a [Converter] for [ResponseBody] to `type` from the available
     * [factories][.converterFactories] except `skipPast`.
     *
     * @throws IllegalArgumentException if no converter available for `type`.
     */
    fun <T> nextResponseBodyConverter(skipPast: Converter.Factory?, type: Type?, annotations: Array<Annotation>?): Converter<ResponseBody, T>? {

        requireNotNull(type){ "type == null" }
        requireNotNull(annotations){ "annotations == null" }

        var resultingConverter: Converter<ResponseBody, T>? = null

        converterFactories?.apply {

            val start = this.indexOf(skipPast) + 1

            for (index in start until size) {

                val converter = get(index).responseBodyConverter(type = type, annotations = annotations, retrofit = this@Retrofit3)

                if (converter != null && resultingConverter == null)
                    resultingConverter = converter as Converter<ResponseBody, T>
            }

            if (resultingConverter == null) {

                val builder = StringBuilder("Could not locate ResponseBody converter for ")
                    .append(type)
                    .append(".\n")

                if (skipPast != null) {
                    builder.append("  Skipped:")
                    for (index in 0..start) {
                        builder.append("\n   * ").append(converterFactories[index].javaClass.name)
                    }
                    builder.append('\n')
                }
                builder.append("   Tried:")
                for (index in start until size) {
                    builder.append("\n   * ").append(converterFactories[index].javaClass.name)
                }

                throw IllegalArgumentException(builder.toString())
            }
        }

        return resultingConverter
    }

//    inner class Builder(retrofit3: Retrofit3? = null) {
//
//        private var callFactory: Call.Factory? = retrofit3?.callFactory
//        private var baseUrl: HttpUrl? = retrofit3?.baseUrl
//        private var callbackExecutor:Executor? = retrofit3?.callbackExecutor
//        private var validateEagerly:Boolean = retrofit3?.validateEagerly ?: DEFAULT_BOOLEAN
//        val callAdapterFactories: ArrayList<CallAdapter.Factory> = ArrayList()
//        val converterFactories: ArrayList<Converter.Factory> = ArrayList()
//
//        init {
//
//            retrofit3?.let { retrofit ->
//
//                // Do not add the default BuiltIntConverters and platform-aware converters added by build().
//                val size = retrofit.converterFactories?.size?.minus(retrofit.defaultConverterFactoriesSize) ?: 0
//
//                for (index in 0 until size) {
//                    retrofit.converterFactories?.get(index)?.let {
//                        converterFactories.add(it)
//                    }
//                }
//
//                // Do not add the default, platform-aware call adapters added by build().
//                val callAdapterSize = retrofit.callAdapterFactories?.size?.minus(retrofit.defaultCallAdapterFactoriesSize) ?: 0
//                for (index in 0 until callAdapterSize) {
//                    retrofit.callAdapterFactories?.get(index)?.let {
//                        callAdapterFactories.add(it)
//                    }
//                }
//            }
//        }
//
//        /**
//         * The HTTP client used for requests.
//         *
//         * <p>This is a convenience method for calling {@link #callFactory}.
//         */
//        fun client(client: OkHttpClient): Builder {
//            return callFactory(Objects.requireNonNull(client, "client == null"))
//        }
//
//        /**
//         * Specify a custom call factory for creating {@link Call} instances.
//         *
//         * <p>Note: Calling {@link #client} automatically sets this value.
//         */
//        fun callFactory(factory: Call.Factory?): Builder {
//            requireNotNull(factory) { "factory == null" }
//            this.callFactory = factory
//            return this
//        }
//
//        fun baseUrl(baseUrl: URL?): Builder {
//            requireNotNull(baseUrl) { "baseUrl == null" }
//            return baseUrl(baseUrl.toString().toHttpUrl())
//        }
//
//        /**
//         * Set the API base URL.
//         *
//         * @see #baseUrl(HttpUrl)
//         */
//        fun baseUrl(baseUrl: String?): Builder {
//            requireNotNull(baseUrl) { "baseUrl == null" }
//            return baseUrl(baseUrl.toHttpUrl())
//        }
//
//        /**
//         * Set the API base URL.
//         *
//         * <p>The specified endpoint values (such as with {@link GET @GET}) are resolved against this
//         * value using {@link HttpUrl#resolve(String)}. The behavior of this matches that of an {@code
//         * <a href="">} link on a website resolving on the current URL.
//         *
//         * <p><b>Base URLs should always end in {@code /}.</b>
//         *
//         * <p>A trailing {@code /} ensures that endpoints values which are relative paths will correctly
//         * append themselves to a base which has path components.
//         *
//         * <p><b>Correct:</b><br>
//         * Base URL: http://example.com/api/<br>
//         * Endpoint: foo/bar/<br>
//         * Result: http://example.com/api/foo/bar/
//         *
//         * <p><b>Incorrect:</b><br>
//         * Base URL: http://example.com/api<br>
//         * Endpoint: foo/bar/<br>
//         * Result: http://example.com/foo/bar/
//         *
//         * <p>This method enforces that {@code baseUrl} has a trailing {@code /}.
//         *
//         * <p><b>Endpoint values which contain a leading {@code /} are absolute.</b>
//         *
//         * <p>Absolute values retain only the host from {@code baseUrl} and ignore any specified path
//         * components.
//         *
//         * <p>Base URL: http://example.com/api/<br>
//         * Endpoint: /foo/bar/<br>
//         * Result: http://example.com/foo/bar/
//         *
//         * <p>Base URL: http://example.com/<br>
//         * Endpoint: /foo/bar/<br>
//         * Result: http://example.com/foo/bar/
//         *
//         * <p><b>Endpoint values may be a full URL.</b>
//         *
//         * <p>Values which have a host replace the host of {@code baseUrl} and values also with a scheme
//         * replace the scheme of {@code baseUrl}.
//         *
//         * <p>Base URL: http://example.com/<br>
//         * Endpoint: https://github.com/square/retrofit/<br>
//         * Result: https://github.com/square/retrofit/
//         *
//         * <p>Base URL: http://example.com<br>
//         * Endpoint: //github.com/square/retrofit/<br>
//         * Result: http://github.com/square/retrofit/ (note the scheme stays 'http')
//         */
//        fun baseUrl(baseUrl: HttpUrl?): Builder {
//            requireNotNull(baseUrl) { "baseUrl == null" }
//            val pathSegments = baseUrl.pathSegments
//            require(pathSegments.last().isNotBlank()) { "baseUrl must end in /: $baseUrl" }
//            this.baseUrl = baseUrl
//            return this
//        }
//
//        /**
//         * Add converter factory for serialization and deserialization of objects.
//         */
//        fun addConverterFactory(factory: Converter.Factory?): Builder {
//            requireNotNull(factory) { "factory == null" }
//            converterFactories.add(factory)
//            return this
//        }
//
//        /**
//         * Add a call adapter factory for supporting service method return types other than {@link
//         * Call}.
//         */
//        fun addCallAdapterFactory(factory: CallAdapter.Factory?): Builder {
//            requireNotNull(factory) { "factory == null" }
//            callAdapterFactories.add(factory)
//            return this
//        }
//
//        /**
//         * The executor on which {@link Callback} methods are invoked when returning {@link Call} from
//         * your service method.
//         *
//         * <p>Note: {@code executor} is not used for {@linkplain #addCallAdapterFactory custom method
//         * return types}.
//         */
//        fun callbackExecutor(executor: Executor?): Builder {
//            requireNotNull(executor)
//            this.callbackExecutor = executor
//            return this
//        }
//
//        /**
//         * When calling {@link #create} on the resulting {@link Retrofit} instance, eagerly validate the
//         * configuration of all methods in the supplied interface.
//         */
//        fun validateEagerly(validateEagerly: Boolean): Builder {
//            this.validateEagerly = validateEagerly
//            return this
//        }
//
//        /**
//         * Create the {@link Retrofit} instance using the configured values.
//         *
//         * <p>Note: If neither {@link #client} nor {@link #callFactory} is called a default {@link
//         * OkHttpClient} will be created and used.
//         */
//        fun build(): Retrofit3 {
//            checkNotNull(baseUrl) { "Base URL required." }
//            //checkNotNull(callFactory) { "CallFactory required." }
//
//            val platform: Platform = Platform.get()
//            val call_factory = this.callFactory ?: OkHttpClient()
//            val call_back_executor = this.callbackExecutor ?: platform.defaultCallbackExecutor()
//
//            // Make a defensive copy of the adapters and add the default Call adapter.
//            val callAdapterFactories = ArrayList(this.callAdapterFactories)
//            val defaultCallAdapterFactories: List<CallAdapter.Factory?> = platform.createDefaultCallAdapterFactories(callbackExecutor)
//            callAdapterFactories.addAll(defaultCallAdapterFactories)
//
//            // Make a defensive copy of the converters.
//            val defaultConverterFactories: List<Converter.Factory> =
//                platform.createDefaultConverterFactories() ?: emptyList()
//            val defaultConverterFactoriesSize = defaultConverterFactories.size
//            val converterFactories: ArrayList<Converter.Factory> = ArrayList<Converter.Factory>().apply {
//                // Add the built-in converter factory first. This prevents overriding its behavior but also
//                // ensures correct behavior when using converters that consume all types.
//                add(BuiltInConverters())
//                addAll(this@Builder.converterFactories)
//                addAll(defaultConverterFactories)
//            }
//
//            return Retrofit3(
//                call_factory,
//                baseUrl,
//                converterFactories.toList(),
//                defaultConverterFactoriesSize,
//                callAdapterFactories.toList(),
//                defaultCallAdapterFactories.size,
//                call_back_executor,
//                validateEagerly
//            )
//        }
//    }
}