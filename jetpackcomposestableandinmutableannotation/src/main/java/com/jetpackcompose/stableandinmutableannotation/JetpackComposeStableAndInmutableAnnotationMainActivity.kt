package com.jetpackcompose.stableandinmutableannotation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.jetpackcompose.stableandinmutableannotation.ui.theme.DataSaverExampleAppTheme

class JetpackComposeStableAndInmutableAnnotationMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DataSaverExampleAppTheme {

                var selected by remember { mutableStateOf(false) }
                Column {
                    Checkbox(
                        checked = selected,
                        onCheckedChange = {
                            selected = it
                        }
                    )
                    ContactList(
                        contactListState = ContactListState(
                            isLoading = false,
                            names = listOf("Peter")
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun ContactList(
    contactListState:ContactListState
) {
    Box(contentAlignment = Alignment.Center) {
        if (contactListState.isLoading) {
            CircularProgressIndicator()
        } else {
            Text(text = contactListState.names.toString())
        }
    }
}

// Data classes are mark as stable by default, but if it contains data type beside Boolean, String, Int, Long and Float it will be consider unstable.
//@Stable // The 'Stable' annotation is like a promise to the compose compiler that it will be notify of changes to the value.

// The 'Immutable' is a even stronger promise than 'Stable' to the compiler.
// We tell the compiler that nothing will change in this instance and if the does an entire new instance will be created.
// In this case avoid using 'var' to prevent issues. In case you need to use 'var' use the 'Stable' annotation.
@Immutable
data class ContactListState(
    val isLoading:Boolean,
    val names:List<String>
)