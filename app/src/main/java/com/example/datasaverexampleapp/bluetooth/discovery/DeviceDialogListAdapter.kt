package com.example.datasaverexampleapp.bluetooth.discovery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.datasaverexampleapp.type_alias.Layout
import com.example.datasaverexampleapp.type_alias.ViewByID

class DeviceDialogListAdapter(private val deviceList:ArrayList<DiscoveryDevice>) : RecyclerView.Adapter<DeviceDialogListAdapter.ViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(Layout.item_discovery_device_list_item,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val deviceFound = deviceList[position]
        holder.bind(deviceFound)
    }

    override fun getItemCount(): Int = deviceList.size

    inner class ViewHolder(view:View) : RecyclerView.ViewHolder(view)
    {
        private val nameTextView = view.findViewById<TextView>(ViewByID.name)
        private val addressTextView = view.findViewById<TextView>(ViewByID.address)

        fun bind(discoveryDevice: DiscoveryDevice)
        {
            nameTextView?.text = discoveryDevice.name?: "-"
            addressTextView?.text = discoveryDevice.device?.address
        }
    }

}