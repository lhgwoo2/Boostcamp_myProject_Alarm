package com.boostcamp.sentialarm.API.Weather;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.boostcamp.sentialarm.Alarm.AlarmPopActivity;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by 현기 on 2017-08-14.
 */

public class WeatherHelper {
    private Gson gson;
    private Handler mHandler;

    public WeatherHelper() {
        super();
        gson = new Gson();
    }
    public void setHandler(Handler mHandler){
        this.mHandler = mHandler;
    }


    public void getWeatherDataAsync(final double latitude, final double longitude) {


        new AsyncTask<Void, Void, WeatherRootDTO>(){
            @Override
            protected WeatherRootDTO doInBackground(Void... voids) {
                //날씨 api url
                String apiURL = String.format("http://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&appid=77d417c06aaa8c6563ff70d7d967f73e&units=metric", latitude, longitude);

                Log.i("apiURl",apiURL);
                WeatherRootDTO weatherRootDTO = null;
                try {
                    JsonReader jsonReader = new JsonReader(new InputStreamReader(new URL(apiURL).openStream()));
                    Type listType = new TypeToken<WeatherRootDTO>() {
                    }.getType();

                    weatherRootDTO = gson.fromJson(jsonReader, listType);


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }

                return weatherRootDTO;
            }

            @Override
            protected void onPostExecute(WeatherRootDTO weatherRootDTO) {
                super.onPostExecute(weatherRootDTO);

                Message msg;
                msg = mHandler.obtainMessage(AlarmPopActivity.HANDLER_MESSAGE_WEATHER);
                msg.obj = weatherRootDTO;
                mHandler.sendMessage(msg);

            }
        }.execute();

    }

    public void getWeatherBackground(){
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    }


}
