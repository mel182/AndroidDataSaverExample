package com.example.datasaverexampleapp.wifi_p2p

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.databinding.ActivityWifiP2PexampleBinding
import com.example.datasaverexampleapp.type_alias.Layout
import com.example.datasaverexampleapp.wifi_p2p.server.Wifi_P2P_ServerExampleActivity

class Wifi_P2P_ExampleActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Wifi peer to peer Example"
        setContentView(R.layout.activity_wifi_p2_pexample)

        DataBindingUtil.setContentView<ActivityWifiP2PexampleBinding>(
            this, Layout.activity_wifi_p2_pexample
        ).apply {
            wifiP2pServer.setOnClickListener {
                val intent = Intent(this@Wifi_P2P_ExampleActivity, Wifi_P2P_ServerExampleActivity::class.java)
                startActivity(intent)
            }
        }
    }
}