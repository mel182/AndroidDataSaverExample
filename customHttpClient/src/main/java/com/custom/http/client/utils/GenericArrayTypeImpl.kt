package com.custom.http.client.utils

import java.lang.reflect.GenericArrayType
import java.lang.reflect.Type

class GenericArrayTypeImpl(private val componentType: Type): GenericArrayType {

    override fun getGenericComponentType(): Type = componentType

    override fun equals(other: Any?): Boolean = other is GenericArrayType && Utils.equals(this, other)

    override fun hashCode(): Int = componentType.hashCode()

    override fun toString(): String = "${Utils.typeToString(componentType)}[]"

}