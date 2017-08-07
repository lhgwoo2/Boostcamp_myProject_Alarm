package com.boostcamp.sentialarm.API.MediaPlayerAPI;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import com.boostcamp.alarmtest.API.FireBase.DTO.MusicInfoDTO;

import java.io.IOException;
import java.util.List;

/**
 * Created by 현기 on 2017-08-06.
 */

public class MusicPlayer extends MediaPlayer {

    private static MusicPlayer musicPlayer = null;
    private static int musicCount=0;
    private List<MusicInfoDTO> playList;

    private MusicPlayer(){

    }

    public static MusicPlayer getMusicPlayerIns(){
        if(musicPlayer == null){
            musicPlayer = new MusicPlayer();
            return musicPlayer;
        } else{
            return musicPlayer;
        }
    }

    public void setMusicInfoList(List<MusicInfoDTO> playList){
        this.playList = playList;
    }

    //곡의 순서를 가져오면
    public void setAsyncMediaPlayer(String mediaURL){

        Log.i("콜백 음원URL",mediaURL);
        try {
            musicPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            musicPlayer.setDataSource(mediaURL);
            musicPlayer.prepareAsync(); // might take long! (for buffering, etc)

            // TODO 음악 리스트 실행
            startAsyncMusicPlayer();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void startAsyncMusicPlayer(){

        musicPlayer.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // TODO media player를 실행
                musicPlayer.start();
            }
        });
    }


}
