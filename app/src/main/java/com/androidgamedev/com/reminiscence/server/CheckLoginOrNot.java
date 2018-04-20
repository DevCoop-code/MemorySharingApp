package com.androidgamedev.com.reminiscence.server;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CheckLoginOrNot
{
    @GET("/checkLogin")
    Call<UserInfoStatusDTO> checkLogin(@Query("id")String id);
}
