package com.custom.http.client.annotation.internal

import androidx.annotation.NonNull
import java.lang.annotation.ElementType


@NonNull
@TypeQualifierDefault([ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER])
@Retention(AnnotationRetention.RUNTIME)
annotation class EverythingIsNonNull
