package com.androidgamedev.com.reminiscence;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class SettingViewAdapter extends RecyclerView.Adapter<SettingViewAdapter.ViewHolder>
{
    protected List<String> dataset;
    private View.OnClickListener onItemViewClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public final TextView textView;

        public ViewHolder(View v)
        {
            super(v);
            textView = (TextView)v.findViewById(R.id.setting_item);
        }
    }

    public SettingViewAdapter(List<String> dataset)
    {
        this.dataset = dataset;
    }

    public void setOnItemViewClickListener(View.OnClickListener onItemViewClickListener)
    {
        this.onItemViewClickListener = onItemViewClickListener;
    }

    @Override
    public SettingViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.settinglist_row, parent, false);

        if(onItemViewClickListener != null)
        {
            v.setOnClickListener(onItemViewClickListener);
        }
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        String text = dataset.get(position);
        Log.v("SettingViewAdapter", text);
        holder.textView.setText(text);
    }

    @Override
    public int getItemCount()
    {
        return dataset.size();
    }
}
