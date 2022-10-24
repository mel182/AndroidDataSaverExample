package com.example.datasaverexampleapp.picture_in_picture_example

import android.app.PendingIntent
import android.app.PictureInPictureParams
import android.app.RemoteAction
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.graphics.drawable.Icon
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Rational
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toAndroidRect
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.viewinterop.AndroidView
import com.example.datasaverexampleapp.R
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class PictureInPictureExampleActivity : AppCompatActivity() {

    private val isPictureInPictureSupported by lazy { if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            packageManager.hasSystemFeature(
                PackageManager.FEATURE_PICTURE_IN_PICTURE
            )
        } else {
            false
        }
    }

    private var videoViewBounds = Rect()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Picture in picture example"

        setContent {

            Column(modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)) {
                val videoID = "jJKzW96S4F8"
                AndroidView(factory = { context ->

                    YouTubePlayerView(context).apply {
                        enableAutomaticInitialization = false

                        initialize(object : AbstractYouTubePlayerListener(){

                            override fun onReady(youTubePlayer: YouTubePlayer) {
                                super.onReady(youTubePlayer)
                                youTubePlayer.cueVideo(videoId = videoID,0f)
                            }

                            override fun onStateChange(
                                youTubePlayer: YouTubePlayer,
                                state: PlayerConstants.PlayerState
                            ) {
                                super.onStateChange(youTubePlayer, state)
                            }
                        })
                    }
                }, modifier = Modifier.onGloballyPositioned {
                    videoViewBounds = it.boundsInWindow().toAndroidRect()
                })
            }
        }
    }

    private fun updatedPictureInPictureParams(): PictureInPictureParams? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PictureInPictureParams.Builder()
                .setSourceRectHint(videoViewBounds)
                .setAspectRatio(Rational(16,9))
                .setActions(listOf(
                    RemoteAction(
                        Icon.createWithResource(applicationContext, R.drawable.audiotrack_icon),
                        "Audio track",
                        "Audio track",
                        PendingIntent.getBroadcast(
                            applicationContext,
                            0,
                            Intent(applicationContext, VideoReceiver::class.java),
                            PendingIntent.FLAG_IMMUTABLE
                        )
                    )
                )
                ).build()
        } else {
            null
        }
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        if (!isPictureInPictureSupported)
            return

        updatedPictureInPictureParams()?.let { params ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                enterPictureInPictureMode(params)
            }
        }
    }

    class VideoReceiver: BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            Log.i("TAG123","Clicked on pip action")
        }
    }
}