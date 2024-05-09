package com.jetpackcompose.circularindicatordraggable.feature

import kotlin.reflect.KProperty

internal interface ReadOnlyProperty<in R, out T>
{
    operator fun getValue(thisRef: R, property: KProperty<*>):T
}