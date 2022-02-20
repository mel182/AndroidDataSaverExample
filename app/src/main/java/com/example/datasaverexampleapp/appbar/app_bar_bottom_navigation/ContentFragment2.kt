package com.example.datasaverexampleapp.appbar.app_bar_bottom_navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.databinding.FragmentContent2Binding
import com.example.datasaverexampleapp.global_adapter.DataAdapter
import com.example.datasaverexampleapp.type_alias.Layout

/**
 * A simple [Fragment] subclass.
 * Use the [ContentFragment2.newInstance] factory method to
 * create an instance of this fragment.
 */
class ContentFragment2 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_content2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.apply {

            DataBindingUtil.setContentView<FragmentContent2Binding>(
                this, Layout.fragment_content2
            ).apply {

                itemList.apply {

                    val data = arrayListOf(
                        "value 1", "value 2", "value 3",
                        "value 4", "value 5", "value 6",
                        "value 7", "value 8", "value 9",
                        "value 10", "value 11", "value 12",
                        "value 13", "value 14", "value 15",
                        "value 16", "value 17", "value 18",
                        "value 19", "value 20", "value 21",
                        "value 22", "value 23", "value 24",
                        "value 25", "value 26", "value 27",
                        "value 28", "value 29", "value 30",
                        "value 31", "value 32", "value 33")

                    layoutManager = LinearLayoutManager(context)
                    adapter = DataAdapter(data)
                }
            }
        }
    }
}