package com.example.datasaverexampleapp.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.datasaverexampleapp.application.AppContext
import com.example.datasaverexampleapp.type_alias.Layout
import kotlinx.android.synthetic.main.item_dialog_fragment_custom_view.view.*

class DialogFragmentCustomView : DialogFragment()
{
    private var title = ""
    private var buttonTitle = ""

    fun setTitle(title:String) : DialogFragmentCustomView
    {
        this.title = title
        return this
    }

    fun setButtonTitle(title:String) : DialogFragmentCustomView
    {
        this.buttonTitle = title
        return this
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(Layout.item_dialog_fragment_custom_view,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        setOnClickListeners(view)
    }

    private fun setupView(view:View)
    {
        view.apply {
            dialog_title.text = title
            dialog_button.text = buttonTitle
        }
    }

    private fun setOnClickListeners(view:View)
    {
        view.dialog_button.setOnClickListener {
            Toast.makeText(AppContext.appContext,"Button clicked!",Toast.LENGTH_SHORT).show()
            dismiss()
        }
    }
}