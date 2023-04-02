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
        ): java.lang.RuntimeException? {
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


    }



}