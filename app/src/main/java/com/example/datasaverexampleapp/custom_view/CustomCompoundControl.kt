package com.example.datasaverexampleapp.custom_view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProviders
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.data_binding.MainViewModel

class CustomCompoundControl : LinearLayout
{
    var editText:EditText? = null
    var clearButton:Button? = null
    private lateinit var mainViewModel:MainViewModel


    constructor(context: Context) : this(context,null)
    constructor(context: Context, attrs: AttributeSet?) : this(context,attrs,0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context,attrs,defStyleAttr)
    {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        layoutInflater.inflate(R.layout.compount_control_view,this,true)
        editText = findViewById<EditText>(R.id.editText)
        clearButton = findViewById<Button>(R.id.clear_button)
        hookupButton()
    }

    private fun hookupButton()
    {
        clearButton?.setOnClickListener {
            editText?.setText("")
        }
    }

}