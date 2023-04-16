package com.custom.http.client

import com.custom.http.client.constant.DEFAULT_BOOLEAN
import com.custom.http.client.constant.DEFAULT_INT
import okhttp3.Call
import okhttp3.RequestBody
import java.lang.reflect.*
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executor

class Retrofit3(private val callFactory: Call.Factory? = null,
                private val baseUrl: okhttp3.HttpUrl? = null,
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
                private val emptyArgs = arrayOfNulls<Any>(0)

                @Throws(Throwable::class)
                override fun invoke(proxy: Any, method: Method, args: Array<Any>?): Any? {
                    // If the method is a method from Object then defer to normal invocation.
                    var args = args
                    if (method.declaringClass == Any::class.java) {
                        return method.invoke(this, *args)
                    }
                    args = args ?: emptyArgs
                    val platform: retrofit2.Platform = retrofit2.Platform.get()
                    return if (platform.isDefaultMethod(method)) platform.invokeDefaultMethod(
                        method,
                        service,
                        proxy,
                        *args
                    ) else loadServiceMethod(method).invoke(args)
                }
            }) as T
    }

    fun <T> stringConverter(type: Type?, annotations: Array<Annotation>?): Converter<T, String>
    {
        TODO("Need to be implemented")
    }

    fun <T> requestBodyConverter(type: Type?, parameterAnnotations: Array<Annotation>?, methodAnnotations: Array<Annotation>?
    ): Converter<T, RequestBody> {

        TODO("Need to be implemented")

        //return nextRequestBodyConverter<T>(null, type, parameterAnnotations, methodAnnotations)
    }


    private fun validateServiceInterface(service: Class<*>) {

        require(service.isInterface) { "API declarations must be interfaces." }

        val check: Deque<Class<*>> = ArrayDeque(1)
        check.add(service)
        while (!check.isEmpty()) {
            val candidate = check.removeFirst()
            if (candidate.typeParameters.isNotEmpty()) {
                val message =
                    StringBuilder("Type parameters are unsupported on ").append(candidate.name)
                if (candidate != service) {
                    message.append(" which is an interface of ").append(service.name)
                }
                throw IllegalArgumentException(message.toString())
            }
            Collections.addAll(check, *candidate.interfaces)
        }
        if (validateEagerly) {
            val platform: retrofit2.Platform = retrofit2.Platform.get()
            for (method in service.declaredMethods) {
                if (!platform.isDefaultMethod(method) && !Modifier.isStatic(method.modifiers)) {
                    loadServiceMethod(method)
                }
            }
        }
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


}