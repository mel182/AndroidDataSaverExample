package com.jetpackcompose.deeplink

import androidx.lifecycle.ViewModel

class DeeplinkTestViewModel: ViewModel() {

    val visiblePermissionDialogQueue = mutableListOf<String>()

    fun dismissDialog(){
        visiblePermissionDialogQueue.removeFirst()
    }

    fun onPermissionResult(permission:String, isGranted:Boolean) {

        if (!isGranted && !visiblePermissionDialogQueue.contains(permission)){
            visiblePermissionDialogQueue.add(0,permission)
        }
    }
}