package com.example.datasaverexampleapp.speech_recognition_example

import android.Manifest.permission.RECORD_AUDIO
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.activityRequestHandler.ActivityResultHandler
import com.example.datasaverexampleapp.databinding.ActivitySpeechRecognitionBinding
import com.example.datasaverexampleapp.type_alias.Layout
import java.util.*

class SpeechRecognitionActivity : AppCompatActivity()
{
    private var activityResultHandler: ActivityResultHandler? = null
    private var binding: ActivitySpeechRecognitionBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speech_recognition)
        title = "Speech recognition example"
        activityResultHandler = ActivityResultHandler(this)
        binding = DataBindingUtil.setContentView<ActivitySpeechRecognitionBinding>(
            this, Layout.activity_speech_recognition
        ).apply {
            speechRecognitionButton.setOnClickListener {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    if (ContextCompat.checkSelfPermission(this@SpeechRecognitionActivity, RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)
                    {
                        launchSpeechRecognition()
                    } else {

                        activityResultHandler?.requestPermission(RECORD_AUDIO, object : OnPermissionResult{
                            override fun onPermissionResult(result: Boolean) {

                                if (result)
                                {
                                    Log.i("SR","Audio permission granted")
                                    launchSpeechRecognition()
                                } else {
                                    Toast.makeText(this@SpeechRecognitionActivity,"Permission denied",Toast.LENGTH_SHORT).show()
                                }
                            }
                        })
                    }

                } else {
                    launchSpeechRecognition()
                }
            }

            speechRecognitionWebButton.setOnClickListener {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    if (ContextCompat.checkSelfPermission(this@SpeechRecognitionActivity, RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)
                    {
                        launchSpeechRecognitionWeb()
                    } else {

                        activityResultHandler?.requestPermission(RECORD_AUDIO, object : OnPermissionResult{
                            override fun onPermissionResult(result: Boolean) {

                                if (result)
                                {
                                    Log.i("SR","Audio permission granted")
                                    launchSpeechRecognitionWeb()
                                } else {
                                    Toast.makeText(this@SpeechRecognitionActivity,"Permission denied",Toast.LENGTH_SHORT).show()
                                }
                            }
                        })
                    }

                } else {
                    launchSpeechRecognitionWeb()
                }
            }
        }
    }

    private fun launchSpeechRecognition()
    {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "or forever hold your peace")
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH)

        activityResultHandler?.startActivityForResult(intent, object : OnActivityResult{
            override fun onActivityResult(result: ActivityResult) {
                if (result.resultCode == RESULT_OK)
                {
                    val results = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    val confidence = result.data?.getFloatArrayExtra(RecognizerIntent.EXTRA_CONFIDENCE_SCORES)

                    Log.i("SR","data array list: $results")
                    Log.i("SR","data array list size: ${results?.size}")

                    results?.let {

                        if (it.size == 1)
                            binding?.speechRecognitionStatus?.text = "speech recognized: ${it[0]}"
                    }

                    confidence?.forEach {
                        Log.i("SR","confidence: $it")
                    }
                }
            }
        })
    }

    private fun launchSpeechRecognitionWeb()
    {
        val intent = Intent(RecognizerIntent.ACTION_WEB_SEARCH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH)
        activityResultHandler?.startActivityForResult(intent, object : OnActivityResult{
            override fun onActivityResult(result: ActivityResult) {
                if (result.resultCode == RESULT_OK)
                {
                    val results = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    val confidence = result.data?.getFloatArrayExtra(RecognizerIntent.EXTRA_CONFIDENCE_SCORES)

                    results?.let {

                        if (it.size == 1)
                            binding?.speechRecognitionStatus?.text = "speech recognized web: ${it[0]}"
                    }

                    confidence?.forEach {
                        Log.i("SR","confidence: $it")
                    }
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        activityResultHandler?.clear()
    }
}