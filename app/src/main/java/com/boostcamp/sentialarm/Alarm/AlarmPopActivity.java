package com.boostcamp.sentialarm.Alarm;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.boostcamp.sentialarm.API.Jamendo.DTO.MusicDTO;
import com.boostcamp.sentialarm.API.Weather.WeatherRootDTO;
import com.boostcamp.sentialarm.AlarmSong.SongDAO;
import com.boostcamp.sentialarm.R;
import com.boostcamp.sentialarm.Util.BaseActivity;
import com.boostcamp.sentialarm.Util.BitmapHelper;
import com.boostcamp.sentialarm.Util.LocationHelper;

import co.mobiwise.library.MusicPlayerView;

public class AlarmPopActivity extends BaseActivity {

    private AlarmService mService = null;
    private boolean mIsBound;

    public static int HANDLER_MESSAGE = 1;
    public static int HANDLER_MESSAGE_WEATHER = 2;
    public static int HANDLER_MESSAGE_LOCATION = 3;
    public static int HANDLER_MESSAGE_WEATHER_BACKGROUND = 4;

    private MusicDTO musicDTO;

    private MusicPlayerView mpv;
    private TextView textViewMusicTitle;
    private TextView textViewMusicianName;
    private TextView timeTextView;

    //날씨 뷰
    private TextView tempTextView;
    private TextView locationTextView;
    private TextView descriptionTextView;
    private ImageView weatherImageView;


    private int hour;
    private int minute;
    private int alarmId;

    private BitmapHelper bitmapHelper;

    private SharedPreferences locationSetting;
    private SharedPreferences.Editor editor;

