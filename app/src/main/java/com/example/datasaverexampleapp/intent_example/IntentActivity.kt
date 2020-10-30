package com.example.datasaverexampleapp.intent_example

import android.app.SearchManager
import android.content.ContentUris
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.handlers.activity_result_handler.constant.ActivityIntent
import com.example.datasaverexampleapp.handlers.activity_result_handler.interfaces.OnActivityResultCallback
import com.example.datasaverexampleapp.handlers.permission.interfaces.RequestPermissionCallback
import com.example.datasaverexampleapp.handlers.permission.permissions.Permission
import com.example.datasaverexampleapp.intent_example.starSignPicker.StarSignPickerActivity
import kotlinx.android.synthetic.main.activity_intent.*
import java.util.*


class IntentActivity : IntentBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intent)

        title = "Intent Activity"

        // Intent to open the phone dialer
        intent_action_dial_button?.setOnClickListener {

            val phoneNumber = "0631564996"
            val callUri = Uri.parse("tel:${phoneNumber}")
            val callIntent = Intent(Intent.ACTION_DIAL, callUri)
            startActivity(callIntent)
        }

        // Intent to make a phone call
        intent_action_call_button?.setOnClickListener {
            // Remember add:
            // <uses-permission android:name="android.permission.CALL_PHONE"></uses-permission>

            // Check for call phone permission through the permission handler
            requestPermission(Permission.CALL_PHONE, object : RequestPermissionCallback {

                override fun onPermissionGranted() {
                    // Make phone call by the number provided
                    val phoneNumber = "0631564996"
                    val callUri = Uri.parse("tel:${phoneNumber}")
                    val callIntent = Intent(Intent.ACTION_CALL, callUri)
                    startActivity(callIntent)
                }

                override fun onPermissionDenied() {
                    Toast.makeText(this@IntentActivity, "Permission denied", Toast.LENGTH_SHORT)
                        .show()
                }
            })
        }

        insert_or_edit_button?.setOnClickListener {

            // The code below should start an activity that lets you pick a contact to edit
            // with the "vrol0004@gmail.com" inserted as a new email, or let you create a
            // new contact with "vrol0004@gmail.com" inserted as the email.

            val intent = Intent(Intent.ACTION_INSERT_OR_EDIT).apply {
                type = ContactsContract.Contacts.CONTENT_ITEM_TYPE
                putExtra(ContactsContract.Intents.Insert.EMAIL, "vrol0004@gmail.com")
            }
            startActivity(intent)
        }

        edit_contact_button?.setOnClickListener {

            // The below code read contact from the phone and let you edit them

            // Check for permission to read/write contact, ones read or write contact is granted, you are able to get and edit contact from the contact list
            requestPermission(Permission.READ_CONTACT, object : RequestPermissionCallback {
                override fun onPermissionGranted() {

                    // Get contact from the contact list
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
                    Toast.makeText(
                        this@IntentActivity,
                        "Read contact permission denied",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }

        pick_contact_button?.setOnClickListener {

            // Activity for result for picking content
            activityForResult(ActivityIntent.PICK_CONTACT, object : OnActivityResultCallback {

                override fun onActivityResult(resultCode: Int, data: Intent?) {

                    if (resultCode == RESULT_OK) {
                        // Get the URI and query the content provider for the phone number
                        val contactUri = data?.data
                        val projection = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER)

                        contactUri?.let { contact_uri ->
                            val cursor = applicationContext.contentResolver.query(
                                contact_uri,
                                projection,
                                null,
                                null,
                                null
                            )

                            // If the cursor returned is valid, get the phone number
                            cursor?.let { contactCursor ->

                                if (contactCursor.moveToFirst()) {
                                    val numberIndex =
                                        contactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                                    val number = contactCursor.getString(numberIndex)

                                    Toast.makeText(
                                        this@IntentActivity,
                                        "Number: ${number}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                contactCursor.close()
                            }
                        }
                    }
                }
            })
        }

        send_email_button?.setOnClickListener {

            // This is a send email example
            val uriText = "mailto:vrol0004@gmail.com" +
                    "?subject=" + Uri.encode("Android Intent Example") +
                    "&body=" + Uri.encode("This is the test body text")

            val uri = Uri.parse(uriText)

            val sendIntent = Intent(Intent.ACTION_SENDTO)
            sendIntent.data = uri
            startActivity(Intent.createChooser(sendIntent, "Send email"))
        }

        action_send?.setOnClickListener {

            // Intent for sending a test message
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        // Open browser intent to www.google.com
        open_browser?.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"))
            startActivity(browserIntent)
        }

        // Open google maps with coordinate lat,long intent
        open_google_map?.setOnClickListener {
            val uri: String = String.format(Locale.ENGLISH, "geo:%f,%f", 52.3676, 4.9041)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            startActivity(intent)
        }

        // Web query intent
        web_query_example?.setOnClickListener {
            val query = "google"
            val intent = Intent(Intent.ACTION_WEB_SEARCH)
            intent.putExtra(SearchManager.QUERY, query)
            startActivity(intent)
        }

        // Star sign example on which activity for is used as an example
        star_sign_picker_example?.setOnClickListener {

            activityForResult(ActivityIntent.PICK_STAR_SIGN, object : OnActivityResultCallback {

                override fun onActivityResult(resultCode: Int, data: Intent?) {

                    if (resultCode == RESULT_OK) {
                        // Get selected sign data string
                        val selectedSign = data?.getStringExtra(StarSignPickerActivity.EXTRA_SIGN_NAME)
                        Toast.makeText(this@IntentActivity, "Selected sign: ${selectedSign}", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }
}