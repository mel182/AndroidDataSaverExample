package com.example.datasaverexampleapp.compose

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.datasaverexampleapp.compose.bottom_navigation.BottomNavigationActivity
import com.example.datasaverexampleapp.compose.bottom_sheet.BottomSheetComposeActivity
import com.example.datasaverexampleapp.compose.canvas.CanvasShapeActivity
import com.example.datasaverexampleapp.compose.clock_example.ComposeClockExampleActivity
import com.example.datasaverexampleapp.compose.lifecycle.LifeCycleOwnerActivity
import com.example.datasaverexampleapp.compose.list_with_paging.ComposeListWithPagingExampleActivity
import com.example.datasaverexampleapp.compose.list_with_paging.native_paging.ComposeNativePagingExampleActivity
import com.example.datasaverexampleapp.compose.navigation.ComposeNavigationActivity
import com.example.datasaverexampleapp.compose.path.PathBasicActivity
import com.example.datasaverexampleapp.compose.path.PathStrokeJoinExampleActivity
import com.example.datasaverexampleapp.compose.path.QuadraticCubicBezierPathExampleActivity
import com.example.datasaverexampleapp.compose.row_and_column_example.RowAndColumnComposeActivity
import com.example.datasaverexampleapp.compose.screen_orientation.ScreenOrientationActivity
import com.example.datasaverexampleapp.compose.simple_list.ComposeSimpleListActivity
import com.example.datasaverexampleapp.compose.tablayout_example.ComposeTabLayoutActivity
import com.example.datasaverexampleapp.compose.textfield_buttons_snackbar_example.TextFieldButtonSnackbarActivity
import com.example.datasaverexampleapp.compose.touch_input.ComposeDetectTouchInputActivity
import com.example.datasaverexampleapp.compose.weight_scale.WeightScaleExampleActivity
import com.example.datasaverexampleapp.databinding.ActivityComposeBinding
import com.example.datasaverexampleapp.type_alias.Layout

class ComposeActivity : AppCompatActivity() {

    private var binding: ActivityComposeBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(Layout.activity_compose)
        title = "Compose example"
        binding = DataBindingUtil.setContentView(this, Layout.activity_compose)

        binding?.apply {

            rowColumnExample.setOnClickListener {
                val intent = Intent(this@ComposeActivity, RowAndColumnComposeActivity::class.java)
                startActivity(intent)
            }

            tablayoutExample.setOnClickListener {
                val intent = Intent(this@ComposeActivity, ComposeTabLayoutActivity::class.java)
                startActivity(intent)
            }

            bottomNavigationExample.setOnClickListener {
                val intent = Intent(this@ComposeActivity, BottomNavigationActivity::class.java)
                startActivity(intent)
            }

            bottomSheetExample.setOnClickListener {
                val intent = Intent(this@ComposeActivity, BottomSheetComposeActivity::class.java)
                startActivity(intent)
            }

            textfieldBottonSnackbarExample.setOnClickListener {
                val intent = Intent(this@ComposeActivity, TextFieldButtonSnackbarActivity::class.java)
                startActivity(intent)
            }

            orientationExample.setOnClickListener {
                val intent = Intent(this@ComposeActivity, ScreenOrientationActivity::class.java)
                startActivity(intent)
            }

            disposableLifecycleExample.setOnClickListener {
                val intent = Intent(this@ComposeActivity, LifeCycleOwnerActivity::class.java)
                startActivity(intent)
            }

            simpleListExample.setOnClickListener {
                val intent = Intent(this@ComposeActivity, ComposeSimpleListActivity::class.java)
                startActivity(intent)
            }

            listWithPagingExample.setOnClickListener {
                val intent = Intent(this@ComposeActivity, ComposeListWithPagingExampleActivity::class.java)
                startActivity(intent)
            }

            listWithPagingExample.setOnClickListener {
                val intent = Intent(this@ComposeActivity, ComposeListWithPagingExampleActivity::class.java)
                startActivity(intent)
            }

            nativeListWithPagingExample.setOnClickListener {
                val intent = Intent(this@ComposeActivity, ComposeNativePagingExampleActivity::class.java)
                startActivity(intent)
            }

            composeNavigationExample.setOnClickListener {
                val intent = Intent(this@ComposeActivity, ComposeNavigationActivity::class.java)
                startActivity(intent)
            }

            bottomCanvasExample.setOnClickListener {
                val intent = Intent(this@ComposeActivity, CanvasShapeActivity::class.java)
                startActivity(intent)
            }

            detectTouchInputExample.setOnClickListener {
                val intent = Intent(this@ComposeActivity, ComposeDetectTouchInputActivity::class.java)
                startActivity(intent)
            }

            weightScaleExample.setOnClickListener {
                val intent = Intent(this@ComposeActivity, WeightScaleExampleActivity::class.java)
                startActivity(intent)
            }

            analogClockExample.setOnClickListener {
                val intent = Intent(this@ComposeActivity, ComposeClockExampleActivity::class.java)
                startActivity(intent)
            }

            pathBasicExample.setOnClickListener {
                val intent = Intent(this@ComposeActivity, PathBasicActivity::class.java)
                startActivity(intent)
            }

            quadraticBezierCubicPathExample.setOnClickListener {
                val intent = Intent(this@ComposeActivity, QuadraticCubicBezierPathExampleActivity::class.java)
                startActivity(intent)
            }

            pathStrokeJoinExample.setOnClickListener {
                val intent = Intent(this@ComposeActivity, PathStrokeJoinExampleActivity::class.java)
                startActivity(intent)
            }



//            PathStrokeJoinExampleActivity
        }
    }
}