package com.custom.http.client

import com.custom.http.client.constant.BLANK_STRING
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.asResponseBody
import okio.Buffer
import java.io.IOException
import java.lang.reflect.*
import java.util.*

class Utils {

    companion object {

        val EMPTY_TYPE_ARRAY:Array<Type> = emptyArray()

        fun methodError(
            method: Method?,
            message: String?,
            vararg args: Any?
        ): java.lang.RuntimeException = methodError(method = method, cause = null, message = message?: BLANK_STRING, args = args)


        fun methodError(method: Method?, cause: Throwable?, message: String, vararg args: Any?): RuntimeException {

            val refineMessage = String.format(message, *args)
            return IllegalArgumentException("$refineMessage\n     for method ${method?.let { it::getDeclaringClass::class.java.simpleName }?: BLANK_STRING}.${method?.name}"
                , cause)
        }

        fun parameterError(method: Method?, cause: Throwable?, p: Int, message: String, vararg args: Any?): java.lang.RuntimeException {
            return methodError(
                method,
                cause,
                "$message (parameter # ${(p + 1)})",
                *args
            )
        }

        fun parameterError(
            method: Method?,
            p: Int,
            message: String,
            vararg args: Any?
        ): java.lang.RuntimeException {
            return methodError(
                method,
                "$message (parameter # ${(p + 1)})",
                *args
            )
        }


        fun hasUnresolvableType(type: Type?): Boolean {
            if (type is Class<*>) {
                return false
            }
            if (type is ParameterizedType) {
                for (typeArgument: Type? in type.actualTypeArguments) {
                    if (hasUnresolvableType(typeArgument)) {
                        return true
                    }
                }
                return false
            }
            if (type is GenericArrayType) {
                return hasUnresolvableType(type.genericComponentType)
            }
            if (type is TypeVariable<*>) {
                return true
            }
            if (type is WildcardType) {
                return true
            }
            val className = if (type == null) "null" else type.javaClass.name
            throw java.lang.IllegalArgumentException(
                "Expected a Class, ParameterizedType, or "
                        + "GenericArrayType, but <"
                        + type
                        + "> is of type "
                        + className
            )
        }

        fun getParameterUpperBound(index: Int, type: ParameterizedType?): Type {

            require(type != null) { "type == null" }

            val types = type.actualTypeArguments
            require(!(index < 0 || index >= types.size)) { "Index $index not in range [0,${types.size}) for $type" }
            val paramType = types[index]
            return if (paramType is WildcardType) {
                paramType.upperBounds[0]
            } else paramType
        }
        fun getParameterLowerBound(index: Int, type: ParameterizedType): Type {
            val paramType = type.actualTypeArguments[index]
            return if (paramType is WildcardType) {
                paramType.lowerBounds[0]
            } else paramType
        }

        private var checkForKotlinUnit = true

        fun isUnit(type: Type?): Boolean {

            return if (checkForKotlinUnit) {
                try {
                    type === Unit::class.java
                }catch (ignored: NoClassDefFoundError) {
                    checkForKotlinUnit = false
                    false
                }
            } else false
        }

        fun isAnnotationPresent(annotations: Array<Annotation>, cls: Class<out Annotation?>): Boolean {
            for (annotation in annotations) {
                if (cls.isInstance(annotation)) {
                    return true
                }
            }
            return false
        }

        fun getRawType(type: Type?): Class<*> {

            requireNotNull(type) {"type == null"}

            if (type is Class<*>)  // Type is a normal class.
                return type

            if (type is ParameterizedType) {

                // I'm not exactly sure why getRawType() returns Type instead of Class. Neal isn't either but
                // suspects some pathological case related to nested classes exists.
                val rawType = type.rawType
                if (rawType !is Class<*>) throw java.lang.IllegalArgumentException()
                return rawType
            }

            if (type is GenericArrayType) {
                val componentType = type.genericComponentType
                return java.lang.reflect.Array.newInstance(getRawType(componentType), 0).javaClass
            }

            if (type is TypeVariable<*>) {
                // We could use the variable's bounds, but that won't work if there are multiple. Having a raw
                // type that's more general than necessary is okay.
                return Any::class.java
            }

            if (type is WildcardType) {
                return getRawType(type.upperBounds[0])
            }

            throw java.lang.IllegalArgumentException("Expected a Class, ParameterizedType, " +
                    "or GenericArrayType, but <$type> is " +
                    "of type ${type.javaClass.name}"
            )
        }

        // https://github.com/ReactiveX/RxJava/blob/6a44e5d0543a48f1c378dc833a155f3f71333bc2/
        // src/main/java/io/reactivex/exceptions/Exceptions.java#L66
        fun throwIfFatal(t: Throwable) {
            when (t) {
                is VirtualMachineError -> throw (t as VirtualMachineError?)!!
                is ThreadDeath -> throw (t as ThreadDeath?)!!
                is LinkageError -> throw (t as LinkageError?)!!
            }
        }

        fun getSupertype(context: Type?, contextRawType: Class<*>?, supertype: Class<*>): Type {
            require(supertype.isAssignableFrom(contextRawType))
            return resolve(
                context,
                contextRawType,
                getGenericSupertype(context, contextRawType, supertype)
            )
        }

        fun checkNotPrimitive(type: Type?) {
            require(!(type is Class<*> && type.isPrimitive))
        }

        fun equals(a: Type?, b: Type?): Boolean {

            return when {

                a == b -> true
                a is Class<*> -> a == b
                a is ParameterizedType -> {
                    if (b !is ParameterizedType)
                        false

                    val parameterizedB = b as ParameterizedType
                    val ownerA = a.ownerType
                    val ownerB = parameterizedB.ownerType

                    return ownerA === ownerB || ownerA != null
                            && ownerA == ownerB
                            && parameterizedB.rawType == parameterizedB.rawType
                            && Arrays.equals(a.actualTypeArguments, a.actualTypeArguments)
                }
                a is GenericArrayType -> {
                    if (b !is GenericArrayType)
                        false

                    val genericTypeB = b as GenericArrayType
                    equals(a.genericComponentType, genericTypeB.genericComponentType)
                }
                a is WildcardType -> {
                    if (b !is WildcardType)
                        false

                    val wildcardTypeB = b as WildcardType

                    Arrays.equals(a.upperBounds, wildcardTypeB.upperBounds) &&
                            Arrays.equals(a.lowerBounds, wildcardTypeB.lowerBounds)
                }
                a is TypeVariable<*> -> {
                    if (b !is TypeVariable<*>)
                        false

                    val typeVariableB = b as TypeVariable<*>
                    a.genericDeclaration == typeVariableB.genericDeclaration && a.name.equals(typeVariableB.name)
                }
                else -> false
            }
        }

        @Throws(IOException::class)
        fun buffer(body: ResponseBody): ResponseBody {
            val buffer = Buffer()
            body.source().readAll(buffer)
            return buffer.asResponseBody(body.contentType(), body.contentLength())
        }


        internal class ParameterizedTypeImpl(private val ownerType: Type? = null, private val rawType:Type, private val typeArguments: Array<Type>) : ParameterizedType {

            init {
                // Require an owner type if the raw type needs it.
                require(!(rawType is Class<*> && ownerType == null != (rawType.enclosingClass == null)))

                for (typeArgument in typeArguments) {
                    checkNotPrimitive(typeArgument)
                }
            }

            override fun getActualTypeArguments(): Array<Type>  = this.typeArguments.clone()

            override fun getRawType(): Type = rawType

            override fun getOwnerType(): Type? = ownerType

            override fun equals(other: Any?): Boolean {

                if (other !is ParameterizedType)
                    return false

                return Utils.equals(
                    this,
                    other as ParameterizedType
                )
            }

        }


        fun resolve(context: Type?, contextRawType: Class<*>?, toResolve: Type?): Type {
            TODO("Need to be implemented")
        }

        fun getGenericSupertype(context: Type?, rawType: Class<*>?, toResolve: Class<*>?): Type? {
            TODO("Need to be implemented")
        }


    }



}