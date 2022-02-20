package com.example.datasaverexampleapp.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.datasaverexampleapp.application.AppContext
import com.example.datasaverexampleapp.databinding.ItemDialogFragmentCustomViewBinding
import com.example.datasaverexampleapp.type_alias.Layout

class DialogFragmentCustomView : DialogFragment()
{
    private var title = ""
    private var buttonTitle = ""
    private var binding: ItemDialogFragmentCustomViewBinding? = null

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

        activity?.apply {
            binding = DataBindingUtil.setContentView<ItemDialogFragmentCustomViewBinding>(
                this, Layout.item_dialog_fragment_custom_view
            )
        }
        setupView(view)
        setOnClickListeners()
    }

    private fun setupView(view:View)
    {
        binding?.let {
            view.apply {
                it.dialogTitle.text = title
                it.dialogTitle.text = buttonTitle
            }
        }
    }

    private fun setOnClickListeners()
    {
        binding?.let {
            it.dialogButton.setOnClickListener {
                Toast.makeText(AppContext.appContext,"Button clicked!",Toast.LENGTH_SHORT).show()
                dismiss()
            }
        }
    }
}