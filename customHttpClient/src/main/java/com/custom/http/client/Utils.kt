package com.custom.http.client

import com.custom.http.client.constant.BLANK_STRING
import java.lang.reflect.*

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

        fun getParameterUpperBound(index: Int, type: ParameterizedType?): Type? {

            require(type != null) { "type == null" }

            val types = type.actualTypeArguments
            require(!(index < 0 || index >= types.size)) { "Index $index not in range [0,${types.size}) for $type" }
            val paramType = types[index]
            return if (paramType is WildcardType) {
                paramType.upperBounds[0]
            } else paramType
        }

        fun getRawType(type: Type?): Class<*>? {

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



    }



}