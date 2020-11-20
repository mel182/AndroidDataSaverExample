package com.example.datasaverexampleapp.handlers.activity_result_handler.entities

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.datasaverexampleapp.handlers.activity_result_handler.interfaces.ActivityResultEntity

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class PickDirectoryIntent : ActivityResultEntity
{
    override fun getAction(): String {
        return Intent.ACTION_OPEN_DOCUMENT_TREE
    }

    override fun activityIntent(): Intent {
        return Intent(getAction())
    }
}