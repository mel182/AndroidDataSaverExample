package com.jetpackcompose.zoomableimage

fun Set<Int>.addOrRemoveUpTo(
    pointerKey: Int?,
    previousPointerKey: Int?,
    initialKey: Int?
): Set<Int> = if (pointerKey == null || previousPointerKey == null || initialKey == null) {
    this
} else {
    this
        .minus(initialKey..previousPointerKey)
        .minus(previousPointerKey..initialKey)
        .plus(initialKey..pointerKey)
        .plus(pointerKey..initialKey)
}