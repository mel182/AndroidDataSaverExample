package com.example.datasaverexampleapp.intent_example.starSignPicker.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class CustomViewHolder<T>(val view:View) : RecyclerView.ViewHolder(view), ViewHolderTemplate<T>