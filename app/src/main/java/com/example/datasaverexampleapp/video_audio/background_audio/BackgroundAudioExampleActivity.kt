package com.example.datasaverexampleapp.video_audio.background_audio

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.datasaverexampleapp.type_alias.Layout
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_background_audio_example.*

/**
 * This is the background audio playback example Activity Example
 * Android  provides the 'MediaBrowserServiceCompat' and 'MediaBrowserCompat' API's, to simplify
 * the separation of your audio playback Service from any connected clients-including your playback
 * Activity.
 *
 * NOTE: As with the 'MediaSession' class, Android 5.0 (API Level 21) introduced a 'MediaBrowserService'
 *       and 'MediaBrowser' class. However, we strongly recommended using the 'MediaBrowserServiceCompat'
 *       and 'MediaBrowserCompat' from the Android Support Library, and will use the compatibility library
 *       classes throughout this chapter.
 *
 */
class BackgroundAudioExampleActivity : AppCompatActivity()
{
    private lateinit var exoPlayerFragment: ExoPlayerFragment
    private lateinit var mediaPlayerFragment: MediaPlayerFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(Layout.activity_background_audio_example)
        title = "Background audio example"

        exoPlayerFragment = ExoPlayerFragment()
        mediaPlayerFragment = MediaPlayerFragment()

        playerTabLayout?.apply {

            supportFragmentManager.beginTransaction().apply {
                fragmentContainer?.let { container ->
                    add(container.id,exoPlayerFragment)
                    commit()
                }
            }

            addOnTabSelectedListener( object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {

                    tab?.apply {

                        val fragmentTransaction = supportFragmentManager.beginTransaction()

                        fragmentContainer?.let {

                            when(position)
                            {
                                0 -> fragmentTransaction.replace(it.id,exoPlayerFragment)
                                else -> fragmentTransaction.replace(it.id,mediaPlayerFragment)
                            }
                        }

                        fragmentTransaction.commit()
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabReselected(tab: TabLayout.Tab?) {}

            })
        }
    }

    fun getPlayerTabLayout() : TabLayout? = playerTabLayout
}