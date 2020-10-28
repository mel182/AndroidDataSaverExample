package com.example.datasaverexampleapp.handlers.activity_result_handler.entities

import android.content.Intent
import android.provider.ContactsContract
import com.example.datasaverexampleapp.handlers.activity_result_handler.constant.ActivityIntent
import com.example.datasaverexampleapp.handlers.activity_result_handler.interfaces.ActivityResultEntity

class PickContentIntent : ActivityResultEntity {

    override fun getAction(): String {
        return ActivityIntent.PICK_CONTACT
    }

    override fun activityIntent(): Intent {

        return Intent(getAction()).apply {
            type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
        }
    }
}