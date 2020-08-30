package com.example.datasaverexampleapp.earth_quake_example

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.earth_quake_example.model.Earthquake
import java.util.*
import kotlin.collections.ArrayList


/**
 * This is an earthquake example app that provide activity and fragment examples in a advance level
 */
class EarthQuakeActivity : AppCompatActivity() {

    val TAG_LIST_FRAGMENT = "TAG_LIST_FRAGMENT"

    private lateinit var earthQuakeListFragment: EarthQuakeListFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_earth_quake)
        title = "Earth Quake Example"

        savedInstanceState?.let {

            earthQuakeListFragment = supportFragmentManager.findFragmentByTag(TAG_LIST_FRAGMENT) as EarthQuakeListFragment

        }?: kotlin.run {

            object : CountDownTimer(4000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    Log.i("TAG","Milli until finished: ${millisUntilFinished}")
                }

                override fun onFinish() {
                    val fragmentTransaction = supportFragmentManager.beginTransaction()
                    earthQuakeListFragment = EarthQuakeListFragment()
                    fragmentTransaction.add(R.id.main_activity_frame,earthQuakeListFragment, TAG_LIST_FRAGMENT)
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN) // This is the default open animation
//                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE) // This is the default closed animation
//                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE) // This is the default fade animation
//                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_NONE) // This is the default none animation
                    fragmentTransaction.commitNow()

                    val now = Date()
                    val dummyQuakes = ArrayList<Earthquake>(0)
                    dummyQuakes.add(Earthquake("0",now,"San Jose",null, 7.3,null))
                    dummyQuakes.add(Earthquake("1",now,"LA",null, 6.5,null))
                    earthQuakeListFragment.setList(dummyQuakes)
                }
            }.start()
        }



    }
}