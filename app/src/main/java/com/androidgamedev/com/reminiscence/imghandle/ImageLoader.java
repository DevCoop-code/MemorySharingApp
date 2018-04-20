package com.androidgamedev.com.reminiscence.imghandle;


import android.content.Context;
import android.widget.ImageView;

import com.androidgamedev.com.reminiscence.R;
import com.androidgamedev.com.reminiscence.imgdata.MediaData;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoader
{
    private final int D_RES_ID = R.mipmap.default_picture;
    private ExecutorService mExecutorService = null;
    private HashMap<ImageView, String> mImageViewMap = null;

    public ImageLoader(Context aContext)
    {
        mImageViewMap = new HashMap<ImageView, String>();
        mExecutorService = Executors.newFixedThreadPool(50);
    }

    public void displayImage(MediaData mediaData)
    {
        if(mImageViewMap == null)
            return;
        if(mediaData == null)
            return;

        //이미지 데이터와 경로 데이터를 HashMap에 넣음
        mImageViewMap.put(mediaData.getImgview(), mediaData.getMediapath());
        mExecutorService.submit(new ImageRunnable(mediaData, mImageViewMap));
        mediaData.getImgview().setImageResource(D_RES_ID);
    }
}
