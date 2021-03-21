package com.example.datasaverexampleapp.video_audio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.datasaverexampleapp.R
import kotlinx.android.synthetic.main.activity_video_audio_streaming.*

class VideoAudioStreamingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_audio_streaming)
        title = "Video/Audio Example"

        media_player_video_playback?.setOnClickListener {
            val intent = Intent(this, MediaPlayerVideoPlaybackActivity::class.java)
            startActivity(intent)
        }

        exo_player_example?.setOnClickListener {
            val intent = Intent(this, ExoPlayerActivity::class.java)
            startActivity(intent)
        }

    }
}