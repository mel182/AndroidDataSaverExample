package com.example.datasaverexampleapp.compose.bottom_navigation.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.datasaverexampleapp.compose.bottom_navigation.BottomNavItem
import com.example.datasaverexampleapp.compose.bottom_navigation.nav_graph.BottomNavGraph
import com.example.datasaverexampleapp.compose.bottom_navigation.repository.BottomNavRepo
import com.example.datasaverexampleapp.compose.tablayout_example.ui.TabItem
import com.example.datasaverexampleapp.compose.tablayout_example.ui.TabItem2
import com.example.datasaverexampleapp.mvvm_coroutine_flow_livedata.DemoViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(tabs:List<TabItem2>) {
    val navController = rememberNavController()
    Scaffold(bottomBar = {
        BottomBar(navController = navController, tabs = tabs)
    }) {
        BottomNavGraph(navController = navController)
    }
}

@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun BottomBar(navController: NavHostController, tabs: List<TabItem2>) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomNavigation {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            tabs.forEach { screen ->
                AddItem(screen = screen, currentDestination = currentDestination, navController = navController)
            }
        }
    }
}

@Composable
fun RowScope.AddItem(screen: TabItem2, currentDestination: NavDestination?, navController: NavHostController) {

    //TODO: if you want to use the default bottom navigation item

//    BottomNavigationItem(
//        icon = {
//            Icon(painter = painterResource(id = screen.icon),
//                contentDescription = null,
//                tint = Color.Unspecified
//            )
//        },
//        label = {
//            Text(text = screen.title)
//        },
//        selected = currentDestination?.hierarchy?.any {
//            it.route == screen.route
//        } == true, onClick = {
//            navController.navigate(screen.route)
//        }
//    )

   //TODO: if you want to use custom view as bottom navigation item

    val interactionSource = remember { MutableInteractionSource() }
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxHeight()
            .weight(0.3f, true)
            .clickable(interactionSource = interactionSource, indication = null) {
                navController.navigate(screen.route)
            },
    ) {
        ConstraintLayout {
            val (box, icon) = createRefs()
            val shape = CircleShape

            Icon(painter = painterResource(id = screen.icon),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.constrainAs(icon) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )

            if(screen.showBadge) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(shape)
                        .background(Color.Red)
                        .constrainAs(box) {
                            top.linkTo(parent.top)
                        }
                )
            }
        }
        Text(text = screen.title)
    }
}

@Composable
fun BottomNavigationBar(navHostController:NavHostController, onItemClick: (BottomNavItem) -> Unit) {
    Log.i("TAG25","bottom nav bar function called!")
    UpdateBottomNavigationBar(backStateEntry = navHostController.currentBackStackEntryAsState(), onItemClick = onItemClick)
    ObserveForBadgeUpdate(navHostController,onItemClick)
}

@Composable
fun ObserveForBadgeUpdate(navHostController:NavHostController, onItemClick: (BottomNavItem) -> Unit) {
    val tabs by DemoViewModel().composeDemoAsyncResponse.observeAsState()
    tabs?.let {
        Log.i("TAG75","tab update from view model!")
        UpdateBottomNavigationBar(backStateEntry = navHostController.currentBackStackEntryAsState(), onItemClick = onItemClick)
    }
}

@Composable
fun UpdateBottomNavigationBar(backStateEntry: State<NavBackStackEntry?>, onItemClick: (BottomNavItem) -> Unit) {
    Log.i("TAG75","UpdateBottomNavigationBar called!")
    BottomNavigation(backgroundColor = Color.Gray, elevation = 5.dp, ) {
        Log.i("TAG75","bottom nav bar row scope called!")
        Log.i("TAG75","tab items: ${BottomNavRepo.tabItems}")
        // To update tab items
        Log.i("TAG75","tab init!")
        BottomNavRepo.tabItems.forEach { item ->
            val selected = item.route == backStateEntry.value?.destination?.route
            Log.i("TAG75","|------------------------------|")
            Log.i("TAG75","count: ${item.badgeCount}")
            Log.i("TAG75","|------------------------------|")
            BottomNavigationItem(
                selected = selected,
                onClick = { onItemClick(item) },
                selectedContentColor = Color.Green,
                unselectedContentColor = Color.Red,
                icon = {

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {

                        if (item.badgeCount > 0 && !selected) {
                            BadgedBox(badge = { Badge(backgroundColor = Color.Green) { Text(text = item.badgeCount.toString()) } }) {
                                Icon(imageVector = item.icon, contentDescription = null)
                            }
                        }
                        else {
                            Icon(imageVector = item.icon, contentDescription = null)
                        }
                        Text(text = item.name, textAlign = TextAlign.Center, fontSize = 10.sp)
                    }
                }
            )
        }
    }
}

@Composable
fun CircleShapeDemo(){
    ExampleBox(shape = CircleShape)
}

@Composable
fun ExampleBox(shape: Shape){
    Column(modifier = Modifier
        .fillMaxWidth()
        .wrapContentSize(Alignment.Center)) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(shape)
                .background(Color.Red)
        )
    }
}
