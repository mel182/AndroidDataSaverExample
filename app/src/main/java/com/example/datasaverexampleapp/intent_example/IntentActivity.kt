package com.example.datasaverexampleapp.intent_example

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.ContentUris
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.databinding.ActivityIntentBinding
import com.example.datasaverexampleapp.handlers.activity_result_handler.constant.ActivityIntent
import com.example.datasaverexampleapp.handlers.activity_result_handler.interfaces.OnActivityResultCallback
import com.example.datasaverexampleapp.handlers.permission.interfaces.RequestPermissionCallback
import com.example.datasaverexampleapp.handlers.permission.permissions.Permission
import com.example.datasaverexampleapp.intent_example.starSignPicker.ActivityForResultActivity
import com.example.datasaverexampleapp.type_alias.Layout
import java.util.*


class IntentActivity : IntentBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intent)

        title = "Intent Activity"

        DataBindingUtil.setContentView<ActivityIntentBinding>(
            this, Layout.activity_intent
        ).apply {
            // Intent to open the phone dialer
            intentActionDialButton.setOnClickListener {

                val phoneNumber = "0631564996"
                val callUri = Uri.parse("tel:${phoneNumber}")
                val callIntent = Intent(Intent.ACTION_DIAL, callUri)
                startActivity(callIntent)
            }

            // Intent to make a phone call
            intentActionCallButton.setOnClickListener {
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

            insertOrEditButton.setOnClickListener {

                // The code below should start an activity that lets you pick a contact to edit
                // with the "vrol0004@gmail.com" inserted as a new email, or let you create a
                // new contact with "vrol0004@gmail.com" inserted as the email.

                val intent = Intent(Intent.ACTION_INSERT_OR_EDIT).apply {
                    type = ContactsContract.Contacts.CONTENT_ITEM_TYPE
                    putExtra(ContactsContract.Intents.Insert.EMAIL, "vrol0004@gmail.com")
                }
                startActivity(intent)
            }

            editContactButton.setOnClickListener {

                // The below code read contact from the phone and let you edit them

                // Check for permission to read/write contact, ones read or write contact is granted, you are able to get and edit contact from the contact list
                requestPermission(Permission.READ_CONTACT, object : RequestPermissionCallback {
                    @SuppressLint("Range")
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

                        cursor?.let { contact_cursor ->

                            var idContact = 0L
                            while (contact_cursor.moveToNext()) {
                                idContact =
                                    contact_cursor.getLong(contact_cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID))
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

            pickContactButton.setOnClickListener {

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

            sendEmailButton.setOnClickListener {

                // This is a send email example
                val uriText = "mailto:vrol0004@gmail.com" +
                        "?subject=" + Uri.encode("Android Intent Example") +
                        "&body=" + Uri.encode("This is the test body text")

                val uri = Uri.parse(uriText)

                val sendIntent = Intent(Intent.ACTION_SENDTO)
                sendIntent.data = uri
                startActivity(Intent.createChooser(sendIntent, "Send email"))
            }

            actionSend.setOnClickListener {

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
            openBrowser.setOnClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"))
                startActivity(browserIntent)
            }

            // Open google maps with coordinate lat,long intent
            openGoogleMap.setOnClickListener {
                val uri: String = String.format(Locale.ENGLISH, "geo:%f,%f", 52.3676, 4.9041)
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                startActivity(intent)
            }

            // Web query intent
            webQueryExample.setOnClickListener {
                val query = "google"
                val intent = Intent(Intent.ACTION_WEB_SEARCH)
                intent.putExtra(SearchManager.QUERY, query)
                startActivity(intent)
            }

            // Star sign example on which activity for is used as an example
            customActivityForResultExample.setOnClickListener {

                activityForResult(ActivityIntent.PICK_STAR_SIGN, object : OnActivityResultCallback {

                    override fun onActivityResult(resultCode: Int, data: Intent?) {

                        if (resultCode == RESULT_OK) {
                            // Get selected sign data string
                            val selectedSign = data?.getStringExtra(ActivityForResultActivity.EXTRA_ACTIVITY_FOR_RESULT_EXAMPLE)
                            Toast.makeText(this@IntentActivity, "Selected sign: ${selectedSign}", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@IntentActivity, "Result cancelled", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }

            openDocumentTreeExample.setOnClickListener {

                /*
                Allow the user to pick a directory subtree.
                When invoked, the system will display the various DocumentsProvider instances installed on the device,
                letting the user navigate through them.
                Apps can fully manage documents within the returned directory.
                */
                activityForResult(ActivityIntent.PICK_OPEN_DIRECTORY, object : OnActivityResultCallback {

                    override fun onActivityResult(resultCode: Int, data: Intent?) {

                        if (resultCode == RESULT_OK) {
                            data?.let {
                                Toast.makeText(this@IntentActivity, "document tree: ${it.data}", Toast.LENGTH_SHORT).show()
                                Log.i("TAG","Data content uri: ${it.data}")
                            }
                        } else if (resultCode == RESULT_CANCELED)
                        {
                            Log.i("TAG","Result cancelled")
                        }
                    }
                })
            }
        }
    }
}