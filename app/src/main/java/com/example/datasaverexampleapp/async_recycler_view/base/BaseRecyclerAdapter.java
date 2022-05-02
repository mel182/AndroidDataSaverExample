package com.example.datasaverexampleapp.async_recycler_view.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datasaverexampleapp.async_recycler_view.interfaces.OnRecyclerClickListener;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public class BaseRecyclerAdapter extends RecyclerView.Adapter<BaseViewHolder>
{
    private Context context;
    private LayoutInflater layoutInflater;
    private RecyclerView recyclerView;
    private OnRecyclerClickListener onRecyclerClickListener;
    private ArrayList<BaseItemWrapper> data = new ArrayList<>();

    public BaseRecyclerAdapter(Context context, RecyclerView recyclerView, OnRecyclerClickListener listener)
    {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.recyclerView = recyclerView;
        this.onRecyclerClickListener = listener;
    }

    protected void notifyDataSetChangeOnUIThread() {

        if (!recyclerView.isComputingLayout() && recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE)
            notifyDataSetChanged();
    }

    public void setData(List<BaseItemWrapper> data)
    {
        if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE)
        {
            postSetData(data);
            return;
        }

        loadData(data);
        notifyDataSetChangeOnUIThread();
    }

    private void loadData(List<BaseItemWrapper> list)
    {
        data.clear();
        data.addAll(list);
    }

    protected void clearData()
    {
        data.clear();
        notifyDataSetChangeOnUIThread();
    }

    private void postSetData(List<BaseItemWrapper> data)
    {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                {
                    setData(data);
                    recyclerView.removeOnScrollListener(this);
                }
            }
        });
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BaseViewHolder<>(DataBindingUtil.inflate(layoutInflater, viewType, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {

        BaseItemWrapper item = data.get(position);

        if (item == null)
            return;

        item.populate(holder.getBinding());
        if (onRecyclerClickListener != null)
            holder.setOnRecyclerViewClickListener(onRecyclerClickListener);
    }

    @Override
    public int getItemViewType(int position) {
        BaseItemWrapper item = getItem(position);
        return item == null ? 0 : item.getLayout();
    }

    @Nullable
    public BaseItemWrapper getItem(int position) {

        if (position < data.size())
            return data.get(position);

        return null;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
