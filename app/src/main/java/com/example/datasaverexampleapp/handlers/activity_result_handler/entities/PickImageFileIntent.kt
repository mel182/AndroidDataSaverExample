package com.example.datasaverexampleapp.handlers.activity_result_handler.entities

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.datasaverexampleapp.handlers.activity_result_handler.interfaces.ActivityResultEntity

@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
class PickImageFileIntent(var chooserTitle:String = "Select picture") : ActivityResultEntity {

    override fun getAction(): String {
        return Intent.ACTION_GET_CONTENT
    }

    override fun activityIntent(): Intent {
        val intent = Intent()
        intent.type = getType()
        intent.action = getAction()
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)
        return Intent.createChooser(intent,chooserTitle)
    }

    override fun getType(): String {
        return "image/*"
    }
}