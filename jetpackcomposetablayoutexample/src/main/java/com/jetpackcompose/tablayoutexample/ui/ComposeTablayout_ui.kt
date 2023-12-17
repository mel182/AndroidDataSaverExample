@file:OptIn(ExperimentalPagerApi::class)

package com.jetpackcompose.tablayoutexample.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.LeadingIconTab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.datasaverexampleapp.compose.tablayout_example.ui.TabItem
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainContent() {

    val list = listOf(TabItem.Home, TabItem.Cart, TabItem.Profile)
    val pagerState = rememberPagerState()

    Column(modifier = Modifier.fillMaxSize()) {
        Tabs(tabs = list, pagerState = pagerState)
        TabContent(tabs = list, pagerState = pagerState)
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Tabs(tabs:List<TabItem>, pagerState: PagerState) {

    val scope = rememberCoroutineScope()

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = Color(0xFFA8EFF0),
        indicator = { tabPositions ->
            Modifier.pagerTabIndicatorOffset(pagerState = pagerState, tabPositions = tabPositions)
        })
    {
        tabs.forEachIndexed { index, tabItem ->

            LeadingIconTab(
                selected = pagerState.currentPage == index,
                onClick = {
                      scope.launch {
                          pagerState.animateScrollToPage(index)
                      }
                },
                text = { Text(text = tabItem.title) },
                icon = {
                    //Icon(imageVector = tabItem.icon, contentDescription = null)
                    Icon(painter = painterResource(id = tabItem.icon),
                        contentDescription = null,
                        tint = Color.Unspecified)
                }
            )
        }
    }
}

@Composable
fun TabContent(tabs:List<TabItem>, pagerState: PagerState) {
    HorizontalPager(count = tabs.size, state = pagerState) { page ->
        tabs[page].screens()
    }
}

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Red),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Home screen", style = TextStyle(color = Color.Black, fontSize = 20.sp))
    }
}

@Composable
fun CartScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Blue),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Cart screen", style = TextStyle(color = Color.Black, fontSize = 20.sp))
    }
}

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Green),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Profile screen", style = TextStyle(color = Color.Black, fontSize = 20.sp))
    }
}

