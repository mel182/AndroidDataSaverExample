package com.custom.http.client.utils

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ParameterizedTypeImpl(private val ownerType: Type? = null, private val rawType: Type, private val typeArguments: Array<Type>) : ParameterizedType {

    init {
        // Require an owner type if the raw type needs it.
        require(!(rawType is Class<*> && ownerType == null != (rawType.enclosingClass == null)))

        for (typeArgument in typeArguments) {
            Utils.checkNotPrimitive(typeArgument)
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