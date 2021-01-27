package com.example.datasaverexampleapp.animation.activity_transition

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.ViewCompat
import com.example.datasaverexampleapp.R
import kotlinx.android.synthetic.main.activity_transition1.*

class ActivityTransition1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transition1)

        activity2_button?.setOnClickListener {

            val intent = Intent(this, ActivityTransition2::class.java)
            val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                Pair.create(view_component as View, ViewCompat.getTransitionName(view_component)),
                Pair.create(view_component as View, ViewCompat.getTransitionName(view_component))).toBundle()
            startActivity(intent,bundle)
        }

    }
}