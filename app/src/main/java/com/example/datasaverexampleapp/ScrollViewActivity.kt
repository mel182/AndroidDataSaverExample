package com.example.datasaverexampleapp

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_scroll_view.*

class ScrollViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroll_view)

       scroller?.onScroll { _, _ ->
           val scrollBounds = Rect()
           scroller?.getDrawingRect(scrollBounds)

           val top = imageView2.y
           val bottom = top + imageView2.height

           if (scrollBounds.top < top && scrollBounds.bottom > bottom)
           {
               top_section.visibility = View.GONE
//               Log.i("TAG","Image view 2 visible")
           } else {
               top_section.visibility = View.VISIBLE
//               Log.i("TAG", "Image view 2 not visible")
           }
       }


//        scroller?.viewTreeObserver?.addOnScrollChangedListener {
//
//            val scrollBounds = Rect()
//            scroller?.getDrawingRect(scrollBounds)
//
//            val top = imageView2.y
//            val bottom = top + imageView2.height
//
//            if (scrollBounds.top < top && scrollBounds.bottom > bottom)
//            {
//                Log.i("TAG","Image view 2 visible")
//            } else {
//                Log.i("TAG","Image view 2 not visible")
//            }
//        }
    }
}