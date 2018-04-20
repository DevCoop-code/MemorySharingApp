package com.androidgamedev.com.reminiscence;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidgamedev.com.reminiscence.contact.UserDBManager;
import com.androidgamedev.com.reminiscence.contact.UserDBSqlData;
import com.androidgamedev.com.reminiscence.deco.DividerItemDecoration;
import com.androidgamedev.com.reminiscence.localprocess.DataInfo;
import com.androidgamedev.com.reminiscence.localprocess.SettingList;

public class SettingFragment extends Fragment
{
    private RecyclerView mListView;
    private SettingViewAdapter settingAdapter;

    Activity activity;

    public SettingFragment()
    {

    }

    public static SettingFragment newInstance()
    {
        return new SettingFragment();
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
        View setting_view = inflater.inflate(R.layout.fragment_setting, container, false);

        mListView = setting_view.findViewById(R.id.setting_list);
        mListView.setHasFixedSize(true);

        settingAdapter = new SettingViewAdapter(SettingList.generateStringListData());
        settingAdapter.setOnItemViewClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                int position = mListView.getChildAdapterPosition(v);
                Log.v("SettingFragment", position + "");
                switch (position)
                {
                    case 0:         //비밀번호 변경
                        Intent i = new Intent(getContext(), ChangePasswordActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(i);
                        break;
                    case 1:         //버전정보
                        Toast.makeText(getContext(), "버전 1.0.1 입니다.",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:         //로그아웃
                        String[] email = {DataInfo.user_id};
                        UserDBManager dbMgr = new UserDBManager(activity);
                        dbMgr.dbOpen();
                        dbMgr.updateUauth(UserDBSqlData.SQL_DB_UPDATE_AUTH, email);
                        dbMgr.dbClose();

                        DataInfo.user_id = null;

                        Toast.makeText(getContext(),"로그아웃에 성공하였습니다", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getContext(), HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        break;
                    default:

                        break;
                }
            }
        });
        mListView.setAdapter(settingAdapter);
        mListView.addItemDecoration(new DividerItemDecoration(getContext()));

        return setting_view;
    }
}
