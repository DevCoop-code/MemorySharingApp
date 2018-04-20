package com.androidgamedev.com.reminiscence;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RecyclerDateAdapter extends RecyclerView.Adapter<RecyclerDateAdapter.ViewHolder>
{
    protected List<String> dataset;
    private View.OnClickListener onItemViewClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public final TextView textView;

        public ViewHolder(View v)
        {
            super(v);
            textView = (TextView)v.findViewById(R.id.simple_date_view);
        }
    }

    public RecyclerDateAdapter(List<String> myDataset)
    {
        dataset = myDataset;
    }

    public void setOnItemViewClickListener(View.OnClickListener onItemViewClickListener)
    {
        this.onItemViewClickListener = onItemViewClickListener;
    }

    @Override
    public RecyclerDateAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.datelist_row, parent, false);
        //View에 클릭 리스너를 붙임
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
        holder.textView.setText(text);
    }

    @Override
    public int getItemCount()
    {
        return dataset.size();
    }
}
