@file:OptIn(ExperimentalMaterial3Api::class)

package com.http.jetpackcomposenavigatewithresult

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.http.jetpackcomposenavigatewithresult.ui.theme.DataSaverExampleAppTheme

class JetpackComposeNavigateWithResultMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DataSaverExampleAppTheme {
                // A surface container using the 'background' color from the theme
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "screen1"
                ) {
                    composable("screen1") { entry ->
                        val text = entry.savedStateHandle.get<String>("my_text")
                        Column(modifier = Modifier.fillMaxSize()) {
                            text?.let {
                                Text(text = it)
                            }
                            Button(onClick = {
                                navController.navigate("screen2")
                            }) {
                                Text(text = "Go to screen 2")
                            }
                        }
                        
                    }
                    composable("screen2") {
                        Column(modifier = Modifier.fillMaxSize()
                        ) {
                            var text by remember {
                                mutableStateOf("")
                            }
                            
                            OutlinedTextField(
                                value = text,
                                textStyle = TextStyle(color = Color.Black),
                                onValueChange = { text = it },
                                modifier = Modifier.width(300.dp)
                            )
                            
                            Button(onClick = {
                                navController.apply {
                                    previousBackStackEntry
                                        ?.savedStateHandle
                                        ?.set("my_text",text)

                                    popBackStack()
                                }
                            }) {
                                Text(text = "Apply")
                            }
                        }
                    }
                }
            }
        }
    }
}