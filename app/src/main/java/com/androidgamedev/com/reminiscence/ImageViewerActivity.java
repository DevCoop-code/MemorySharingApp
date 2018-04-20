package com.androidgamedev.com.reminiscence;

import android.Manifest;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.ScaleGestureDetectorCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidgamedev.com.reminiscence.contact.UserContactData;
import com.androidgamedev.com.reminiscence.contact.UserDBManager;
import com.androidgamedev.com.reminiscence.contact.UserDBSqlData;
import com.androidgamedev.com.reminiscence.dialog.GetMemoryDialog;
import com.androidgamedev.com.reminiscence.dialog.PermissionAgainDialog;
import com.androidgamedev.com.reminiscence.dialog.WritingMemoryDialog;
import com.androidgamedev.com.reminiscence.imghandle.ImageUtils;
import com.androidgamedev.com.reminiscence.sensor.GpsInfoService;

public class ImageViewerActivity extends Activity implements View.OnClickListener
{
    private String imagePath;
    private int orientation;

    ImageButton share_btn, delete_btn, write_btn, comment_btn;
    static final int REQUEST_CODE = 200;

    GpsInfoService gps;

    int year, month;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        share_btn = (ImageButton)findViewById(R.id.memory_share_btn);
        delete_btn = (ImageButton)findViewById(R.id.memory_delete_btn);
        write_btn = (ImageButton)findViewById(R.id.memory_write_btn);
        comment_btn = (ImageButton)findViewById(R.id.memory_comment_btn);

        share_btn.setOnClickListener(this);
        delete_btn.setOnClickListener(this);
        write_btn.setOnClickListener(this);
        comment_btn.setOnClickListener(this);

        Intent i = getIntent();
        imagePath = i.getStringExtra("IMAGE_PATH");
        orientation = i.getIntExtra("ORIENTATION", 0);
        year = i.getIntExtra("year", 0);
        month = i.getIntExtra("month", 0);

        showImageView(imagePath, orientation);

        //이미지 삭제 처리
        /*
        int mRowsDeleted = 0;
        mRowsDeleted = getContentResolver().delete(
                Uri.parse(imagePath),
                null,
                null
        );
        */
    }

    private void showImageView(String aImagePath, int aOrientation)
    {
        Bitmap bmp = ImageUtils.getImageBitmap(aImagePath, aOrientation);

        ImageView iView = (ImageView)findViewById(R.id.showimage);

        iView.setImageBitmap(bmp);
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.memory_share_btn:
                //로그인 여부
                boolean login = SignInOrNot();

                if(login)
                {
                    //위치정보 사용 허가 여부
                    locationPermissionCheck();
                }else
                {
                    Toast.makeText(getApplicationContext(), "로그인이 필요한 서비스 입니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.memory_delete_btn:
                delete_picture();
                break;
            case R.id.memory_write_btn:
                /*
                Intent i = new Intent(this, ImageDescriptActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                i.putExtra("IMAGE_PATH",imagePath);
                i.putExtra("ORIENTATION",orientation);
                i.putExtra("year", year);
                i.putExtra("month", month);
                startActivity(i);
                */
                WritingMemoryDialog writingDialog = WritingMemoryDialog.newInstance(imagePath, orientation);
                writingDialog.show(getFragmentManager(), "dialog");
                break;
            case R.id.memory_comment_btn:
                show_memory();
                break;
            default:
                break;
        }
    }

    public void delete_picture()
    {
        PermissionAgainDialog permissionDialog = PermissionAgainDialog.newInstance(R.string.alert_dialog_title, imagePath, year, month);
        permissionDialog.show(getFragmentManager(), "dialog");
    }

    public void show_memory()
    {
        /*
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if(prev != null)
            ft.remove(prev);
        DialogFragment memoryFragment = GetMemoryDialog.newInstance(imagePath);
        memoryFragment.show(ft,"dialog");
        */
        GetMemoryDialog memoryDialog = GetMemoryDialog.newInstance(imagePath);
        memoryDialog.show(getFragmentManager(), "dialog");
    }

    public void locationPermissionCheck()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION))
                {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);
                }else
                {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);
                }
            }else
            {
                goSharingMemory();
            }
        }else
        {
            goSharingMemory();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        switch (requestCode)
        {
            case REQUEST_CODE:
                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    //Permission Acceped
                    goSharingMemory();

                }else
                {
                    //Permission Denied
                    finish();
                }
                break;
        }
    }

    private void goSharingMemory()
    {
        Intent i = new Intent(this, UploadMemoryActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.putExtra("IMAGE_PATH", imagePath);
        i.putExtra("ORIENTATION", orientation);
        i.putExtra("year", year);
        i.putExtra("month", month);
        startActivity(i);
    }

    private boolean SignInOrNot()
    {
        String[] auth = {"0"};
        UserDBManager dbMgr = new UserDBManager(this);
        dbMgr.dbOpen();
        String auth_email = dbMgr.selectAuthEmail(UserDBSqlData.SQL_DB_SELECT_AUTH, auth);
        dbMgr.dbClose();

        if(auth_email == null)
            return false;
        else
            return true;
    }
}