package com.example.datasaverexampleapp.async_recycler_view.base;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datasaverexampleapp.async_recycler_view.async_views.AsyncFrameLayout;
import com.example.datasaverexampleapp.async_recycler_view.interfaces.OnRecyclerClickListener;

public class BaseViewHolder<Binding extends ViewDataBinding> extends RecyclerView.ViewHolder
{
    Binding binding;
    private OnRecyclerClickListener onRecyclerClickListener;

    public BaseViewHolder(Binding binding)
    {
        super(binding.getRoot());
        AsyncLoadItem view = new AsyncLoadItem(binding.getRoot().getContext(), binding, this);
        this.binding = view.binding;
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

    private class AsyncLoadItem extends AsyncFrameLayout {

        public Binding binding;

        public AsyncLoadItem(@NonNull Context context, Binding binding, BaseViewHolder viewHolder) {
            super(context);
            this.binding = binding;
            binding.setVariable(com.example.datasaverexampleapp.BR.holder,viewHolder);
        }

        @Override
        public int getLayoutId() {
            return binding.getRoot().getId();
        }

        @Nullable
        @Override
        public View createDataBindingView(@NonNull View view) {
            return binding.getRoot();
        }
    }
}
