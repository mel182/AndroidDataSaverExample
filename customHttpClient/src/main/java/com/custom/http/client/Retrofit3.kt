package com.custom.http.client

import com.custom.http.client.constant.DEFAULT_BOOLEAN
import com.custom.http.client.constant.DEFAULT_INT
import com.custom.http.client.platform.Platform
import okhttp3.Call
import okhttp3.RequestBody
import okhttp3.ResponseBody
import java.lang.reflect.*
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executor

class Retrofit3(
    val callFactory: Call.Factory,
    val baseUrl: okhttp3.HttpUrl? = null,
    private val converterFactories: List<Converter.Factory>? = null,
    private val defaultConverterFactoriesSize:Int = DEFAULT_INT,
    private val callAdapterFactories: List<CallAdapter.Factory>? = null,
    private val defaultCallAdapterFactoriesSize:Int = DEFAULT_INT,
    private val callbackExecutor: Executor? = null,
    private val validateEagerly:Boolean = DEFAULT_BOOLEAN
) {

    private val serviceMethodCache: ConcurrentHashMap<Method, MethodService<*>> = ConcurrentHashMap<Method, MethodService<*>>()

    fun <T> create(service: Class<T>): T {
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
                    ) else loadServiceMethod(method)?.invoke(_args)
                }
            }) as T
    }

    private fun validateServiceInterface(service: Class<*>) {

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
                    loadServiceMethod(method)
                }
            }
        }
    }

    fun loadServiceMethod(method: Method): MethodService<*>? {
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


//    private fun validateServiceInterface(service: Class<*>) {
//        require(service.isInterface) { "API declarations must be interfaces." }
//        val check: Deque<Class<*>> = ArrayDeque(1)
//        check.add(service)
//        while (!check.isEmpty()) {
//            val candidate = check.removeFirst()
//            if (candidate.typeParameters.size != 0) {
//                val message = java.lang.StringBuilder("Type parameters are unsupported on ")
//                    .append(candidate.name)
//                if (candidate != service) {
//                    message.append(" which is an interface of ").append(service.name)
//                }
//                throw java.lang.IllegalArgumentException(message.toString())
//            }
//            Collections.addAll(check, *candidate.interfaces)
//        }
//        if (validateEagerly) {
//            val platform: retrofit2.Platform = retrofit2.Platform.get()
//            for (method in service.declaredMethods) {
//                if (!platform.isDefaultMethod(method) && !Modifier.isStatic(method.modifiers)) {
//                    loadServiceMethod(method)
//                }
//            }
//        }
//    }


    fun <T> stringConverter(type: Type?, annotations: Array<Annotation>?): Converter<T, String>
    {
        TODO("Need to be implemented")
    }

    fun <T> requestBodyConverter(type: Type?, parameterAnnotations: Array<Annotation>?, methodAnnotations: Array<Annotation>?
    ): Converter<T, RequestBody> {

        TODO("Need to be implemented")

        //return nextRequestBodyConverter<T>(null, type, parameterAnnotations, methodAnnotations)
    }




    fun callAdapter(returnType: Type?, annotations: Array<Annotation>?): CallAdapter<*, *>? {
        return nextCallAdapter(null, returnType, annotations)
    }

    fun loadServiceMethod(method: Method?): MethodService<*>? {
        requireNotNull(method) { "Method is null" }
        var result: MethodService<*>? = serviceMethodCache[method]
        if (result != null) return result
        synchronized(serviceMethodCache) {
            result = serviceMethodCache[method]
            if (result == null) {
                result = AnnotationParser.parseAnnotations<Any>(this, method)
                serviceMethodCache[method] = result as MethodService<*>
            }
        }
        return result
    }

    fun nextCallAdapter(skipPast: CallAdapter.Factory?, returnType: Type?, annotations: Array<Annotation>?): CallAdapter<*, *>? {

        requireNotNull(returnType) { "returnType == null" }
        requireNotNull(annotations) { "annotations == null" }

        var resultingAdapter: CallAdapter<*,*>? = null

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

    /**
     * Returns a [Converter] for [ResponseBody] to `type` from the available
     * [factories][.converterFactories] except `skipPast`.
     *
     * @throws IllegalArgumentException if no converter available for `type`.
     */
    fun <T> nextResponseBodyConverter(skipPast: Converter.Factory?, type: Type?, annotations: Array<Annotation>?): Converter<ResponseBody, T>? {

        requireNotNull(type){ "type == null" }
        requireNotNull(annotations){ "annotations == null" }

        var resultingConverter: Converter<ResponseBody,T>? = null

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


}