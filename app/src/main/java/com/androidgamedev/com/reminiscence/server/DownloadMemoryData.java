package com.androidgamedev.com.reminiscence.server;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DownloadMemoryData
{
    @GET("/showingMemory")
    Call<List<MemoryDataDTO>> downloadMemory(@Query("latitude") Double latitude, @Query("longitude") Double longitude);
}
