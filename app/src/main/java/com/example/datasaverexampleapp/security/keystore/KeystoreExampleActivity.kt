package com.example.datasaverexampleapp.security.keystore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

class KeystoreExampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Keystore security example"
        setContent {

            var keystoreValue by remember {
                mutableStateOf("")
            }

            var newKeystoreKey by remember {
                mutableStateOf("")
            }

            var newKeystoreValue by remember {
                mutableStateOf("")
            }

            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(all = 10.dp)) {

                Row(modifier = Modifier.padding(all = 10.dp)) {
                    TextField(value = "", onValueChange = {

                    })
                    Button(onClick = {  }, modifier = Modifier.padding(start = 10.dp)) {
                        Text(text = "Get value")
                    }
                }

                BasicTextField(
                    value = newKeystoreKey,
                    onValueChange = { newKeystoreKey = it },
                    decorationBox = { innerTextField ->
                        Row(
                            Modifier
                                .background(Color.LightGray, RoundedCornerShape(percent = 30))
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {

                            if (newKeystoreKey.isEmpty()) {
                                Text("Enter keystore key...")
                            }
                            // <-- Add this
                            innerTextField()
                        }
                    },
                    modifier = Modifier.padding(all = 10.dp)
                )

                BasicTextField(
                    value = newKeystoreValue,
                    onValueChange = { newKeystoreValue = it },
                    decorationBox = { innerTextField ->
                        Row(
                            Modifier
                                .background(Color.LightGray, RoundedCornerShape(percent = 30))
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {

                            if (newKeystoreValue.isEmpty()) {
                                Text("Enter keystore value...")
                            }
                            // <-- Add this
                            innerTextField()
                        }
                    },
                    modifier = Modifier.padding(all = 10.dp)
                )

                val context = LocalContext.current

                Button(onClick = {

                    if (newKeystoreKey.isEmpty() || newKeystoreValue.isEmpty()) {
                        //context
                        Toast.makeText(context,"Key or value field is empty",Toast.LENGTH_SHORT).show()
                    } else {
                        newKeystoreKey = ""
                        newKeystoreValue = ""
                        Toast.makeText(context,"New keystore inserted",Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.padding(all = 10.dp)) {
                    Text(text = "add")
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Keystore value",
                        modifier = Modifier.padding(16.dp),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = keystoreValue,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}