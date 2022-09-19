package com.example.datasaverexampleapp.compose.lifecycle.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

@Composable
fun LifeCycleOwnerActivityView() {

    Text(text = "Check logcat with the tag 'TAG90' for more info about the life cycle")
    ComposableLifecycle { source, event ->

        when(event) {
            Lifecycle.Event.ON_CREATE -> { Log.i("TAG90","on create called") }
            Lifecycle.Event.ON_PAUSE -> { Log.i("TAG90","on pause called") }
            Lifecycle.Event.ON_RESUME -> { Log.i("TAG90","on resume called") }
            Lifecycle.Event.ON_START -> { Log.i("TAG90","on start called") }
            Lifecycle.Event.ON_STOP -> { Log.i("TAG90","on stop called") }
            Lifecycle.Event.ON_DESTROY -> { Log.i("TAG90","on destroy called") }
            Lifecycle.Event.ON_ANY -> { Log.i("TAG90","on any called") }
        }
    }
}

@Composable
fun ComposableLifecycle(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onEvent:(LifecycleOwner, Lifecycle.Event) -> Unit) {

    DisposableEffect(lifecycleOwner){
        val observer = LifecycleEventObserver { source, event ->
            onEvent(source, event)
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}