package com.androidgamedev.com.reminiscence.util;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.androidgamedev.com.reminiscence.HomeActivity;
import com.androidgamedev.com.reminiscence.contact.UserDBManager;
import com.androidgamedev.com.reminiscence.contact.UserDBSqlData;
import com.androidgamedev.com.reminiscence.localprocess.DataInfo;
import com.androidgamedev.com.reminiscence.server.CheckLoginOrNot;
import com.androidgamedev.com.reminiscence.server.ServerInfo;
import com.androidgamedev.com.reminiscence.server.UserInfoStatusDTO;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CheckAccount
{
    //서버로 부터 존재하는 계정인지 여부를 확인
    public static boolean checkAccount(Context context)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ServerInfo.server_address)
                .build();
        CheckLoginOrNot checklogin = retrofit.create(CheckLoginOrNot.class);
        Call<UserInfoStatusDTO> check = checklogin.checkLogin(DataInfo.user_id);
        try
        {
            Response<UserInfoStatusDTO> response = check.execute();

            //서버와의 연결 여부
            boolean status = response.isSuccessful();
            //서버로부터 받은 데이터
            UserInfoStatusDTO dto = response.body();

            //서버와 연결이 양호함
            if(status == true)
            {
                //존재하는 계정
                if(dto.getStatus().equals("exists_account"))
                {
                    return true;
                }else   //존재하지 않는 계정
                {
                    changeUauth(context);
                }
            }
            //서버와 연결이 되지 않음
            else
            {
                Toast.makeText(context, "서버와 연결이 되지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    //존재하지 않는 계정의 uauth값을 1로 바꾸는 함수
    private static void changeUauth(Context context)
    {
        String[] email = {DataInfo.user_id};
        UserDBManager dbMgr = new UserDBManager(context);
        dbMgr.dbOpen();
        dbMgr.updateUauth(UserDBSqlData.SQL_DB_UPDATE_AUTH, email);
        dbMgr.dbClose();

        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }
}
