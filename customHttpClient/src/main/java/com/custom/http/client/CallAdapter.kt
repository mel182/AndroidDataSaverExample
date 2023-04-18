package com.custom.http.client

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

interface CallAdapter<R,T> {

    fun responseType(): Type?

    fun adapt(call: Call<R>?): T

    abstract class Factory {

        /**
         * Returns a call adapter for interface methods that return {@code returnType}, or null if it
         * cannot be handled by this factory.
         */
        abstract operator fun get(returnType: Type?, annotations: Array<Annotation>?, retrofit: Retrofit3?): CallAdapter<*, *>?

        /**
         * Extract the upper bound of the generic parameter at {@code index} from {@code type}. For
         * example, index 1 of {@code Map<String, ? extends Runnable>} returns {@code Runnable}.
         */
        protected open fun getParameterUpperBound(index: Int, type: ParameterizedType?): Type? = Utils.getParameterUpperBound(index, type)

        /**
         * Extract the raw class type from {@code type}. For example, the type representing {@code
         * List<? extends Runnable>} returns {@code List.class}.
         */
        protected open fun getRawType(type: Type?): Class<*>? = Utils.getRawType(type)

    }

}