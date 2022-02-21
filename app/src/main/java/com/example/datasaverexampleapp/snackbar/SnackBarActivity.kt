package com.example.datasaverexampleapp.snackbar

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.databinding.ActivitySnackBarBinding
import com.example.datasaverexampleapp.type_alias.Layout
import com.google.android.material.snackbar.Snackbar

class SnackBarActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_snack_bar)

        DataBindingUtil.setContentView<ActivitySnackBarBinding>(
            this, Layout.activity_snack_bar
        ).apply {

            // Note: When a Snackbar is used with a coordinator layout users are able to swipe it away and any FloatingActionButton that would
            // potentially overlap the incoming Snackbar will be smoothly animated alongside the Snackbar
            showButton.setOnClickListener {

                val snackBar = Snackbar.make(rootView,"Snackbar", Snackbar.LENGTH_LONG)

                snackBar.setAction("Undo", object : View.OnClickListener{
                    override fun onClick(v: View?)
                    {
                        Snackbar.make(rootView,"Undo button clicked!", Snackbar.LENGTH_LONG).show()
                    }
                })

                snackBar.addCallback( object: Snackbar.Callback()
                {
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        Toast.makeText(this@SnackBarActivity, "Snackbar dismissed",Toast.LENGTH_SHORT).show()
                    }
                })
                snackBar.show()
            }
        }
    }
}