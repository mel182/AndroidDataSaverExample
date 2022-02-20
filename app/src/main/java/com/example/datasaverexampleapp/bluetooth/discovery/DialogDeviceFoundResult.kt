package com.example.datasaverexampleapp.bluetooth.discovery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datasaverexampleapp.databinding.ItemDialogFragmentDeviceResultViewBinding
import com.example.datasaverexampleapp.type_alias.Layout

class DialogDeviceFoundResult(private val deviceFoundList:ArrayList<DiscoveryDevice>) : DialogFragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(Layout.item_dialog_fragment_device_result_view,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.apply {

            DataBindingUtil.setContentView<ItemDialogFragmentDeviceResultViewBinding>(
                this, Layout.item_dialog_fragment_device_result_view
            ).apply {

                deviceFoundResultList.apply {
                    layoutManager = LinearLayoutManager(this@DialogDeviceFoundResult.context)
                    adapter = DeviceDialogListAdapter(deviceFoundList)
                }

                closeDialogButton.setOnClickListener {
                    dismiss()
                }
            }
        }
    }
}