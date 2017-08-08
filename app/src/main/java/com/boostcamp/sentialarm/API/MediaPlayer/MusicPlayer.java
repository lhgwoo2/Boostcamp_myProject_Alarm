package com.boostcamp.sentialarm.API.MediaPlayer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import com.boostcamp.sentialarm.API.Jamendo.DTO.MusicDTO;
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

public class MusicPlayer extends MediaPlayer {

    private static MusicPlayer musicPlayer = null;
    private static int musicCount = 0;
    private List<MusicInfoDTO> playList;

    private MusicPlayer() {
    }

    public static MusicPlayer getMusicPlayerIns() {
        if (musicPlayer == null) {
            musicPlayer = new MusicPlayer();
            return musicPlayer;
        } else {
            return musicPlayer;
        }
    }

    public void setMusicInfoList(List<MusicInfoDTO> playList) {
        this.playList = playList;
    }


    public void MusicProcess(int musicCount) {
        //jamendoAPI url 매칭
        String jamendoURL = musicPlayer.addURL(musicCount);
        loadMusicURL(jamendoURL);
    }

    //가져온 데이터를 통하여
    public String addURL(int position) {
        Log.i("음원리스트위치", String.valueOf(position));
        MusicInfoDTO dto = playList.get(position);
        String urlString = "https://api.jamendo.com/v3.0/tracks/?client_id=8ed69917&limit=1";
        String addURl = String.format(urlString + "&name=%s&artist_name=%s&type=%s", dto.getName(), dto.getArtist_name(), dto.getType());

        return addURl;
    }


    // API에서 오디오 URL을 얻어온다.
    public String getMusicUrlInAPI(String apiURL) {

        Gson gson = new Gson();
        Log.i("음악 url", apiURL);

        String audioUrlString = null;

        try {
            JsonReader jsonReader = new JsonReader(new InputStreamReader(new URL(apiURL).openStream()));
            Type listType = new TypeToken<MusicDTO>() {
            }.getType();
            MusicDTO musicDTO = gson.fromJson(jsonReader, listType);

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
            musicPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            musicPlayer.setDataSource(mediaURL);
            musicPlayer.prepareAsync(); // might take long! (for buffering, etc)

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 뮤직플레이어 실행
    public void startAsyncMusicPlayer() {
        musicPlayer.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // TODO media player를 실행
                musicPlayer.start();
            }
        });
    }

    // 비동기로 실행된 결과를 받아 처리하는 코드
    private AsyncCallback<String> callback = new AsyncCallback<String>() {
        @Override
        public void onResult(String mediaURL) {

            Log.i("콜백 음원URL", mediaURL);

            musicPlayer.setAsyncMediaPlayer(mediaURL);    // 뮤직플레이어 준비
            musicPlayer.startAsyncMusicPlayer();        // 뮤직플레이어실행

            musicPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    if (musicCount + 1 < playList.size()) {

                        //다음트랙 재생
                        mediaPlayer.reset();
                        MusicProcess(++musicCount);
                    } else {
                        mediaPlayer.reset();
                        musicCount = 0;
                        MusicProcess(musicCount);
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


}
