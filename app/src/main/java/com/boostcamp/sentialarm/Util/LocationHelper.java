package com.boostcamp.sentialarm.Util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.boostcamp.sentialarm.API.Weather.WeatherHelper;
import com.boostcamp.sentialarm.Activity.AlarmPopActivity;

import java.io.IOException;
import java.util.List;

/**
 * Created by 현기 on 2017-08-14.
 */

public class LocationHelper {


    private Geocoder geocoder;
    private LocationManager manager;


    private double latitude;
    private double longitude;

    private Handler mHandler;

    private Context context;

    public void setLocationManager(Context context) {
        manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.context = context;
    }

    public LocationManager getLocationManager() {
        return manager;
    }

    public void setHandler(Handler mHandler) {
        this.mHandler = mHandler;
    }

    private LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {

            Log.d("test", "onLocationChanged, location:" + location);
            longitude = location.getLongitude(); //경도
            latitude = location.getLatitude();   //위도

            Looper.myLooper().quit();
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };



    // GPS 가져온후 날씨 동작
    public void getLocationNameAsyncBeforeWeather(final double latitude, final double longitude) {

        new AsyncTask<Void, Void, Address>() {
            @Override
            protected Address doInBackground(Void... voids) {
                geocoder = new Geocoder(context);

                Address address = null;
                List<Address> list = null;
                try {

                    list = geocoder.getFromLocation(latitude, longitude, 1); // 얻어올 값의 개수
                    address = list.get(0);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                return address;
            }

            @Override
            protected void onPostExecute(Address address) {
                super.onPostExecute(address);

                Message msg;
                msg = mHandler.obtainMessage(AlarmPopActivity.HANDLER_MESSAGE_LOCATION);
                msg.obj = address;
                mHandler.sendMessage(msg);

                WeatherHelper weatherHelper = new WeatherHelper();
                weatherHelper.setHandler(mHandler);
                weatherHelper.getWeatherDataAsync(latitude, longitude);


            }
        }.execute();

    }

    public double[] getLatAndLongitude() {

        Looper.prepare();
        try {
            manager.requestSingleUpdate(LocationManager.GPS_PROVIDER, mLocationListener, null);
            manager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, mLocationListener, null);

            Looper.loop();      //콜백이 응답할 때까지 무한 루프

            //위치 받아오면 리스너 해제
            manager.removeUpdates(mLocationListener);
            Log.i("위치", "리스너 해제");
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        double[] list = {latitude, longitude};

        return list;

    }


    public void removeListener() {
        manager.removeUpdates(mLocationListener);
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

}
