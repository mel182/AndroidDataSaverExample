package com.custom.http.client.utils

import java.lang.reflect.Type
import java.lang.reflect.WildcardType

class WildcardTypeImpl(private val upperBounds: Array<Type?>, private val lowerBounds: Array<Type?>): WildcardType {

    private var upper_bound:Type? = null
    private var lower_bound:Type? = null

    init {

        if (lowerBounds.size > 1)
            throw IllegalArgumentException()

        if (upperBounds.size != 1)
            throw IllegalArgumentException()

        if (lowerBounds.size == 1) {

            if (lowerBounds[0] == null)
                throw NullPointerException()

            Utils.checkNotPrimitive(lowerBounds[0])
            lower_bound = lowerBounds[0]

            if (upperBounds[0] != Any::class.java)
                throw IllegalArgumentException()

            upper_bound = Any::class.java

        } else {

            if (upperBounds[0] == null)
                throw NullPointerException()

            Utils.checkNotPrimitive(upperBounds[0])

            lower_bound = null
            upper_bound = upperBounds[0]
        }
    }

    override fun getUpperBounds(): Array<Type?> = upperBounds

    override fun getLowerBounds(): Array<Type?> = lowerBounds

    override fun equals(other: Any?): Boolean = other is WildcardType && Utils.equals(this, other)

    override fun hashCode(): Int {
        // This equals Arrays.hashCode(getLowerBounds()) ^ Arrays.hashCode(getUpperBounds()).
        return (if (lower_bound != null) 31 + lower_bound.hashCode() else 1) xor 31 + upper_bound.hashCode()
    }

    override fun toString(): String {
        if (lower_bound != null)
            return "? super ${Utils.typeToString(lower_bound)}"

        if (upper_bound == Any::class.java)
            return "?"

        return "? extends ${Utils.typeToString(upper_bound)}"
    }

}