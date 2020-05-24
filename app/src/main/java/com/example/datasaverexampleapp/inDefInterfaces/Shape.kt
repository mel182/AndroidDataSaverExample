package com.example.datasaverexampleapp.inDefInterfaces

import androidx.annotation.IntDef

//An alternative for memory efficiency for enumerations.
// Since enums consume a lof of resources since it has to it create an object for each enum option.
// To make matters worse, the enumeration needs to replicate itself in every process the app is using it.
// Such operation cost a lot of memory resource in a multiprocess application.

// Android provides a useful annotation to simplify the transition from enumeration to integer values, namely: @IntDef
// @IntDef(flag = true, value = {value1, value2, value3})
// public @interface MODE { }

@IntDef(
    Constants.RECTANGLE,
    Constants.TRIANGLE,
    Constants.SQUARE,
    Constants.CIRCLE
)
annotation class Shape