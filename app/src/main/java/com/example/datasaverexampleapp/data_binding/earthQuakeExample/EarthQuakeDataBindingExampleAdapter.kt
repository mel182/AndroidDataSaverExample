package com.example.datasaverexampleapp.data_binding.earthQuakeExample

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.datasaverexampleapp.databinding.ListItemEarthquakeDataBindingBinding
import com.example.datasaverexampleapp.earth_quake_example.model.Earthquake

class EarthQuakeDataBindingExampleAdapter : RecyclerView.Adapter<EarthQuakeDataBindingExampleAdapter.ViewHolder>(){

    private val list: ArrayList<Earthquake> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemEarthquakeDataBindingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val earthquake = list[position]
        holder.binding.earthQuake = earthquake
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun loadData(data: Earthquake)
    {
        list.add(data)
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ListItemEarthquakeDataBindingBinding) :
        RecyclerView.ViewHolder(binding.getRoot()) {
        val binding: ListItemEarthquakeDataBindingBinding

        init {
            this.binding = binding
        }
    }
}