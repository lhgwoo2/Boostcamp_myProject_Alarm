package com.boostcamp.sentialarm.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.boostcamp.sentialarm.AlarmSong.SongDTO;
import com.boostcamp.sentialarm.R;
import com.boostcamp.sentialarm.Util.BitmapHelper;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 현기 on 2017-08-16.
 */

public class SongInfoDialogFragment extends DialogFragment {

    private static SongInfoDialogFragment songInfoDialogFragment;

    private SongDTO songDTO;

    private TextView songArtistNameTextView;
    private TextView songTitleTextView;

    private TextView songDateTextView;
    private TextView songPlayTimeTextView;
    private TextView songTempTextView;
    private TextView songAddressTextView;
    private ImageView songTempIconImageView;
    private ImageView songWeatherBackImageView;
    private ImageView songCoverImageView;

    private ImageView songBackgroundImageView;

    private ImageView songPlayButtonImageView;
    private ImageView songShareButtonImageView;



    public static SongInfoDialogFragment getSongInfoDialogFragmentIns(){

        if(songInfoDialogFragment==null){
            songInfoDialogFragment = new SongInfoDialogFragment();
        }

        return songInfoDialogFragment;
    }

    public void setSongListDataInDialog(SongDTO songDTO) {
        this.songDTO = songDTO;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View dialogView = inflater.inflate(R.layout.song_info_dialog_fragment, null);

        songArtistNameTextView = dialogView.findViewById(R.id.song_info_dialog_song_artist_name_tv);
        songTitleTextView = dialogView.findViewById(R.id.song_info_dialog_song_title_tv);

        songDateTextView = dialogView.findViewById(R.id.song_info_dialog_play_date_tv);
        songPlayTimeTextView = dialogView.findViewById(R.id.song_info_dialog_play_time_tv);
        songTempTextView = dialogView.findViewById(R.id.song_info_dialog_temperate_tv);
        songAddressTextView = dialogView.findViewById(R.id.song_info_dialog_address_tv);
        songTempIconImageView = dialogView.findViewById(R.id.song_info_dialog_temp_icon_iv);
        songWeatherBackImageView = dialogView.findViewById(R.id.song_info_dialog_weather_background_iv);
        songCoverImageView = dialogView.findViewById(R.id.song_info_dialog_song_cover_iv);

        // songBackgroundImageView = dialogView.findViewById(R.id.song_info_dialog_background_iv);


        songPlayButtonImageView = dialogView.findViewById(R.id.song_info_dialog_play_button_iv);
        songShareButtonImageView = dialogView.findViewById(R.id.song_info_dialog_share_button_iv);

        songPlayButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(songDTO.isLocalSong()){
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setDataAndType(Uri.parse(songDTO.getSongShareURL()), "audio/mp3");
                    view.getContext().startActivity(i);
                }else{
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(songDTO.getSongShareURL()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().getApplicationContext().startActivity(intent);
                }
            }
        });


        songArtistNameTextView.setText(songDTO.getArtistName());
        songTitleTextView.setText(songDTO.getMusicTitle());

        String[] dateAndTime = divideDateAndTime(songDTO);
        songDateTextView.setText(dateAndTime[0]);
        songPlayTimeTextView.setText(dateAndTime[1]);


        if(!songDTO.isLocalSong()) {
            songTempTextView.setText(songDTO.getWeatherInfoDTO().getTemperate());
            songAddressTextView.setText(songDTO.getWeatherInfoDTO().getAddress());
            songTempIconImageView.setImageResource(songDTO.getWeatherInfoDTO().getWeatherIconID());


            BitmapHelper bitmapHelper = new BitmapHelper();
            String coverImagePath = bitmapHelper.getImageResourcePath(songDTO.getFileName(), dialogView.getContext());
            String weatherImagePath = bitmapHelper.getImageResourcePath(songDTO.getWeatherInfoDTO().getWeatherFileName(), dialogView.getContext());
            Glide.with(dialogView).load(coverImagePath).into(songCoverImageView);
            Glide.with(dialogView).load(weatherImagePath).into(songWeatherBackImageView);
        }else{
            Glide.with(dialogView).load(R.drawable.ic_default_record).into(songCoverImageView);
            Glide.with(dialogView).load(R.drawable.bg_default_localbackground).into(songWeatherBackImageView);
        }
        //Glide.with(dialogView).load(R.drawable.bg_song_dialog_background2).into(songBackgroundImageView);

        return dialogView;

    }

    private String[] divideDateAndTime(SongDTO songDTO){
        Date current = songDTO.getPlayDate();
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy. MM. dd E");
        SimpleDateFormat formatTime = new SimpleDateFormat("a hh:mm");
        String times[] = new String[2];
        times[0] = formatDate.format(current);
        times[1] = formatTime.format(current);

        return times;
    }

}