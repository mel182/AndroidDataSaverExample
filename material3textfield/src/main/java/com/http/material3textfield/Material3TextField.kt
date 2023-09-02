package com.http.material3textfield

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MonitorWeight
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.http.material3textfield.ui.theme.DataSaverExampleAppTheme

class Material3TextField : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DataSaverExampleAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    val context = LocalContext.current

                    Column(modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        var filledText by remember {
                            mutableStateOf("")
                        }

                        TextField(
                            value = filledText,
                            onValueChange = {
                                filledText = it
                            },
                            textStyle = LocalTextStyle.current.copy(
                                textAlign = TextAlign.Right
                            ),
                            label = {
                                Text(text = "Enter your weight")
                            },
                            placeholder = {
                                Text(text = "Weight")
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.MonitorWeight,
                                    contentDescription = null
                                )
                            },
                            suffix = {
                                Text(text = "kg")
                            },
                            supportingText = {
                                Text(text = "*required")
                            },
                            isError = false,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Decimal,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = {
                                    Toast.makeText(context,"Keyboard action clicked!",Toast.LENGTH_SHORT).show()
                                }
                            )
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        var outlineText by remember {
                            mutableStateOf("")
                        }

                        OutlinedTextField(
                            value = outlineText,
                            onValueChange = {
                                filledText = it
                            },
                            textStyle = LocalTextStyle.current.copy(
                                textAlign = TextAlign.Right
                            ),
                            label = {
                                Text(text = "Enter your weight")
                            },
                            placeholder = {
                                Text(text = "Weight")
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.MonitorWeight,
                                    contentDescription = null
                                )
                            },
                            suffix = {
                                Text(text = "kg")
                            },
                            supportingText = {
                                Text(text = "*required")
                            },
                            isError = false,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Decimal,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = {
                                    Toast.makeText(context,"Keyboard action clicked!",Toast.LENGTH_SHORT).show()
                                }
                            )
                        )

                    }
                }
            }
        }
    }
}