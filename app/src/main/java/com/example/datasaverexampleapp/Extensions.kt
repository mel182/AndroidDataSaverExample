package com.example.datasaverexampleapp

import android.view.View

fun View?.onScroll(callback: (x: Int, y: Int) -> Unit) {
    var currentXCoordinate = 0
    var currentYCoordinate = 0
    this?.viewTreeObserver?.addOnScrollChangedListener {
        if (currentXCoordinate != scrollX || currentYCoordinate != scrollY) {
            callback(scrollX, scrollY)
            currentXCoordinate = scrollX
            currentYCoordinate = scrollY
        }
    }
}