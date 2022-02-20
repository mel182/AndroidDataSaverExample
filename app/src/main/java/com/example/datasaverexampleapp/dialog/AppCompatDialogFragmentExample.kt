package com.example.datasaverexampleapp.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDialogFragment

@Suppress("UNUSED_ANONYMOUS_PARAMETER")
class AppCompatDialogFragmentExample() :
    AppCompatDialogFragment() {

    private var title: String = "Title"
    private var message: String = "The message"
    private var positiveButtonTitle: String = "Positive button"
    private var negativeButtonTitle: String = "Positive button"
    private var callback: DialogInterface.OnClickListener? = null

    fun setTitle(title: String?): AppCompatDialogFragmentExample {
        this.title = title ?: "Title"
        return this
    }

    fun setMessage(message: String?): AppCompatDialogFragmentExample {
        this.message = message ?: "The message"
        return this
    }

    fun setPositiveButton(positiveButton: String?): AppCompatDialogFragmentExample {
        this.positiveButtonTitle = positiveButton ?: "Positive button"
        return this
    }

    fun setNegativeButton(positiveButton: String?): AppCompatDialogFragmentExample {
        this.negativeButtonTitle = positiveButton ?: "Positive button"
        return this
    }

    fun setListener(callback: DialogInterface.OnClickListener): AppCompatDialogFragmentExample {
        this.callback = callback
        return this
    }

    //callback: DialogInterface.OnClickListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)

        builder
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(
                this.positiveButtonTitle
            ) { dialog, which ->
                Log.i("TAG", "Positive button clicked!")
                callback?.onClick(null, DialogInterface.BUTTON_POSITIVE)
            }.setNegativeButton(
                this.negativeButtonTitle
            ) { dialog, which ->
                Log.i("TAG", "Negative button clicked!")
                callback?.onClick(null, DialogInterface.BUTTON_NEGATIVE)
            }

        return builder.create()
    }

    // Override the onCreateView in order to inflate your own layout.
}