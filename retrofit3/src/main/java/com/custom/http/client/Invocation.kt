package com.custom.http.client

import java.lang.reflect.Method

class Invocation(val method:Method, val arguments:List<*>) {

    companion object {

        fun of(method:Method?, arguments:List<*>?): Invocation {
            requireNotNull(method) { "method == null" }
            requireNotNull(arguments) { "arguments == null" }
            return Invocation(method = method, arguments = arguments) // Defensive copy.
        }

    }

    override fun toString(): String  = "${method.declaringClass.name}.${method.name}() $arguments"
}