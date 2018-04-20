package com.androidgamedev.com.reminiscence;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidgamedev.com.reminiscence.server.MemoryDataDTO;
import com.androidgamedev.com.reminiscence.server.ServerInfo;
import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MemoryViewAdapter extends RecyclerView.Adapter<MemoryViewAdapter.ViewHolder>
{
    protected List<MemoryDataDTO> dataset;
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public final TextView nameView, dateView, desView;
        public final ImageView imageView;

        public ViewHolder(View v)
        {
            super(v);
            nameView = (TextView)v.findViewById(R.id.name_view);
            dateView = (TextView)v.findViewById(R.id.date_view);
            desView = (TextView)v.findViewById(R.id.des_view);
            imageView = (ImageView)v.findViewById(R.id.image_view);
        }
    }

    public MemoryViewAdapter(List<MemoryDataDTO> dataset, Context context)
    {
        this.dataset = dataset;
        this.mContext = context;
    }

    @Override
    public MemoryViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.othermemory_card_view, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    //View안의 데이터 변경 부분
    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:MM");

        MemoryDataDTO dto = dataset.get(position);

        String str_date = format.format(dto.getRegdate());
        String name_txt = dto.getUname();
        String date_txt = str_date;
        String des_txt = dto.getDescription();
        String image_url = dto.getImagepath();

        image_url = image_url.replace("s_","");
        Glide.with(mContext).load(ServerInfo.server_address+"/displayFile?fileName="+image_url).into(holder.imageView);
        holder.nameView.setText(name_txt);
        holder.dateView.setText(date_txt);
        holder.desView.setText(des_txt);
    }

    //데이터 수를 반환
    @Override
    public int getItemCount()
    {
        if(dataset == null)
            return 0;
        return dataset.size();
    }
}
