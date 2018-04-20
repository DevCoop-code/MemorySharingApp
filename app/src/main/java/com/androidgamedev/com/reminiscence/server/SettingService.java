package com.androidgamedev.com.reminiscence.server;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface SettingService
{
    @FormUrlEncoded
    @POST("/setting/changePassword")
    Call<UserInfoStatusDTO> changePassword(@FieldMap HashMap<String, Object> data);
}
