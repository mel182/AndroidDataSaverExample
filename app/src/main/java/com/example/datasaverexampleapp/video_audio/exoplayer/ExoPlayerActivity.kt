@file:Suppress("DEPRECATION")

package com.example.datasaverexampleapp.video_audio.exoplayer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.databinding.ActivityExoPlayerBinding
import com.example.datasaverexampleapp.type_alias.Layout
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import com.google.android.exoplayer2.util.Util


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
    private var player:ExoPlayer? = null
    private var binding: ActivityExoPlayerBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exo_player)
        binding = DataBindingUtil.setContentView(
            this, Layout.activity_exo_player
        )
        title = "Exoplayer Example (api 16+)"
    }

    override fun onResume() {
        super.onResume()
        initializePlayer()

//        CoroutineScope(Dispatchers.Main).launch {
//            delay(5000)
//            // To pause video programmatically
//            this@ExoPlayerActivity.player?.apply {
//                playWhenReady = false
//                pause()
//            }
//
//            delay(5000)
//            // To play video programmatically
//            this@ExoPlayerActivity.player?.apply {
//                playWhenReady = true
//                play()
//            }
//        }
    }

    private fun initializePlayer() {

        if (player == null)
        {
            // Create a new Exo Player
            player = ExoPlayer.Builder(this)
                .setRenderersFactory(DefaultRenderersFactory(this))
                .setTrackSelector(DefaultTrackSelector())
                .setLoadControl(DefaultLoadControl())
                .build()

            // associate the ExoPlayer with the player View
            binding?.playerView?.player = player

            this.player?.apply {
                playWhenReady = true
                seekTo(0L)

                //Build a DataSource.Factory capable of loading http and local content
                val dataSourceFactory = DefaultDataSourceFactory(
                    this@ExoPlayerActivity, Util.getUserAgent(
                        this@ExoPlayerActivity,
                        getString(R.string.app_name)
                    )
                )

                // This is the MediaSource representing the media to be played.
                val mediaItem = MediaItem.fromUri(RawResourceDataSource.buildRawResourceUri(R.raw.sample_video))
                //val mediaItem = MediaItem.fromUri(Uri.parse("https://objectstore.true.nl/voetbalinternational:video/2022/carr_nistelrooij_ajax.mp4"))
                val rawDirectoryResource: MediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(mediaItem)

                setMediaSource(rawDirectoryResource)
                prepare()
            }
        }
    }


    override fun onStop() {
        binding?.playerView?.player = null
        player?.release()
        player = null
        super.onStop()
    }
}