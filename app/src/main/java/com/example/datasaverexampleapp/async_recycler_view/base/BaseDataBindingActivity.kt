package com.example.datasaverexampleapp.async_recycler_view.base

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.datasaverexampleapp.base_classes.BaseActivity

abstract class BaseDataBindingActivity<Binding : ViewDataBinding>(private val layout:Int) : BaseActivity(layout)
{
    protected var view: Binding? = null
    private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = DataBindingUtil.setContentView(this, layout)
    }
}