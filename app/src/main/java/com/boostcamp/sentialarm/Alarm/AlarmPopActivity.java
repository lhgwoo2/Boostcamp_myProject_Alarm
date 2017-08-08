package com.boostcamp.sentialarm.Alarm;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.boostcamp.sentialarm.API.MediaPlayer.MusicInfoDTO;
import com.boostcamp.sentialarm.API.Jamendo.DTO.MusicDTO;
import com.boostcamp.sentialarm.API.MediaPlayer.MusicPlayer;
import com.boostcamp.sentialarm.R;
import com.boostcamp.sentialarm.Util.BaseAsyncTask.AsyncCallback;
import com.boostcamp.sentialarm.Util.BaseAsyncTask.AsyncExecutor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

public class AlarmPopActivity extends AppCompatActivity {

    public Gson gson = new Gson();
    private MusicPlayer musicPlayer;
    private int musicCount = 0;
    private List<MusicInfoDTO> playList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_pop);

        ImageView iv = (ImageView) findViewById(R.id.iv_music_circle);

        Animation anim = AnimationUtils.loadAnimation(
                getApplicationContext(), // 현재 화면의 제어권자
                R.anim.rotate_anim);    // 설정한 에니메이션 파일
        iv.startAnimation(anim);


        // MediaPlayer 객체 생성 - 싱글톤
        musicPlayer = (MusicPlayer) MusicPlayer.getMusicPlayerIns();
        playList = testDATA();
        Collections.shuffle(playList);

        MusicProcess(musicCount);
    }

    public void MusicProcess(int musicCount) {

        //jamendoAPI url 매칭
        String jamendoURL = musicPlayer.addURL(musicCount);
        loadMusicURL(jamendoURL);
    }

    public String addURL(int position) {

        Log.i("음원리스트위치", String.valueOf(position));

        MusicInfoDTO dto = playList.get(position);
        String urlString = "https://api.jamendo.com/v3.0/tracks/?client_id=8ed69917&limit=1";
        String addURl = String.format(urlString + "&name=%s&artist_name=%s&type=%s", dto.getName(), dto.getArtist_name(), dto.getType());

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

    // 비동기로 실행된 결과를 받아 처리하는 코드
    private AsyncCallback<String> callback = new AsyncCallback<String>() {
        @Override
        public void onResult(String mediaURL) {

            Log.i("콜백 음원URL", mediaURL);

            musicPlayer.setAsyncMediaPlayer(mediaURL);    // 뮤직플레이어 셋팅
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


    @Override
    protected void onDestroy() {
        // 미디어플레이어 해제
        // mediaPlayer.release();
        super.onDestroy();
    }

    public List<MusicInfoDTO> testDATA() {
        List<MusicInfoDTO> list = new ArrayList<>();
        MusicInfoDTO info = new MusicInfoDTO();
        info.setName("good+old+times");
        info.setArtist_name("alex+cohen");
        info.setType("single");
        list.add(info);

        info = new MusicInfoDTO();
        info.setName("new+life");
        info.setArtist_name("explosive+ear+candy");
        info.setType("single");
        list.add(info);

        info = new MusicInfoDTO();
        info.setName("me+canse+version+urbana");
        info.setArtist_name("harveys");
        info.setType("single");
        list.add(info);

        info = new MusicInfoDTO();
        info.setName("usted+se+me+llevo+la+vida+feat+blow+rasta");
        info.setArtist_name("harveys");
        info.setType("albumtrack");
        list.add(info);

        info = new MusicInfoDTO();
        info.setName("the+promise");
        info.setArtist_name("wordsmith");
        info.setType("single");
        list.add(info);

        info = new MusicInfoDTO();
        info.setName("boom+batida");
        info.setArtist_name("boom+boom+beckett");
        info.setType("albumtrack");
        list.add(info);

        return list;
    }
}
