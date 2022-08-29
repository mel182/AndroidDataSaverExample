package com.example.datasaverexampleapp.compose

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.datasaverexampleapp.compose.bottom_navigation.BottomNavigationActivity
import com.example.datasaverexampleapp.compose.bottom_sheet.BottomSheetComposeActivity
import com.example.datasaverexampleapp.compose.canvas.CanvasShapeActivity
import com.example.datasaverexampleapp.compose.row_and_column_example.RowAndColumnComposeActivity
import com.example.datasaverexampleapp.compose.simple_list.ComposeSimpleListActivity
import com.example.datasaverexampleapp.compose.tablayout_example.ComposeTabLayoutActivity
import com.example.datasaverexampleapp.compose.textfield_buttons_snackbar_example.TextFieldButtonSnackbarActivity
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

            simpleListExample.setOnClickListener {
                val intent = Intent(this@ComposeActivity, ComposeSimpleListActivity::class.java)
                startActivity(intent)
            }

            bottomCanvasExample.setOnClickListener {
                val intent = Intent(this@ComposeActivity, CanvasShapeActivity::class.java)
                startActivity(intent)
            }
        }
    }
}