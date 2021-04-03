package com.example.datasaverexampleapp.video_audio.media_router_and_casting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.type_alias.Layout

class MediaRouterAndCastingActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(Layout.activity_media_router_and_casting)
        title = "Media router and casting Example"
    }
}