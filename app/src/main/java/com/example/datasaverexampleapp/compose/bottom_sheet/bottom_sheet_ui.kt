package com.example.datasaverexampleapp.compose.bottom_sheet

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
val BottomSheetState.expandProgress: Float
get() {
    return when (progress.from) {
        BottomSheetValue.Collapsed -> {
            when (progress.to) {
                BottomSheetValue.Collapsed -> 0f
                BottomSheetValue.Expanded -> progress.fraction
            }
        }
        BottomSheetValue.Expanded -> {
            when (progress.to) {
                BottomSheetValue.Collapsed -> 1f - progress.fraction
                BottomSheetValue.Expanded -> 1f
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetView() {
    val sheetState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Collapsed,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy
        )
    )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )
    val scope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }

    BottomSheetScaffold(scaffoldState = scaffoldState,
        sheetContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Bottom sheet content", fontSize = 60.sp)
            }
        }, sheetBackgroundColor = Color.Green,
        sheetPeekHeight = 0.dp)
    {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            val test = scaffoldState.bottomSheetState.expandProgress
            if (test != 0.0f) {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Red.copy(alpha = test)).clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        scope.launch {
                            if (sheetState.isExpanded){
                                sheetState.collapse()
                            }
                        }
                    }) {
                }
            }

            Button(onClick = {
                scope.launch {
                    if (sheetState.isCollapsed){
                        sheetState.expand()
                    } else {
                        sheetState.collapse()
                    }
                }
            }) {
                // button row scope implementation
                Text(text = "Toggle button")
            }
        }
    }
}