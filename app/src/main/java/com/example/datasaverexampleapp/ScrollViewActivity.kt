package com.example.datasaverexampleapp

import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import kotlinx.android.synthetic.main.activity_scroll_view.*

class ScrollViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroll_view)

        scroller?.viewTreeObserver?.addOnScrollChangedListener {

            val scrollBounds = Rect()
            scroller?.getDrawingRect(scrollBounds)

            val top = imageView2.y
            val bottom = top + imageView2.height

            if (scrollBounds.top < top && scrollBounds.bottom > bottom)
            {
                Log.i("TAG","Image view 2 visible")
            } else {
                Log.i("TAG","Image view 2 not visible")
            }
        }
    }
}