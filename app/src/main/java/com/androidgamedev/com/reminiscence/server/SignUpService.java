package com.androidgamedev.com.reminiscence.server;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface SignUpService
{
    @FormUrlEncoded
    @POST("/user/signupPost")
    Call<UserInfoStatusDTO> signupEvent(@FieldMap HashMap<String, Object> user);

    @FormUrlEncoded
    @POST("/user/signinPost")
    Call<ResponseLoginInfoDTO> signinEvent(@FieldMap HashMap<String, Object> user);
}
