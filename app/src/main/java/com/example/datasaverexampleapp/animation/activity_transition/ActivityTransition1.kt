package com.example.datasaverexampleapp.animation.activity_transition

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.databinding.ActivityTransition1Binding
import com.example.datasaverexampleapp.type_alias.Layout

class ActivityTransition1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transition1)

        DataBindingUtil.setContentView<ActivityTransition1Binding>(
            this, Layout.activity_transition1
        ).apply {

            activity2Button.setOnClickListener {

                val intent = Intent(this@ActivityTransition1, ActivityTransition2::class.java)
                val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this@ActivityTransition1,
                    Pair.create(viewComponent as View, ViewCompat.getTransitionName(viewComponent)),
                    Pair.create(viewComponent as View, ViewCompat.getTransitionName(viewComponent))).toBundle()
                startActivity(intent,bundle)
            }
        }
    }
}