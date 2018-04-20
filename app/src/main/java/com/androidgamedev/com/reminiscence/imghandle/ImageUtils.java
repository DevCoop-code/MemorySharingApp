package com.androidgamedev.com.reminiscence.imghandle;


import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.provider.MediaStore;

import com.androidgamedev.com.reminiscence.imgdata.MediaData;

public class ImageUtils
{
    public static Bitmap getImageThumbnail(MediaData aMediaData)
    {
        ContentResolver thumbnailCR = aMediaData.getImgview().getContext().getContentResolver();
        Bitmap thumnailBm = MediaStore.Images.Thumbnails.getThumbnail(
                thumbnailCR,
                aMediaData.getMediaid(),
                MediaStore.Images.Thumbnails.MINI_KIND,
                null
        );
        return getCenterBitmap(thumnailBm, aMediaData.getOrientation());
    }

    //이미지 회전
    private static Bitmap getRotateBitmap(Bitmap aSrc, int aOrientation)
    {
        if(aSrc == null)
            return null;
        int width = aSrc.getWidth();
        int height = aSrc.getHeight();

        Matrix m = new Matrix();

        if(aOrientation != 0)
            m.setRotate(aOrientation);
        return Bitmap.createBitmap(aSrc, 0, 0, width, height, m, false);
    }

    //썸네일 이미지 가져오기
    private static Bitmap getCenterBitmap(Bitmap src, int aOrientation)
    {
        if(src == null)
            return null;

        Bitmap bitmap = null;
        int width = src.getWidth();
        int height = src.getHeight();

        Matrix m = new Matrix();

        if(aOrientation != 0)
        {
            m.setRotate(aOrientation);
        }

        if(width >= height)
        {
            bitmap = Bitmap.createBitmap(src, width/2 - height/2, 0, height, height, m, true);
        }else
        {
            bitmap = Bitmap.createBitmap(src, 0, height/2 - width/2, width, width, m, true);
        }
        return bitmap;
    }

    //원본 이미지 가져오기
    public static Bitmap getImageBitmap(String aImagePath, int aOrientation)
    {
        try
        {
            Bitmap displayImg = null;
            /*
            BitmapFactory : 이미지 경로의 파일을 Bitmap으로 반환하는 기능을 가짐
            Options : 원본 이미지의 크기를 가로/세로 비율에 맞게 줄임
             */
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            displayImg = BitmapFactory.decodeFile(aImagePath, options);

            return getRotateBitmap(displayImg, aOrientation);
        }catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
