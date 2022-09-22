package com.example.datasaverexampleapp.security.keystore

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.unit.sp
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

@RequiresApi(Build.VERSION_CODES.M)
class KeystoreExampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Keystore security example"
        setContent {
            val context = LocalContext.current
            var valueToDecrypt by remember {
                mutableStateOf("")
            }

            var valueToEncrypt by remember {
                mutableStateOf("")
            }

            var valueToShow by remember {
                mutableStateOf("")
            }

            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(all = 10.dp)) {

                Row(modifier = Modifier.padding(start = 5.dp, end = 5.dp)) {
                    TextField(value = valueToEncrypt, onValueChange = {
                        valueToEncrypt = it
                    }, placeholder = { Text(text = "Encrypt value") })
                    Button(onClick = {
                        if (valueToEncrypt.isNotBlank()) {
                            val bytes = valueToEncrypt.encodeToByteArray()
                            val file = File(filesDir,"secret.txt")
                            if (!file.exists())
                                file.createNewFile()

                            val fileOutputStream = FileOutputStream(file)
                            val encryptedValue = CryptoManager.encrypt(
                                bytes = bytes,
                                outputStream = fileOutputStream
                            )
                            valueToEncrypt = ""
                            valueToShow = encryptedValue.decodeToString()
                            Toast.makeText(context,"Value encrypted",Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context,"Value to encrypt is empty",Toast.LENGTH_SHORT).show()
                        }
                    }, modifier = Modifier.padding(start = 10.dp)) {
                        Text(text = "Encrypt", fontSize = 12.sp)
                    }
                }

                BasicTextField(
                    value = valueToDecrypt,
                    onValueChange = { valueToDecrypt = it },
                    decorationBox = { innerTextField ->
                        Row(
                            Modifier
                                .background(Color.LightGray, RoundedCornerShape(percent = 30))
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {

                            if (valueToDecrypt.isEmpty()) {
                                Text("Enter value to encrypt...")
                            }
                            // <-- Add this
                            innerTextField()
                        }
                    },
                    modifier = Modifier.padding(all = 10.dp)
                )

                Button(onClick = {

                    if (valueToDecrypt.isEmpty()) {
                        //context
                        Toast.makeText(context,"Values is empty",Toast.LENGTH_SHORT).show()
                    } else {

                        val file = File(filesDir,"secret.txt")
                        val decryptedValue = CryptoManager.decrypt(
                            FileInputStream(file)
                        )
                        valueToDecrypt = ""
                        valueToShow = decryptedValue.decodeToString()
                        Toast.makeText(context,"New encrypted value inserted",Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.padding(all = 10.dp)) {
                    Text(text = "Decrypt", fontSize = 12.sp)
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Value",
                        modifier = Modifier.padding(16.dp),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = valueToShow,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}