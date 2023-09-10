package com.example.audiorecorderexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import com.example.audiorecorderexample.player.AppAudioPlayer
import com.example.audiorecorderexample.recorder.AppAudioRecorder
import com.example.audiorecorderexample.ui.theme.DataSaverExampleAppTheme
import java.io.File

class AudioRecorderExampleMainActivity : ComponentActivity() {

    private val recorder by lazy {
        AppAudioRecorder(applicationContext)
    }

    private val player by lazy {
        AppAudioPlayer(applicationContext)
    }

    private var audioFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityCompat.requestPermissions(this,
            arrayOf(android.Manifest.permission.RECORD_AUDIO),
            0)

        setContent {
            DataSaverExampleAppTheme {

                Column(modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Button(onClick = {
                        File(cacheDir, "audio.mp3").also {
                            recorder.start(it)
                            audioFile = it
                        }
                    }) {
                        Text(text = "Start recording")
                    }

                    Button(onClick = {
                        recorder.stop()
                    }) {
                        Text(text = "Stop recording")
                    }

                    Button(onClick = {
                        player.playFile(audioFile ?: return@Button)
                    }) {
                        Text(text = "Play")
                    }

                    Button(onClick = {
                        player.stop()
                    }) {
                        Text(text = "Stop playing")
                    }

                }

            }
        }
    }
}
