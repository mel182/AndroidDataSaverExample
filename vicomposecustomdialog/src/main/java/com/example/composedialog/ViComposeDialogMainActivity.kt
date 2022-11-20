@file:OptIn(ExperimentalAnimationApi::class)

package com.example.composedialog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.composedialog.ui.theme.DataSaverExampleAppTheme

class ViComposeDialogMainActivity : ComponentActivity() {

    val dialogComposeViewModel by viewModels<ViDialogExampleViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val openDialog = dialogComposeViewModel.showDialog.collectAsState()

            DataSaverExampleAppTheme {
                // A surface container using the 'background' color from the theme
                
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White)) {

                    Column(modifier = Modifier
                        .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center) {

                        Button(onClick = { dialogComposeViewModel.showDialog(show = true) }) {
                            Text(text = "Show dialog")
                        }
                    }
                        
                    AnimatedVisibility(
                        visible = openDialog.value,
                        enter = scaleIn(),
                        exit = scaleOut()
                    ) {
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .background(color = Color(0x80000000)), contentAlignment = Alignment.Center) {
                            Button(onClick = { dialogComposeViewModel.showDialog(show = false) }) {
                                Text(text = "Dismiss")
                            }
                        }
                    }
                }
            }
        }
    }
}