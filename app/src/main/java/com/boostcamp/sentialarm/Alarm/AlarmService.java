package com.boostcamp.sentialarm.Alarm;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

import com.boostcamp.sentialarm.API.MediaPlayer.MusicInfoDAO;
import com.boostcamp.sentialarm.API.MediaPlayer.MusicInfoDTO;
import com.boostcamp.sentialarm.API.MediaPlayer.MusicPlayer;

import java.util.Collections;
import java.util.List;

/**
 * Created by 현기 on 2017-08-09.
 */

public class AlarmService extends Service {

    MusicPlayer musicPlayer;
    private MusicInfoDAO musicDAO;

    public Handler mHandler = null;

    private final IBinder mBinder = new AlarmBinder();

    public class AlarmBinder extends Binder {
        AlarmService getService() {
            // Return this instance of LocalService so clients can call public methods
            return AlarmService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Service 객체와 (화면단 Activity 사이에서)
        // 통신(데이터를 주고받을) 때 사용하는 메서드
        // 데이터를 전달할 필요가 없으면 return null;
        return mBinder;
    }

    public void setHandler(Handler handler) {
        mHandler = handler;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        // 서비스에서 가장 먼저 호출됨(최초에 한번만)
        // 파이어베이스에서 데이터 가져오기 초기화
        musicDAO = new MusicInfoDAO();
        musicDAO.initAlarmFirebase();

        musicPlayer = new MusicPlayer();
        musicPlayer.createMediaPlayer();


    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 서비스가 호출될 때마다 실행

        // 날씨API를 활용하여 날씨를 가져온다.
        String weather = intent.getStringExtra("weather");

        //파이어베이스 데이터를 순차적으로 받기 위한 작업
        new AsyncTask<String, Void, List<MusicInfoDTO>>() {
            @Override
            protected List<MusicInfoDTO> doInBackground(String... strings) {

                List<MusicInfoDTO> playList = null;
                musicDAO.getSongListInFirebase(strings[0]);


                while (playList == null || playList.size() == 0) {
                    playList = musicDAO.getMusicInfoList();
                }


                return playList;
            }

            @Override
            protected void onPostExecute(List<MusicInfoDTO> musicInfoDTOs) {
                super.onPostExecute(musicInfoDTOs);

                Collections.shuffle(musicInfoDTOs);
                musicPlayer.setMusicInfoList(musicInfoDTOs);
                musicPlayer.setHandler(mHandler);

                //음원 프로세스 시작
                musicPlayer.musicProcess();
            }

        }.execute(weather);


        // 한번만 실행
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {


        // 서비스가 종료될 때 실행
        musicPlayer.stopMediaPlayer();

        if (mHandler != null) {
            mHandler = null;
        }
        super.onDestroy();
    }


}
