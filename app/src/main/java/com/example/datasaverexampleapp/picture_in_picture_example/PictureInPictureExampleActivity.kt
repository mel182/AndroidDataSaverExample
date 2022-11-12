package com.example.datasaverexampleapp.picture_in_picture_example

import android.app.PendingIntent
import android.app.PictureInPictureParams
import android.app.RemoteAction
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Rect
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Rational
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    private var isChecked by mutableStateOf(false)
    private var isCheckboxVisible by mutableStateOf(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Picture in picture example"

        setContent {

            Column(modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)) {
                val videoID = "eCEXpukJTIs"
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

                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically) {

                    AnimatedVisibility(visible = isCheckboxVisible, exit = fadeOut()) {
                        Checkbox(checked = isChecked, onCheckedChange = {
                            isChecked = it
                            isCheckboxVisible = !it
                        })
                    }

                    Text(text = if (isChecked) "Picture in picture enabled" else "Enable picture in picture",
                         color = if (isChecked) Color.Blue else Color.Black)
                }
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
                        Icon.createWithResource(applicationContext, R.drawable.video_icon),
                        "Video track",
                        "Video track",
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

    override fun onPause() {
        super.onPause()

        if (isChecked) {
            enterPictureInPicture()
        }
    }

    private fun enterPictureInPicture() {
        if (!isPictureInPictureSupported)
            return

        updatedPictureInPictureParams()?.let { params ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                enterPictureInPictureMode(params)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)

        if (isInPictureInPictureMode) {
            // in picture in picture mode
            supportActionBar?.hide()
        } else {
            //out picture in picture mode
            supportActionBar?.show()
        }
    }

    class VideoReceiver: BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            Log.i("TAG123","Clicked on pip action")
        }
    }
}