package com.androidgamedev.com.reminiscence.imgdata;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import com.androidgamedev.com.reminiscence.MyPhotoFragment;

import java.util.ArrayList;

public class GetMediaData
{
    public static final int androidTimeStartYear = 1970;
    public static final long oneDayTimeSecond = 86400;
    public static final long oneYearTimeSecond = 31536000;

    public static void getImageArrayData(Context aContext, ArrayList<MediaData> aMediaData, int year, int month)
    {
        Cursor imageCursor = null;

        String[] imagecolumns = {
                MediaStore.Images.Media._ID,        //미디어 ID
                MediaStore.Images.Media.DATA,       //미디어 경로
                MediaStore.Images.Media.ORIENTATION, //이미지 방향
                MediaStore.Images.Media.DATE_ADDED  //추가 날짜, 초단위
        };

        int differentofyear = year - androidTimeStartYear;

        boolean diff = differentofyear % 4 == 2 ? true : false;

        long dateToSecond = 0;
        long dateToSecond_after = 0;

        //년을 초단위로 바꿈
        for(int i = 0; i < differentofyear; i++)
        {
            if(i % 4 == 2)      //2월이 29일까지 있는 해
            {
                dateToSecond += oneYearTimeSecond;
                dateToSecond += oneDayTimeSecond;
            }else               //2월이 28일까지 있는 해
            {
                dateToSecond += oneYearTimeSecond;
            }
        }

        int februaryday = 0;
        if(diff)
            februaryday = 29;
        else
            februaryday = 28;

        dateToSecond_after = dateToSecond;

        for(int i=0; i < month; i++)
        {
            //1월
            if(i==0)
            {
                dateToSecond_after += ((31)*oneDayTimeSecond) - 1;
            }
            //2월
            else if(i==1)
            {
                dateToSecond += (31)*oneDayTimeSecond;
                dateToSecond_after += ((februaryday)*oneDayTimeSecond) - 1;
            }
            //3월
            else if(i==2)
            {
                dateToSecond += (februaryday)*oneDayTimeSecond;
                dateToSecond_after += ((31)*oneDayTimeSecond) - 1;
            }
            //4월
            else if(i==3)
            {
                dateToSecond += ((31)*oneDayTimeSecond);
                dateToSecond_after += ((30)*oneDayTimeSecond) - 1;
            }
            //5월
            else if(i==4)
            {
                dateToSecond += (30)*oneDayTimeSecond;
                dateToSecond_after += ((31)*oneDayTimeSecond) - 1;
            }
            //6월
            else if(i==5)
            {
                dateToSecond += (31)*oneDayTimeSecond;
                dateToSecond_after += ((30)*oneDayTimeSecond) - 1;
            }
            //7월
            else if(i==6)
            {
                dateToSecond += (30)*oneDayTimeSecond;
                dateToSecond_after += ((31)*oneDayTimeSecond) - 1;
            }
            //8월
            else if(i==7)
            {
                dateToSecond += (31)*oneDayTimeSecond;
                dateToSecond_after += ((31)*oneDayTimeSecond) - 1;
            }
            //9월
            else if(i==8)
            {
                dateToSecond += (31)*oneDayTimeSecond;
                dateToSecond_after += ((30)*oneDayTimeSecond) - 1;
            }
            //10월
            else if(i==9)
            {
                dateToSecond += (30)*oneDayTimeSecond;
                dateToSecond_after += ((31)*oneDayTimeSecond) - 1;
            }
            //11월
            else if(i==10)
            {
                dateToSecond += (31)*oneDayTimeSecond;
                dateToSecond_after += ((30)*oneDayTimeSecond) - 1;
            }
            //12월
            else if(i==11)
            {
                dateToSecond += (30)*oneDayTimeSecond;
                dateToSecond_after += ((31)*oneDayTimeSecond) - 1;
            }
        }

        //Log.v("GetMediaData",dateToSecond+", "+dateToSecond_after);
        String mSelectionClause = MediaStore.Images.Media.DATE_ADDED + " BETWEEN ? AND ?";
        String[] mSelectionArgs = {dateToSecond+"", dateToSecond_after+""};

        //컨텐트 리졸버의 query() 함수와 URI를 이용하여 이미지 정보를 가져옴.
        imageCursor = aContext.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,   //URI 정보, MediaStore 클래스에서 각 미디어 파일별 제공하는 URI정보 사용
                imagecolumns,                                   //필요한 컬럼 정보를 추가
                mSelectionClause,                               //SQL의 where 구문에 해당 조건을 입력, 조건이 값에 따라 다른 경우 ?통해 값을 받을 수 있음
                mSelectionArgs,                                 //?로 지정하였을 경우 그 조건들을 입력
                null                                            //group by 구문에 해당(SQL)
        );

        //가져온 이미지 정보는 Cursor를 통해 확인할 수 있음
        if(imageCursor != null && imageCursor.moveToFirst())
        {
            int imageid = 0;
            String imagePath = null;
            int orientation = 0;
            long date = 0;

            //MediaStore.Images.Media 테이블의 속성 index값 가져오기
            int imageIdColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media._ID);
            int imagePathColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
            int imageOrientationColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION);
            int imageDateColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED);

            do
            {
                //실제 정보 가져오기
                imageid = imageCursor.getInt(imageIdColumnIndex);
                imagePath = imageCursor.getString(imagePathColumnIndex);
                orientation = imageCursor.getInt(imageOrientationColumnIndex);
                date = imageCursor.getLong(imageDateColumnIndex);

                Log.v("GetMediaData", date+"");

                MediaData info = new MediaData();
                info.setType(MyPhotoFragment.TYPE_IMAGE);
                info.setMediaid(imageid);
                info.setMediapath(imagePath);
                info.setOrientation(orientation);

                aMediaData.add(info);
            }while(imageCursor.moveToNext());
        }
    }
}
