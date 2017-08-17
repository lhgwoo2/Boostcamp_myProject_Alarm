package com.boostcamp.sentialarm.Alarm;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boostcamp.sentialarm.API.Jamendo.DTO.MusicDTO;
import com.boostcamp.sentialarm.API.Weather.WeatherRootDTO;
import com.boostcamp.sentialarm.AlarmSong.SongDAO;
import com.boostcamp.sentialarm.AlarmSong.WeatherInfoDTO;
import com.boostcamp.sentialarm.R;
import com.boostcamp.sentialarm.Util.BaseActivity;
import com.boostcamp.sentialarm.Util.BitmapHelper;
import com.boostcamp.sentialarm.Util.LocationHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Random;

import co.mobiwise.library.MusicPlayerView;

public class AlarmPopActivity extends BaseActivity {

    private AlarmService mService = null;
    private boolean mIsBound;

    public static int HANDLER_MESSAGE = 1;
    public static int HANDLER_MESSAGE_WEATHER = 2;
    public static int HANDLER_MESSAGE_LOCATION = 3;
    public static int HANDLER_MESSAGE_WEATHER_BACKGROUND = 4;

    private MusicDTO musicDTO;

    private LinearLayout popMainLinearLayout;

    private MusicPlayerView mpv;
    private TextView textViewMusicTitle;
    private TextView textViewMusicianName;
    private TextView timeTextView;

    //날씨 뷰
    private TextView tempTextView;
    private TextView locationTextView;
    private TextView descriptionTextView;
    private ImageView weatherImageView;


    private KenBurnsView background_kenburnsView;


    private int hour;
    private int minute;
    private int alarmId;

    private BitmapHelper bitmapHelper;

    private SharedPreferences locationSetting;
    private SharedPreferences.Editor editor;

    private LocationHelper locationHelper;

    private FirebaseStorage storage;
    private StorageReference storageRef;

    private String backImageFileName=null;
    private String coverImageFileName=null;
    private WeatherInfoDTO currentWeatherInfoDTO = null;
    private String curLocation=null;

