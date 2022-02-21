@file:Suppress("DEPRECATION")

package com.example.datasaverexampleapp.room_db

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datasaverexampleapp.databinding.FragmentDbObserverBinding
import com.example.datasaverexampleapp.type_alias.Layout

/**
 * A simple [Fragment] subclass.
 * Use the [DbObserverFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DbObserverFragment : Fragment(Layout.fragment_db_observer) {

    private lateinit var roomDBViewModel: RoomDBViewModel
    private var dataListAdapter = DatabaseListAdapter()
    private var titleText:String? = null

    fun setTitle(title:String) : DbObserverFragment
    {
        this.titleText = title
        return this
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        roomDBViewModel = ViewModelProvider(this).get(RoomDBViewModel::class.java)

        activity?.apply {

            DataBindingUtil.setContentView<FragmentDbObserverBinding>(
                this, Layout.fragment_db_observer
            ).apply {
                title.text = titleText
                userList.apply {
                    layoutManager = LinearLayoutManager(activity)
                    adapter = dataListAdapter
                }
            }
        }

        roomDBViewModel.getAllUsers().observe(viewLifecycleOwner) {
            dataListAdapter.addUsers(it)
        }
    }
}