package com.http.material3selectioncomponent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import com.http.material3selectioncomponent.ui.theme.DataSaverExampleAppTheme

class Material3SelectionComponent : ComponentActivity() {

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
                        .padding(all = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(32.dp)
                    ) {
                        Checkboxes()
                        MySwitch()
                        RadioButtons()
                    }
                }
            }
        }
    }
}

data class ToggleableInfo(
    val isChecked:Boolean,
    val text:String
)

@Composable
private fun Checkboxes() {

    val checkboxes = remember {
        mutableStateListOf(
            ToggleableInfo(
                isChecked = false,
                text = "Photos"
            ),
            ToggleableInfo(
                isChecked = false,
                text = "Videos"
            ),
            ToggleableInfo(
                isChecked = false,
                text = "Audio"
            )
        )
    }

    var triState by remember {
        mutableStateOf(ToggleableState.Indeterminate)
    }
    val toggleTriState = {
        triState = when(triState) {
            ToggleableState.Indeterminate -> ToggleableState.On
            ToggleableState.On -> ToggleableState.Off
            else -> ToggleableState.On
        }
        checkboxes.indices.forEach { index ->
            checkboxes[index] = checkboxes[index].copy(
                isChecked = triState == ToggleableState.On
            )
        }
    }

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable {
                    toggleTriState()
                }
                .padding(end = 16.dp)
        ) {
            TriStateCheckbox(
                state = triState,
                onClick = toggleTriState
            )

            Text(text = "File types")
        }

        checkboxes.forEachIndexed { index, info ->

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(start = 32.dp)
                    .clickable {
                        checkboxes[index] = info.copy(
                            isChecked = !info.isChecked
                        )
                    }
                    .padding(end = 16.dp)
            ) {
                Checkbox(
                    checked = info.isChecked,
                    onCheckedChange = { isChecked ->
                        checkboxes[index] = info.copy(
                            isChecked = isChecked
                        )
                    }
                )
                Text(text = info.text)
            }
        }
    }
}

@Composable
private fun MySwitch() {

    var switch by remember {
        mutableStateOf(
            ToggleableInfo(
                isChecked = false,
                text = "Dark mode"
            )
        )
    }

    Row(modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = switch.text)
        Switch(
            checked = switch.isChecked,
            onCheckedChange = { isChecked ->
                switch = switch.copy(isChecked = isChecked)
            },
            thumbContent = {
                Icon(
                    imageVector = if (switch.isChecked) {
                        Icons.Default.Check
                    } else {
                        Icons.Default.Close
                    },
                    contentDescription = null
                )
            }
        )
    }
}

@Composable
private fun RadioButtons() {

    val radioButtons = remember {
        mutableStateListOf(
            ToggleableInfo(
                isChecked = true,
                text = "Photos"
            ),
            ToggleableInfo(
                isChecked = false,
                text = "Videos"
            ),
            ToggleableInfo(
                isChecked = false,
                text = "Audio"
            )
        )
    }

    Column {

        radioButtons.forEachIndexed { index, info ->

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable {
                        for (list_index in radioButtons.indices) {
                            radioButtons[list_index] = radioButtons[list_index].copy(
                                isChecked = radioButtons[list_index].text == info.text
                            )
                        }
                    }
                    .padding(end = 16.dp)
            ) {
                RadioButton(
                    selected = info.isChecked,
                    onClick = {
                        for (list_index in radioButtons.indices) {
                            radioButtons[list_index] = radioButtons[list_index].copy(
                                isChecked = radioButtons[list_index].text == info.text
                            )
                        }
                    }
                )
                Text(text = info.text)
            }
        }
    }
}