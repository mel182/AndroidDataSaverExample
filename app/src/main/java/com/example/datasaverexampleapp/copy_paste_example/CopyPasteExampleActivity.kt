package com.example.datasaverexampleapp.copy_paste_example

import android.content.ClipData
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ClipboardManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.databinding.ActivityCopyPasteExampleBinding
import com.example.datasaverexampleapp.type_alias.Layout

class CopyPasteExampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_copy_paste_example)
        title = "Copy and past Example"

        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

        DataBindingUtil.setContentView<ActivityCopyPasteExampleBinding>(
            this, Layout.activity_copy_paste_example
        ).apply {

            textViewCopyText.apply {

                setOnLongClickListener {

                    ClipData.newPlainText(this.text,this.text)?.apply {
                        clipboard.setPrimaryClip(this)
                        Toast.makeText(this@CopyPasteExampleActivity,"Text copied",Toast.LENGTH_SHORT).show()
                    }

                    true
                }
            }

            editTextPasteText.apply {

                clearFocus()

                setOnLongClickListener {

                    if (!clipboard.hasPrimaryClip())
                    {
                        Toast.makeText(this@CopyPasteExampleActivity,"Does not have primary clip",Toast.LENGTH_SHORT).show()
                    } else {
                        Log.i("TAG","Does have primary clip")

                        clipboard.primaryClipDescription?.let { clipDescription ->

                            if (clipDescription.hasMimeType(MIMETYPE_TEXT_PLAIN))
                            {
                                // Disable the paste UI if the content in the clipboard is not of supported type
                                clipboard.primaryClip?.getItemAt(0)?.let { clipDataItem ->

                                    val pasteData = clipDataItem.text
                                    editTextPasteText.setText(pasteData)
                                }

                            } else {
                                // Enable the paste UI option if the clipboard contains data of supported type
                            }
                        }
                    }

                    true
                }

            }

            isSelectableTextViewCopyText.setTextIsSelectable(true) // Set text view as selectable text programmatically
            // Through xml = android:textIsSelectable="true"
        }
    }
}