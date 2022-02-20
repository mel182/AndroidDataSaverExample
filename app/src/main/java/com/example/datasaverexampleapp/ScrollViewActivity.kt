package com.example.datasaverexampleapp

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.datasaverexampleapp.databinding.ActivityScrollViewBinding
import com.example.datasaverexampleapp.type_alias.Layout

class ScrollViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroll_view)

        DataBindingUtil.setContentView<ActivityScrollViewBinding>(this, Layout.activity_scroll_view).apply {

            scroller?.onScroll { _, _ ->
                val scrollBounds = Rect()
                scroller?.getDrawingRect(scrollBounds)

                val top = imageView2.y
                val bottom = top + imageView2.height

                if (scrollBounds.top < top && scrollBounds.bottom > bottom)
                {
                    topSection.visibility = View.GONE
//               Log.i("TAG","Image view 2 visible")
                } else {
                    topSection.visibility = View.VISIBLE
//               Log.i("TAG", "Image view 2 not visible")
                }
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