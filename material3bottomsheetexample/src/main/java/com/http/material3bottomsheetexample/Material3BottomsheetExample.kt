@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.http.material3bottomsheetexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.http.material3bottomsheetexample.ui.theme.DataSaverExampleAppTheme
import kotlinx.coroutines.launch

class Material3BottomsheetExample : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DataSaverExampleAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    val sheetState = rememberModalBottomSheetState()
                    var isSheetOpen by rememberSaveable {
                        mutableStateOf(false)
                    }
                    val scaffoldState = rememberBottomSheetScaffoldState()
                    val scope = rememberCoroutineScope()

                    // This is used for the modal bottom sheet
//                    Box(
//                        modifier = Modifier.fillMaxSize(),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Button(onClick = {
//                            isSheetOpen = true
//                        }) {
//                            Text(text = "Open sheet")
//                        }
//                    }

                    BottomSheetScaffold(
                        scaffoldState = scaffoldState,
                        sheetContent = {
                            Column(modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 52.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ferrari_f1),
                                    contentDescription = null
                                )
                            }
                        },
                        sheetPeekHeight = 0.dp
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize().background(color = Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Button(onClick = {
                                scope.launch {
                                    scaffoldState.bottomSheetState.expand()
                                }
                            }) {
                                Text(text = "Open sheet")
                            }
                        }
                    }
                    
                    // This is the modal bottom sheet
//                    if (isSheetOpen) {
//                        ModalBottomSheet(
//                            sheetState = sheetState,
//                            onDismissRequest = {
//                                isSheetOpen = false
//                            }
//                        ) {
//                            Column(modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 52.dp),
//                                horizontalAlignment = Alignment.CenterHorizontally,
//                                verticalArrangement = Arrangement.Center
//                            ) {
//                                Image(
//                                    painter = painterResource(id = R.drawable.ferrari_f1),
//                                    contentDescription = null
//                                )
//                            }
//                        }
//                    }
                }
            }
        }
    }
}