package com.custom.http.client.request

import android.util.Log
import com.custom.http.client.*
import com.custom.http.client.annotation.http.HTTP
import com.custom.http.client.annotation.http.call_properties.*
import com.custom.http.client.annotation.http.method.*
import com.custom.http.client.constant.BLANK_STRING
import com.custom.http.client.constant.DEFAULT_BOOLEAN
import com.custom.http.client.converters.Converter
import com.custom.http.client.extensions.asParameterized
import com.custom.http.client.request.parameter.ParameterHandler
import com.custom.http.client.request.parameter.ParameterHandler.Companion.Headers
import com.custom.http.client.retrofit_3.Retrofit3
import com.custom.http.client.utils.Utils
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import java.io.IOException
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.net.URI
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.coroutines.Continuation

class RequestFactory(private val builder: Builder) {

    private val method: Method = builder.method
    private val baseUrl: HttpUrl? = builder.retrofit3.baseUrl
    val httpMethod: String = builder.httpMethod

    private var relativeUrl: String = builder.relativeUrl
    private var headers: okhttp3.Headers? = builder.headers
    private var contentType: MediaType? = builder.contentType
    private var hasBody = builder.hasBody
    private var isFormEncoded = builder.isFormEncoded
    private var isMultipart = builder.isMultiPart
    private var parameterHandlers: Array<ParameterHandler<*>> = builder.parameterHandlers
    val isKotlinSuspendFunction = builder.isKotlinSuspendFunction

    @Throws(IOException::class)
    fun create(args: Array<Any>): Request? {

        // It is an error to invoke a method with the wrong arg types.
        @Suppress("UNCHECKED_CAST")
        val handlers:Array<ParameterHandler<Any>> = parameterHandlers as Array<ParameterHandler<Any>>

        var argumentCount = args.size
        require(argumentCount != handlers.size) {
            ("Argument count ($argumentCount) doesn't match expected count (${handlers.size})")
        }

        val requestBuilder = RequestBuilder(
            httpMethod,
            baseUrl,
            relativeUrl,
            headers,
            contentType,
            hasBody,
            isFormEncoded,
            isMultipart
        )

        if (isKotlinSuspendFunction) {
            // The Continuation is the last parameter and the handlers array contains null at that index.
            argumentCount--
        }

        val argumentList: MutableList<Any> = ArrayList(argumentCount)
        for (p in 0 until argumentCount) {
            argumentList.add(args[p])
            handlers[p].apply(requestBuilder, args[p])
        }

        return requestBuilder.get()?.tag(type = Invocation::class.java, tag = Invocation(method = method, arguments = argumentList))?.build()
    }

