package com.androidgamedev.com.reminiscence.imghandle;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.androidgamedev.com.reminiscence.imgdata.MediaData;

import java.util.HashMap;

public class ImageRunnable implements Runnable
{
    private MediaData mMediaData = null;
    private HashMap<ImageView, String> mImageHashMap = null;

    public ImageRunnable(MediaData aMediaData, HashMap<ImageView, String> aImageViewMap)
    {
        mMediaData = aMediaData;
        mImageHashMap = aImageViewMap;
    }

    @Override
    public void run()
    {
        /*
        if(isImageViewValid(mMediaData) == false)
            return;
*/
        Bitmap bmp = null;

        bmp = ImageUtils.getImageThumbnail(mMediaData);
/*
        if(isImageViewValid(mMediaData) == false)
            return;
*/
        ImageViewRunnable bd = new ImageViewRunnable(bmp, mMediaData, mImageHashMap);
        Activity a = (Activity)mMediaData.getImgview().getContext();
        /*
        이미지는 UI스레드(메인 스레드)에서만 처리할 수 있기 때문에 메인 스레드에서 이미지를 그려주는 스레드를 실행시킨다
         */
        a.runOnUiThread(bd);
    }

    private boolean isImageViewValid(MediaData aMediaData)
    {
        String mediaPath = mImageHashMap.get(aMediaData.getImgview());

        if(mediaPath == null || mediaPath.equals(aMediaData.getMediapath()))
        {
            Log.v("ImageRunnable", "이미지 경로가 맞지 않습니다.");
            return false;
        }
        Log.v("ImageRunnable", "올바른 이미지 경로입니다.");

        return true;
    }
}
