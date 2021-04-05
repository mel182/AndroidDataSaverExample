package com.example.datasaverexampleapp.video_audio.background_audio

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.type_alias.Layout
import kotlinx.android.synthetic.main.item_player_layout.*

/**
 * A simple [Fragment] subclass.
 * Use the [MediaPlayerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MediaPlayerFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(Layout.fragment_media_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playerTitle?.text = "Media player"
    }
}