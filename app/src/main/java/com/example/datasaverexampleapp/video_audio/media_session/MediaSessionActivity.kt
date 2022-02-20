@file:Suppress("DEPRECATION")

package com.example.datasaverexampleapp.video_audio.media_session

import android.os.Bundle
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.type_alias.Layout
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_exo_player.*

/**
 * This is a media session example using exo player
 *
 * NOTE:
 * One of the most useful and common clients for displaying media metadata
 * and hosting media playback control is a Notification. We discuss how to
 * create custom Notifications.
 */
class MediaSessionActivity : AppCompatActivity()
{
    private var player: SimpleExoPlayer? = null

    // Create and initialize a Media Session, create a new instance of the
    // 'MediaSessionCompat' class from within the 'onCreate' method of your
    // Activity, passing in a 'Context' and a 'String' for logging error messages.
    private var mediaSession : MediaSessionCompat? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(Layout.activity_media_session)
        title = "Media session Example"

        mediaSession = MediaSessionCompat(applicationContext, "TAG")
        // Create a new Exo Player
        player = ExoPlayerFactory.newSimpleInstance(this, DefaultTrackSelector())

        // To receive media control from devices such as Bluetooth headsets, Wear OS,
        // and Android Auto, you must then call 'setFlag', indicating that you wish
        // the Media Session to handle media buttons and transport controls.
        mediaSession?.setFlags(
            MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or
                    MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
        )

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
            .createMediaSource(RawResourceDataSource.buildRawResourceUri(R.raw.bon_vibe))

        // Start loading the media source
        player?.prepare(rawDirectoryResource)

        // The final step is to create and set an instance of the
        // 'MediaSessionCompat.Callback' class. The callback methods
        // you implement within this class will receive the media button
        // requests and allow you to respond to them appropriately.
        mediaSession?.setCallback( object : MediaSessionCompat.Callback(){

            override fun onPlay() {
                Toast.makeText(applicationContext, "onPlay pressed!",Toast.LENGTH_SHORT).show()
                player?.playWhenReady
            }

            override fun onPause() {
                Toast.makeText(applicationContext, "onPause pressed!",Toast.LENGTH_SHORT).show()
            }

            override fun onSeekTo(pos: Long) {
                Toast.makeText(applicationContext, "onSeekTo pressed!",Toast.LENGTH_SHORT).show()
                player?.seekTo(pos)
            }
        })

        updatePlaybackState()
    }

    private fun updatePlaybackState()
    {
        val playbackStateBuilder = PlaybackStateCompat.Builder().apply {

            // Available actions
            setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE or
                       PlaybackStateCompat.ACTION_PLAY or
                       PlaybackStateCompat.ACTION_PAUSE or
                       PlaybackStateCompat.ACTION_STOP or
                       PlaybackStateCompat.ACTION_SEEK_TO)

            // Current playback state
            setState(PlaybackStateCompat.STATE_PLAYING,
                0, // Track position in ms
                1.0f) // playback speed
        }

        mediaSession?.setPlaybackState(playbackStateBuilder.build())
    }

    override fun onStop() {
        player_view?.player = null
        player?.release()
        player = null
        super.onStop()
    }
}