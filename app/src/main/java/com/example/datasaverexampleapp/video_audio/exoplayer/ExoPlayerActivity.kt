@file:Suppress("DEPRECATION")

package com.example.datasaverexampleapp.video_audio.exoplayer

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.databinding.ActivityExoPlayerBinding
import com.example.datasaverexampleapp.type_alias.Layout
import com.google.android.exoplayer2.*
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
class ExoPlayerActivity : AppCompatActivity(), Player.Listener
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

            player?.addListener(this)

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
//                val mediaItem = MediaItem.fromUri(RawResourceDataSource.buildRawResourceUri(R.raw.sample_video))
                //3968e3af-cc38-41c3-b19e-b72bdd272c26
                val mediaItem = MediaItem.Builder().setUri("https://objectstore.true.nl/voetbalinternational:video/2022/carr_nistelrooij_ajax.mp4")
                    .setMediaId("3968e3af-cc38-41c3-b19e-b72bdd272c26")
                    .setAdsConfiguration(MediaItem.AdsConfiguration.Builder(Uri.parse("https://hbopenbid.pubmatic.com/translator?source=prebid-client")).build())
                    .build()
//                val mediaItem = MediaItem.fromUri(Uri.parse("https://objectstore.true.nl/voetbalinternational:video/2022/carr_nistelrooij_ajax.mp4"))
                val mediaItem2 = MediaItem.Builder().setUri("https://objectstore.true.nl/voetbalinternational:video/2022/carr_pijlers_vannistelrooij.mp4")
                    .setAdsConfiguration(MediaItem.AdsConfiguration.Builder(Uri.parse("https://ib.adnxs.com/ut/v3/prebid")).build())
                    .setMediaId("d86a443c-4237-435f-895b-f31ae5025c3c")
                    .build()
                //val mediaItem2 = MediaItem.fromUri(Uri.parse("https://objectstore.true.nl/voetbalinternational:video/2022/carr_pijlers_vannistelrooij.mp4"))

                /*
                MediaItem mediaItem =
    new MediaItem.Builder()
        .setUri(videoUri)
        .setAdsConfiguration(
            new MediaItem.AdsConfiguration.Builder(adTagUri).build())
        .build();
                */

                // This is used when using exoplayer playlist
                addMediaItem(mediaItem)
                addMediaItem(mediaItem2)

                // If you want to use progressive media source
//                val rawDirectoryResource: MediaSource = ProgressiveMediaSource.Factory(dataSourceFactory!!)
//                    .createMediaSource(mediaItem)
//
//                setMediaSource(rawDirectoryResource)

                prepare()

                // Enable this code when using playlist
                play()
            }
        }
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        super.onPlaybackStateChanged(playbackState)

        when(playbackState) {

            Player.STATE_BUFFERING -> {
                Log.i("TAG45","state buffering")
            }

            Player.STATE_ENDED -> {
                Log.i("TAG45","state ended")
                player?.seekToNextMediaItem()
            }

            Player.STATE_IDLE -> {
                Log.i("TAG45","state idle")
            }

            Player.STATE_READY -> {
                Log.i("TAG45","state ready")
            }

            else -> {
                Log.i("TAG45","state else statement")
            }
        }
    }

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        super.onMediaItemTransition(mediaItem, reason)
        Log.i("TAG45","onMediaItemTransition: ${mediaItem?.mediaId}")
    }

    override fun onStop() {
        binding?.playerView?.player = null
        player?.release()
        player = null
        super.onStop()
    }
}