package com.androidgamedev.com.reminiscence;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.androidgamedev.com.reminiscence.imgdata.GetMediaData;
import com.androidgamedev.com.reminiscence.imgdata.MediaData;

import java.util.ArrayList;

public class ImageGridActivity extends Activity implements AdapterView.OnItemClickListener
{
    private ArrayList<MediaData> mMediaList = null;

    int year, month;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imggrid);

        GridView gv = (GridView)findViewById(R.id.gridview);

        mMediaList = new ArrayList<MediaData>();

        Intent i = getIntent();
        year = i.getIntExtra("year",0);
        month = i.getIntExtra("month", 0);

        //Log.v("ImageGridActivity", "year:"+year+", month" + month);
        //MediaList Array에 데이터 넣기
        GetMediaData.getImageArrayData(this, mMediaList, year, month);

        ImageAdapter imageAdapter = new ImageAdapter(this);
        imageAdapter.initData(mMediaList);
        gv.setAdapter(imageAdapter);
        gv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View aView, int aPos, long aId)
    {
        showOriImage(mMediaList.get(aPos).getMediapath(), mMediaList.get(aPos).getOrientation());
    }

    private void showOriImage(String aImagePath, int aOrientation)
    {
        Intent i = new Intent(this, ImageViewerActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.putExtra("IMAGE_PATH", aImagePath);
        i.putExtra("ORIENTATION", aOrientation);
        i.putExtra("year", year);
        i.putExtra("month", month);
        startActivity(i);
    }
}
