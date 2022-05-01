package com.example.datasaverexampleapp.async_recycler_view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.datasaverexampleapp.R

/**
 * In this example we are going your going to show how to optimized complex items in RecyclerView (and some animation in some time).
 * When you do layout inflation in runtime your app should render frames in about 16ms to achieve 60 frames per second.
 * It is very difficult to achieve when you load RecyclerView.
 * This example shows how to improve the loading performance and make the rendering faster.
 * Furthermore, we are going to implement a best practice to show how to use data binding efficiently.
 */
class AsyncRecyclerViewExampleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_async_recycler_view_example)
    }
}