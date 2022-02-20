package com.example.datasaverexampleapp.bluetooth.discovery

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.datasaverexampleapp.bluetooth.base.BluetoothBaseActivity
import com.example.datasaverexampleapp.type_alias.Drawable
import com.example.datasaverexampleapp.type_alias.Layout
import kotlinx.android.synthetic.main.activity_bluetooth_discovery_example.*

/**
 * The process of two devices finding each other to connect is called 'discovery'. Before you can establish a
 * Bluetooth socket for communication, the local Bluetooth Adapter must bond with the remote device.
 * Before two devices can bond and connect, they first need to discover each other.
 */
class BluetoothDiscoveryExampleActivity : BluetoothBaseActivity(Layout.activity_bluetooth_discovery_example)
{
    private val devicesFound : ArrayList<DiscoveryDevice> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Bluetooth discovery Example"

        bluetooth_action_button?.apply {

            setOnClickListener {

                when{
                    this.text == "Enable Bluetooth" -> {

                        enabledBluetooth { enabled ->

                            if (enabled)
                            {
                                bluetooth_icon?.setImageResource(Drawable.ic_bluetooth_enabled)
                                progress_status_text?.text = ""
                                text = "Start discovery"
                            }
                        }
                    }

                    this.text == "Start discovery" -> {
                        setScanMode()
                        enabledDiscovery { enable ->

                            if (enable)
                            {
                                // Once a device has been made discoverable, it can then be discovered by another device.
                                Toast.makeText(this@BluetoothDiscoveryExampleActivity,"discovery enabled",Toast.LENGTH_SHORT).show()
                                startDiscovery()
                            } else {
                                Toast.makeText(this@BluetoothDiscoveryExampleActivity,"discovery failed",Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    this.text == "Stop discovering" -> {
                        progress_status_text?.text = "Stopping discovery...."
                        cancelDiscovery()
                    }
                }
            }

            if (!isBluetoothEnabled())
            {
                bluetooth_icon?.setImageResource(Drawable.ic_bluetooth_disabled)
                text = "Enable Bluetooth"
            } else{
                bluetooth_icon?.setImageResource(Drawable.ic_bluetooth_enabled)
                text = "Start discovery"
                setScanMode()

            }
        }

        view_discover_devices_button?.setOnClickListener {
            DialogDeviceFoundResult(devicesFound).show(supportFragmentManager,null)
        }
    }

    private fun startDiscovery() {
    // To discover a new device, initiate a discovery scan from your local Bluetooth Adapter.
    // Note: The discovery process can take some time to complete (up to 12 seconds). During this time,
    //       performance of your Bluetooth Adapter communications will be seriously degraded. Use the
    //       following technique to check and monitor the discovery status of the Bluetooth Adapter, and
    //       avoid doing high-bandwidth bluetooth operations (including connecting to a new remote Bluetooth
    //       Device) while discovery is in progress.

        devicesFound.clear()
        discover_device_layout?.visibility = View.INVISIBLE
        progress_status_text?.text = "Starting discovery...."
        startDiscovery { succeed ->

            if (succeed)
            {
                bluetooth_icon?.setImageResource(Drawable.ic_bluetooth_enabled)
                bluetooth_action_button?.text = "Stop discovering"
                progress_status_text?.text = "Starting discovery...."
                setScanMode()
            } else {

                if (!isBluetoothEnabled())
                {
                    bluetooth_icon?.setImageResource(Drawable.ic_bluetooth_disabled)
                    bluetooth_action_button?.text = "Enable Bluetooth"
                } else{
                    bluetooth_icon?.setImageResource(Drawable.ic_bluetooth_enabled)
                    bluetooth_action_button?.text = "Start discovery"
                    setScanMode()
                }
                progress_status_text?.text = ""
            }

        }
    }

    private fun setScanMode()
    {
        if (isBluetoothEnabled())
        {
            getBluetoothScanMode { mode ->
                scan_mode?.text = if (mode == -1) "-" else mode.asScanModeText()
            }
        } else {
            scan_mode?.text = "-"
        }
    }

    override fun onBluetoothDiscoveryStarted() {
        super.onBluetoothDiscoveryStarted()
        progress_status_text?.text = "Discovering...."
        bluetooth_action_button?.text = "Stop discovering"
    }

    override fun onBluetoothDiscoveryEnded() {
        super.onBluetoothDiscoveryEnded()
        progress_status_text?.text = ""
        bluetooth_action_button?.text = "Start discovery"
        discover_device_layout?.visibility = View.VISIBLE
        devices_text?.text = "${devicesFound.size} device(s) found"
        view_discover_devices_button?.isEnabled = devicesFound.isNotEmpty()
    }

    override fun onBluetoothRemoteDeviceFound(name: String?, device: BluetoothDevice?) {
        super.onBluetoothRemoteDeviceFound(name, device)
        devicesFound.add(DiscoveryDevice(name, device))
    }

    override fun onBluetoothStateChanged(state: Int) {
        super.onBluetoothStateChanged(state)

        when(state)
        {
            BluetoothAdapter.STATE_OFF -> {
                bluetooth_icon?.setImageResource(Drawable.ic_bluetooth_disabled)
                progress_status_text?.text = ""
            }

            BluetoothAdapter.STATE_ON -> {
                bluetooth_icon?.setImageResource(Drawable.ic_bluetooth_enabled)
                progress_status_text?.text = ""
            }

            BluetoothAdapter.STATE_TURNING_OFF -> {
                bluetooth_icon?.setImageResource(Drawable.ic_bluetooth_enabled)
                progress_status_text?.text = "Turning off..."
            }

            BluetoothAdapter.STATE_TURNING_ON -> {
                bluetooth_icon?.setImageResource(Drawable.ic_bluetooth_disabled)
                progress_status_text?.text = "Turning on..."
            }
        }
    }
}