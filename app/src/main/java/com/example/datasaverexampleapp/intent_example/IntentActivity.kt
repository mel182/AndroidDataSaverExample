package com.example.datasaverexampleapp.intent_example

import android.content.ContentUris
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.handlers.PermissionRequestHandler
import com.example.datasaverexampleapp.handlers.interfaces.RequestPermissionCallback
import com.example.datasaverexampleapp.handlers.permissions.Permission
import kotlinx.android.synthetic.main.activity_intent.*

class IntentActivity : IntentBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intent)

        intent_action_dial_button?.setOnClickListener {

            val phoneNumber = "0631564996"
            val callUri = Uri.parse("tel:${phoneNumber}")
            val callIntent = Intent(Intent.ACTION_DIAL,callUri)
            startActivity(callIntent)
        }

        intent_action_call_button?.setOnClickListener {
            // Remember add:
            // <uses-permission android:name="android.permission.CALL_PHONE"></uses-permission>

            requestPermission(Permission.CALL_PHONE, object : RequestPermissionCallback{

                override fun onPermissionGranted() {
                    val phoneNumber = "0631564996"
                    val callUri = Uri.parse("tel:${phoneNumber}")
                    val callIntent = Intent(Intent.ACTION_CALL, callUri)
                    startActivity(callIntent)
                }

                override fun onPermissionDenied() {
                    Toast.makeText(this@IntentActivity, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            })
        }

        insert_or_edit_button?.setOnClickListener {

            // The code below should start an activity that lets you pick a contact to edit
            // with the "vrol0004@gmail.com" inserted as a new email, or let you create a
            // new contact with "vrol0004@gmail.com" inserted as the email.

            val intent = Intent(Intent.ACTION_INSERT_OR_EDIT).apply {
                type = ContactsContract.Contacts.CONTENT_ITEM_TYPE
                putExtra(ContactsContract.Intents.Insert.EMAIL,"vrol0004@gmail.com")
            }
            startActivity(intent)
        }

        edit_contact_button?.setOnClickListener {

            // The below code read contact from the phone and let you edit them

            requestPermission(Permission.READ_CONTACT, object : RequestPermissionCallback {
                override fun onPermissionGranted() {

                    val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                    val cursor = applicationContext.contentResolver.query(
                        uri,
                        null,
                        null,
                        null,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                    )

                    cursor?.let {

                        var idContact = 0L
                        while (it.moveToNext()) {
                            idContact =
                                it.getLong(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID))
                        }

                        val intent = Intent(Intent.ACTION_EDIT)
                        val contactUri = idContact.let { id ->
                            ContentUris.withAppendedId(
                                ContactsContract.Contacts.CONTENT_URI,
                                id
                            )
                        }

                        intent.data = contactUri
                        intent.putExtra("finishActivityOnSaveCompleted", true)
                        startActivity(intent)
                    }
                }

                override fun onPermissionDenied() {
                    Toast.makeText(this@IntentActivity, "Read contact permission denied", Toast.LENGTH_SHORT).show()
                }
            })


        }
    }
}