@file:Suppress("DEPRECATION")

package com.example.datasaverexampleapp.text_to_speech_example

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.databinding.ActivityTextToSpeechBinding
import com.example.datasaverexampleapp.type_alias.Layout

class TextToSpeechActivity : AppCompatActivity()
{
    private var ttsInit = false
    private var tts : TextToSpeech? = null
    private var binding: ActivityTextToSpeechBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_to_speech)
        title = "Text to Speech Example"
        checkTextToSpeechStatus()

        binding = DataBindingUtil.setContentView<ActivityTextToSpeechBinding>(
            this, Layout.activity_text_to_speech
        ).apply {
            actionButton.setOnClickListener {

                if (actionButton.text == "install")
                {
                    installTextToSpeechLibrary()
                } else if (actionButton.text == "Speak")
                {
                    speak()
                }
            }
        }
    }

    private fun checkTextToSpeechStatus()
    {
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            binding?.apply {
                textToSpeechStatus.text = if (result.resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                    initializeTextToSpeech()
                    "Initializing....."
                } else {
                    actionButton.text = "install"
                    "Library not installed"
                }
            }
        }.launch(Intent(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA))
    }

    private fun installTextToSpeechLibrary()
    {
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS)
            {
                initializeTextToSpeech()
            }

        }.launch(Intent(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA))
    }

    private fun initializeTextToSpeech()
    {
        if(tts == null)
        {
            tts = TextToSpeech(this) { status ->
                if (status == TextToSpeech.SUCCESS)
                {
                    ttsInit = true
                    binding?.apply {
                        textToSpeechStatus.text = "Library imported"
                        actionButton.text = "Speak"
                    }
                }
            }
        }
    }

    private fun speak()
    {
        val parameter:Bundle? = null
        val utterInstance:String? = null
        if (ttsInit)
        {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            {
                tts?.speak("Hello, Android",TextToSpeech.QUEUE_ADD, HashMap())
            } else {
                tts?.speak("Hello, Android", TextToSpeech.QUEUE_ADD, parameter, utterInstance)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tts?.apply {
            stop()
            shutdown()
        }
    }
}