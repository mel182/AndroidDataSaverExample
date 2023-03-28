package com.example.permissionhandlingguide

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel: ViewModel() {

    val visiblePermissionDialogQueue = mutableListOf<String>()

    fun dismissDialog(){
        visiblePermissionDialogQueue.removeFirst()
    }

    fun onPermissionResult(permission:String, isGranted:Boolean) {

        Log.i("TAG55","on permission result: ${permission} - is granted: ${isGranted}")

        if (!isGranted && !visiblePermissionDialogQueue.contains(permission)){
            Log.i("TAG55","permission added to list!")
            visiblePermissionDialogQueue.add(0,permission)
        }
    }

}