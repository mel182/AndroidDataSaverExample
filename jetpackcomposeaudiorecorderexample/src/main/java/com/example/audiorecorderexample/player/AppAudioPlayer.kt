package com.example.audiorecorderexample.player

import android.content.Context
import android.media.MediaPlayer
import androidx.core.net.toUri
import java.io.File

class AppAudioPlayer(private val context: Context): AudioPlayer {

    private var player: MediaPlayer? = null

    override fun playFile(outputFile: File) {
        MediaPlayer.create(context,outputFile.toUri()).apply {
            player = this
            start()
        }
    }

    override fun stop() {
        player?.apply {
            stop()
            release()
        }
        player = null
    }

}