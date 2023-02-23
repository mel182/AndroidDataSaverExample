package com.example.jetpackcomposedownloadmanagerexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.jetpackcomposedownloadmanagerexample.ui.theme.DataSaverExampleAppTheme

class DownLoadManagerExampleMainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DataSaverExampleAppTheme {
                // A surface container using the 'background' color from the theme
                val download = AndroidDownloader(context = this)
                download.downloadFile("https://pl-coding.com/wp-content/uploads/2022/04/pic-squared.jpg")
            }
        }
    }
}