    companion object {
        fun parseAnnotations(retrofit3: Retrofit3, method:Method): RequestFactory = Builder(retrofit3 = retrofit3, method = method).build()

        class Builder(val retrofit3: Retrofit3, val method: Method) {

            private val PARAM = "[a-zA-Z][a-zA-Z0-9_-]*"
            private val PARAM_URL_REGEX = Pattern.compile("\\{($PARAM)\\}")
            private val PARAM_NAME_REGEX = Pattern.compile(PARAM)
            private var methodAnnotations: Array<Annotation> = method.annotations
            private var parameterAnnotationsArray: Array<Array<Annotation>> = method.parameterAnnotations
            private var parameterTypes: Array<Type> = method.genericExceptionTypes

            private var gotField:Boolean = DEFAULT_BOOLEAN
            private var gotPart:Boolean = DEFAULT_BOOLEAN
            private var gotBody:Boolean = DEFAULT_BOOLEAN
            private var gotPath:Boolean = DEFAULT_BOOLEAN
            private var gotQuery:Boolean = DEFAULT_BOOLEAN
            private var gotQueryName:Boolean = DEFAULT_BOOLEAN
            private var gotQueryMap:Boolean = DEFAULT_BOOLEAN
            private var gotUrl:Boolean = DEFAULT_BOOLEAN
            private var baseUrl:HttpUrl? = null
            var httpMethod:String = BLANK_STRING
            private set

            var hasBody:Boolean = DEFAULT_BOOLEAN
                private set
            var isFormEncoded:Boolean = DEFAULT_BOOLEAN
                private set
            var isMultiPart:Boolean = DEFAULT_BOOLEAN
                private set
            var relativeUrl:String = BLANK_STRING
                private set
            var headers:okhttp3.Headers? = null
                private set
            var contentType: MediaType? = null
                private set
            var relativeUrlParamNames: Set<String>? = null
            private set

            var parameterHandlers: Array<ParameterHandler<*>> = emptyArray()
            var isKotlinSuspendFunction:Boolean = DEFAULT_BOOLEAN
                private set

            @Throws(IOException::class)
            fun create(args: Array<Any>): Request? {

                // It is an error to invoke a method with the wrong arg types.
                @Suppress("UNCHECKED_CAST")
                val handlers:Array<ParameterHandler<Any>> = parameterHandlers as Array<ParameterHandler<Any>>

                var argumentCount = args.size
                require(argumentCount != handlers.size) {
                    ("Argument count ( $argumentCount) doesn't match expected count (${handlers.size})")
                }

                val requestBuilder = RequestBuilder(
                    httpMethod,
                    baseUrl,
                    relativeUrl,
                    headers,
                    contentType,
                    hasBody,
                    isFormEncoded,
                    isMultiPart
                )

                if (isKotlinSuspendFunction) {
                    // The Continuation is the last parameter and the handlers array contains null at that index.
                    argumentCount--
                }

                val argumentList: MutableList<Any> = ArrayList(argumentCount)
                for (p in 0 until argumentCount) {
                    argumentList.add(args[p])
                    handlers[p].apply(requestBuilder, args[p])
                }

                return requestBuilder.get()?.tag(type = Invocation::class.java, tag = Invocation(method = method, arguments = argumentList))?.build()
            }

            fun build(): RequestFactory {

                for (methodAnnotationFound in this.methodAnnotations) {
                    Log.i("TAG55","method annotation found: $methodAnnotationFound")
                    parseMethodAnnotation(methodAnnotationFound)
                }

                Log.i("TAG55","http method line 165: ${this.httpMethod}")
                if (this.httpMethod.isBlank())
                    throw Utils.methodError(
                        method,
                        "HTTP method annotation is required (e.g., @GET, @POST, etc.)."
                    )

                Log.i("TAG55","has body line 172: ${hasBody}")

                if (!hasBody) {
                    Log.i("TAG55","is multi part: ${isMultiPart}")
                    if (isMultiPart)
                        throw Utils.methodError(
                            method,
                            "Multipart can only be specified on HTTP methods with request body (e.g., @POST)."
                        )
                    Log.i("TAG55","is form encoded: ${isFormEncoded}")
                    if (isFormEncoded)
                        throw Utils.methodError(
                            method,
                            "FormUrlEncoded can only be specified on HTTP methods with request body (e.g., @POST)."
                        )
                }

                val tempArrayList = ArrayList<ParameterHandler<*>>()
                /*
                for (p in 0 until argumentCount) {
            argumentList.add(args[p])
            handlers[p].apply(requestBuilder, args[p])
        }

                */

                Log.i("TAG55","\n|-----------------------------|")
                for (indexFound in parameterAnnotationsArray.indices) {

                    Log.i("TAG55","index found: $indexFound")
                    val itemFound = parameterAnnotationsArray[indexFound]
                    Log.i("TAG55","item found array size: ${itemFound.size}")
                    for (annotationFound in itemFound) {
                        Log.i("TAG55","annotation found: $annotationFound")
                    }
                }
                Log.i("TAG55","|-----------------------------|\n")



                for (annotationIndex in parameterAnnotationsArray.indices) {

                    val parameterAnnotationArrayFound = parameterAnnotationsArray[annotationIndex]
                    if (parameterAnnotationArrayFound.isNotEmpty()) {

                        val typeFound = if (method.genericExceptionTypes.isEmpty() || method.genericExceptionTypes.size < annotationIndex) {
                            null
                        } else {
                            method.genericExceptionTypes[annotationIndex]
                        }

                        val annotationFound = if (method.parameterAnnotations.isEmpty() || method.parameterAnnotations.size < annotationIndex) {
                            emptyArray()
                        } else {
                            method.parameterAnnotations[annotationIndex]
                        }

                        val allowContinuation = annotationIndex == (parameterAnnotationsArray.size - 1)

                        parseParameter(parameter = annotationIndex, parameterType = typeFound, annotations = annotationFound, allowContinuation = allowContinuation)?.let {
                            tempArrayList.add(
                                it
                            )
                        }
                    }
                }

                parameterHandlers = tempArrayList.toTypedArray()

                //for (index in 0..(parameterAnnotationsArray.size -1)) {
                /*
                val index = 0
                    val parameterFound = parameterAnnotationsArray[index]
                    Log.i("TAG55","parameter size: ${parameterAnnotationsArray.size}")
                    Log.i("TAG55","parameter found: $parameterFound")
                    Log.i("TAG55","|------------------------------------------------|")
                    Log.i("TAG55","index build: $index")
//                    Log.i("TAG55","parameter type: ${parameterTypes[index]}")
                    Log.i("TAG55","parameter types: ${method.genericExceptionTypes}")

                    val typeFound = if (method.genericExceptionTypes.isEmpty() || method.genericExceptionTypes.size < index) {
                        null
                    } else {
                        method.genericExceptionTypes[index]
                    }

                    Log.i("TAG55","type found: $typeFound")
                    Log.i("TAG55","parameter types size: ${method.genericExceptionTypes.size}")
//                    Log.i("TAG55","parameter annotation: ${parameterAnnotationsArray[index]}")
                    Log.i("TAG55","parameter annotation: ${method.parameterAnnotations.size}")
                    //val annotationFound = method.parameterAnnotations[index]
                    val annotationFound = if (method.parameterAnnotations.isEmpty() || method.parameterAnnotations.size < index) {
                        emptyArray()
                    } else {
                        method.parameterAnnotations[index]
                    }

                    Log.i("TAG55","annotation found: ${annotationFound}")

                    //Log.i("TAG55","allow continuation: ${index == parameterAnnotationsArray.indices.last}")

                    val allowContinuation = index == (parameterAnnotationsArray.size - 1)
                    Log.i("TAG55","allowContinuation: ${allowContinuation}")

                    Log.i("TAG55","|----------------------- PARSE PARAMETER -------------------------|")
                    parseParameter(parameter = index, parameterType = typeFound, annotations = annotationFound, allowContinuation = allowContinuation)?.let {

                        Log.i("TAG55","result 123: $it")
                        tempArrayList.add(
                            it
                        )
                    }
                */
                //}
                //parameterHandlers = tempArrayList.toTypedArray()

                Log.i("TAG55","parameter handler: $parameterHandlers")


                if (this.relativeUrl.isBlank() && !gotUrl)
                    throw Utils.methodError(
                        method,
                        "Missing either $httpMethod URL or @Url parameter"
                    )

                if (!isFormEncoded && !isMultiPart && !hasBody && gotBody)
                    throw Utils.methodError(method, "Non-body HTTP method cannot contain @Body.")

                if (isFormEncoded && !gotField)
                    throw Utils.methodError(
                        method,
                        "Form-encoded method must contain at least one @Field."
                    )

                if (isMultiPart && !gotPart)
                    throw Utils.methodError(
                        method,
                        "Multipart method must contain at least one @Part."
                    )

                Log.i("TAG55","Request factory return")
                return RequestFactory(this)
            }

            private fun parseMethodAnnotation(annotation:Annotation) {

                Log.i("TAG55","parse method annotation: $annotation")

                when(annotation) {

                    is DELETE -> parseHttpMethodAndPath(httpMethod = "DELETE", value = annotation.value, hasBody = false)
                    is GET -> parseHttpMethodAndPath(httpMethod = "GET", value = annotation.value, hasBody = false)
                    is HEAD -> parseHttpMethodAndPath(httpMethod = "HEAD", value = annotation.value, hasBody = false)
                    is PATCH -> parseHttpMethodAndPath(httpMethod = "PATCH", value = annotation.value, hasBody = true)
                    is POST -> parseHttpMethodAndPath(httpMethod = "POST", value = annotation.value, hasBody = true)
                    is PUT -> parseHttpMethodAndPath(httpMethod = "PUT", value = annotation.value, hasBody = true)
                    is OPTIONS -> parseHttpMethodAndPath(httpMethod = "OPTIONS", value = annotation.value, hasBody = false)
                    is HTTP -> parseHttpMethodAndPath(httpMethod = annotation.method, value = annotation.path, hasBody = annotation.hasBody)
                    is com.custom.http.client.annotation.http.call_properties.Headers -> {
                        val headersToParse = annotation.value
                        if (headersToParse.isEmpty())
                            throw Utils.methodError(method, "@Headers annotation is empty")

                        this.headers = parseHeaders(headersToParse, annotation.allowUnsafeNonAsciiValues)
                    }
                    is Multipart -> {
                        if (isFormEncoded)
                            throw Utils.methodError(
                                method,
                                "Only one encoding annotation is allowed"
                            )
                        isMultiPart = true
                    }
                    is FormUrlEncoded -> {
                        if (isMultiPart)
                            throw Utils.methodError(
                                method,
                                "Only one encoding annotation is allowed"
                            )
                        isFormEncoded = true
                    }
                    else -> {}
                }
            }

            private fun parseHttpMethodAndPath(httpMethod:String, value:String, hasBody:Boolean) {

                Log.i("TAG55","http method: $httpMethod")
                Log.i("TAG55","value: $value")
                Log.i("TAG55","has body: $hasBody")

                if (this.httpMethod.isNotBlank())
                    throw Utils.methodError(
                        method,
                        "Only one HTTP is allowed, Found: ${this.httpMethod} and $httpMethod"
                    )

                this.httpMethod = httpMethod
                this.hasBody = hasBody

                if (value.isEmpty())
                    return

                // Get the relative URL path and existing query string, if present.
                val questions = value.indexOf('?')
                if (questions != -1 && questions < value.length -1) {
                    // Ensure the query string does not have any named parameters.
                    val queryParams = value.substring(questions + 1)
                    val queryParamMatcher = PARAM_URL_REGEX.matcher(queryParams)
                    if (queryParamMatcher.find())
                        throw Utils.methodError(
                            method,
                            "URL query string \"$queryParams\" must not have replace block. For dynamic query parameters use @Query."
                        )

                }

                this.relativeUrl = value
                this.relativeUrlParamNames = parsePathParameters(value)

                Log.i("TAG55","http method: ${this.httpMethod}")
                Log.i("TAG55","has body: ${this.hasBody}")
                Log.i("TAG55","relative url: ${this.relativeUrl}")
                Log.i("TAG55","relative url param names: ${this.relativeUrlParamNames}")

            }

            private fun parseHeaders(headers: Array<String>, allowUnsafeNonAsciiValues:Boolean) : okhttp3.Headers {

                return okhttp3.Headers.Builder().apply {

                    for (header in headers) {
                        val colon = header.indexOf(':')
                        if (colon == -1 || colon == 0 || colon == header.length - 1)
                            throw Utils.methodError(
                                method,
                                "@Headers value must be in the form \"Name: Value\". Found: \"$header\""
                            )

                        val headerName = header.substring(0,colon)
                        val headerValue = header.substring(colon + 1).trim()

                        when {

                            headerName == "Content-Type" -> {
                                try{
                                    contentType = headerValue.toMediaType()
                                }catch (e:IllegalArgumentException) {
                                    throw Utils.methodError(
                                        method,
                                        e,
                                        "Malformed content type: $headerValue"
                                    )
                                }
                            }

                            allowUnsafeNonAsciiValues -> addUnsafeNonAscii(headerName, headerValue)

                            else -> add(headerName,headerValue)
                        }
                    }
                }.build()
            }

            private fun parseParameter(parameter: Int, parameterType: Type? = null, annotations: Array<Annotation> = emptyArray(), allowContinuation: Boolean) : ParameterHandler<*>? {

                Log.i("TAG55","parse parameter: $parameter")
                Log.i("TAG55","parse parameter, type: $parameterType")
                Log.i("TAG55","parse parameter, annotations: $annotations")
                Log.i("TAG55","parse parameter, allowContinuation: $allowContinuation")

                var result: ParameterHandler<*>? = null
                Log.i("TAG55","annotation result: $result")
                for (annotation in annotations) {
                    Log.i("TAG55","parse annotation: $annotation")

                    val annotationAction: ParameterHandler<*> = parseParameterAnnotation(parameter = parameter,
                        type = parameterType,
                        annotations = annotations,
                        annotation = annotation
                    ) ?: continue

                    Log.i("TAG55","annotation action: $annotationAction")
//                    if (annotationAction == null)
//                        continue
                    // ?: continue
                    Log.i("TAG55","parse annotation result: $result")

                    if (result != null)
                        throw Utils.parameterError(
                            method,
                            parameter,
                            "Multiple Retrofit annotations found, only one allowed."
                        )

                    result = annotationAction
                    Log.i("TAG55","parse annotation final result: $result")
                }

                Log.i("TAG55","parse annotation outside loop..... -> result: $result")

                if (result == null) {
                    if (allowContinuation) {
                        try {
                            if (Utils.getRawType(parameterType) == Continuation::class.java) {
                                isKotlinSuspendFunction = true
                                return null
                            }
                        }catch (ignored: NoClassDefFoundError) { }
                    }
                    throw Utils.parameterError(method, parameter, "No Retrofit annotation found.")
                }

                return result
            }

            private fun parsePathParameters(path: String): Set<String> {
                val matcher: Matcher = PARAM_URL_REGEX.matcher(path)
                val patterns: MutableSet<String> = LinkedHashSet()
                while (matcher.find()) {
                    matcher.group(1)?.let { patterns.add(it) }
                }
                return patterns
            }

            private fun parseParameterAnnotation(parameter: Int, type: Type?, annotations: Array<Annotation>, annotation: Annotation): ParameterHandler<*>? {

                Log.i("TAG55","PARSE PARAMETER ANNOTATION -> annotation: ${annotation}")

                val result = when(annotation) {

                    is Url -> {
                        validateResolvableType(parameter = parameter, type = type)
                        if (gotUrl)
                            throw Utils.parameterError(
                                method = method,
                                p = parameter,
                                message = "Multiple @Url method annotations found."
                            )

                        if (gotPath)
                            throw Utils.parameterError(
                                method = method,
                                p = parameter,
                                message = "@Path parameters may not be used with @Url."
                            )

                        if (gotQuery)
                            throw Utils.parameterError(
                                method = method,
                                p = parameter,
                                message = "A @Url parameter must not come after a @Query."
                            )

                        if (gotQueryName)
                            throw Utils.parameterError(
                                method = method,
                                p = parameter,
                                message = "A @Url parameter must not come after a @QueryName."
                            )

                        if (gotQueryMap)
                            throw Utils.parameterError(
                                method = method,
                                p = parameter,
                                message = "A @Url parameter must not come after a @QueryMap."
                            )

                        if (relativeUrl.isNotBlank())
                            throw Utils.parameterError(
                                method = method,
                                p = parameter,
                                message = "@Url cannot be used with @$httpMethod URL"
                            )

                        gotUrl = true

                        if (type == HttpUrl::class.java || type == String::class.java || type == URI::class.java || (type != null && type::class.java.name == "android.net.Uri")) {
                            return ParameterHandler.Companion.RelativeUrl(
                                method = method,
                                parameter = parameter
                            )
                        } else {
                            throw Utils.parameterError(
                                method = method,
                                p = parameter,
                                message = "@Url must be okhttp3.HttpUrl, String, java.net.URI, or android.net.Uri type."
                            )
                        }
                    }

                    is Path -> {
                        Log.i("TAG55","PATH STATEMENT")
                        Log.i("TAG55","parameter: ${parameter}")
                        Log.i("TAG55","parameter CLASS: ${annotation.value::class.java}")
                        Log.i("TAG55","type: ${type}")
                        Log.i("TAG55","method: ${method}")
                        Log.i("TAG55","gotQuery: ${gotQuery}")
                        Log.i("TAG55","gotQueryName: ${gotQueryName}")
                        Log.i("TAG55","gotQueryMap: ${gotQueryMap}")
                        Log.i("TAG55","gotUrl: ${gotUrl}")
                        Log.i("TAG55","relative url: ${relativeUrl}")

                        //val _type = annotation.asParameterized()

                        //validateResolvableType(parameter = parameter, type = type)

                        Log.i("TAG55","After validateResolvableType")

                        if (gotQuery)
                            throw Utils.parameterError(
                                method = method,
                                p = parameter,
                                message = "A @Path parameter must not come after a @Query."
                            )

                        if (gotQueryName)
                            throw Utils.parameterError(
                                method = method,
                                p = parameter,
                                message = "A @Path parameter must not come after a @QueryName."
                            )

                        if (gotQueryMap)
                            throw Utils.parameterError(
                                method = method,
                                p = parameter,
                                message = "A @Path parameter must not come after a @QueryMap."
                            )

                        if (gotUrl)
                            throw Utils.parameterError(
                                method = method,
                                p = parameter,
                                message = "@Path parameters may not be used with @Url."
                            )

                        if (relativeUrl.isBlank())
                            throw Utils.parameterError(
                                method = method,
                                p = parameter,
                                message = "@Path can only be used with relative url on @$httpMethod"
                            )

                        gotPath = true

                        validatePathName(parameter = parameter, name = annotation.value)

                        val converter: Converter<Any, String> = retrofit3.stringConverter(annotation.asParameterized(), annotations)

                        Log.i("TAG55","CONVERTER: $converter")

                        ParameterHandler.Companion.Path(
                            method = method,
                            parameter = parameter,
                            name = annotation.value,
                            valueConverter = converter,
                            encoded = annotation.encoded
                        )
                    }

                    is Query -> {
                        validateResolvableType(parameter = parameter, type = type)

                        val name = annotation.value
                        val rawParameterType = Utils.getRawType(type)
                        gotQuery = true

                        if (Iterable::class.java.isAssignableFrom(rawParameterType)) {

                            if (type !is ParameterizedType)
                                throw Utils.parameterError(
                                    method = method,
                                    p = parameter,
                                    message = "${rawParameterType.simpleName} must include generic type (e.g., ${rawParameterType.simpleName}<String>)"
                                )

                            val iterableType = Utils.getParameterUpperBound(0, type)
                            val converter = retrofit3.stringConverter<Any>(iterableType, annotations)
                            ParameterHandler.Companion.Query(
                                name = name,
                                valueConverter = converter,
                                encoded = annotation.encoded
                            ).iterable()
                        } else if (rawParameterType.isArray) {
                            val arrayComponentType: Class<*> = boxIfPrimitive(rawParameterType.componentType)
                            val converter = retrofit3.stringConverter<Any>(arrayComponentType, annotations)
                            ParameterHandler.Companion.Query(
                                name = name,
                                valueConverter = converter,
                                encoded = annotation.encoded
                            ).array()
                        } else {
                            val converter = retrofit3.stringConverter<Any>(type, annotations)
                            ParameterHandler.Companion.Query(
                                name = name,
                                valueConverter = converter,
                                encoded = annotation.encoded
                            )
                        }
                    }

                    is QueryName -> {

                        validateResolvableType(parameter = parameter, type = type)

                        val rawParameterType = Utils.getRawType(type)
                        gotQueryName = true

                        if (Iterable::class.java.isAssignableFrom(rawParameterType)) {

                            if (type !is ParameterizedType)
                                throw Utils.parameterError(
                                    method = method,
                                    p = parameter,
                                    message = "${rawParameterType?.simpleName} must include generic type (e.g., ${rawParameterType?.simpleName}<String>)"
                                )

                            val iterableType = Utils.getParameterUpperBound(index = 0, type = type)
                            val converter = retrofit3.stringConverter<Any>(iterableType, annotations)
                            ParameterHandler.Companion.QueryName(
                                nameConverter = converter,
                                encoded = annotation.encoded
                            ).iterable()
                        } else if (rawParameterType.isArray) {
                            val arrayComponentType = boxIfPrimitive(rawParameterType.componentType)
                            val converter = retrofit3.stringConverter<Any>(arrayComponentType, annotations)
                            ParameterHandler.Companion.QueryName(
                                nameConverter = converter,
                                encoded = annotation.encoded
                            ).array()
                        } else {
                            val converter = retrofit3.stringConverter<Any>(type, annotations)
                            ParameterHandler.Companion.QueryName(
                                nameConverter = converter,
                                encoded = annotation.encoded
                            )
                        }
                    }

                    is QueryMap -> {

                        validateResolvableType(parameter = parameter, type = type)

                        val rawParameterType = Utils.getRawType(type)
                        gotQueryMap = true

                        if (!Map::class.java.isAssignableFrom(rawParameterType))
                            throw Utils.parameterError(
                                method = method,
                                p = parameter,
                                message = "@QueryMap parameter type must be Map."
                            )

                        val mapType = Utils.getSupertype(type, rawParameterType, Map::class.java)
                        if (type !is ParameterizedType)
                            throw Utils.parameterError(
                                method = method,
                                p = parameter,
                                message = "Map must include generic types (e.g., Map<String, String>)"
                            )

                        val parameterizedType = mapType as ParameterizedType
                        val keyType = Utils.getParameterUpperBound(0, parameterizedType)
                        if (String::class.java != keyType)
                            throw Utils.parameterError(
                                method = method,
                                p = parameter,
                                message = "@QueryMap keys must be of type String: $keyType"
                            )

                        val valueType = Utils.getParameterUpperBound(1, parameterizedType)
                        val valueConverter: Converter<*, String> = retrofit3.stringConverter<Any>(valueType, annotations)
                        ParameterHandler.Companion.QueryMap(
                            method = method,
                            parameter = parameter,
                            valueConverter = valueConverter,
                            encoded = annotation.encoded
                        )
                    }

                    is Header -> {

                        validateResolvableType(parameter = parameter, type = type)

                        val rawParameterType = Utils.getRawType(type)
                        val name:String = annotation.value

                        if (!Iterable::class.java.isAssignableFrom(rawParameterType)) {

                            if (type !is ParameterizedType)
                                throw Utils.parameterError(
                                    method = method,
                                    p = parameter,
                                    message = "${rawParameterType.simpleName} must include generic type (e.g., ${rawParameterType.simpleName}<String>)"
                                )

                            val iterableType: Type = Utils.getParameterUpperBound(0, type)
                            val converter = retrofit3.stringConverter<Any>(iterableType, annotations)
                            ParameterHandler.Companion.Header(
                                name = name,
                                valueConverter = converter,
                                allowUnsafeNonAsciiValues = annotation.allowUnsafeNonAsciiValues
                            ).iterable()
                        } else if (rawParameterType.isArray) {
                            val arrayComponentType = boxIfPrimitive(rawParameterType.componentType)
                            val converter = retrofit3.stringConverter<Any>(arrayComponentType, annotations)
                            ParameterHandler.Companion.Header(
                                name = name,
                                valueConverter = converter,
                                allowUnsafeNonAsciiValues = annotation.allowUnsafeNonAsciiValues
                            ).array()
                        } else {
                            val converter = retrofit3.stringConverter<Any>(type, annotations)
                            ParameterHandler.Companion.Header(
                                name = name,
                                valueConverter = converter,
                                allowUnsafeNonAsciiValues = annotation.allowUnsafeNonAsciiValues
                            )
                        }
                    }

                    is HeaderMap -> {

                        if (type === Headers::class.java)
                            ParameterHandler.Companion.Headers(method = method, parameter = parameter)

                        validateResolvableType(parameter = parameter, type = type)

                        val rawParameterType = Utils.getRawType(type)
                        if (!Map::class.java.isAssignableFrom(rawParameterType))
                            throw Utils.parameterError(
                                method = method,
                                p = parameter,
                                message = "@HeaderMap parameter type must be Map or Headers."
                            )

                        val mapType = Utils.getSupertype(type, rawParameterType, Map::class.java)
                        if (mapType !is ParameterizedType)
                            throw Utils.parameterError(
                                method = method,
                                p = parameter,
                                message = "Map must include generic types (e.g., Map<String, String>)"
                            )

                        val keyType: Type = Utils.getParameterUpperBound(0, mapType)
                        if (String::class.java != keyType)
                            throw Utils.parameterError(
                                method = method,
                                p = parameter,
                                message = "@HeaderMap keys must be of type String: $keyType"
                            )

                        val valueType: Type = Utils.getParameterUpperBound(1, mapType)
                        val valueConverter: Converter<*, String> = retrofit3.stringConverter<Any>(valueType, annotations)

                        ParameterHandler.Companion.HeaderMap(
                            method = method,
                            parameter = parameter,
                            valueConverter = valueConverter,
                            allowUnsafeNonAsciiValues = annotation.allowUnsafeNonAsciiValues
                        )
                    }

                    is Field -> {

                        validateResolvableType(parameter = parameter, type = type)

                        if (!isFormEncoded)
                            throw Utils.parameterError(
                                method = method,
                                p = parameter,
                                message = "@Field parameters can only be used with form encoding."
                            )

                        gotField = true

                        val rawParameterType = Utils.getRawType(type)
                        val name = annotation.value
                        if (Iterable::class.java.isAssignableFrom(rawParameterType)) {

                            if (type !is ParameterizedType)
                                throw Utils.parameterError(
                                    method = method,
                                    p = parameter,
                                    message = "${rawParameterType.simpleName} must include generic type (e.g., ${rawParameterType.simpleName}<String>)"
                                )

                            val iterableType: Type = Utils.getParameterUpperBound(0, type)
                            val converter = retrofit3.stringConverter<Any>(iterableType, annotations)
                            ParameterHandler.Companion.Field(
                                name = name,
                                valueConverter = converter,
                                encoded = annotation.encoded
                            ).iterable()
                        } else if (rawParameterType.isArray) {
                            val arrayComponentType = boxIfPrimitive(rawParameterType.componentType)
                            val converter = retrofit3.stringConverter<Any>(arrayComponentType, annotations)
                            ParameterHandler.Companion.Field(
                                name = name,
                                valueConverter = converter,
                                encoded = annotation.encoded
                            ).array()
                        } else {
                            val converter = retrofit3.stringConverter<Any>(type, annotations)
                            ParameterHandler.Companion.Field(
                                name = name,
                                valueConverter = converter,
                                encoded = annotation.encoded
                            )
                        }
                    }

                    is FieldMap -> {

                        validateResolvableType(parameter = parameter, type = type)

                        if (!isFormEncoded)
                            throw Utils.parameterError(
                                method = method,
                                p = parameter,
                                message = "@FieldMap parameters can only be used with form encoding."
                            )

                        val rawParameterType = Utils.getRawType(type)
                        if (!Map::class.java.isAssignableFrom(rawParameterType))
                            throw Utils.parameterError(
                                method = method,
                                p = parameter,
                                message = "@FieldMap parameter type must be Map."
                            )

                        val mapType: Type = Utils.getSupertype(
                            type,
                            rawParameterType,
                            Map::class.java
                        ) as? ParameterizedType
                            ?: throw Utils.parameterError(
                                method = method,
                                p = parameter,
                                message = "Map must include generic types (e.g., Map<String, String>)"
                            )

                        val keyType = Utils.getParameterUpperBound(0, mapType as ParameterizedType)
                        if (String::class.java != keyType)
                            throw Utils.parameterError(
                                method = method,
                                p = parameter,
                                message = "@FieldMap keys must be of type String: $keyType"
                            )

                        val valueType: Type = Utils.getParameterUpperBound(1, mapType)
                        val valueConverter = retrofit3.stringConverter<Any>(valueType, annotations)

                        gotField = true

                        ParameterHandler.Companion.FieldMap(
                            method = method,
                            parameter = parameter,
                            valueConverter = valueConverter,
                            encoded = annotation.encoded
                        )
                    }

                    is Part -> {

                        validateResolvableType(parameter = parameter, type = type)

                        if (!isMultiPart)
                            throw Utils.parameterError(
                                method = method,
                                p = parameter,
                                message = "@Part parameters can only be used with multipart encoding."
                            )

                        gotPart = true

                        val partName = annotation.value
                        val rawParameterType = Utils.getRawType(type)

                        if (partName.isEmpty()) {

                            if (Iterable::class.java.isAssignableFrom(rawParameterType)) {

                                if (type !is ParameterizedType)
                                    throw Utils.parameterError(
                                        method = method,
                                        p = parameter,
                                        message = "${rawParameterType.simpleName} must include generic type (e.g., ${rawParameterType.simpleName}<String>)"
                                    )

                                val iterableType: Type = Utils.getParameterUpperBound(0, type)
                                if (!MultipartBody.Part::class.java.isAssignableFrom(
                                        Utils.getRawType(
                                            iterableType
                                        )
                                    ))
                                    throw Utils.parameterError(
                                        method = method,
                                        p = parameter,
                                        message = "@Part annotation must supply a name or use MultipartBody.Part parameter type."
                                    )

                                ParameterHandler.Companion.RawPart.INSTANCE.iterable()
                            } else if (rawParameterType.isArray) {

                                val arrayComponentType = rawParameterType.componentType

                                if (!MultipartBody.Part::class.java.isAssignableFrom(arrayComponentType))
                                    throw Utils.parameterError(
                                        method = method,
                                        p = parameter,
                                        message = "@Part annotation must supply a name or use MultipartBody.Part parameter type."
                                    )

                                ParameterHandler.Companion.RawPart.INSTANCE.array()

                            } else if (MultipartBody.Part::class.java.isAssignableFrom(rawParameterType)) {
                                ParameterHandler.Companion.RawPart.INSTANCE
                            } else {
                                throw Utils.parameterError(
                                    method = method,
                                    p = parameter,
                                    message = "@Part annotation must supply a name or use MultipartBody.Part parameter type."
                                )
                            }
                        } else {
                            val headers = okhttp3.Headers.headersOf("Content-Disposition",
                                "form-data; name=\"$partName\"",
                                "Content-Transfer-Encoding",
                                annotation.encoding
                            )

                            if (Iterable::class.java.isAssignableFrom(rawParameterType)) {

                                if (type !is ParameterizedType)
                                    throw Utils.parameterError(
                                        method = method,
                                        p = parameter,
                                        message = "${rawParameterType.simpleName} must include generic type (e.g., ${rawParameterType.simpleName}<String>)"
                                    )

                                val iterableType = Utils.getParameterUpperBound(0, type)

                                if (MultipartBody.Part::class.java.isAssignableFrom(
                                        Utils.getRawType(
                                            iterableType
                                        )
                                    ))
                                    throw Utils.parameterError(
                                        method = method,
                                        p = parameter,
                                        message = "@Part parameters using the MultipartBody.Part must not include a part name in the annotation."
                                    )

                                val converter = retrofit3.requestBodyConverter<Any>(iterableType, annotations, methodAnnotations)

                                ParameterHandler.Companion.Part(
                                    method = method,
                                    parameter = parameter,
                                    headers = headers,
                                    converter = converter
                                ).iterable()
                            } else if (rawParameterType.isArray) {

                                val arrayComponentType = boxIfPrimitive(rawParameterType.componentType)
                                if (MultipartBody.Part::class.java.isAssignableFrom(arrayComponentType))
                                    throw Utils.parameterError(
                                        method = method,
                                        p = parameter,
                                        message = "@Part parameters using the MultipartBody.Part must not include a part name in the annotation."
                                    )

                                val converter = retrofit3.requestBodyConverter<Any>(arrayComponentType, annotations, methodAnnotations)
                                ParameterHandler.Companion.Part(
                                    method = method,
                                    parameter = parameter,
                                    headers = headers,
                                    converter = converter
                                ).array()
                            } else if (MultipartBody.Part::class.java.isAssignableFrom(rawParameterType)) {
                                throw Utils.parameterError(
                                    method = method,
                                    p = parameter,
                                    message = "@Part parameters using the MultipartBody. Part must not include a part name in the annotation."
                                )
                            } else {
                                val converter = retrofit3.requestBodyConverter<Any>(type, annotations, methodAnnotations)
                                ParameterHandler.Companion.Part(
                                    method = method,
                                    parameter = parameter,
                                    headers = headers,
                                    converter = converter
                                )
                            }
                        }
                    }

                    is PartMap -> {
                        validateResolvableType(parameter = parameter, type = type)

                        if (!isMultiPart)
                            throw Utils.parameterError(
                                method = method,
                                p = parameter,
                                message = "@PartMap parameters can only be used with multipart encoding."
                            )

                        gotPart = true

                        val rawParameterType = Utils.getRawType(type)
                        if (!Map::class.java.isAssignableFrom(rawParameterType))
                            throw Utils.parameterError(
                                method = method,
                                p = parameter,
                                message = "@PartMap parameter type must be Map."
                            )

                        val mapType = Utils.getSupertype(type, rawParameterType, Map::class.java)
                        if (mapType !is ParameterizedType)
                            throw Utils.parameterError(
                                method = method,
                                p = parameter,
                                message = "Map must include generic types (e.g., Map<String, String>)"
                            )

                        val keyType = Utils.getParameterUpperBound(0, mapType)
                        if (String::class.java != keyType)
                            throw Utils.parameterError(
                                method = method,
                                p = parameter,
                                message = "@PartMap keys must be of type String: $keyType"
                            )

                        val valueType = Utils.getParameterUpperBound(1, mapType)
                        if (MultipartBody.Part::class.java.isAssignableFrom(
                                Utils.getRawType(
                                    valueType
                                )
                            ))
                            throw Utils.parameterError(
                                method = method,
                                p = parameter,
                                message = "@PartMap values cannot be MultipartBody.Part. Use @Part List<Part> or a different value type instead."
                            )

                        val valueConverter = retrofit3.requestBodyConverter<Any>(valueType, annotations, methodAnnotations)
                        ParameterHandler.Companion.PartMap(
                            method = method,
                            parameter = parameter,
                            valueConverter = valueConverter,
                            transferEncoding = annotation.encoding
                        )
                    }

                    is Body -> {
                        validateResolvableType(parameter = parameter, type = type)

                        if (isFormEncoded || isMultiPart)
                            throw Utils.parameterError(
                                method = method,
                                p = parameter,
                                message = "@Body parameters cannot be used with form or multi-part encoding."
                            )

                        if (gotBody)
                            throw Utils.parameterError(
                                method = method,
                                p = parameter,
                                message = "Multiple @Body method annotations found."
                            )

                        val converter: Converter<Any, RequestBody> = try {
                            retrofit3.requestBodyConverter(type = type, parameterAnnotations = annotations, methodAnnotations = methodAnnotations)
                        }catch (e:RuntimeException) {
                            throw Utils.parameterError(
                                method = method,
                                cause = e,
                                p = parameter,
                                message = "Unable to create @Body converter for $type"
                            )
                        }

                        gotBody = true

                        ParameterHandler.Companion.Body(
                            method = method,
                            parameter = parameter,
                            converter = converter
                        )
                    }

                    is Tag -> {
                        validateResolvableType(parameter = parameter, type = type)

                        val tagType = Utils.getRawType(type)
                        for (index in parameter - 1 downTo 0) {

                            val otherHandler: ParameterHandler<*> = parameterHandlers[index]
                            if (otherHandler is ParameterHandler.Companion.Tag && otherHandler.cls == tagType)
                                throw Utils.parameterError(
                                    method = method,
                                    p = parameter,
                                    message = "@Tag type ${tagType.name} is duplicate of parameter # ${(index + 1)} and would always overwrite its value."
                                )
                        }

                        ParameterHandler.Companion.Tag(tagType)
                    }

                    else -> null // Not a Retrofit annotation.
                }

                Log.i("TAG55","PARSE PARAMETER ANNOTATION -> result: ${result}")

                return result
            }

            @Throws(IllegalArgumentException::class)
            private fun validateResolvableType(parameter:Int, type:Type?) {

                Log.i("TAG55","validateResolvableType function called!")

                val result = Utils.hasUnresolvableType(type)
                Log.i("TAG55","result: $result")

                if (Utils.hasUnresolvableType(type))
                    throw Utils.parameterError(
                        method = method,
                        p = parameter,
                        "Parameter type must not include a type variable or wildcard: $type"
                    )

            }

            private fun validatePathName(parameter:Int, name:String) {

                if (!PARAM_NAME_REGEX.matcher(name).matches())
                    throw Utils.parameterError(
                        method = method,
                        p = parameter,
                        message = "@Path parameter name must match ${PARAM_NAME_REGEX.pattern()}. Found: $name"
                    )

                relativeUrlParamNames?.let { relative_url_param_names ->

                    if (!relative_url_param_names.contains(name))
                        throw Utils.parameterError(
                            method = method,
                            p = parameter,
                            message = "URL \"$relativeUrl\" does not contain \"{$name}\"."
                        )
                }
            }

            private fun boxIfPrimitive(type: Class<*>): Class<*> {

                return when {
                    Boolean::class.javaPrimitiveType == type -> Boolean::class.java
                    Byte::class.javaPrimitiveType == type -> Byte::class.java
                    Char::class.javaPrimitiveType == type -> Char::class.java
                    Double::class.javaPrimitiveType == type -> Double::class.java
                    Float::class.javaPrimitiveType == type -> Float::class.java
                    Int::class.javaPrimitiveType == type -> Int::class.java
                    Long::class.javaPrimitiveType == type -> Long::class.java
                    Short::class.javaPrimitiveType == type -> Short::class.java
                    else -> type
                }
            }
        }
    }
}