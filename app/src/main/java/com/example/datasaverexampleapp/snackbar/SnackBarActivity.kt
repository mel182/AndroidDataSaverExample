package com.example.datasaverexampleapp.snackbar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.datasaverexampleapp.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_snack_bar.*

class SnackBarActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_snack_bar)

        // Note: When a Snackbar is used with a coordinator layout users are able to swipe it away and any FloatingActionButton that would
        // potentially overlap the incoming Snackbar will be smoothly animated alongside the Snackbar
        show_button?.setOnClickListener {

            val snackBar = Snackbar.make(root_view,"Snackbar", Snackbar.LENGTH_LONG)

            snackBar.setAction("Undo", object : View.OnClickListener{
                override fun onClick(v: View?)
                {
                    Snackbar.make(root_view,"Undo button clicked!", Snackbar.LENGTH_LONG).show()
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