    private LocationHelper locationHelper;


    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == HANDLER_MESSAGE) {   // Message id 가 HANDLER_MESSAGE 이면

                musicDTO = (MusicDTO) msg.obj;
                mpv.setCoverURL(musicDTO.getResults().get(0).getImage());
                mpv.setMax((int) musicDTO.getResults().get(0).getDuration());
                mpv.start();

                textViewMusicTitle.setText(musicDTO.getResults().get(0).getName());
                textViewMusicianName.setText(musicDTO.getResults().get(0).getArtist_name());

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {


                        //음악 이미지 다운로드드
                        Bitmap imgBitmap = bitmapHelper.getBitmapOnURL(musicDTO.getResults().get(0).getImage());
                        String imgFileName = bitmapHelper.bitmapSaveInApp(getApplicationContext(), imgBitmap, musicDTO);

                        return imgFileName;
                    }

                    @Override
                    protected void onPostExecute(String fileName) {
                        super.onPostExecute(fileName);

                        SongDAO songDAO = new SongDAO();
                        songDAO.createSongRealm();

                        //음악 재생 기록 저장
                        songDAO.setSongData(musicDTO, fileName);
                        songDAO.closeSongRealm();

                    }
                }.execute();
            } else if (msg.what == HANDLER_MESSAGE_WEATHER) {      //날씨 화면 처리
                Log.i("날씨", "날씨");

                WeatherRootDTO weatherRootDTO = (WeatherRootDTO) msg.obj;


                //현재 날씨 입력
                String weather = weatherRootDTO.getWeather().get(0).getMainCondition();
                Intent sendServiceIntent = new Intent(getApplicationContext(), AlarmService.class);

                Log.i("weather", weather);
                weather = "sunshine";

                sendServiceIntent.putExtra("weather", weather);

                startService(sendServiceIntent);
                doBindService();

                String resWeatherIcon = "@drawable/ic_weather_" + weatherRootDTO.getWeather().get(0).getIcon();
                int weatherIconID = getResources().getIdentifier(resWeatherIcon, "drawable", getApplication().getPackageName());
                weatherImageView.setImageResource(weatherIconID);

                String temps[] = String.valueOf(weatherRootDTO.getTemp().getTemp()).split("\\.");
                tempTextView.setText(temps[0] + "℃");
                descriptionTextView.setText(weatherRootDTO.getWeather().get(0).getDescription());


            } else if (msg.what == HANDLER_MESSAGE_LOCATION) {

                Address address = (Address) msg.obj;
                String locations[] = address.getAddressLine(0).split(" ");

                locationTextView.setText(locations[1] + " " + locations[2] + " " + locations[3]);


                // 프리퍼런스에 이전 위치 저장
                editor = locationSetting.edit();
                editor.putFloat("latitude", (float) address.getLatitude());
                editor.putFloat("longitude", (float) address.getLongitude());
                editor.commit();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_pop);

        locationHelper = new LocationHelper();
        locationHelper.setLocationManager(getApplicationContext());
        LocationManager manager = locationHelper.getLocationManager();

        //프리퍼런스 init, 0은 읽고 쓰기 모두 가능
        locationSetting = getSharedPreferences("locationSetting", 0);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {  //gps가 꺼져있으면 프리퍼런스에 저장된 이전위치 가져옴.

            // 프리퍼런스
            double latitude = locationSetting.getFloat("latitude", (float) 37.57599);      //위도               , 초기화 광화문 위치
            double longitude = locationSetting.getFloat("longitude", (float) 126.97692);      //경도 가져오기

            //핸들러로 장소 이름을 가져온다.
            locationHelper.setHandler(mHandler);
            locationHelper.getLocationNameAsyncBeforeWeather(latitude, longitude);


        } else {      //gps가 켜져 있는 경우

            new AsyncTask<Void, Void, double[]>() {

                @Override
                protected double[] doInBackground(Void... voids) {

                    return locationHelper.getLatAndLongitude();
                }

                @Override
                protected void onPostExecute(double[] latLon) {
                    super.onPostExecute(latLon);
                    //핸들러로 장소이름을 가져온다.
                    locationHelper.setHandler(mHandler);
                    locationHelper.getLocationNameAsyncBeforeWeather(latLon[0], latLon[1]);

                }
            }.execute();
        }

        bitmapHelper = new BitmapHelper();


        textViewMusicTitle = (TextView) findViewById(R.id.tv_music_title);
        textViewMusicianName = (TextView) findViewById(R.id.tv_music_musician_name);
        timeTextView = (TextView) findViewById(R.id.alarmpop_tv_time);

        //날씨 뷰
        tempTextView = (TextView) findViewById(R.id.pop_weather_temperate_tv);
        locationTextView = (TextView) findViewById(R.id.pop_weather_location_tv);
        descriptionTextView = (TextView) findViewById(R.id.pop_weather_description_tv);
        weatherImageView = (ImageView) findViewById(R.id.pop_weather_iv);

        mpv = (MusicPlayerView) findViewById(R.id.mpv);
        mpv.setProgressEmptyColor(Color.LTGRAY);
        mpv.setProgressLoadedColor(Color.GRAY);
        mpv.setTimeColor(Color.LTGRAY);


        Intent receiverIntent = getIntent();
        hour = receiverIntent.getIntExtra("hour", 0);
        minute = receiverIntent.getIntExtra("minute", 0);
        alarmId = receiverIntent.getIntExtra("alarmID", 0);

        setTimeTextView();


    }

    private void setTimeTextView() {
        String time = "";

        if (hour < 10) {
            time = "0" + hour;
        } else {
            time += hour;
        }
        time += " : ";
        if (minute < 10) {
            time += "0" + minute;
        } else {
            time += minute;
        }

        timeTextView.setText(time);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mService = ((AlarmService.AlarmBinder) iBinder).getService();
            mService.setHandler(mHandler);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mService = null;
        }
    };

    private void doBindService() {
        bindService(new Intent(this,
                AlarmService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    private void doUnbindService() {
        if (mIsBound) {
            // Detach our existing connection.
            unbindService(mConnection);
            mIsBound = false;
        }
    }

    @Override
    protected void onDestroy() {
        doUnbindService();
        super.onDestroy();
    }


}
