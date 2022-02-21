@file:Suppress("UNNECESSARY_SAFE_CALL")

package com.example.datasaverexampleapp.bluetooth.discovery

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.datasaverexampleapp.bluetooth.base.BluetoothBaseActivity
import com.example.datasaverexampleapp.databinding.ActivityBluetoothDiscoveryExampleBinding
import com.example.datasaverexampleapp.type_alias.Drawable
import com.example.datasaverexampleapp.type_alias.Layout

/**
 * The process of two devices finding each other to connect is called 'discovery'. Before you can establish a
 * Bluetooth socket for communication, the local Bluetooth Adapter must bond with the remote device.
 * Before two devices can bond and connect, they first need to discover each other.
 */
class BluetoothDiscoveryExampleActivity : BluetoothBaseActivity(Layout.activity_bluetooth_discovery_example)
{
    private val devicesFound : ArrayList<DiscoveryDevice> = ArrayList()
    private var binding: ActivityBluetoothDiscoveryExampleBinding? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Bluetooth discovery Example"

        binding = DataBindingUtil.setContentView<ActivityBluetoothDiscoveryExampleBinding>(
            this, Layout.activity_bluetooth_discovery_example
        ).apply {

            bluetoothActionButton.apply {

                setOnClickListener {

                    when{
                        this.text == "Enable Bluetooth" -> {

                            enabledBluetooth { enabled ->

                                if (enabled)
                                {
                                    bluetoothIcon.setImageResource(Drawable.ic_bluetooth_enabled)
                                    progressStatusText.text = ""
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
                            progressStatusText.text = "Stopping discovery...."
                            cancelDiscovery()
                        }
                    }
                }

                if (!isBluetoothEnabled())
                {
                    bluetoothIcon.setImageResource(Drawable.ic_bluetooth_disabled)
                    text = "Enable Bluetooth"
                } else{
                    bluetoothIcon?.setImageResource(Drawable.ic_bluetooth_enabled)
                    text = "Start discovery"
                    setScanMode()

                }
            }

            viewDiscoverDevicesButton.setOnClickListener {
                DialogDeviceFoundResult(devicesFound).show(supportFragmentManager,null)
            }
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

        binding?.apply {

            discoverDeviceLayout.visibility = View.INVISIBLE
            progressStatusText.text = "Starting discovery...."
            startDiscovery { succeed ->

                if (succeed)
                {
                    bluetoothIcon.setImageResource(Drawable.ic_bluetooth_enabled)
                    bluetoothActionButton.text = "Stop discovering"
                    progressStatusText.text = "Starting discovery...."
                    setScanMode()
                } else {

                    if (!isBluetoothEnabled())
                    {
                        bluetoothIcon.setImageResource(Drawable.ic_bluetooth_disabled)
                        bluetoothActionButton.text = "Enable Bluetooth"
                    } else{
                        bluetoothIcon.setImageResource(Drawable.ic_bluetooth_enabled)
                        bluetoothActionButton.text = "Start discovery"
                        setScanMode()
                    }
                    progressStatusText.text = ""
                }
            }
        }
    }

    private fun setScanMode()
    {
        binding?.apply {
            if (isBluetoothEnabled())
            {
                getBluetoothScanMode { mode ->
                    scanMode.text = if (mode == -1) "-" else mode.asScanModeText()
                }
            } else {
                scanMode.text = "-"
            }
        }
    }

    override fun onBluetoothDiscoveryStarted() {
        super.onBluetoothDiscoveryStarted()
        binding?.apply {
            progressStatusText.text = "Discovering...."
            bluetoothActionButton.text = "Stop discovering"
        }

    }

    override fun onBluetoothDiscoveryEnded() {
        super.onBluetoothDiscoveryEnded()
        binding?.apply {
            progressStatusText.text = ""
            bluetoothActionButton.text = "Start discovery"
            discoverDeviceLayout.visibility = View.VISIBLE
            devicesText.text = "${devicesFound.size} device(s) found"
            viewDiscoverDevicesButton.isEnabled = devicesFound.isNotEmpty()
        }
    }

    override fun onBluetoothRemoteDeviceFound(name: String?, device: BluetoothDevice?) {
        super.onBluetoothRemoteDeviceFound(name, device)
        devicesFound.add(DiscoveryDevice(name, device))
    }

    override fun onBluetoothStateChanged(state: Int) {
        super.onBluetoothStateChanged(state)

        binding?.apply {

            when(state)
            {
                BluetoothAdapter.STATE_OFF -> {
                    bluetoothIcon.setImageResource(Drawable.ic_bluetooth_disabled)
                    progressStatusText.text = ""
                }

                BluetoothAdapter.STATE_ON -> {
                    bluetoothIcon.setImageResource(Drawable.ic_bluetooth_enabled)
                    progressStatusText.text = ""
                }

                BluetoothAdapter.STATE_TURNING_OFF -> {
                    bluetoothIcon.setImageResource(Drawable.ic_bluetooth_enabled)
                    progressStatusText.text = "Turning off..."
                }

                BluetoothAdapter.STATE_TURNING_ON -> {
                    bluetoothIcon.setImageResource(Drawable.ic_bluetooth_disabled)
                    progressStatusText.text = "Turning on..."
                }
            }
        }
    }
}