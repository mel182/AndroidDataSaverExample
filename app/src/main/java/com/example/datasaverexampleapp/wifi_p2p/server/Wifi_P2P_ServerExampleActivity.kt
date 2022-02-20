@file:Suppress("BlockingMethodInNonBlockingContext")

package com.example.datasaverexampleapp.wifi_p2p.server

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.datasaverexampleapp.R
import kotlinx.android.synthetic.main.activity_wifi_p2_pserver_example.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

        start_wifi_p2p_host_button?.apply {


            setOnClickListener {

                when(this.text.toString().lowercase(Locale.ROOT))
                {
                    "start" -> {
                        wifi_p2p_server_host_status?.text = "Starting...."

                        try {
                            val serverSocket = ServerSocket(port)
                            wifi_p2p_server_host_status?.text = "Hosting on port: $port"
                            start_wifi_p2p_host_button?.text = "stop"
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

                            wifi_p2p_server_host_status?.text = "-"
                            start_wifi_p2p_host_button?.text = "start"
                        }
                    }
                }
            }
        }
    }
}