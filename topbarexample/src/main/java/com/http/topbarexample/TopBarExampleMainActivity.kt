@file:OptIn(ExperimentalMaterial3Api::class)

package com.http.topbarexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.http.topbarexample.ui.theme.DataSaverExampleAppTheme

class TopBarExampleMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DataSaverExampleAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    val scrollBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior()
                    //val scrollBehaviour = TopAppBarDefaults.pinnedScrollBehavior()
                    Scaffold(
                        modifier = Modifier
                            .fillMaxSize()
                            .nestedScroll(scrollBehaviour.nestedScrollConnection),
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(text = "My notes")
                                }, navigationIcon = {
                                    IconButton(onClick = {  }) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowBack,
                                            contentDescription = null
                                        )
                                    }
                                },
                                actions = {
                                    IconButton(onClick = { /*TODO*/ }) {
                                        Icon(
                                            imageVector = Icons.Default.FavoriteBorder,
                                            contentDescription = null
                                        )
                                    }
                                    IconButton(onClick = { /*TODO*/ }) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = null
                                        )
                                    }
                                },
                                scrollBehavior = scrollBehaviour,
                                colors = TopAppBarDefaults.smallTopAppBarColors(
                                    containerColor = Color.Transparent,
                                    titleContentColor = Color.White,
                                    actionIconContentColor = Color.White,
                                    navigationIconContentColor = Color.White
                                )
                            )
                        }
                    ) { paddingValues ->
                        println(paddingValues)

                        Column(modifier = Modifier
                            .fillMaxSize()
                            .background(color = Color.LightGray)
                            .verticalScroll(rememberScrollState())
                        ) {

                            for (i in 1..100) {
                                Text(
                                    text = "Item$i",
                                    modifier = Modifier.padding(all = 16.dp),
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}