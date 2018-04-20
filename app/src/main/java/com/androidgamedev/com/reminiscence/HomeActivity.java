package com.androidgamedev.com.reminiscence;

import android.Manifest;
import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.androidgamedev.com.reminiscence.contact.UserDBManager;
import com.androidgamedev.com.reminiscence.contact.UserDBSqlData;
import com.androidgamedev.com.reminiscence.localprocess.DataInfo;

import java.io.File;

public class HomeActivity extends Activity implements NavigationDrawerFragment.NavigationDrawerCallbacks
{

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    private CharSequence mTitle;

    private final int REQUEST_CODE_GPS = 100;
    private final int REQUEST_CODE_STORAGE = 101;

    FragmentManager fragmentManager = getFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        Log.v("HomeActivity", mTitle.toString());

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

        //업로드할 사진을 임시로 넣을 폴더를 생성
        String path = DataInfo.pic_loc;

        File file = new File(path);
        if(!file.exists())
        {
            file.mkdir();
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position)
    {
        // update the main content by replacing fragments
        /*
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1)) //첫번째 매개변수: ViewGroup(Fragment를 배치할 뷰그룹), 두번째 매개변수: 추가할 Fragment
                .commit();
        */

        //로그인 했었는지 여부 확인
        String[] auth = {"0"};
        UserDBManager dbMgr = new UserDBManager(getApplicationContext());
        dbMgr.dbOpen();
        String auth_email = dbMgr.selectAuthEmail(UserDBSqlData.SQL_DB_SELECT_AUTH, auth);
        dbMgr.dbClose();

        //Memories
        if(position == 0)
        {
            //로그인 안 한 경우
            if(auth_email == null)
            {
                fragmentManager.beginTransaction()
                        .replace(R.id.container, LoginFragment.newInstance())
                        //.addToBackStack(null)
                        .commit();
            }else   //로그인을 한 경우
            {
                DataInfo.user_id = auth_email;

                /*
                fragmentManager.beginTransaction()
                        .replace(R.id.container, OtherMemorysFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                */
                checkLocationPermissionCheck();
            }
        }
        //Photos
        else if(position==1)
        {
            checkReadStoragePermissionCheck();
        }
        //Setting
        else if(position == 2)
        {
            //Toast.makeText(this, "Setting Tag", Toast.LENGTH_SHORT).show();
            //로그인 안 한 경우
            if(auth_email == null)
            {
                fragmentManager.beginTransaction()
                        .replace(R.id.container, LoginFragment.newInstance())
                        //.addToBackStack(null)
                        .commit();
            }else   //로그인을 한 경우
            {
                DataInfo.user_id = auth_email;

                fragmentManager.beginTransaction()
                        .replace(R.id.container, SettingFragment.newInstance())
                        //.addToBackStack(null)
                        .commit();
            }
        }
    }

    public void onSectionAttached(int number)
    {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }
    /*
    public void restoreActionBar()
    {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }
    */
    public void checkReadStoragePermissionCheck()
    {
        /*
        RunTime Permission
         */
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            //권한 확인
        /*
        앱에 권한이 있는 경우 = PackageManager.PERMISSON_GRANTED 반환
        앱에 권한이 없는 경우 = PackageManager.PERMISSON_DENIED 반환
         */
            //앱에 권한이 없는 경우
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) //Permission 승낙이 안된 경우
            {
                //사용자에게 권한이 필요한 이유를 보여줄지 말지를 결정하는 부분
            /*
            이전에 앱이 이 권한을 요청했고 사용자가 요청을 거부한 경우 이 메소드는 true를 반환

            과거에 사용자가 권한 요청을 거절하고 권한 요청 시스템 대화상자에서 Don't ask again 옵션을 선택한 경우 false를 반환
             */
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE);
                }else
                {
                /*
                requestPermissions()를 호출하는 경우 시스템은 표준 대화상자를 사용자에게 표시
                 */
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE);
                }
            }else   //Permission 승낙이 된 경우
            {
                goMyPhoto();
            }
        }else
        {
            goMyPhoto();
        }
    }

    public void goMyPhoto()
    {
        fragmentManager.beginTransaction()
                .replace(R.id.container, MyPhotoFragment.newInstance())
                //.addToBackStack(null)
                .commit();
    }

    public void checkLocationPermissionCheck()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION))
                {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_GPS);
                }else
                {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_GPS);
                }
            }else
            {
                goOthersMemory();
            }
        }else
        {
            goOthersMemory();
        }
    }

    public void goOthersMemory()
    {
        fragmentManager.beginTransaction()
                .replace(R.id.container, OtherMemorysFragment.newInstance())
                //.addToBackStack(null)
                .commit();
    }

    //ActivityCompat.requestPermissions의 콜백함수
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        switch (requestCode)
        {
            case REQUEST_CODE_STORAGE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    //Permission Acceped
                    goMyPhoto();

                }else
                {
                    //Permission Denied
                    finish();
                }
                break;
            case REQUEST_CODE_GPS:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    goOthersMemory();
                }else
                {
                    finish();
                }
                break;
        }
    }
}