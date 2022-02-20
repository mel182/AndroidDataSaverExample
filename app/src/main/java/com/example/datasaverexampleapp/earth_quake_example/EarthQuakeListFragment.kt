package com.example.datasaverexampleapp.earth_quake_example

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.databinding.FragmentEarthQuakeListBinding
import com.example.datasaverexampleapp.earth_quake_example.model.Earthquake
import com.example.datasaverexampleapp.type_alias.Layout


/**
 * A simple [Fragment] subclass.
 * Use the [EarthQuakeListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EarthQuakeListFragment : Fragment() {

    private val earthQuakes:ArrayList<Earthquake> = ArrayList()
    private val earthQuakeRecyclerViewAdapter = EarthQuakeRecyclerViewAdapter(earthQuakes)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_earth_quake_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.apply {
            DataBindingUtil.setContentView<FragmentEarthQuakeListBinding>(
                this, Layout.fragment_earth_quake_list
            ).apply {
                list.layoutManager = LinearLayoutManager(activity)
                list.adapter = EarthQuakeRecyclerViewAdapter(earthQuakes)
            }
        }
    }

    fun setList(data:ArrayList<Earthquake>)
    {
        for (earthquake in data)
        {
            if (!earthQuakes.contains(earthquake))
            {
                this.earthQuakes.add(earthquake)
                this.earthQuakeRecyclerViewAdapter.notifyItemInserted(earthQuakes.indexOf(earthquake))
            }
        }
    }
}