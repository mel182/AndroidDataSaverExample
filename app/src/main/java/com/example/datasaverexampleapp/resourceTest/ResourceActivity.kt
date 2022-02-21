@file:Suppress("UNUSED_ANONYMOUS_PARAMETER", "DEPRECATION")

package com.example.datasaverexampleapp.resourceTest

import android.graphics.Color
import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.databinding.ActivityResourceBinding
import com.example.datasaverexampleapp.type_alias.Layout

class ResourceActivity : AppCompatActivity() {

    private var binding: ActivityResourceBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resource)

        binding = DataBindingUtil.setContentView<ActivityResourceBinding>(
            this, Layout.activity_resource
        ).apply {

            //region plural test
            pluralSwitch.apply {

                pluralTextInitializeState()
                setOnCheckedChangeListener { buttonView, isChecked ->

                    when(isChecked)
                    {
                        true -> {
                            setTextColor(Color.GREEN)
                            pluralResult.text = resources.getQuantityString(R.plurals.item_count, 2, 2)
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
                textHtml.text = HtmlCompat.fromHtml(stringFormatted, HtmlCompat.FROM_HTML_MODE_COMPACT)
            } else {
                textHtml.text = Html.fromHtml(stringFormatted)
            }
        }
    }

    private fun pluralTextInitializeState()
    {
        binding?.apply {
            pluralSwitch.setTextColor(Color.GRAY)
            pluralResult.text = resources.getQuantityString(R.plurals.item_count, 2, 2)
        }
    }
}