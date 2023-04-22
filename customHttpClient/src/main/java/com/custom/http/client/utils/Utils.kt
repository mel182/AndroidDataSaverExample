package com.custom.http.client.utils

import com.custom.http.client.constant.BLANK_STRING
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.asResponseBody
import okio.Buffer
import java.io.IOException
import java.lang.reflect.*
import java.util.*

class Utils {

    companion object {

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
            requireNotNull(context){ "get supertype context == null" }
            requireNotNull(contextRawType){ "get supertype contextRawType == null" }
            require(supertype.isAssignableFrom(contextRawType))

            return resolve(
                context,
                contextRawType,
                getGenericSupertype(context, contextRawType, supertype)!!
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

        fun resolve(context: Type, contextRawType: Class<*>, toResolve: Type): Type {
            // This implementation is made a little more complicated in an attempt to avoid object-creation.
            while (true) {
                if (toResolve is TypeVariable<*>) {

                    val to_resolve = resolveTypeVariable(context = context, contextRawType = contextRawType, unknown = toResolve)

                    if (to_resolve == toResolve)
                        return to_resolve

                } else if (toResolve is Class<*> && toResolve.isArray) {
                    val original = toResolve
                    val componentType: Type = original.componentType
                    val newComponentType: Type = resolve(context, contextRawType, componentType)
                    return if (componentType === newComponentType) original else GenericArrayTypeImpl(
                        newComponentType
                    )
                } else if (toResolve is GenericArrayType) {
                    val original = toResolve
                    val componentType = original.genericComponentType
                    val newComponentType: Type = resolve(context, contextRawType, componentType)
                    return if (componentType === newComponentType) original else GenericArrayTypeImpl(newComponentType)
                } else if (toResolve is ParameterizedType) {
                    val original = toResolve
                    val ownerType = original.ownerType
                    val newOwnerType: Type = resolve(context, contextRawType, ownerType)
                    var changed = newOwnerType !== ownerType
                    var args = original.actualTypeArguments
                    var t = 0
                    val length = args.size
                    while (t < length) {
                        val resolvedTypeArgument: Type = resolve(context, contextRawType, args[t])
                        if (resolvedTypeArgument !== args[t]) {
                            if (!changed) {
                                args = args.clone()
                                changed = true
                            }
                            args[t] = resolvedTypeArgument
                        }
                        t++
                    }

                    return if (changed)
                        ParameterizedTypeImpl(newOwnerType, original.rawType, args)
                    else
                        original

                } else if (toResolve is WildcardType) {
                    val original = toResolve
                    val originalLowerBound = original.lowerBounds
                    val originalUpperBound = original.upperBounds
                    if (originalLowerBound.size == 1) {
                        val lowerBound: Type = resolve(
                            context, contextRawType,
                            originalLowerBound[0]
                        )
                        if (lowerBound !== originalLowerBound[0]) {
                            return WildcardTypeImpl(
                                arrayOf(Any::class.java),
                                arrayOf(lowerBound)
                            )
                        }
                    } else if (originalUpperBound.size == 1) {
                        val upperBound: Type = resolve(
                            context, contextRawType,
                            originalUpperBound[0]
                        )
                        if (upperBound !== originalUpperBound[0]) {
                            return WildcardTypeImpl(
                                arrayOf(upperBound),
                                emptyArray()
                            )
                        }
                    }
                    return original
                } else {
                    return toResolve
                }
            }
        }

        private fun resolveTypeVariable(context: Type, contextRawType: Class<*>, unknown: TypeVariable<*>): Type? {

            // If class is null, we can't reduce this further.
            val declaredByRaw: Class<*> = declaringClassOf(unknown) ?: return unknown
            val declaredBy: Type? = getGenericSupertype(context, contextRawType, declaredByRaw)

            if (declaredBy is ParameterizedType) {
                val index: Int = indexOf(declaredByRaw.typeParameters, unknown)
                return declaredBy.actualTypeArguments[index]
            }
            return unknown
        }

        /**
         * Returns the declaring class of {@code typeVariable}, or {@code null} if it was not declared by
         * a class.
         */
        private fun declaringClassOf(typeVariable: TypeVariable<*>): Class<*>? {
            val genericDeclaration = typeVariable.genericDeclaration
            return if (genericDeclaration is Class<*>) genericDeclaration else null
        }

        fun getGenericSupertype(context: Type?, rawType: Class<*>?, toResolve: Class<*>): Type? {

            if (toResolve == rawType)
                return context

            // We skip searching through interfaces if unknown is an interface.
            rawType?.apply {

                if (toResolve.isInterface) {

                    for (clazzItemIndex in interfaces.indices) {
                        val clazzFound = interfaces[clazzItemIndex]
                        if (clazzFound == toResolve) {
                            return genericInterfaces[clazzItemIndex]
                        } else if (toResolve.isAssignableFrom(clazzFound)) {
                            return getGenericSupertype(genericInterfaces[clazzItemIndex], interfaces[clazzItemIndex], toResolve)
                        }
                    }
                }

                // Check our supertypes.
                if (!isInterface) {

                    while (this != Any::class.java) {

                        val rawSuperType = superclass
                        if (rawSuperType == toResolve) {
                            return genericSuperclass
                        } else if (toResolve.isAssignableFrom(rawSuperType)) {
                            return getGenericSupertype(genericSuperclass, rawSuperType, toResolve)
                        }
                    }
                }
            }

            // We can't resolve this further.
            return toResolve
        }

        private fun indexOf(array: Array<*>?, toFind: Any): Int {

            val itemArray = array ?: Array(0){}
            for (i in itemArray.indices) {
                if (toFind == itemArray[i]) return i
            }

            throw NoSuchElementException()
        }

        fun typeToString(type: Type?): String? = if (type is Class<*>) type.name else type.toString()

    }
}