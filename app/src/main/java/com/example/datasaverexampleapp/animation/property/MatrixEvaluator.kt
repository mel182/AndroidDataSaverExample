package com.example.datasaverexampleapp.animation.property

import android.animation.TypeEvaluator
import android.graphics.Matrix

class MatrixEvaluator : TypeEvaluator<Matrix>
{
    override fun evaluate(fraction: Float, startValue: Matrix?, endValue: Matrix?): Matrix {
        val startEntries = FloatArray(9)
        val endEntries = FloatArray(9)
        val currentEntries = FloatArray(9)

        startValue?.getValues(startEntries)
        endValue?.getValues(endEntries)

        for (index in 0..8)
        {
            currentEntries[index] = (1-fraction)*startEntries[index] + fraction*endEntries[index]
        }

        return Matrix().apply {
            setValues(currentEntries)
        }
    }

}