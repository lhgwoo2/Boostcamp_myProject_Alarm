package com.boostcamp.sentialarm.Alarm;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.boostcamp.sentialarm.API.Jamendo.DTO.MusicDTO;
import com.boostcamp.sentialarm.AlarmSong.SongDTO;
import com.boostcamp.sentialarm.R;
import com.boostcamp.sentialarm.Util.Application.ApplicationClass;
import com.boostcamp.sentialarm.Util.BaseAtivity.BaseActivity;

import java.util.Date;

import co.mobiwise.library.MusicPlayerView;
import io.realm.Realm;

public class AlarmPopActivity extends BaseActivity {

    AlarmService mService;
    boolean mBound = false;

    public static int HANDLER_MESSAGE = 1;
    private static MusicDTO musicDTO;

    public static MusicPlayerView mpv;
    public static TextView textViewMusicTitle;
    public static TextView textViewMusicianName;

    public static Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == HANDLER_MESSAGE){   // Message id 가 HANDLER_MESSAGE 이면
                Log.i("tests","핸들러메시지 도착:"+msg.obj.toString());
                musicDTO = (MusicDTO) msg.obj;
                mpv.setCoverURL(musicDTO.getResults().get(0).getImage());
                mpv.setMax((int)musicDTO.getResults().get(0).getDuration());
                mpv.start();

                textViewMusicTitle.setText(musicDTO.getResults().get(0).getName());
                textViewMusicianName.setText(musicDTO.getResults().get(0).getArtist_name());

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {

                        // 송리스트에 데이터 저장
                        Realm realm = Realm.getInstance(ApplicationClass.songListConfig);

                        realm.executeTransactionAsync(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                SongDTO songDTO = realm.createObject(SongDTO.class);
                                songDTO.setId(0);
                                songDTO.setArtistName(musicDTO.getResults().get(0).getArtist_name());
                                songDTO.setMusicTitle(musicDTO.getResults().get(0).getName());
                                songDTO.setSongShareURL(musicDTO.getResults().get(0).getShareurl());
                                songDTO.setPlayDate(new Date());

                                musicDTO.getResults().get(0);
                            }
                        });

                        return null;
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);


                    }
                }.execute();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_pop);

        //ImageView iv = (ImageView) findViewById(R.id.iv_music_circle);

        /*
        Animation anim = AnimationUtils.loadAnimation(
                getApplicationContext(), // 현재 화면의 제어권자
                R.anim.rotate_anim);    // 설정한 에니메이션 파일
        iv.startAnimation(anim);
        */
        textViewMusicTitle = (TextView)findViewById(R.id.tv_music_title) ;
        textViewMusicianName = (TextView)findViewById(R.id.tv_music_musician_name);

        mpv = (MusicPlayerView) findViewById(R.id.mpv);
        mpv.setProgressEmptyColor(Color.LTGRAY);
        mpv.setProgressLoadedColor(Color.GRAY);
        mpv.setTimeColor(Color.LTGRAY);



    }



    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, AlarmService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            AlarmService.AlarmBinder binder = (AlarmService.AlarmBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };




}
