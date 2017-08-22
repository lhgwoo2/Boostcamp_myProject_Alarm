package com.boostcamp.sentialarm.Alarm;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.boostcamp.sentialarm.API.Jamendo.DTO.MusicDTO;
import com.boostcamp.sentialarm.API.MediaPlayer.MusicLocalDTO;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import co.mobiwise.library.MusicPlayerView;

public class AlarmPopActivity extends BaseActivity {


    private AlarmService mService = null;
    private boolean mIsBound;

    public static int HANDLER_MESSAGE = 1;
    public static int HANDLER_MESSAGE_WEATHER = 2;
    public static int HANDLER_MESSAGE_LOCATION = 3;

    private MusicDTO musicDTO;

    private MusicPlayerView mpv;
    private TextView textViewMusicTitle;
    private TextView textViewMusicianName;
    private TextView timeTextView;
    private TextView weekTextView;

    //날씨 뷰
    private TextView tempTextView;
    private TextView locationTextView;
    private TextView descriptionTextView;
    private ImageView weatherImageView;
    private KenBurnsView background_kenburnsView;

    private TextView exitTextView;
    private TextView postponeTextView;


    private int hour;
    private int minute;
    private int alarmId;

    private BitmapHelper bitmapHelper;

    private SharedPreferences locationSetting;
    private SharedPreferences.Editor editor;

    private LocationHelper locationHelper;
    private LocationManager manager;

    private FirebaseStorage storage;
    private StorageReference storageRef;

    private String backImageFileName = null;
    private String coverImageFileName = null;
    private WeatherInfoDTO currentWeatherInfoDTO = null;
    private String curLocation = null;

