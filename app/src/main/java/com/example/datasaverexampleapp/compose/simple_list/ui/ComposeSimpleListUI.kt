package com.example.datasaverexampleapp.compose.simple_list.ui

import android.os.CountDownTimer
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun ComposeSimpleListView() {

    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier.verticalScroll(state = scrollState)
    ) {
        for (i in 1..50){
            Text(text = "Item $i",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                 modifier = Modifier
                     .fillMaxWidth()
                     .padding(vertical = 14.dp)
            )
        }
    }
}

@Composable
fun ComposeSimpleLazyColumnList() {

    val items = remember {
        
        mutableStateListOf(
            "item 1"
            ,"item 2"
            ,"item 3"
            ,"item 4"
            ,"item 5"
            ,"item 6"
            ,"item 7"
            ,"item 8"
            ,"item 9"
            ,"item 10"
            ,"item 11"
            ,"item 12"
            ,"item 13"
            ,"item 14"
            ,"item 15"
            ,"item 16"
            ,"item 17"
            ,"item 18"
            ,"item 19"
            ,"item 20"
        )
    }
    
    LazyColumn {
        
        itemsIndexed(items) { index, item ->
            Text(text = item,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 14.dp)
            )
        }
    }
}