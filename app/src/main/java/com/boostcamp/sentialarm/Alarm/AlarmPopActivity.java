package com.boostcamp.sentialarm.Alarm;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.boostcamp.sentialarm.API.Jamendo.DTO.MusicDTO;
import com.boostcamp.sentialarm.AlarmSong.SongDAO;
import com.boostcamp.sentialarm.R;
import com.boostcamp.sentialarm.Util.BaseActivity;
import com.boostcamp.sentialarm.Util.BitmapHelper;

import co.mobiwise.library.MusicPlayerView;

public class AlarmPopActivity extends BaseActivity {

    private AlarmService mService = null;
    private boolean mIsBound;

    public static int HANDLER_MESSAGE = 1;
    private MusicDTO musicDTO;

    private MusicPlayerView mpv;
    private TextView textViewMusicTitle;
    private TextView textViewMusicianName;
    private TextView timeTextView;

    private int hour;
    private int minute;
    private int alarmId;


    private BitmapHelper bitmapHelper;


    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == HANDLER_MESSAGE) {   // Message id 가 HANDLER_MESSAGE 이면
                Log.i("tests", "핸들러메시지 도착:" + msg.obj.toString());
                musicDTO = (MusicDTO) msg.obj;
                mpv.setCoverURL(musicDTO.getResults().get(0).getImage());
                mpv.setMax((int) musicDTO.getResults().get(0).getDuration());
                mpv.start();

                textViewMusicTitle.setText(musicDTO.getResults().get(0).getName());
                textViewMusicianName.setText(musicDTO.getResults().get(0).getArtist_name());

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {

                        Log.i("tests","이미지 저장 준비");
                        //음악 이미지 다운로드드
                        Bitmap imgBitmap = bitmapHelper.getBitmapOnURL(musicDTO.getResults().get(0).getImage());
                        String imgFileName = bitmapHelper.bitmapSaveInApp(getApplicationContext(), imgBitmap, musicDTO);

                        Log.i("tests","이미지 저장 완료");
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
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("알람실행", "처음화면");



        bitmapHelper = new BitmapHelper();

        setContentView(R.layout.activity_alarm_pop);

        textViewMusicTitle = (TextView) findViewById(R.id.tv_music_title);
        textViewMusicianName = (TextView) findViewById(R.id.tv_music_musician_name);
        timeTextView = (TextView) findViewById(R.id.alarmpop_tv_time);

        mpv = (MusicPlayerView) findViewById(R.id.mpv);
        mpv.setProgressEmptyColor(Color.LTGRAY);
        mpv.setProgressLoadedColor(Color.GRAY);
        mpv.setTimeColor(Color.LTGRAY);


        Intent receiverIntent = getIntent();
        hour = receiverIntent.getIntExtra("hour", 0);
        minute = receiverIntent.getIntExtra("minute", 0);
        alarmId = receiverIntent.getIntExtra("alarmID", 0);

        setTimeTextView();


        Log.i("tests", hour + ":" + minute + " 브로드캐스트릿버에서 왓음.");
        Intent sendServiceIntent = new Intent(this.getBaseContext(), AlarmService.class);

        String weather = "sunshine";
        sendServiceIntent.putExtra("weather", weather);

        startService(sendServiceIntent);
        doBindService();


    }
    private void setTimeTextView(){
        String time = "";

        if(hour<10){
            time = "0"+hour;
        }else{
            time += hour;
        }
        time += " : ";
        if(minute<10){
            time+= "0"+minute;
        }else{
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
