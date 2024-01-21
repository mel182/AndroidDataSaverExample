package com.jetpackcompose.localsearch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jetpackcompose.localsearch.ui.theme.DataSaverExampleAppTheme

class JetpackComposeLocalSearchMainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MainViewModel(TodoSearchManager(applicationContext)) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DataSaverExampleAppTheme {
                val state = mainViewModel.state
                // A surface container using the 'background' color from the theme
                Column(modifier = Modifier.fillMaxSize()) {
                    TextField(
                        value = state.searchQuery,
                        onValueChange = mainViewModel::onSearchQueryChange,
                        modifier = Modifier
                            .padding(8.dp)
                            .background(color = Color.LightGray)
                            .fillMaxWidth()
                    )
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentPadding = PaddingValues(8.dp),
                        content = {
                            items(state.todos) { todo ->
                                TodoItem(
                                    todo = todo,
                                    onDoneChange = { isDone ->
                                        mainViewModel.onDoneChanged(todo = todo, isDone = isDone)
                                    }
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TodoItem(todo: Todo, onDoneChange: (Boolean) -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = todo.title,
                fontSize = 16.sp
            )
            Text(
                text = todo.text,
                fontSize = 10.sp
            )
        }

        Checkbox(
            checked = todo.isDone,
            onCheckedChange = onDoneChange
        )
    }
}