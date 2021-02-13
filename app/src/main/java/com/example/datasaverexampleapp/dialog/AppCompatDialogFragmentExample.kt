package com.example.datasaverexampleapp.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDialogFragment

class AppCompatDialogFragmentExample : AppCompatDialogFragment()
{
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
    {
        val builder = AlertDialog.Builder(activity)

        builder
            .setTitle("Title")
            .setMessage("The message")
            .setPositiveButton("Positive button", object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    Log.i("TAG","Positive button clicked!")
                }
            }).setNegativeButton("Negative button", object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    Log.i("TAG","Negative button clicked!")
                }
            })

        return builder.create()
    }

    // Override the onCreateView in order to inflate your own layout.
}