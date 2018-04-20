package com.androidgamedev.com.reminiscence.imghandle;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.androidgamedev.com.reminiscence.R;
import com.androidgamedev.com.reminiscence.imgdata.MediaData;

import java.util.HashMap;

public class ImageViewRunnable implements Runnable
{
    private final int D_RES_ID = R.mipmap.default_picture;
    private Bitmap mBitmap = null;
    private MediaData mMediaData = null;
    private HashMap<ImageView, String> mImageViewMap = null;

    public ImageViewRunnable(Bitmap aBitmap, MediaData aMediaData, HashMap<ImageView, String> aImageViewMap)
    {
        mBitmap = aBitmap;
        mMediaData = aMediaData;
        mImageViewMap = aImageViewMap;
    }

    public void run()
    {
        if(isImageViewValid() == false)
        {
            return;
        }

        if(mBitmap != null)
        {
            mMediaData.getImgview().setImageBitmap(mBitmap);
        }else
        {
            mMediaData.getImgview().setImageResource(D_RES_ID);
        }
    }

    private boolean isImageViewValid()
    {
        String mediaPath = mImageViewMap.get(mMediaData.getImgview());
        if(mediaPath == null || mediaPath.equals(mMediaData.getMediapath()) == false)
        {
            return false;
        }
        return true;
    }
}
