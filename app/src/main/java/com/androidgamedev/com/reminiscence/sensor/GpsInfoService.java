package com.androidgamedev.com.reminiscence.sensor;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.androidgamedev.com.reminiscence.HomeActivity;
import com.androidgamedev.com.reminiscence.R;

public class GpsInfoService extends Service implements LocationListener
{
    private final Context mContext;

    //현재 GPS 사용유무
    boolean isGPSEnabled = false;

    //네트워크 사용유무
    boolean isNetworkEnabled = false;

    //GPS 상태값
    boolean isGetLocation = false;

    Location location;
    double lat;     //위도
    double lon;     //경도

    //최소 GPS 정보 업데이트 거리 : 10M
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;

    //최소 GPS 정보 업데이트 시간(기본단위 : ms)
    private static final long MIN_TIME_BW_UPDATES = 100 * 60 * 1;

    protected LocationManager locationManager;

    public static final int REQUEST_CODE = 100;
    public GpsInfoService(Context context)
    {
        this.mContext = context;
        getLocation();
    }
    public Location getLocation()
    {
        try
        {
            locationManager = (LocationManager)mContext.getSystemService(LOCATION_SERVICE);

            //GPS 상태 정보 가져오기
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            //네트워크 상태 정보 가져오기
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            //GPS와 네트워크 모두 사용이 가능하지 않는 경우
            if(!isGPSEnabled && !isNetworkEnabled)
            {

            }else
            {
                this.isGetLocation = true;

                //네트워크 정보로 부터 위치값 가져오기
                if(isNetworkEnabled)
                {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if(locationManager != null)
                    {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if(location != null)
                        {
                            //위,경도 저장
                            lat = location.getLatitude();
                            lon = location.getLongitude();
                        }
                    }
                }

                //GPS 센서로부터 위치값 가져오기
                if(isGPSEnabled)
                {
                    if(location == null)
                    {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        if(locationManager != null)
                        {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if(location != null)
                            {
                                lat = location.getLatitude();
                                lon = location.getLongitude();
                            }
                        }
                    }
                }
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return location;
    }

    //종료
    public void stopUsingGPS()
    {
        if(locationManager != null)
            locationManager.removeUpdates(GpsInfoService.this);
    }

    //위도값 가져오기
    public double getLatitude()
    {
        if(location != null)
            lat = location.getLatitude();
        return lat;
    }

    //경도값 가져오기
    public double getLongitude()
    {
        if(location != null)
            lon = location.getLongitude();
        return lon;
    }

    //GPS 또는 Wifi 정보가 켜져 있는지 여부를 확인
    public boolean isGetLocation()
    {
        return this.isGetLocation;
    }

    /*
    GPS 정보를 가져오지 못한 경우 설정값으로 갈지 물어보는 alert 창
     */
    public void showSettingsAlert()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext, R.style.AppCompatDialogStyle);

        alertDialog.setTitle("위치 서비스 사용");
        alertDialog.setMessage("Reminiscence에서 내 위치 정보를 사용하려 합니다. 단말기의 설정에서 위치 서비스 사용을 허용해주세요");
        alertDialog.setPositiveButton("설정하기", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("취소", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    public void onLocationChanged(Location location)
    {

    }

    public void onStatusChanged(String provider, int status, Bundle extras)
    {

    }

    public void onProviderEnabled(String provider)
    {

    }

    public void onProviderDisabled(String provider)
    {

    }
}
