package com.androidgamedev.com.reminiscence.date;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class GetDateInfo
{
    public static List getDateArrayData(Context aContext)
    {
        Cursor imageDateCursor = null;

        String[] dateColumns = {
                MediaStore.Images.Media.DATE_ADDED
        };

        imageDateCursor = aContext.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                dateColumns,
                null,
                null,
                null
        );

        List<Long> dateinfo_list = new ArrayList<>();

        if(imageDateCursor != null && imageDateCursor.moveToFirst())
        {
            long date = 0;

            int imageDateColumnIndex = imageDateCursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED);

            do
            {
                date = imageDateCursor.getLong(imageDateColumnIndex);

                dateinfo_list.add(date);

            }while(imageDateCursor.moveToNext());
        }

        return dateinfo_list;
    }
}