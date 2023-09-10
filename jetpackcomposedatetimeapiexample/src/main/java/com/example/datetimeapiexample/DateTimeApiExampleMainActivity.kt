package com.example.datetimeapiexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.datetimeapiexample.ui.theme.DataSaverExampleAppTheme
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class DateTimeApiExampleMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            ComposeView(this).apply {
                setContent {
                    DataSaverExampleAppTheme {

                        val date = remember {
                            LocalDate.now()
                        }

                        val dayOfWeek = remember {
                            LocalDate.now().dayOfWeek
                        }

                        val dayOfYear = remember {
                            LocalDate.now().dayOfYear
                        }

                        val plus13days = remember {
                            LocalDate.now().plusDays(13)
                        }

                        val localTimeNow = remember {
                            LocalTime.now()
                        }

                        val localTimePlus2Hours = remember {
                            LocalTime.now().plusHours(2)
                        }

                        val localDateTimeNow = remember {
                            LocalDateTime.now()
                        }

                        val zonedDateTimeNow = remember {
                            ZonedDateTime.now()
                        }

                        val zonedDateTimeFormatter = remember {
                            DateTimeFormatter.ofPattern("EEE dd MM yyyy HH:mm:ss").format(zonedDateTimeNow)
                        }

                        Column(modifier = Modifier
                            .fillMaxSize()
                            .background(color = Color.White)
                            .padding(horizontal = 16.dp)
                        ) {
                            Text(text = "Local date now")
                            Text(text = date.toString(), fontWeight = FontWeight.Bold, color = Color.Blue, modifier = Modifier.padding(all = 8.dp))
                            Text(text = "Local date Day of week")
                            Text(text = dayOfWeek.toString(), fontWeight = FontWeight.Bold, color = Color.Blue, modifier = Modifier.padding(all = 8.dp))
                            Text(text = "Local date Day of year")
                            Text(text = dayOfYear.toString(), fontWeight = FontWeight.Bold, color = Color.Blue, modifier = Modifier.padding(all = 8.dp))
                            Text(text = "Local date plus 13 days")
                            Text(text = plus13days.toString(), fontWeight = FontWeight.Bold, color = Color.Blue, modifier = Modifier.padding(all = 8.dp))

                            Divider(modifier = Modifier.fillMaxWidth().height(1.dp).background(color = Color.Blue))

                            Text(text = "Local time now")
                            Text(text = localTimeNow.toString(),
                                fontWeight = FontWeight.Bold,
                                color = Color.Blue,
                                modifier = Modifier.padding(all = 8.dp)
                            )
                            Text(text = "Local time plus 2 hours")
                            Text(text = localTimePlus2Hours.toString(),
                                fontWeight = FontWeight.Bold,
                                color = Color.Blue,
                                modifier = Modifier.padding(all = 8.dp)
                            )

                            Divider(modifier = Modifier.fillMaxWidth().height(1.dp).background(color = Color.Blue))

                            Text(text = "Local datetime now")
                            Text(text = localDateTimeNow.toString(),
                                fontWeight = FontWeight.Bold,
                                color = Color.Blue,
                                modifier = Modifier.padding(all = 8.dp)
                            )

                            Divider(modifier = Modifier.fillMaxWidth().height(1.dp).background(color = Color.Blue))

                            Text(text = "Zoned date time now")
                            Text(text = zonedDateTimeNow.toString(),
                                fontWeight = FontWeight.Bold,
                                color = Color.Blue,
                                modifier = Modifier.padding(all = 8.dp)
                            )

                            Text(text = "Zoned date time formatter")
                            Text(text = zonedDateTimeFormatter,
                                fontWeight = FontWeight.Bold,
                                color = Color.Blue,
                                modifier = Modifier.padding(all = 8.dp)
                            )

                        }
                    }
                }
            }
        )
    }
}