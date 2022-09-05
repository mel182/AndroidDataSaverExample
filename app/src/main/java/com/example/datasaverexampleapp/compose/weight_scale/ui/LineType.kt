package com.example.datasaverexampleapp.compose.weight_scale.ui

sealed class LineType {
    object Normal : LineType()
    object FiveStep : LineType()
    object TenStep : LineType()
}
