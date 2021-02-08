package com.example.datasaverexampleapp.appbar.app_bar_bottom_navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.datasaverexampleapp.R

/**
 * A simple [Fragment] subclass.
 * Use the [ContentFragment1.newInstance] factory method to
 * create an instance of this fragment.
 */
class ContentFragment1 : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_content1, container, false)
    }
}