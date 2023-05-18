package com.example.sharedatabetweencomposeview

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sharedatabetweencomposeview.ui.theme.DataSaverExampleAppTheme
import com.example.sharedatabetweencomposeview.using_composition_local_of.UsingCompositionLocalOfExampleMainActivity
import com.example.sharedatabetweencomposeview.using_navigation.ShareDataUsingNavigationExample
import com.example.sharedatabetweencomposeview.using_shared_viewmodel.UsingSharedViewModelMainActivity
import com.example.sharedatabetweencomposeview.using_stateful_dependencies.UsingStatefulDependenciesExampleMainActivity

class ShareDataBetweenComposeViewMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DataSaverExampleAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .padding(all = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        val activity = LocalContext.current as Activity

                        Text(text = "Navigation example", color = Color.Black, fontSize = 20.sp, modifier = Modifier.fillMaxWidth().padding(all = 10.dp), textAlign = TextAlign.Center)

                        Button(onClick = {
                            val shareDataUsingNavigationIntent = Intent(activity, ShareDataUsingNavigationExample::class.java)
                            activity.startActivity(shareDataUsingNavigationIntent)
                        }) {
                            Text(text = "Shara data using navigation")
                        }

                        Button(onClick = {
                            val usingSharedViewModelExampleIntent = Intent(activity, UsingSharedViewModelMainActivity::class.java)
                            activity.startActivity(usingSharedViewModelExampleIntent)
                        }) {
                            Text(text = "Using shared view model")
                        }

                        Button(onClick = {
                            val usingStatefulDependenciesExampleIntent = Intent(activity, UsingStatefulDependenciesExampleMainActivity::class.java)
                            activity.startActivity(usingStatefulDependenciesExampleIntent)
                        }) {
                            Text(text = "Using stateful dependencies")
                        }

                        Button(onClick = {
                            val usingCompositionLocalOfExampleIntent = Intent(activity, UsingCompositionLocalOfExampleMainActivity::class.java)
                            activity.startActivity(usingCompositionLocalOfExampleIntent)
                        }) {
                            Text(text = "Using composition local of")
                        }
                    }
                }
            }
        }
    }
}