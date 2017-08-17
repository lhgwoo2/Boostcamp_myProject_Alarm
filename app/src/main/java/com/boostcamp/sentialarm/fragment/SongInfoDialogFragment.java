package com.boostcamp.sentialarm.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
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


    public static SongInfoDialogFragment getSongInfoDialogFragmentIns(){

        if(songInfoDialogFragment==null){
            songInfoDialogFragment = new SongInfoDialogFragment();
        }

        return songInfoDialogFragment;
    }

    public void setSongListDataInDialog(SongDTO songDTO) {
        this.songDTO = songDTO;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
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

        songBackgroundImageView = dialogView.findViewById(R.id.song_info_dialog_background_iv);

        songArtistNameTextView.setText(songDTO.getArtistName());
        songTitleTextView.setText(songDTO.getMusicTitle());

        String[] dateAndTime = divideDateAndTime(songDTO);
        songDateTextView.setText(dateAndTime[0]);
        songPlayTimeTextView.setText(dateAndTime[1]);
        songTempTextView.setText(songDTO.getWeatherInfoDTO().getTemperate());
        songAddressTextView.setText(songDTO.getWeatherInfoDTO().getAddress());
        songTempIconImageView.setImageResource(songDTO.getWeatherInfoDTO().getWeatherIconID());


        BitmapHelper bitmapHelper = new BitmapHelper();
        String coverImagePath = bitmapHelper.getImageResourcePath(songDTO.getFileName(),dialogView.getContext());
        String weatherImagePath = bitmapHelper.getImageResourcePath(songDTO.getWeatherInfoDTO().getWeatherFileName(),dialogView.getContext());
        Glide.with(dialogView).load(coverImagePath).into(songCoverImageView);
        Glide.with(dialogView).load(weatherImagePath).into(songWeatherBackImageView);
        Glide.with(dialogView).load(R.drawable.bg_song_dialog_background).into(songBackgroundImageView);

        builder.setView(dialogView);


        return builder.create();
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