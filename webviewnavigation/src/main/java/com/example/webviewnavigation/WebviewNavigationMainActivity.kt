package com.example.webviewnavigation

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.webviewnavigation.delegates.topRowHeightDelegate
import com.example.webviewnavigation.ui.theme.DataSaverExampleAppTheme

class WebviewNavigationMainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            Log.i("TAG12","set content called!")
            val webViewViewModel by viewModels<WebViewViewModel>()
            val rowHeightDelegate = topRowHeightDelegate(webViewViewModel = webViewViewModel)

            Column(modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)) {

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .height(rowHeightDelegate.height.dp),
                    verticalAlignment = Alignment.CenterVertically) {

                    Image(painter = painterResource(id = R.drawable.back_button), modifier = Modifier
                        .size(rowHeightDelegate.iconSize.dp)
                        .clickable {
                            Log.i("TAG12", "go back clicked!")
                            webViewViewModel.goBack(true)
                        },contentDescription = null)
                }

                CreateView(webViewViewModel = webViewViewModel,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight())
            }
        }
    }
}


@Composable
fun CreateView(webViewViewModel:WebViewViewModel,modifier: Modifier) {

    Log.i("TAG12","create view")

    val mustGoBack by webViewViewModel.goBack.collectAsState()

    AndroidView(factory = {
        Log.i("TAG12","android view factory")
        WebView(it).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    webViewViewModel.goBackEnabled(view?.canGoBack() ?: false)
                    Log.i("TAG12","page started with url: $url")
                }
            }
            loadUrl("https://www.google.com")
        }
    }, update = {

        Log.i("TAG12","update statement")

        if (mustGoBack) {
            Log.i("TAG12","must go back web view with web view: ${it}")
            if (it.canGoBack()) {
                it.goBack()
                webViewViewModel.goBack(false)
            }
        }

    }, modifier = modifier)
}