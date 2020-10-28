package com.example.datasaverexampleapp.handlers.activity_result_handler

import android.app.Activity
import android.content.Intent
import com.example.datasaverexampleapp.handlers.activity_result_handler.constant.ActivityIntent
import com.example.datasaverexampleapp.handlers.activity_result_handler.interfaces.ActivityResultEntity
import com.example.datasaverexampleapp.handlers.activity_result_handler.entities.PickContentIntent
import com.example.datasaverexampleapp.handlers.activity_result_handler.interfaces.OnActivityResultCallback

class ActivityForResultHandler(private val activity:Activity)
{
    private val requestCode = 145
    private var callback: OnActivityResultCallback? = null

    fun startActivityForResult(intent:String,callback: OnActivityResultCallback)
    {
        this.callback = callback

        when(intent)
        {
            ActivityIntent.PICK_CONTACT -> {
                startActivityForResult(PickContentIntent())
            }
        }
    }

    private fun startActivityForResult(intent: ActivityResultEntity)
    {
        activity.startActivityForResult(intent.activityIntent(),requestCode)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        if (requestCode == requestCode)
            callback?.onActivityResult(resultCode,data)
    }
}