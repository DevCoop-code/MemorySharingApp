package com.androidgamedev.com.reminiscence.server;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface FileUploadService
{
    @Multipart
    @POST("/uploadForm")
    Call<ResponseLoginInfoDTO> postMemory(@Part MultipartBody.Part image, @Part("description")RequestBody description, @Part("userid")RequestBody userid, @Part("latitude")RequestBody latitude, @Part("longitude")RequestBody longitude);
}
