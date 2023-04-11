package com.custom.http.client.annotation.util

@CustomRetention(java.lang.annotation.RetentionPolicy.CLASS)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CONSTRUCTOR, AnnotationTarget.TYPE,
    AnnotationTarget.CLASS
)
annotation class IgnoreJRERequirement
