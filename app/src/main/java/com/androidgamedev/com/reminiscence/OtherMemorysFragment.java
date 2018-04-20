package com.androidgamedev.com.reminiscence;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidgamedev.com.reminiscence.localprocess.SettingList;
import com.androidgamedev.com.reminiscence.sensor.GpsInfoService;
import com.androidgamedev.com.reminiscence.server.DownloadMemoryData;
import com.androidgamedev.com.reminiscence.server.MemoryDataDTO;
import com.androidgamedev.com.reminiscence.server.ServerInfo;
import com.androidgamedev.com.reminiscence.test.DummyDataGenerator;
import com.androidgamedev.com.reminiscence.util.CheckAccount;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class OtherMemorysFragment extends Fragment
{

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mListView;
    private MemoryViewAdapter memoryAdapter;

    private GpsInfoService gps;

    GetMemoryData getMemory = null;

    Activity activity;

    List<MemoryDataDTO> response_dataList = null;

    //TextView memoryRefreshTxt;

    public OtherMemorysFragment()
    {
        Log.v("OtherMemorysFragment", "constructor calling");
    }

    public static OtherMemorysFragment newInstance()
    {
        return new OtherMemorysFragment();
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View memory_view = inflater.inflate(R.layout.othersmemory_activity, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) memory_view.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                startShowingMemorys();
                Log.v("OtherMemorysFragment", "onRefresh called from SwipeRefreshLayout");
            }
        });
        mListView = (RecyclerView)memory_view.findViewById(android.R.id.list);
        mListView.setHasFixedSize(true);
        mListView.setLayoutManager(new LinearLayoutManager(activity));

        //memoryRefreshTxt = memory_view.findViewById(R.id.memory_refresh_txt);

        startShowingMemorys();

        memoryAdapter = new MemoryViewAdapter(response_dataList, activity);
        mListView.setAdapter(memoryAdapter);

        return memory_view;
    }

    /*
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
    }
    */

    /*
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }
    */

    private void onRefreshComplete()
    {
        Log.v("OtherMemorysFragment", "onRefreshComplete");

        mSwipeRefreshLayout.setRefreshing(false);
    }

    private class GetMemoryData extends AsyncTask<Void, Void, Boolean>
    {
        double latitude, longitude;
        boolean status;
        public GetMemoryData(double latitude, double longitude)
        {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            boolean check_account = CheckAccount.checkAccount(activity);

            if(check_account)
            {
                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(JacksonConverterFactory.create())
                        .baseUrl(ServerInfo.server_address)
                        .build();
                DownloadMemoryData downloadmemory = retrofit.create(DownloadMemoryData.class);
                Call<List<MemoryDataDTO>> memorys = downloadmemory.downloadMemory(latitude, longitude);
                try
                {
                    Response<List<MemoryDataDTO>> response = memorys.execute();
                    response_dataList = response.body();
                    status = response.isSuccessful();

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            return status;
        }

        @Override
        protected void onPostExecute(Boolean status)
        {
            if(getContext() != null)
            {
                if(status == false)
                    Toast.makeText(getContext(), "현재 서버에 문제가 있습니다", Toast.LENGTH_SHORT).show();
                else
                {
                    if(response_dataList != null)
                    {
                        memoryAdapter = new MemoryViewAdapter(response_dataList, activity);
                        mListView.setAdapter(memoryAdapter);
                        if (response_dataList.size() == 0)
                        {
                            Toast.makeText(getContext(), "근방에 작성된 추억이 없습니다", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                getMemory = null;
                onRefreshComplete();
            }
        }

        @Override
        protected void onCancelled()
        {
            getMemory = null;
        }
    }
    private void startShowingMemorys()
    {
        gps = new GpsInfoService(activity);

        //GPS가 켜져 있는 경우
        if(gps.isGetLocation())
        {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            //GPS 데이터 값 받아오기
            if(latitude == 0 || longitude == 0)
            {
                Toast.makeText(getContext(), "GPS 데이터를 받아오지 못했습니다 새로고침 해주세요", Toast.LENGTH_SHORT).show();
                onRefreshComplete();
            }else
            {
                //memoryRefreshTxt.setVisibility(View.GONE);
                //서버로 부터 값 받아오기
                getMemory = new GetMemoryData(latitude, longitude);
                getMemory.execute();
            }
        }else   //GPS가 꺼져 있는 경우
        {
            gps.showSettingsAlert();
        }
    }

    /*
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.refresh, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.menu_refresh)
        {
            startShowingMemorys();
        }
        return super.onOptionsItemSelected(item);
    }
    */
}
