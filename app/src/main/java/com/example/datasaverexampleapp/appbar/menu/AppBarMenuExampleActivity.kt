package com.example.datasaverexampleapp.appbar.menu

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.datasaverexampleapp.R
import kotlinx.android.synthetic.main.activity_app_bar_menu_example.*

class AppBarMenuExampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_bar_menu_example)
        title = "App bar menu example"

//        val fragmentTransaction = supportFragmentManager.beginTransaction()
//        fragmentTransaction.add(appMenuExampleContainer.id, AppBarMenuFragment())
//        fragmentTransaction.commit()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        // You should always call super.onCreateOptionsMenu()
        // to ensure this call is also dispatched to Fragment
        menuInflater.inflate(R.menu.custom_menu,menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        super.onPrepareOptionsMenu(menu)

        // By overriding the activity or Fragment 'onPrepareOptionsMenu' method,
        // you can modify a Menu based on an application current state at run time,
        // immediately before the Menu is displayed. This lets you dynamically disable/enable Menu,
        // set visibility, and even modify text.
        val item = menu?.findItem(R.id.about_menu)
        item?.isVisible = true
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        // Android handles the app bar action and overflow menu using single event handler
        // which is this function, the 'onOptionsItemSelected' function.
        // If you supplied Menu Items from within a Fragment, you can choose to handle them within the
        // 'onOptionsItemSelected' handler of either the Activity or the Fragment. Note that the Activity
        // will receive the selected Menu Item first, and that the Fragment will not receive it if the
        // Activity handles it and returns true

        return when(item.itemId)
        {
            R.id.about_menu -> {
                Toast.makeText(this,"About menu selected activity!",Toast.LENGTH_SHORT).show()
                true
            }
            R.id.settings_menu -> {
                Toast.makeText(this,"Setting menu selected activity!",Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}