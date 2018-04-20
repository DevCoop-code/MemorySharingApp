package com.androidgamedev.com.reminiscence;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidgamedev.com.reminiscence.date.DateClassifier;
import com.androidgamedev.com.reminiscence.date.GetDateInfo;
import com.androidgamedev.com.reminiscence.datedata.DateData;
import com.androidgamedev.com.reminiscence.deco.DividerItemDecoration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class MyPhotoFragment extends Fragment
{
    public static String TYPE = "TYPE";
    public static final int TYPE_IMAGE = 0;
    public static final int TYPE_VIDEO = 1;

    private RecyclerDateAdapter dateAdapter;

    List<DateData> datedataAlignList = null;

    Activity activity = null;

    RecyclerView recyclerView = null;

    public MyPhotoFragment()
    {

    }

    public static MyPhotoFragment newInstance()
    {
        MyPhotoFragment main_photos = new MyPhotoFragment();

        return main_photos;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance)
    {
        View myphotoView = inflater.inflate(R.layout.activity_main, container, false);
        recyclerView = myphotoView.findViewById(R.id.photo_date_recyclerview);

        createDateListView(activity);

        return myphotoView;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        //createDateListView(activity);
        this.activity = activity;
    }

    private void createDateListView(Context context)
    {
        List<Long> dateInfo = null;
        dateInfo = GetDateInfo.getDateArrayData(context);

        HashSet<Integer> imagedateInfo = DateClassifier.classifyDate(dateInfo);
        List<DateData> datedataInfo = new ArrayList<>();
        List<String> datedataAlignInfo = new ArrayList<>();
        datedataAlignList = new ArrayList<>();

        for(Integer imagedate : imagedateInfo)
        {
            String imagedateString = imagedate+"";
            char[] imagedateCharArray = imagedateString.toCharArray();

            int year = Integer.parseInt(imagedateString.substring(0, 4));
            int month = Integer.parseInt(imagedateString.substring(4, imagedateCharArray.length));

            //Log.v("MainActivity", "year:"+year+", month:"+month);
            datedataInfo.add(new DateData(year, month));
        }

        //올해 값
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        Date currentTime = new Date();
        int yearTime = Integer.parseInt(formatter.format(currentTime));


        for(int i = yearTime; i > 1970; i--)
        {
            for(int z=12; z > 0; z--)
            {
                for(int j=0; j<datedataInfo.size(); j++)
                {
                    if(datedataInfo.get(j).getYear() == i && datedataInfo.get(j).getMonth() == z)
                    {
                        String date = "Photo "+"By "+datedataInfo.get(j).getYear()+"년 \t"+datedataInfo.get(j).getMonth()+"월";
                        datedataAlignInfo.add(date);

                        datedataAlignList.add(new DateData(datedataInfo.get(j).getYear(), datedataInfo.get(j).getMonth()));
                    }
                }
            }
        }

        //ListView 생성
        setupRecyclerView(datedataAlignInfo);
    }

    private void setupRecyclerView(List date_list)
    {
        recyclerView.setHasFixedSize(true);

        //Adapter를 설정
        dateAdapter = new RecyclerDateAdapter(date_list);
        dateAdapter.setOnItemViewClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                //Toast.makeText(v.getContext(), "Position: " + recyclerView.getChildAdapterPosition(v) + "가 클릭됐습니다", Toast.LENGTH_LONG).show();
                int yearData = datedataAlignList.get(recyclerView.getChildAdapterPosition(v)).getYear();
                int monthData = datedataAlignList.get(recyclerView.getChildAdapterPosition(v)).getMonth();

                openImageViewer(yearData, monthData);
            }
        });
        recyclerView.setAdapter(dateAdapter);

        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
    }

    private void openImageViewer(int year, int month)
    {
        Intent i = new Intent(getContext(), ImageGridActivity.class);
        i.putExtra("year", year);
        i.putExtra("month", month);
        startActivity(i);
    }
}