    private boolean networkConnection = false;
    private boolean locationFlag = false;
    private boolean weatherFlag = false;
    private boolean musicFlag = false;

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == HANDLER_MESSAGE) {   // Message id 가 HANDLER_MESSAGE 이면

                if (networkConnection) {
                    musicFlag = true;
                    musicDTO = (MusicDTO) msg.obj;
                    mpv.setCoverURL(musicDTO.getResults().get(0).getImage());
                    mpv.setMax((int) musicDTO.getResults().get(0).getDuration());
                    mpv.start();

                    textViewMusicTitle.setText(musicDTO.getResults().get(0).getName());
                    textViewMusicianName.setText(musicDTO.getResults().get(0).getArtist_name());

                    saveMusicInfoAsync();
                } else {
                    MusicLocalDTO musicLocalDTO = (MusicLocalDTO) msg.obj;
                    mpv.setCoverDrawable(R.drawable.ic_default_record);
                    mpv.setMax(musicLocalDTO.getDuration());
                    mpv.start();

                    textViewMusicTitle.setText(musicLocalDTO.getTitle());
                    textViewMusicianName.setText(musicLocalDTO.getArtist());

                    Glide.with(getApplicationContext()).load(R.drawable.bg_default_localbackground).into(background_kenburnsView);
                    arriveLocalSongDataRealm(musicLocalDTO);
                }

                Log.i("시간", "오나?");
                //시간 뷰 설정
                setTimeTextView();

            } else if (msg.what == HANDLER_MESSAGE_WEATHER) {      //날씨 화면 처리
                weatherFlag = true;
                WeatherRootDTO weatherRootDTO = (WeatherRootDTO) msg.obj;

                //현재 날씨 입력
                String weather = weatherRootDTO.getWeather().get(0).getMainCondition();
                Intent sendServiceIntent = new Intent(getApplicationContext(), AlarmService.class);

                Log.i("날씨", weather);
                sendServiceIntent.putExtra("weather", weather);
                sendServiceIntent.putExtra("network", true);

                // 날씨기반으로 노래를 가져옴 musicplayer 서비스 시작
                startService(sendServiceIntent);
                doBindService();

                setWeatherViewAndDTO(weatherRootDTO);

            } else if (msg.what == HANDLER_MESSAGE_LOCATION) {
                locationFlag = true;
                Address address = (Address) msg.obj;
                String locations[] = address.getAddressLine(0).split(" ");

                String addString = locations[1] + " " + locations[2] + " " + locations[3];

                // 현재 위치 저장
                curLocation = addString;
                locationTextView.setText(addString);

                // 날씨, 음악, 위치 데이터가 모두 도착했을 때 DB저장
                arriveDataSetRealm();
                setPreferencePostion(address);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        turnOnScreen();

        setContentView(R.layout.activity_alarm_pop);

        Intent receiverIntent = getIntent();
        alarmId = receiverIntent.getIntExtra("alarmID", 0);

        initUtil();
        initView();
        initAudioVolume();      // 알람이 켜질 때 강제로 소리 키우기


        // Network가 연결확인
        if (isNetworkConnected(getApplicationContext())) {
            networkConnection = true;
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {  //gps가 꺼져있으면 프리퍼런스에 저장된 이전위치 가져옴.
                getPreferencePosition();
            } else {      //gps가 켜져 있는 경우
                getGPSPostionAsync();
            }
        } else {
            networkConnection = false;
            Intent sendServiceIntent = new Intent(getApplicationContext(), AlarmService.class);
            sendServiceIntent.putExtra("network", false);
            startService(sendServiceIntent);
            doBindService();

        }


    }

    private void initUtil() {
        locationHelper = new LocationHelper();
        locationHelper.setLocationManager(getApplicationContext());
        manager = locationHelper.getLocationManager();

        //파이어베이스 저장소 초기화
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        bitmapHelper = new BitmapHelper();

        //프리퍼런스 init, 0은 읽고 쓰기 모두 가능
        locationSetting = getSharedPreferences("locationSetting", 0);
    }
    private void turnOnScreen(){
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
                );
    }

    private void initView() {

        textViewMusicTitle = (TextView) findViewById(R.id.tv_music_title);
        textViewMusicianName = (TextView) findViewById(R.id.tv_music_musician_name);
        timeTextView = (TextView) findViewById(R.id.alarmpop_tv_time);
        weekTextView = (TextView) findViewById(R.id.alarmpop_tv_week);

        //날씨 뷰
        tempTextView = (TextView) findViewById(R.id.pop_weather_temperate_tv);
        locationTextView = (TextView) findViewById(R.id.pop_weather_location_tv);
        descriptionTextView = (TextView) findViewById(R.id.pop_weather_description_tv);
        weatherImageView = (ImageView) findViewById(R.id.pop_weather_iv);
        background_kenburnsView = (KenBurnsView) findViewById(R.id.pop_backgound_kenburns_iv);

        // 알람 종료 및 다시울림 버튼
        exitTextView = (TextView) findViewById(R.id.tv_alarm_exit);
        postponeTextView = (TextView) findViewById(R.id.tv_alarm_postpone);

        mpv = (MusicPlayerView) findViewById(R.id.mpv);

        viewSetListener();
    }

    private void initAudioVolume() {
        AudioManager audio;
        audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audio.setStreamVolume(AudioManager.STREAM_MUSIC,
                audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC), AudioManager.FLAG_VIBRATE);

    }

    private void viewSetListener() {
        exitTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrate(20);
                Toast.makeText(getApplicationContext(), "알람이 종료되었습니다.", Toast.LENGTH_LONG).show();

                finish();

            }
        });

        postponeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrate(20);
                Toast.makeText(getApplicationContext(), "알람이 5분 연기되었습니다.", Toast.LENGTH_LONG).show();

                Calendar curCal = Calendar.getInstance();
                int curHour = curCal.get(Calendar.HOUR_OF_DAY);
                int curMinute = curCal.get(Calendar.MINUTE);

                int postponeHour = 0;
                int postponeMinute = 0;

                // 5분 연기
                if (curMinute + 5 >= 60) {
                    postponeHour = curHour + 1;
                    postponeMinute = curMinute + 5 - 60;
                } else {
                    postponeHour = curHour;
                    postponeMinute = curMinute + 5;
                }

                AlarmScheduler.registerAlarm(getApplicationContext(), alarmId, postponeHour, postponeMinute);

                finish();

            }
        });
    }

    private boolean isNetworkConnected(Context context) {
        boolean isConnected = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();


        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                Toast.makeText(context, activeNetwork.getTypeName(), Toast.LENGTH_SHORT).show();
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                Toast.makeText(context, activeNetwork.getTypeName(), Toast.LENGTH_SHORT).show();
            }
            isConnected = true;

        } else isConnected = false;

        return isConnected;
    }

    private void getPreferencePosition() {

        // 프리퍼런스
        double latitude = locationSetting.getFloat("latitude", (float) 37.57599);      //위도               , 초기화 광화문 위치
        double longitude = locationSetting.getFloat("longitude", (float) 126.97692);      //경도 가져오기

        //핸들러로 장소 이름을 가져온다.
        locationHelper.setHandler(mHandler);
        locationHelper.getLocationNameAsyncBeforeWeather(latitude, longitude);
    }

    private void setPreferencePostion(Address address) {
        // 프리퍼런스에 이전 위치 저장
        editor = locationSetting.edit();
        editor.putFloat("latitude", (float) address.getLatitude());
        editor.putFloat("longitude", (float) address.getLongitude());
        editor.commit();
    }

    private void getGPSPostionAsync() {
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

    private void saveMusicInfoAsync() {
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
    }

    private void setWeatherViewAndDTO(WeatherRootDTO weatherRootDTO) {

        String resWeatherIcon = "@drawable/ic_weather_" + weatherRootDTO.getWeather().get(0).getIcon();
        int weatherIconID = getResources().getIdentifier(resWeatherIcon, "drawable", getApplication().getPackageName());
        Glide.with(getApplicationContext()).load(weatherIconID).into(weatherImageView);


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

        Log.i("background file Name", ref[1]);
        Log.i("background ref", ref[0]);
        final StorageReference islandRef = storageRef.child(ref[0]);

        islandRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Target<Bitmap> target = Glide.with(getApplicationContext()).asBitmap().load(uri).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {

                        Log.i("background file down 완료", backImageFileName);
                        background_kenburnsView.setImageBitmap(resource);

                        new AsyncTask<Bitmap, Void, Void>() {
                            @Override
                            protected Void doInBackground(Bitmap... bitmaps) {

                                BitmapHelper bitmapHelper = new BitmapHelper();
                                bitmapHelper.bitmapSaveInApp(getApplicationContext(), bitmaps[0], backImageFileName);

                                return null;
                            }
                        }.execute(resource);
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
    }

    private void arriveLocalSongDataRealm(final MusicLocalDTO musicLocalDTO) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SongDAO songDAO = new SongDAO();
                songDAO.createSongRealm();

                //음악 재생 기록 저장
                songDAO.setLocalSongData(musicLocalDTO);
                songDAO.closeSongRealm();
            }
        }).start();
    }

    private void arriveDataSetRealm() {

        if (weatherFlag && locationFlag && musicFlag) {
            currentWeatherInfoDTO.setAddress(curLocation);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SongDAO songDAO = new SongDAO();
                    songDAO.createSongRealm();

                    //음악 재생 기록 저장
                    songDAO.setSongData(musicDTO, currentWeatherInfoDTO, coverImageFileName);
                    songDAO.closeSongRealm();
                }
            }).start();
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

        Calendar cal = Calendar.getInstance();
        hour = cal.get(Calendar.HOUR_OF_DAY);
        minute = cal.get(Calendar.MINUTE);


        String sHour = hour+"";
        String sMin = minute+"";
        if(minute<10){
            sMin = "0" + minute;
        }
        if(hour<10){
            sHour = "0"+hour;
        }

        String time = sHour+":"+sMin;


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM월 dd일 E요일");
        String curDate = simpleDateFormat.format(new Date());

        timeTextView.setText(time);
        weekTextView.setText(curDate);
    }

    private void vibrate(long duration) {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {
                0, duration
        };
        vibrator.vibrate(pattern, -1);
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
        Intent sendServiceIntent = new Intent(getApplicationContext(), AlarmService.class);
        stopService(sendServiceIntent);
        super.onDestroy();
    }


}
