package com.example.composedialog

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ViDialogExampleViewModel: ViewModel()
{
    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean>
        get() = _showDialog.asStateFlow()

    fun showDialog(show:Boolean) {
        _showDialog.value = show
    }
}