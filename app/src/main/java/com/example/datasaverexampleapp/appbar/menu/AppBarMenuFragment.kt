package com.example.datasaverexampleapp.appbar.menu

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.datasaverexampleapp.R

/**
 * A simple [Fragment] subclass.
 * Use the [AppBarMenuFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AppBarMenuFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_app_bar_menu, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.custom_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when(item.itemId)
        {
            R.id.about_menu -> {
                Toast.makeText(activity,"About menu selected fragment!", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.settings_menu -> {
                Toast.makeText(activity,"Setting menu selected fragment!", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}