@file:Suppress("DEPRECATION")

package com.example.datasaverexampleapp.room_db

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datasaverexampleapp.type_alias.Layout
import kotlinx.android.synthetic.main.fragment_db_observer.*

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

        title?.text = titleText
        user_list?.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = dataListAdapter
        }

        roomDBViewModel.getAllUsers().observe(viewLifecycleOwner) {
            dataListAdapter.addUsers(it)
        }
    }


}