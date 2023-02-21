package com.example.alarmmanagerexample

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.alarmmanagerexample.ui.theme.DataSaverExampleAppTheme
import java.time.LocalDateTime

/**
 * This is a alarm manager example and the difference between alarm manager and work manager is that the
 * user is aware of the task being executed. Work manager on the other hand is used for background tasks
 * and the user is not aware of the task.
 */
class AlarmManagerMainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val scheduler = AndroidAlarmScheduler(this)
        var alarmItem: AlarmItem? = null

        setContentView(
            ComposeView(this).apply {
                setContent {
                    DataSaverExampleAppTheme {

                        var secondText by remember {
                            mutableStateOf("")
                        }

                        var message by remember {
                            mutableStateOf("")
                        }

                        Column(verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = Color.White)
                        ) {

                            Column(modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = 16.dp)) {

                                OutlinedTextField(
                                    value = secondText,
                                    onValueChange = { secondText = it },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(width = 1.dp, color = Color.Blue),
                                    placeholder = {
                                        Text(text = "Trigger alarm in seconds", color = Color.LightGray)
                                    }
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                OutlinedTextField(
                                    value = message,
                                    onValueChange = { message = it },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(width = 1.dp, color = Color.Blue),
                                    placeholder = {
                                        Text(text = "Message", color = Color.LightGray)
                                    }
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {

                                    Button(onClick = {

                                        secondText.toLongOrNull()?.let {
                                            alarmItem = AlarmItem(
                                                localDateTime = LocalDateTime.now().plusSeconds(secondText.toLong()),
                                                message = message
                                            )
                                            alarmItem?.let(scheduler::schedule)
                                            secondText = ""
                                            message = ""
                                        }?: kotlin.run {
                                            Toast.makeText(this@AlarmManagerMainActivity,"Invalid second value", Toast.LENGTH_LONG).show()
                                        }
                                    }) {
                                        Text(text = "Schedule")
                                    }

                                    Button(onClick = {
                                        alarmItem?.let(scheduler::cancel)
                                    }) {
                                        Text(text = "Cancel")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}