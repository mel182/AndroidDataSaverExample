package com.example.datasaverexampleapp.async_recycler_view.base;

import android.view.View;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datasaverexampleapp.async_recycler_view.interfaces.OnRecyclerClickListener;

public class BaseViewHolder<Binding extends ViewDataBinding> extends RecyclerView.ViewHolder
{
    Binding binding;
    private OnRecyclerClickListener onRecyclerClickListener;

    public BaseViewHolder(Binding binding)
    {
        super(binding.getRoot());
        this.binding = binding;
        binding.setVariable(com.example.datasaverexampleapp.BR.holder,this);
    }

    public Binding getBinding() {
        return binding;
    }

    public void setOnRecyclerViewClickListener(OnRecyclerClickListener listener)
    {
        this.onRecyclerClickListener = listener;
    }

    public void onClick(View view, BaseItemWrapper wrapper) {
        onClick(view, wrapper, null);
    }

    public void onClick(View view, BaseItemWrapper wrapper, Object object) {

        if (onRecyclerClickListener != null)
            onRecyclerClickListener.onItemClicked(getAbsoluteAdapterPosition(), this, view, wrapper, null);

    }
}
