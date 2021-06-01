package com.example.datasaverexampleapp.wifi_p2p

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.wifi_p2p.server.Wifi_P2P_ServerExampleActivity
import kotlinx.android.synthetic.main.activity_wifi_p2_pexample.*

class Wifi_P2P_ExampleActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Wifi peer to peer Example"
        setContentView(R.layout.activity_wifi_p2_pexample)

        wifi_p2p_server?.setOnClickListener {
            val intent = Intent(this, Wifi_P2P_ServerExampleActivity::class.java)
            startActivity(intent)
        }
    }
}