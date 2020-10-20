package com.example.datasaverexampleapp.custom_view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.KeyEvent
import androidx.appcompat.widget.AppCompatTextView
import com.example.datasaverexampleapp.R
import java.text.NumberFormat

class CustomTextView : AppCompatTextView
{
    var price:Float? = null
        private set(value) {

            text = value?.let {
                "${it}"
            }?:""
            field = value
        }

    constructor(context: Context) : this(context,null)

    constructor(context:Context, attrs: AttributeSet?) : this(context, attrs,0)

    constructor(context:Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context,attrs,defStyleAttr)
    {
        val a = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView,defStyleAttr,0)
        if (a.hasValue(R.styleable.CustomTextView_price))
            price = a.getFloat(R.styleable.CustomTextView_price,0F)

        a.recycle()
    }

    companion object {
        private val CURRENCY_FORMAT = NumberFormat.getCurrencyInstance()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return super.onKeyDown(keyCode, event)
    }
}