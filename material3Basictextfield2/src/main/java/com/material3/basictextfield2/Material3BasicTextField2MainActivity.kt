@file:OptIn(ExperimentalFoundationApi::class)

package com.material3.basictextfield2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.BasicTextField2
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.foundation.text.input.TextFieldCharSequence
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.material3.basictextfield2.ui.theme.DataSaverExampleAppTheme

class Material3BasicTextField2MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DataSaverExampleAppTheme {
                // A surface container using the 'background' color from the theme
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.White)
                        .padding(all = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    var text1 by remember {
                        mutableStateOf("")
                    }
                    var text2 by remember {
                        mutableStateOf("")
                    }

                    val modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.LightGray)
                        .padding(all = 16.dp)

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "Normal basic text field")
                        BasicTextField(
                            modifier = modifier,
                            value = text1,
                            onValueChange = { text1 = it }
                        )
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "Basic text field 2")
                        //val state = rememberTextFieldState() // Such state survive screen rotation and process death
                        val state = TextFieldState() // Can be used in a view model to keep track of change and also validate input
                        BasicTextField2(
                            state = state,
                            modifier = modifier,
                            inputTransformation = AndroidInputTransformation
                        )
                    }
                }
            }
        }
    }
}

// Example to only let user insert Android in text field
object AndroidInputTransformation: InputTransformation {
    override fun transformInput(
        originalValue: TextFieldCharSequence,
        valueWithChanges: TextFieldBuffer
    ) {
        if (!"Android".contains(valueWithChanges.asCharSequence())) {
            valueWithChanges.revertAllChanges()
        }
    }
}

