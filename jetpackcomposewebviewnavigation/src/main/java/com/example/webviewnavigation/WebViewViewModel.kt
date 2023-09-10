package com.example.webviewnavigation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class WebViewViewModel : ViewModel()
{
    private val _goback = MutableStateFlow(false)
    val goBack: StateFlow<Boolean>
    get() = _goback.asStateFlow()

    private val _goBackEnabled = MutableStateFlow(false)
    val goBackEnabled: StateFlow<Boolean>
        get() = _goBackEnabled.asStateFlow()

    fun goBack(goBack:Boolean) {
        _goback.value = goBack
    }

    fun goBackEnabled(enabled:Boolean) {
        _goBackEnabled.value = enabled
    }
}