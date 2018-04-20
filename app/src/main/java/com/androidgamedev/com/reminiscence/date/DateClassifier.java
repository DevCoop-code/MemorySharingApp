package com.androidgamedev.com.reminiscence.date;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class DateClassifier
{
    public static final int androidTimeStartYear = 1970;
    public static final long oneDayTimeSecond = 86400;
    public static final long oneYearTimeSecond = 31536000;

    public static HashSet<Integer> classifyDate(List<Long> imageDateInfoList)
    {
        int february_day = 0;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        Date currentTime = new Date();
        String yearTime = formatter.format(currentTime);

        int current_year = Integer.parseInt(yearTime);
        int last_index = (current_year - androidTimeStartYear) + 2;

        long yearToSecond = 0;
        Long[] yearToSecondArray = new Long[last_index];
        yearToSecondArray[0] = yearToSecond;

        HashSet<Integer> cardViewDate = new HashSet<>();

        for(int i = 1; i < last_index; i++)
        {
            if(i % 4 == 2)      //2월이 29일까지 있는 해
            {
                yearToSecond += oneYearTimeSecond;
                yearToSecond += oneDayTimeSecond;
            }else               //2월이 28일까지 있는 해
            {
                yearToSecond += oneYearTimeSecond;
            }
            yearToSecondArray[i] = yearToSecond;
        }

        Iterator<Long> imageDateInfoIter = imageDateInfoList.iterator();
        while(imageDateInfoIter.hasNext())
        {
            String year = null;
            String month = null;

            //사진들의 초단위 날짜
            Long imageTime = imageDateInfoIter.next();

            //Log.v("DateClassifier", imageTime+"");
            for(int j=0; j<last_index-1; j++)
            {
                //년도 구분
                if(yearToSecondArray[j]<= imageTime && yearToSecondArray[j+1] > imageTime)
                {
                    //Log.v("DateClassifier", (j+1970) + "년대 사진");

                    year = (j+1970) + "";

                    //월 구분
                    imageTime -= yearToSecondArray[j];
                    //January
                    if(imageTime >= 0 && imageTime < oneDayTimeSecond*31)
                    {
                        month = 1 + "";
                        int cardimagedate = Integer.parseInt((year+month));
                        cardViewDate.add(cardimagedate);
                    }

                    if(j%4 == 2)    //2월 29일이 있는 해
                    {
                        february_day = 29;
                    }else           //2월 29일이 없는 해
                    {
                        february_day = 28;
                    }

                    //February
                    if(imageTime >= oneDayTimeSecond*31 && imageTime < oneDayTimeSecond*(31+february_day))
                    {
                        month = 2 + "";
                        int cardimagedate = Integer.parseInt((year+month));
                        cardViewDate.add(cardimagedate);
                    }
                    //March
                    if(imageTime >= oneDayTimeSecond*(31+february_day) && imageTime < oneDayTimeSecond*(31+february_day+31))
                    {
                        month = 3 + "";
                        int cardimagedate = Integer.parseInt((year+month));
                        cardViewDate.add(cardimagedate);
                    }
                    //April
                    if(imageTime >= oneDayTimeSecond*(31+february_day+31) && imageTime < oneDayTimeSecond*(31+february_day+31+30))
                    {
                        month = 4 + "";
                        int cardimagedate = Integer.parseInt((year+month));
                        cardViewDate.add(cardimagedate);
                    }
                    //May
                    if(imageTime >= oneDayTimeSecond*(31+february_day+31+30) && imageTime < oneDayTimeSecond*(31+february_day+31+30+31))
                    {
                        month = 5 + "";
                        int cardimagedate = Integer.parseInt((year+month));
                        cardViewDate.add(cardimagedate);
                    }
                    //June
                    if(imageTime >= oneDayTimeSecond*(31+february_day+31+30+31) && imageTime < oneDayTimeSecond*(31+february_day+31+30+31+30))
                    {
                        month = 6 + "";
                        int cardimagedate = Integer.parseInt((year+month));
                        cardViewDate.add(cardimagedate);
                    }
                    //July
                    if(imageTime >= oneDayTimeSecond*(31+february_day+31+30+31+30) && imageTime < oneDayTimeSecond*(31+february_day+31+30+31+30+31))
                    {
                        month = 7 + "";
                        int cardimagedate = Integer.parseInt((year+month));
                        cardViewDate.add(cardimagedate);
                    }
                    //August
                    if(imageTime >= oneDayTimeSecond*(31+february_day+31+30+31+30+31) && imageTime < oneDayTimeSecond*(31+february_day+31+30+31+30+31+31))
                    {
                        month = 8 + "";
                        int cardimagedate = Integer.parseInt((year+month));
                        cardViewDate.add(cardimagedate);
                    }
                    //September
                    if(imageTime >= oneDayTimeSecond*(31+february_day+31+30+31+30+31+31) && imageTime < oneDayTimeSecond*(31+february_day+31+30+31+30+31+31+30))
                    {
                        month = 9 + "";
                        int cardimagedate = Integer.parseInt((year+month));
                        cardViewDate.add(cardimagedate);
                    }
                    //October
                    if(imageTime >= oneDayTimeSecond*(31+february_day+31+30+31+30+31+31+30) && imageTime < oneDayTimeSecond*(31+february_day+31+30+31+30+31+31+30+31))
                    {
                        month = 10 + "";
                        int cardimagedate = Integer.parseInt((year+month));
                        cardViewDate.add(cardimagedate);
                    }
                    //November
                    if(imageTime >= oneDayTimeSecond*(31+february_day+31+30+31+30+31+31+30+31) && imageTime < oneDayTimeSecond*(31+february_day+31+30+31+30+31+31+30+31+30))
                    {
                        month = 11 + "";
                        int cardimagedate = Integer.parseInt((year+month));
                        cardViewDate.add(cardimagedate);
                    }
                    //December
                    if(imageTime >= oneDayTimeSecond*(31+february_day+31+30+31+30+31+31+30+31+30) && imageTime < oneDayTimeSecond*(31+february_day+31+30+31+30+31+31+30+31+30+31))
                    {
                        month = 12 + "";
                        int cardimagedate = Integer.parseInt((year+month));
                        cardViewDate.add(cardimagedate);
                    }
                }
            }
        }
        return cardViewDate;
    }
}
