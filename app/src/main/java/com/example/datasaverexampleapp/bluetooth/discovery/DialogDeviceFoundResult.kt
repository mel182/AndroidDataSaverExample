package com.example.datasaverexampleapp.bluetooth.discovery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datasaverexampleapp.type_alias.Layout
import kotlinx.android.synthetic.main.item_dialog_fragment_device_result_view.*

class DialogDeviceFoundResult(private val deviceFoundList:ArrayList<DiscoveryDevice>) : DialogFragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(Layout.item_dialog_fragment_device_result_view,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        device_found_result_list?.apply {
            layoutManager = LinearLayoutManager(this@DialogDeviceFoundResult.context)
            adapter = DeviceDialogListAdapter(deviceFoundList)
        }

        close_dialog_button?.setOnClickListener {
            dismiss()
        }
    }

}