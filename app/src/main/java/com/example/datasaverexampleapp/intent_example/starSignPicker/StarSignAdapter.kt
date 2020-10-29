package com.example.datasaverexampleapp.intent_example.starSignPicker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.intent_example.starSignPicker.viewholder.CustomViewHolder

class StarSignAdapter(val listener:OnItemClicked?) : RecyclerView.Adapter<StarSignAdapter.ViewHolder>()
{
    private val starSigns = arrayOf("Arias","Taurus","Gemini","Cancer","Leo","Virgo","Libra","Scorpio","Capricorn","Aquarius")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_star_picker_custom_cell,parent,false)
        return ViewHolder(view,listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemFound = starSigns[position]
        holder.bind(itemFound)
    }

    override fun getItemCount(): Int {
        return starSigns.size
    }

    class ViewHolder(view:View, val listener:OnItemClicked?): CustomViewHolder<String>(view)
    {
        val mainView = view.findViewById<LinearLayout>(R.id.main_view)
        val title = view.findViewById<TextView>(R.id.itemTextView)

        override fun bind(item:String)
        {
            title?.text = item
            mainView?.setOnClickListener {
                listener?.onItemClick(item)
            }
        }
    }
}