    private boolean locationFlag=false;
    private boolean weatherFlag=false;
    private boolean musicFlag=false;

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == HANDLER_MESSAGE) {   // Message id 가 HANDLER_MESSAGE 이면

                musicFlag=true;
                musicDTO = (MusicDTO) msg.obj;
                mpv.setCoverURL(musicDTO.getResults().get(0).getImage());
                mpv.setMax((int) musicDTO.getResults().get(0).getDuration());
                mpv.start();

                textViewMusicTitle.setText(musicDTO.getResults().get(0).getName());
                textViewMusicianName.setText(musicDTO.getResults().get(0).getArtist_name());

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {


                        //음악 이미지 다운로드
                        Bitmap imgBitmap = bitmapHelper.getBitmapOnURL(musicDTO.getResults().get(0).getImage());
                        String imgFileName = bitmapHelper.bitmapSaveInApp(getApplicationContext(), imgBitmap, musicDTO);

                        return imgFileName;
                    }

                    @Override
                    protected void onPostExecute(String fileName) {
                        super.onPostExecute(fileName);

                        coverImageFileName = fileName;

                        // 날씨, 음악, 위치 데이터가 모두 도착했을 때 DB저장
                        arriveDataSetRealm();
                    }
                }.execute();
            } else if (msg.what == HANDLER_MESSAGE_WEATHER) {      //날씨 화면 처리
                Log.i("날씨", "날씨");
                weatherFlag=true;
                WeatherRootDTO weatherRootDTO = (WeatherRootDTO) msg.obj;


                //현재 날씨 입력
                String weather = weatherRootDTO.getWeather().get(0).getMainCondition();
                Intent sendServiceIntent = new Intent(getApplicationContext(), AlarmService.class);

                Log.i("weather", weather);
                sendServiceIntent.putExtra("weather", weather);

                // musicplayer 서비스 시작
                startService(sendServiceIntent);
                doBindService();

                String resWeatherIcon = "@drawable/ic_weather_" + weatherRootDTO.getWeather().get(0).getIcon();
                int weatherIconID = getResources().getIdentifier(resWeatherIcon, "drawable", getApplication().getPackageName());
                weatherImageView.setImageResource(weatherIconID);

                String temps[] = String.valueOf(weatherRootDTO.getTemp().getTemp()).split("\\.");
                tempTextView.setText(temps[0] + "℃");
                descriptionTextView.setText(weatherRootDTO.getWeather().get(0).getDescription());


                // 날씨에 따른 파이어베이스 이미지 파일경로 생성
                String ref[] = getBackgroundPathInFirebaseStorage(weatherRootDTO);
                //ref[0] 은 경로, ref[1]은 파일이름.png제외

                // 저장할 데이터 객체
                currentWeatherInfoDTO = new WeatherInfoDTO();
                currentWeatherInfoDTO.setWeatherIconID(weatherIconID);
                currentWeatherInfoDTO.setTemperate(temps[0] + "℃");
                currentWeatherInfoDTO.setWeatherFileName(ref[1]);

                //배경 이미지 이름 멤버로 저장 다른곳에 활용을 위해
                backImageFileName = ref[1];

                Log.i("백그라운드 파일",ref[1]);

                Log.i("배경파일 경로", ref[0]);
                final StorageReference islandRef = storageRef.child(ref[0]);

               islandRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Target<Bitmap> target = Glide.with(getApplicationContext()).asBitmap().load(uri).into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                background_kenburnsView.setImageBitmap(resource);
                                //Bitmap backgroundImage background_kenburnsView
                                BitmapHelper bitmapHelper = new BitmapHelper();
                                bitmapHelper.bitmapSaveInApp(getApplicationContext(), resource,backImageFileName);

                                Log.i("성공", "Background 이미지 로컬에 저장, 이미지 용량:"+resource.getByteCount()+"Byte");
                            }
                        });

                        // 날씨, 음악, 위치 데이터가 모두 도착했을 때 DB저장
                        arriveDataSetRealm();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
            } else if (msg.what == HANDLER_MESSAGE_LOCATION) {
                locationFlag=true;
                Address address = (Address) msg.obj;
                String locations[] = address.getAddressLine(0).split(" ");

                String addString = locations[1] + " " + locations[2] + " " + locations[3];

                // 현재 위치 저장
                curLocation = addString;
                locationTextView.setText(addString);

                // 날씨, 음악, 위치 데이터가 모두 도착했을 때 DB저장
                arriveDataSetRealm();

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

        //파이어베이스 저장소 초기화
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

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
        popMainLinearLayout = (LinearLayout) findViewById(R.id.pop_main_layout);
        background_kenburnsView = (KenBurnsView) findViewById(R.id.pop_backgound_kenburns_iv);

        mpv = (MusicPlayerView) findViewById(R.id.mpv);

        Intent receiverIntent = getIntent();
        hour = receiverIntent.getIntExtra("hour", 0);
        minute = receiverIntent.getIntExtra("minute", 0);
        alarmId = receiverIntent.getIntExtra("alarmID", 0);

        setTimeTextView();


    }
    private void arriveDataSetRealm(){


        if(weatherFlag && locationFlag && musicFlag){
            currentWeatherInfoDTO.setAddress(curLocation);
            Log.i("데이터 들어온다.musiDTO",currentWeatherInfoDTO.getAddress()+"주소 , 정보"+currentWeatherInfoDTO.getTemperate()+" 커버이미지 "+coverImageFileName);
            SongDAO songDAO = new SongDAO();
            songDAO.createSongRealm();

            //음악 재생 기록 저장
            songDAO.setSongData(musicDTO, currentWeatherInfoDTO, coverImageFileName);
            songDAO.closeSongRealm();
        }
    }

    private String[] getBackgroundPathInFirebaseStorage(WeatherRootDTO weatherRootDTO) {

        String weather = weatherRootDTO.getWeather().get(0).getMainCondition();
        String backFileName = weather.toLowerCase() + "_";
        String ref = weather + "/";

        char iconDaynNight = String.valueOf(weatherRootDTO.getWeather().get(0).getIcon()).charAt(2);
        if (iconDaynNight == 'd') {
            ref += "Day/";
            backFileName += "day_";
        } else if (iconDaynNight == 'n') {
            ref += "Night/";
            backFileName += "night_";
        }
        int range = new Random().nextInt(2) + 1;
        backFileName += range + ".png";

        ref += backFileName;

        String[] name = new String[2];

        name[0] = ref;
        name[1] = backFileName.split("\\.")[0];

        return name;
    }

    private void setTimeTextView() {
        String time = "";

        if (hour < 10) {
            time = "0" + hour;
        } else {
            time += hour;
        }
        time += ":";
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

    // back 작동 막기
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        doUnbindService();
        super.onDestroy();
    }


}
