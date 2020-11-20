package com.example.datasaverexampleapp.handlers.activity_result_handler

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.datasaverexampleapp.handlers.activity_result_handler.constant.ActivityIntent
import com.example.datasaverexampleapp.handlers.activity_result_handler.interfaces.ActivityResultEntity
import com.example.datasaverexampleapp.handlers.activity_result_handler.entities.PickContentIntent
import com.example.datasaverexampleapp.handlers.activity_result_handler.entities.PickDirectoryIntent
import com.example.datasaverexampleapp.handlers.activity_result_handler.entities.StarSignIntent
import com.example.datasaverexampleapp.handlers.activity_result_handler.interfaces.OnActivityResultCallback

class ActivityForResultHandler(private val activity:Activity)
{
    private val requestCode = 145
    private var callback: OnActivityResultCallback? = null

    fun startActivityForResult(intent:String, callback: OnActivityResultCallback)
    {
        this.callback = callback

        when(intent)
        {
            ActivityIntent.PICK_CONTACT -> {
                startActivityForResult(PickContentIntent())
            }

            ActivityIntent.PICK_STAR_SIGN -> {
                startActivityForResult(StarSignIntent())
            }

            ActivityIntent.PICK_OPEN_DIRECTORY -> {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivityForResult(PickDirectoryIntent())
                } else {
                    callback.onActivityResult(AppCompatActivity.RESULT_CANCELED,null)
                }
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