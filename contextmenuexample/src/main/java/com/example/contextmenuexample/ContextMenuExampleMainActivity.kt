package com.example.contextmenuexample

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.contextmenuexample.ui.theme.DataSaverExampleAppTheme

class ContextMenuExampleMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DataSaverExampleAppTheme {
                // A surface container using the 'background' color from the theme

                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    items(
                        listOf(
                            "Melchior 1",
                            "Melchior 2",
                            "Melchior 3",
                            "Melchior 4",
                            "Melchior 5",
                            "Melchior 6",
                            "Melchior 7",
                            "Melchior 8",
                            "Melchior 9",
                            "Melchior 10",
                            "Melchior 11",
                            "Melchior 12",
                            "Melchior 13",
                            "Melchior 14",
                            "Melchior 15",
                            "Melchior 16",
                            "Melchior 17",
                            "Melchior 18",
                            "Melchior 19",
                            "Melchior 20"
                        )
                    ) { name ->
                        PersonItem(
                            personName = name,
                            dropDownItems = listOf(
                                DropDownItem(title = "item 1"),
                                DropDownItem(title = "item 2"),
                                DropDownItem(title = "item 3"),
                            ),
                            onItemClick = {
                                Toast.makeText(
                                    applicationContext,
                                    it.title,
                                    Toast.LENGTH_LONG
                                ).show()
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}