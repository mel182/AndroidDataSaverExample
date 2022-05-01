package com.example.datasaverexampleapp.async_recycler_view.base

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding

open class BaseItemWrapper<Binding : ViewDataBinding>(@LayoutRes val layout:Int?) {

    var binding: Binding? = null

    open fun populate(binding: Binding?) {
        this.binding = binding
        binding?.apply {
            setVariable(com.example.datasaverexampleapp.BR.wrapper,this)
            executePendingBindings()
        }
    }
}