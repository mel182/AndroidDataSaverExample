package com.custom.http.client

import com.custom.http.client.annotation.http.HTTP
import com.custom.http.client.annotation.http.call_properties.FormUrlEncoded
import com.custom.http.client.annotation.http.call_properties.Header
import com.custom.http.client.annotation.http.call_properties.Multipart
import com.custom.http.client.annotation.http.method.*
import com.custom.http.client.constant.BLANK_STRING
import com.custom.http.client.constant.DEFAULT_BOOLEAN
import okhttp3.Headers
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import java.io.IOException
import java.lang.reflect.Method
import java.lang.reflect.Type
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.coroutines.Continuation

class RequestFactory(private val builder: Builder) {


    private var parameterHandlers: Array<ParameterHandler<*>> = emptyArray()

    companion object {
        fun parseAnnotations(retrofit3: Retrofit3, method:Method): RequestFactory {
            return RequestFactory()
        }

        class Builder(private val retrofit3: Retrofit3, private val method: Method) {

            private val PARAM = "[a-zA-Z][a-zA-Z0-9_-]*"
            private val PARAM_URL_REGEX = Pattern.compile("\\{($PARAM)\\}")
            private val PARAM_NAME_REGEX = Pattern.compile(PARAM)
            private var methodAnnotations: Array<Annotation> = emptyArray()
            private var parameterAnnotationsArray: Array<Array<Annotation>> = emptyArray()
            private var parameterTypes: Array<Type> = emptyArray()

            private var gotField:Boolean = DEFAULT_BOOLEAN
            private var gotPart:Boolean = DEFAULT_BOOLEAN
            private var gotBody:Boolean = DEFAULT_BOOLEAN
            private var gotPath:Boolean = DEFAULT_BOOLEAN
            private var gotQuery:Boolean = DEFAULT_BOOLEAN
            private var gotQueryName:Boolean = DEFAULT_BOOLEAN
            private var gotQueryMap:Boolean = DEFAULT_BOOLEAN
            private var gotUrl:Boolean = DEFAULT_BOOLEAN
            var httpMethod:String = BLANK_STRING
            private set

            private var hasBody:Boolean = DEFAULT_BOOLEAN
            private var isFormEncoded:Boolean = DEFAULT_BOOLEAN
            private var isMultiPart:Boolean = DEFAULT_BOOLEAN

            private var relativeUrl:String = BLANK_STRING
            private var headers:Headers? = null
            private var contentType: MediaType? = null
            var relativeUrlParamNames: Set<String>? = null
            private set

            var parameterHandlers: Array<ParameterHandler<*>> = emptyArray()
            private var isKotlinSuspendFunction:Boolean = DEFAULT_BOOLEAN

            init {
                this.methodAnnotations = method.annotations
                this.parameterTypes = method.genericExceptionTypes
                this.parameterAnnotationsArray = method.parameterAnnotations
            }

            fun build(): RequestFactory {

                for (methodAnnotationFound in this.methodAnnotations) {
                    parseMethodAnnotation(methodAnnotationFound)
                }

                if (this.httpMethod.isBlank())
                    throw Utils.methodError(method,"HTTP method annotation is required (e.g., @GET, @POST, etc.).")

                if (!hasBody) {

                    if (isMultiPart)
                        throw Utils.methodError(method,"Multipart can only be specified on HTTP methods with request body (e.g., @POST).")

                    if (isFormEncoded)
                        throw Utils.methodError(method,"FormUrlEncoded can only be specified on HTTP methods with request body (e.g., @POST).")
                }

                val tempArrayList = ArrayList<ParameterHandler<*>>()
                for (index in parameterAnnotationsArray.indices) {
                    parseParameter(parameter = index, parameterTypes[index], parameterAnnotationsArray[index], index == parameterAnnotationsArray.indices.last)?.let {
                        tempArrayList.add(
                            it
                        )
                    }
                }
                parameterHandlers = tempArrayList.toTypedArray()

                if (this.relativeUrl.isBlank() && !gotUrl)
                    throw Utils.methodError(method,"Missing either $httpMethod URL or @Url parameter")

                if (!isFormEncoded && !isMultiPart && !hasBody && gotBody)
                    throw Utils.methodError(method,"Non-body HTTP method cannot contain @Body.")

                if (isFormEncoded && !gotField)
                    throw Utils.methodError(method,"Form-encoded method must contain at least one @Field.")

                if (isMultiPart && !gotPart)
                    throw Utils.methodError(method,"Multipart method must contain at least one @Part.")

                return RequestFactory(this)
            }

            private fun parseMethodAnnotation(annotation:Annotation) {

                when(annotation) {

                    is DELETE -> parseHttpMethodAndPath(httpMethod = "DELETE", value = annotation.value, hasBody = false)
                    is GET -> parseHttpMethodAndPath(httpMethod = "GET", value = annotation.value, hasBody = false)
                    is HEAD -> parseHttpMethodAndPath(httpMethod = "HEAD", value = annotation.value, hasBody = false)
                    is PATCH -> parseHttpMethodAndPath(httpMethod = "PATCH", value = annotation.value, hasBody = true)
                    is POST -> parseHttpMethodAndPath(httpMethod = "POST", value = annotation.value, hasBody = true)
                    is PUT -> parseHttpMethodAndPath(httpMethod = "PUT", value = annotation.value, hasBody = true)
                    is OPTIONS -> parseHttpMethodAndPath(httpMethod = "OPTIONS", value = annotation.value, hasBody = false)
                    is HTTP -> parseHttpMethodAndPath(httpMethod = annotation.method, value = annotation.path, hasBody = annotation.hasBody)
                    is Header -> {
                        val headersToParse = annotation.value
                        if (headersToParse.isEmpty())
                            throw Utils.methodError(method,"@Headers annotation is empty")

                        this.headers = parseHeaders(headersToParse, annotation.allowUnsafeNonAsciiValues)
                    }
                    is Multipart -> {
                        if (isFormEncoded)
                            throw Utils.methodError(method,"Only one encoding annotation is allowed")
                        isMultiPart = true
                    }
                    is FormUrlEncoded -> {
                        if (isMultiPart)
                            throw Utils.methodError(method,"Only one encoding annotation is allowed")
                        isFormEncoded = true
                    }
                    else -> {}
                }
            }

            private fun parseHttpMethodAndPath(httpMethod:String, value:String, hasBody:Boolean) {

                if (this.httpMethod.isNotBlank())
                    throw Utils.methodError(method,"Only one HTTP is allowed, Found: ${this.httpMethod} and $httpMethod")

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
                        throw Utils.methodError(method,"URL query string \"%s\" must not have replace block. For dynamic query parameters use @Query.")

                }

                this.relativeUrl = value
                this.relativeUrlParamNames = parsePathParameters(value)
            }

            private fun parseHeaders(headers: Array<String>, allowUnsafeNonAsciiValues:Boolean) : Headers {

                return Headers.Builder().apply {

                    for (header in headers) {
                        val colon = header.indexOf(':')
                        if (colon == -1 || colon == 0 || colon == header.length - 1)
                            throw Utils.methodError(method,"@Headers value must be in the form \"Name: Value\". Found: \"$header\"")

                        val headerName = header.substring(0,colon)
                        val headerValue = header.substring(colon + 1).trim()

                        when {

                            headerName == "Content-Type" -> {
                                try{
                                    contentType = headerValue.toMediaType()
                                }catch (e:IllegalArgumentException) {
                                    throw Utils.methodError(method,e,"Malformed content type: $headerValue")
                                }
                            }

                            allowUnsafeNonAsciiValues -> addUnsafeNonAscii(headerName, headerValue)

                            else -> add(headerName,headerValue)
                        }
                    }
                }.build()
            }

            private fun parseParameter(parameter: Int, parameterType: Type, annotations: Array<Annotation> = emptyArray(), allowContinuation: Boolean) : ParameterHandler<*>? {

                var result: ParameterHandler<*>? = null

                for (annotation in annotations) {

                    val annotationAction: ParameterHandler<*>? = parseParameterAnnotation(parameter = parameter,
                        type = parameterType,
                        annotations = annotations,
                        annotation = annotation
                    ) ?: continue

                    if (result != null)
                        throw Utils.parameterError(method,parameter,"Multiple Retrofit annotations found, only one allowed.")

                    result = annotationAction
                }

                if (result == null) {
                    if (allowContinuation) {
                        try {
                            if (Utils.getRawType(parameterType) == Continuation::class.java) {
                                isKotlinSuspendFunction = true
                                return null
                            }
                        }catch (ignored: NoClassDefFoundError) { }
                    }
                    throw Utils.parameterError(method,parameter,"No Retrofit annotation found.")
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

            private fun parseParameterAnnotation(parameter: Int, type: Type, annotations: Array<Annotation>, annotation: Annotation): ParameterHandler<*>? {

                TODO("Not implemented yet")
            }

        }


    }

    @Throws(IOException::class)
    fun create(args: Array<Any>): Request {
        //val handlers:  Array<ParameterHandler<Any>> = parameterHandlers as Array<ParameterHandler<Any>>

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
        return requestBuilder.get()
            .tag<Invocation>(Invocation::class.java, Invocation(method, argumentList)).build()
    }

}