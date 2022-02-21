@file:Suppress("BlockingMethodInNonBlockingContext")

package com.example.datasaverexampleapp.wifi_p2p.server

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.databinding.ActivityWifiP2PserverExampleBinding
import com.example.datasaverexampleapp.type_alias.Layout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.ServerSocket
import java.net.Socket
import java.util.*

class Wifi_P2P_ServerExampleActivity : AppCompatActivity() {

    private val port = 8666
    private var serverClient: Socket? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wifi_p2_pserver_example)
        title = "Wifi peer to peer server"

        DataBindingUtil.setContentView<ActivityWifiP2PserverExampleBinding>(
            this, Layout.activity_wifi_p2_pserver_example
        ).apply {
            startWifiP2pHostButton.apply {

                setOnClickListener {

                    when(this.text.toString().lowercase(Locale.ROOT))
                    {
                        "start" -> {
                            wifiP2pServerHostStatus.text = "Starting...."

                            try {
                                val serverSocket = ServerSocket(port)
                                wifiP2pServerHostStatus.text = "Hosting on port: $port"
                                startWifiP2pHostButton.text = "stop"
                                CoroutineScope(Dispatchers.IO).launch {
                                    serverClient = serverSocket.accept()
                                }
                            } catch (e: IOException) {
                                Log.i("TAG","Error starting server: ${e.message}")
                            }
                        }

                        "stop" -> {
                            serverClient?.let {
                                if (!it.isClosed)
                                    it.close()

                                wifiP2pServerHostStatus.text = "-"
                                startWifiP2pHostButton.text = "start"
                            }
                        }
                    }
                }
            }
        }
    }
}