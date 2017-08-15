package com.boostcamp.sentialarm.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boostcamp.sentialarm.AlarmSong.SongDAO;
import com.boostcamp.sentialarm.AlarmSong.SongDTO;
import com.boostcamp.sentialarm.R;
import com.boostcamp.sentialarm.Util.BitmapHelper;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.RealmResults;

/**
 * Created by 현기 on 2017-08-12.
 */

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.SongListViewHolder> {

    private RealmResults<SongDTO> songDTOs;
    private Context parentContext;
    private SongDAO songDAO;
    @Override
    public SongListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        parentContext = parent.getContext();
        return new SongListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.song_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(SongListViewHolder holder, int position) {

        final SongDTO currentSongDTO = songDTOs.get(position);

        holder.artistNameTextView.setText(currentSongDTO.getArtistName());
        holder.musicTitleTextView.setText(currentSongDTO.getMusicTitle());

        String sDate = changeDateToString(currentSongDTO.getPlayDate());
        holder.playDateTextView.setText(sDate);

        BitmapHelper bitmapHelper = new BitmapHelper();
        String coverImagePath = bitmapHelper.getImageResourcePath(currentSongDTO.getFileName(),parentContext);
        Glide.with(parentContext).load(coverImagePath).into(holder.songCoverImageView);

        holder.songShareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentSongDTO.getSongShareURL()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                parentContext.getApplicationContext().startActivity(intent);

            }
        });




    }
    private String changeDateToString(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 M월 dd일 hh시 mm분");
        return simpleDateFormat.format(date);
    }

    public void setSongDTOs(RealmResults<SongDTO> songDTOs) {
        this.songDTOs = songDTOs;
    }


    @Override
    public int getItemCount() {
        return songDTOs.size();
    }

    public void setSongDAO(SongDAO songDAO) {
        this.songDAO = songDAO;
    }

    public class SongListViewHolder extends RecyclerView.ViewHolder {

        public TextView artistNameTextView;
        public TextView musicTitleTextView;
        public TextView playDateTextView;
        public ImageView songCoverImageView;
        public ImageView songShareImageView;

        public SongListViewHolder(View itemView) {
            super(itemView);

            artistNameTextView = (TextView) itemView.findViewById(R.id.song_list_item_artistname_tv);
            musicTitleTextView = (TextView) itemView.findViewById(R.id.song_list_item_musictitle_tv);
            playDateTextView = (TextView) itemView.findViewById(R.id.song_list_item_playdate_tv);
            songCoverImageView = (ImageView) itemView.findViewById(R.id.song_list_item_songimage_iv);
            songShareImageView = (ImageView) itemView.findViewById(R.id.song_list_item_share_url_iv);
        }
    }
}
