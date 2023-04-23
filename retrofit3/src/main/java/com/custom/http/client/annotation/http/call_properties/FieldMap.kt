package com.custom.http.client.annotation.http.call_properties

import com.custom.http.client.constant.DEFAULT_BOOLEAN

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class FieldMap(
   /** Specifies whether the names and values are already URL encoded.  */
   val encoded: Boolean = DEFAULT_BOOLEAN
)
