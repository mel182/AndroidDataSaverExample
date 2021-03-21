package com.example.datasaverexampleapp.video_audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.datasaverexampleapp.R
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_exo_player.*
import java.io.File


/**
 * ExoPlayer library has been build by Google to provide consistent experience,
 * better extensibility, and additional format support for media playback on all
 * devices running Android 4.1 (API Level 16) or higher
 *
 * Dependencies:
 * The 'exoplayer-core' library is the only required dependency for integrating ExoPlayer
 * into your app; however, ExoPlayer also provide a number of subcomponents that offer
 * additional functionality. For example, the 'exoplayer-ui' library provides pre-built
 * UI components that greatly simplify common operations including playback controls.
 *
 * Usage:
 * To use ExoPlayer for video playback, you must add the ExoPlayer core and UI libraries as
 * dependencies to your module's 'build.gradle' file:
 *
 * implementation 'com.google.android.exoplayer:exoplayer-core:2.8.2'
 * implementation 'com.google.android.exoplayer:exoplayer-ui:2.8.2'
 *
 * The ExoPlayer UI library provides a 'PlayerView' class that encapsulates
 * both the playback surface and playback controls including play,pause,
 * fast forward, rewind and a seek bar for skipping through the video, and
 * which can be added to your Activity or Fragment layout.
 */
class ExoPlayerActivity : AppCompatActivity()
{
    private var player:SimpleExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exo_player)
        title = "Exoplayer Example (api 16+)"
    }

    override fun onStart() {
        super.onStart()

        // Create a new Exo Player
        player = ExoPlayerFactory.newSimpleInstance(this, DefaultTrackSelector())

        // associate the ExoPlayer with the player View
        player_view?.player = player

        //Build a DataSource.Factory capable of loading http and local content
        val dataSourceFactory = DefaultDataSourceFactory(
            this, Util.getUserAgent(
                this,
                getString(R.string.app_name)
            )
        )

        // This is the MediaSource representing the media to be played.
        val rawDirectoryResource: MediaSource = ExtractorMediaSource.Factory(dataSourceFactory)
            .createMediaSource(RawResourceDataSource.buildRawResourceUri(R.raw.sample_video))

        // Start loading the media source
        player?.prepare(rawDirectoryResource)

        // Start playback automatically when ready
        player?.playWhenReady = true
    }


    override fun onStop() {
        player_view?.player = null
        player?.release()
        player = null
        super.onStop()
    }
}