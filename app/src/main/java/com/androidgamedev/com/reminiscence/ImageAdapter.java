package com.androidgamedev.com.reminiscence;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.androidgamedev.com.reminiscence.imgdata.MediaData;
import com.androidgamedev.com.reminiscence.imghandle.ImageLoader;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter
{
    private LayoutInflater mInflater = null;
    private Context mContext = null;
    private ImageLoader mImageLoader = null;

    private ArrayList<MediaData> mMediaList = null;

    public ImageAdapter(Context aContext)
    {
        mContext = aContext;
        mInflater = LayoutInflater.from(mContext);
        mImageLoader = new ImageLoader(mContext.getApplicationContext());
    }

    public void initData(ArrayList<MediaData> aMediaList)
    {
        mMediaList = aMediaList;
    }

    public int getCount()
    {
        if(mMediaList == null)
        {
            return 0;
        }
        return mMediaList.size();
    }

    public Object getItem(int position)
    {
        return position;
    }

    public long getItemId(int position)
    {
        return position;
    }

    /*
    전달 받은 이미지 정보를 ImageView를 통해 화면에 보여줌
     */
    public View getView(int position, View convertView, ViewGroup parent)
    {
        MediaData mediaData = null;

        if(convertView == null)
        {
            convertView = mInflater.inflate(R.layout.thumbnail, null);
            mediaData = new MediaData();
            mediaData.setImgview((ImageView)convertView.findViewById(R.id.thumbnail));

            //뷰를 식별하기 위한 TAG 장치
            convertView.setTag(mediaData);
        }else
        {
            mediaData = (MediaData)convertView.getTag();
        }

        if(mMediaList == null || mMediaList.size() == 0)
            return convertView;

        mediaData.setType(mMediaList.get(position).getType());
        mediaData.setMediaid(mMediaList.get(position).getMediaid());
        mediaData.setMediapath(mMediaList.get(position).getMediapath());
        mediaData.setOrientation(mMediaList.get(position).getOrientation());

        //각 뷰마다의 mediaData객체를 displayImage 함수에 넘김
        mImageLoader.displayImage(mediaData);

        return convertView;
    }
}
