package com.example.datasaverexampleapp.bluetooth.server

import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.bluetooth.base.BluetoothBaseActivity
import com.example.datasaverexampleapp.type_alias.Layout
import kotlinx.android.synthetic.main.activity_bluetooth_server_example.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

/**
 * The Android Bluetooth communication APIs are wrappers around RFCOMM, the Bluetooth radio frequency communication protocol.
 * RFCOMM support RS232 serial communication over the Logical Link Control and Adaptation Protocol (L2CAP) layer.
 * In practice, this alphabet soup provides a mechanism for opening communication sockets between two paired Bluetooth devices.
 *
 * Note: Before your application can communicate between devices, the users will be prompted to pair them before the connection
 *       is established.
 *
 * You can establish an RFCOMM communication channel for bidirectional communications using the following classes:
 * - 'BluetoothServerSocket' -> Used to establish a listening socket for initiating a link between devices. To establish
 *                              a handshake,one device acts as a server to listen for, and accept, incoming connection
 *                              requests.
 *
 * - 'BluetoothSocket' -> Used to create a new client to connect to a listening Bluetooth Server Socket. Also
 *                        returned by the Bluetooth Server Socket after a connection is established, Bluetooth
 *                        Sockets are used by both the server and client to transfer data streams.
 *
 * When creating an application that uses Bluetooth as a peer-to-peer transport layer across devices, you'll need to implement
 * both a Bluetooth Server Socket to listen for connections and a Bluetooth Socket to initiate a new channel and handle communication.
 *
 * When connected, the Bluetooth Server Socket returns a 'BluetoothSocket' that can be used to send and receive data. This server-side
 * Bluetooth Socket is used in exactly the same way as the client socket. The designations of server and client are relevant only to how
 * the connection is established; they don't effect how data flows after that connection is made.
 *
 * |-------- BLUETOOTH SERVER --------|
 * Bluetooth Server Socket is used to listen for incoming Bluetooth Socket connection requests from remote Bluetooth Devices.
 */
class BluetoothServerExampleActivity : BluetoothBaseActivity(Layout.activity_bluetooth_server_example)
{
    private val name = "MyTestBluetoothServer"
    private val uuid = UUID.randomUUID()
    private var socket : BluetoothServerSocket? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Bluetooth Server Example"



        start_host_button?.setOnClickListener {
            startHost()
        }

    }

    private fun startHost()
    {
        server_host_status?.text = "Starting....."
        if (isBluetoothEnabled())
        {
            listeningUsingRfcommWithServiceRecord(name, uuid) { bluetooth_server ->
                socket = bluetooth_server
                server_host_status?.text = "Hosting...."
            }
        } else {
            enabledBluetooth { succeed ->

                if (succeed)
                {
                    listeningUsingRfcommWithServiceRecord(name, uuid) { bluetooth_server ->
                        socket = bluetooth_server
                    }

                } else {
                    server_host_status?.text = "Failed starting server"
                }
            }
        }
    }
}