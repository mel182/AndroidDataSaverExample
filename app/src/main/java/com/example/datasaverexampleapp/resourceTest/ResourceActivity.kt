package com.example.datasaverexampleapp.resourceTest

import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.text.Html.FROM_HTML_MODE_LEGACY
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.example.datasaverexampleapp.R
import kotlinx.android.synthetic.main.activity_resource.*


class ResourceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resource)

        //region plural test
        plural_switch?.apply {

            pluralTextInitializeState()
            setOnCheckedChangeListener { buttonView, isChecked ->

                when(isChecked)
                {
                    true -> {
                        setTextColor(Color.GREEN)
                        plural_result.text = resources.getQuantityString(R.plurals.item_count, 2, 2)
                    }

                    else -> {
                        pluralTextInitializeState()
                    }
                }
            }
        }
        //endregion

        val string = getString(R.string.html_bold_styling)
        val stringFormatted = String.format(string,"Test")

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            text_html?.text = HtmlCompat.fromHtml(stringFormatted, HtmlCompat.FROM_HTML_MODE_COMPACT)
        } else {
            text_html?.text = Html.fromHtml(stringFormatted)
        }
    }

    private fun pluralTextInitializeState()
    {
        plural_switch?.setTextColor(Color.GRAY)
        plural_result?.text = resources.getQuantityString(R.plurals.item_count, 2, 2)
    }
}