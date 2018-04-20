package com.androidgamedev.com.reminiscence;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidgamedev.com.reminiscence.imghandle.ImageUtils;
import com.androidgamedev.com.reminiscence.localprocess.DataInfo;
import com.androidgamedev.com.reminiscence.sensor.GpsInfoService;
import com.androidgamedev.com.reminiscence.server.FileUploadService;
import com.androidgamedev.com.reminiscence.server.ResponseLoginInfoDTO;
import com.androidgamedev.com.reminiscence.server.ServerInfo;
import com.androidgamedev.com.reminiscence.util.CheckAccount;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadMemoryActivity extends AppCompatActivity
{
    EditText input_memory;

    GpsInfoService gps;

    String imagePath;
    int orientation;

    File fileCacheItem = null;

    private View mProgressView;
    private ViewGroup mShareFormViewGroup;

    PostMemoryToServer mpostMemory;
    String img_des;     //추억 글

    int year, month;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharememory);

        input_memory = (EditText)findViewById(R.id.memory_input);

        Intent i = getIntent();
        imagePath = i.getStringExtra("IMAGE_PATH");
        orientation = i.getIntExtra("ORIENTATION", 0);
        year = i.getIntExtra("year", 0);
        month = i.getIntExtra("month", 0);

        mProgressView = findViewById(R.id.sharememory_progress);
        mShareFormViewGroup = findViewById(R.id.share_form);

        gps = new GpsInfoService(UploadMemoryActivity.this);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
        {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mShareFormViewGroup.setVisibility(show ? View.GONE : View.VISIBLE);
            mShareFormViewGroup.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationEnd(Animator animation)
                {
                    mShareFormViewGroup.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationEnd(Animator animation)
                {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        }else
        {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mShareFormViewGroup.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.upload, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        switch(id)
        {
            case R.id.upload_pic:
                uploadFile();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //업로드할 이미지 파일을 임시로 생성
    private void createImmediatePicture()
    {
        Bitmap bmp = ImageUtils.getImageBitmap(imagePath, orientation);

        fileCacheItem = new File(DataInfo.pic_loc+"/immediate.jpg");
        OutputStream out = null;

        try
        {
            fileCacheItem.createNewFile();
            out = new FileOutputStream(fileCacheItem);

            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
        }catch(Exception e)
        {
            e.printStackTrace();
        }finally
        {
            try
            {
                if(out != null)
                    out.close();
            }catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private class PostMemoryToServer extends AsyncTask<Void, Void, Boolean>
    {
        double latitude, longitude;
        Context context;
        public PostMemoryToServer(double latitude, double longitude, Context context)
        {
            this.latitude = latitude;
            this.longitude = longitude;
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {

            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), fileCacheItem);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", fileCacheItem.getName(), reqFile);

            RequestBody description = RequestBody.create(MediaType.parse("text/plain"), img_des);
            RequestBody userid = RequestBody.create(MediaType.parse("text/plain"), DataInfo.user_id);
            RequestBody latitude_body = RequestBody.create(MediaType.parse("text/plain"), latitude+"");
            RequestBody longitude_body = RequestBody.create(MediaType.parse("text/plain"), longitude+"");

            boolean check_account = CheckAccount.checkAccount(context);
            if(check_account)
            {
                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(ServerInfo.server_address)
                        .build();

                FileUploadService fileupService = retrofit.create(FileUploadService.class);
                Call<ResponseLoginInfoDTO> fileup = fileupService.postMemory(body, description, userid, latitude_body, longitude_body);
                fileup.enqueue(new Callback<ResponseLoginInfoDTO>()
                {
                    @Override
                    public void onResponse(Call<ResponseLoginInfoDTO> call, Response<ResponseLoginInfoDTO> response)
                    {
                        if(response.isSuccessful())
                        {
                            Log.v("UploadMemoryActivity", "Network Response Successful");
                            String status = response.body().getStatus();

                            if(status.equals("success"))
                            {
                                Toast.makeText(getApplicationContext(),"성공적으로 업로드가 완료되었습니다.",Toast.LENGTH_SHORT).show();
                                fileCacheItem.delete();
                            }
                            showProgress(false);
                        }else
                        {
                            Log.v("UploadMemoryActivity", "Network Response Failure");
                            Toast.makeText(getApplicationContext(),"서버로부터 응답을 받지 못하였습니다.",Toast.LENGTH_SHORT).show();
                            showProgress(false);
                            fileCacheItem.delete();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseLoginInfoDTO> call, Throwable t)
                    {
                        Log.v("UploadMemoryActivity", "Network Request Failure");
                        Toast.makeText(getApplicationContext(),"업로드에 실패하였습니다.",Toast.LENGTH_SHORT).show();
                        showProgress(false);
                        fileCacheItem.delete();
                    }
                });
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean success)
        {
            mpostMemory = null;
        }

        @Override
        protected void onCancelled()
        {
            mpostMemory = null;
        }
    }

    private void uploadFile()
    {
        gps = new GpsInfoService(UploadMemoryActivity.this);

        if(gps.isGetLocation())
        {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            if(latitude == 0 || longitude == 0)
            {
                Toast.makeText(getApplicationContext(),"위치정보를 아직 올바르게 받아오지 못했습니다. 다시 한번 더 시도해 주시길 바랍니다.",Toast.LENGTH_SHORT).show();
            }else
            {
                Log.v("UploadMemoryActivity", "latitude : " + latitude + ", longitude : " + longitude);

                img_des = input_memory.getText().toString();

                createImmediatePicture();

                showProgress(true);

                //AsyncTask로 바꿔야 함
                mpostMemory = new PostMemoryToServer(latitude, longitude, UploadMemoryActivity.this);
                mpostMemory.execute();
            }
        }else
        {
            gps.showSettingsAlert();
        }
    }
}
