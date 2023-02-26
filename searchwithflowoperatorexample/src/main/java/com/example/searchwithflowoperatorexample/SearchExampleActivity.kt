package com.example.searchwithflowoperatorexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.searchwithflowoperatorexample.ui.theme.DataSaverExampleAppTheme

class SearchExampleActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DataSaverExampleAppTheme {
                val viewmodel = viewModel<SearchViewModel>()
                val searchText by viewmodel.searchText.collectAsState()
                val persons by viewmodel.persons.collectAsState()
                val isSearching by viewmodel.isSearching.collectAsState()

                Column(modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White)
                    .padding(all = 16.dp)
                ) {
                    TextField(
                        value = searchText,
                        onValueChange = viewmodel::onSearchTextChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Color.LightGray,
                                shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                            ),
                        placeholder = { Text(text = "Search", color = Color.DarkGray) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    if (isSearching) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        }
                    } else {
                        LazyColumn(modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                        ) {
                            items(persons) { person ->
                                Text(text = "${person.firstName} ${person.lastName}",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(all = 16.dp)
                                )
                            }
                        }
                    }


                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DataSaverExampleAppTheme {
        Greeting("Android")
    }
}