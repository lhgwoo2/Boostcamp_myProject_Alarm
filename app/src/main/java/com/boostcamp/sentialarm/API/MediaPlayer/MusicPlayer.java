package com.boostcamp.sentialarm.API.MediaPlayer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.boostcamp.sentialarm.API.Jamendo.DTO.MusicDTO;
import com.boostcamp.sentialarm.Alarm.AlarmPopActivity;
import com.boostcamp.sentialarm.Util.BaseAsyncTask.AsyncCallback;
import com.boostcamp.sentialarm.Util.BaseAsyncTask.AsyncExecutor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by 현기 on 2017-08-06.
 */

public class MusicPlayer {

    private MediaPlayer mediaPlayer;
    private List<MusicInfoDTO> playList;
    private int musicCount = 0;
    private Gson gson;

    public Handler mHandler=null;

    public MusicPlayer() {
        gson = new Gson();
    }

    public void createMediaPlayer() {
        mediaPlayer = new MediaPlayer();
    }

    public void setHandler(Handler mHandler){
        Log.i("tests","핸들러 뮤직플레이어로 전달");
        this.mHandler = mHandler;
    }

    // 미디어 플레이어 내부 리스트
    public void setMusicInfoList(List<MusicInfoDTO> playList) {
        this.playList = playList;
    }

    // 음원 재생 프로세스
    public void musicProcess() {

        String jamendoURL = addURL();
        loadMusicURL(jamendoURL);
    }

    //
    public String addURL() {
        Log.i("음원리스트위치", String.valueOf(musicCount));
        MusicInfoDTO dto = playList.get(musicCount);
        String urlString = "https://api.jamendo.com/v3.0/tracks/?client_id=8ed69917&limit=1";
        String addURl = String.format(urlString + "&name=%s&artist_name=%s&type=%s", dto.getName(), dto.getArtist_name(), dto.getType());

        //다음 음원을 위해 음원위치리스트 이동
        musicCount++;

        return addURl;
    }


    // API에서 오디오 URL을 얻어온다.
    public String getMusicUrlInAPI(String apiURL) {

        Log.i("음악 url", apiURL);
        String audioUrlString = null;

        try {
            JsonReader jsonReader = new JsonReader(new InputStreamReader(new URL(apiURL).openStream()));
            Type listType = new TypeToken<MusicDTO>() {
            }.getType();
            MusicDTO musicDTO = gson.fromJson(jsonReader, listType);

            Message msg;
            msg = mHandler.obtainMessage(AlarmPopActivity.HANDLER_MESSAGE);
            msg.obj = musicDTO;
            mHandler.sendMessage(msg);

            Log.i("tests","핸들러 메시지 전달, 뮤직플레이어에서 -> 액티비티");

            audioUrlString = musicDTO.getResults().get(0).getAudio();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return audioUrlString;
    }


    private void loadMusicURL(final String url) {
        // 비동기로 실행될 코드
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                return getMusicUrlInAPI(url);
            }
        };

        new AsyncExecutor<String>()
                .setCallable(callable)
                .setCallback(callback)
                .execute();

    }

    // 뮤직 플레이어 준비
    public void setAsyncMediaPlayer(String mediaURL) {

        Log.i("콜백 음원URL", mediaURL);
        try {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(mediaURL);
            mediaPlayer.prepareAsync(); // might take long! (for buffering, etc)

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 뮤직플레이어 실행
    public void startAsyncMusicPlayer() {
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                // TODO media player를 실행
                mediaPlayer.start();
            }
        });
    }

    // 비동기로 실행된 결과를 받아 처리하는 코드
    private AsyncCallback<String> callback = new AsyncCallback<String>() {
        @Override
        public void onResult(String mediaURL) {

            Log.i("콜백 음원URL", mediaURL);

            setAsyncMediaPlayer(mediaURL);    // 뮤직플레이어 준비
            startAsyncMusicPlayer();        // 뮤직플레이어실행

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    if (musicCount + 1 <= playList.size()) {

                        //다음트랙 재생
                        mediaPlayer.reset();
                        musicProcess();
                    } else {
                        mediaPlayer.reset();
                        musicCount = 0;
                        musicProcess();
                    }
                }
            });

        }

        @Override
        public void exceptionOccured(Exception e) {
        }

        @Override
        public void cancelled() {
        }
    };

    public void stopMediaPlayer() {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer=null;
    }


}
