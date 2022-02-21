@file:Suppress("DEPRECATION")

package com.example.datasaverexampleapp.video_audio.audio_recording

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.os.Environment.getExternalStoragePublicDirectory
import androidx.databinding.DataBindingUtil
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.base_classes.BaseActivity
import com.example.datasaverexampleapp.databinding.ActivityAudioRecordingBinding
import com.example.datasaverexampleapp.type_alias.Drawable
import com.example.datasaverexampleapp.type_alias.Layout
import java.io.File

/**
 * This is an example of how to record audio using the microphone.
 * In order to record audio in Android, you must use the 'MediaRecorder' class.
 *
 * NOTE: For privacy reasons, the RECORD_AUDIO permissions is considered a dangerous permission.
 *       It must be requested at run time on devices running Android 6.0 or higher.
 *
 * <uses-permission android:name="android.permission.RECORD_AUDIO"/>
 *
 * The Media recorder manages recording as a state machine. This means that the order in which you
 * configure and manage the Media Recorder is important. In the simplest terms, the transitions through
 *  the state machine can be described as follows:
 *  1. Create a new Media Recorder.
 *  2. Specify the input sources to record from
 *  3. Specify the output format and audio encoder
 *  4. Select an output file
 *  5. Prepare the Media Recorder for recording
 *  6. Record
 *  7. End the recording
 */
class AudioRecordingActivity : BaseActivity(R.layout.activity_audio_recording)
{
    private var mediaRecorder : MediaRecorder? = null
    private var isListening : Boolean = false
    private var isMediaPlayerPlaying : Boolean = false
    private var outputFile : File? = null
    private var binding: ActivityAudioRecordingBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Audio Recording Example"

        binding = DataBindingUtil.setContentView<ActivityAudioRecordingBinding>(
            this, Layout.activity_audio_recording
        ).apply {
            val micPresent: Boolean = packageManager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE)
            if (micPresent)
            {
                setState(RecorderState.READY)
                micIcon.setOnClickListener {

                    if (!isMediaPlayerPlaying)
                    {
                        mediaRecorder?.let { recorder ->

                            if (isListening)
                            {
                                recorder.stop()

                                // Reset and release the media recorder
                                recorder.reset()
                                recorder.release()
                                isListening = false
                                setState(RecorderState.READY)
                                playRecordedAudio()
                            } else {
                                startRecording()
                            }

                        }?:startRecording()
                    }
                }
            } else {
                setState(RecorderState.NO_MIC_DETECTED)
            }
        }
    }

    private fun startRecording()
    {
        requestPermission(Manifest.permission.RECORD_AUDIO) { granted ->

            if (granted)
            {
                setState(RecorderState.READY)

                mediaRecorder = MediaRecorder().apply {

                    // Configure the input sources
                    // The 'setAudioSource' method lets you specify a 'MediaRecorder.AudioSource'
                    // static constant that defines the audio source. For audio recording, this
                    // almost always 'MediaRecorder.AudioSource.MIC'
                    setAudioSource(MediaRecorder.AudioSource.MIC)

                    // Set the output format and encoder
                    // After selecting your input source, you need to select the output
                    // format using the 'setOutputFormat' method with a 'MediaRecorder.OutputFormat'
                    // constant.
                    setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)

                    // Use the 'setAudioEncoder' methods with an audio encoder constant from
                    // the 'MediaRecorder.AudioEncoder' class
                    setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

                    // Specify the output file
                    val mediaDirectory = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        externalMediaDirs[0]
                    } else {
                        getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    }

                    // Specify the output file
                    // Assign a file to store the recorded media using the 'setOutputFile' method
                    // before calling 'prepare'
                    //
                    // WARNING: The 'setOutputFile' method must be called before 'prepare' and after
                    //          'setOutputFormat', otherwise, it will throw an 'IllegalStateException'
                    outputFile = File(mediaDirectory,"audio_recording.3gp")
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        setOutputFile(outputFile)
                    } else {
                        setOutputFile(outputFile?.path)
                    }

                    // Prepare to record
                    // After configuring the Media Recorder and preparing, you can begin
                    // recording at any time by calling the 'start' method.
                    prepare()

                    // start recording
                    start()
                    isListening = true
                    setState(RecorderState.RECORDING)
                }

            } else {
                setState(RecorderState.PERMISSION_DENIED)
            }
        }
    }

    private fun playRecordedAudio()
    {
        outputFile?.let {

            if (it.exists())
            {
                MediaPlayer().apply {
                    setDataSource(it.absolutePath)

                    setOnCompletionListener {
                        stop()
                        release()
                        setState(RecorderState.READY)
                        isMediaPlayerPlaying = false
                    }

                    prepare()
                    start()

                    setState(RecorderState.PLAYING_AUDIO)
                    isMediaPlayerPlaying = true
                }
            }
        }
    }

    private fun setState(state: RecorderState)
    {
        binding?.apply {

            when(state)
            {
                RecorderState.PERMISSION_DENIED -> {
                    recordingStatus.text = "Recording permission denied"
                    micIcon.setImageResource(Drawable.ic_microphone_disabled)
                }
                RecorderState.PLAYING_AUDIO -> {
                    recordingStatus.text = "Playing recording...."
                    micIcon.setImageResource(Drawable.ic_music)
                }
                RecorderState.READY -> {
                    recordingStatus.text = "Press microphone icon to start recording"
                    micIcon.setImageResource(Drawable.ic_microphone_enable)
                }
                RecorderState.RECORDING -> {
                    recordingStatus.text = "Recording... press recording icon to stop"
                    micIcon.setImageResource(Drawable.ic_recording)
                }

                RecorderState.NO_MIC_DETECTED -> {
                    recordingStatus.text = "No microphone detected"
                    micIcon.setImageResource(Drawable.ic_microphone_disabled)
                }
            }
        }

    }
}

