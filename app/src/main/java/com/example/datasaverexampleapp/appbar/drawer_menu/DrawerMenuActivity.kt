package com.example.datasaverexampleapp.appbar.drawer_menu

import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.datasaverexampleapp.R
import kotlinx.android.synthetic.main.item_drawer_layout.*

class DrawerMenuActivity : AppCompatActivity() {

    private var drawerToggle:ActionBarDrawerToggle? = null
    private var selectedItem = 0
    private var title:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer_menu)

        // Ensure that navigation button is visible
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        drawerToggle = ActionBarDrawerToggle(this, drawer_layout, R.string.cardinal_east, R.string.cardinal_north)
        drawer_layout?.addDrawerListener(object : DrawerLayout.DrawerListener{
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

            override fun onDrawerOpened(drawerView: View) {}

            override fun onDrawerClosed(drawerView: View) {

                var newFragment:Fragment? = null

                when(selectedItem)
                {
                    R.id.nav_home -> {
                        newFragment = HomeFragment()
                        supportActionBar?.title = this@DrawerMenuActivity.title
                    }

                    R.id.nav_account -> {
                        newFragment = AccountFragment()
                        supportActionBar?.title = this@DrawerMenuActivity.title
                    }

                    R.id.nav_about -> {
                        newFragment = AboutFragment()
                        supportActionBar?.title = this@DrawerMenuActivity.title
                    }
                }

                newFragment?.apply {

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.drawer_content_view, this)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit()
                }

                selectedItem = 0
            }

            override fun onDrawerStateChanged(newState: Int) {}
        })

        nav_view?.setNavigationItemSelectedListener {
            selectedItem = it.itemId
            it.isChecked = true
            title = it.title.toString()
            drawer_layout.closeDrawer(nav_view)
            true
        }

        supportFragmentManager.beginTransaction()
            .add(R.id.drawer_content_view, HomeFragment())
            .commit()

    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle?.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        drawerToggle?.syncState()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        drawerToggle?.let { toggle ->

            if (toggle.onOptionsItemSelected(item))
                return true
        }

        // Follow with your own menu item selection logic
        return super.onOptionsItemSelected(item)
    }
}