package com.boostcamp.sentialarm.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boostcamp.sentialarm.AlarmSong.SongDAO;
import com.boostcamp.sentialarm.AlarmSong.SongDTO;
import com.boostcamp.sentialarm.R;
import com.boostcamp.sentialarm.Util.BitmapHelper;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.realm.RealmResults;

/**
 * Created by 현기 on 2017-08-12.
 */

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.SongListViewHolder> {

    private RealmResults<SongDTO> songDTOs;
    private Context parentContext;
    private SongDAO songDAO;
    private Vibrator vibrator;

    public SongListAdapter(Context context) {
        super();
        vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
    }

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

    }
    private String changeDateToString(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 M월 dd일 HH시 mm분 E");
        return simpleDateFormat.format(date);
    }

    public void setSongDTOs(RealmResults<SongDTO> songDTOs) {
        this.songDTOs = songDTOs;
    }

    public void showWheelDialog(View view, int position) {
        // Create an instance of the dialog fragment and show it
        SongInfoDialogFragment dialog = SongInfoDialogFragment.getSongInfoDialogFragmentIns();

        dialog.setSongListDataInDialog(songDTOs.get(position));

        List<Fragment> fList = ((FragmentActivity)view.getContext()).getSupportFragmentManager().getFragments();
        if(!fList.contains(dialog)){
            Log.i("dialgo 추가", "다이얼로그 추가");
            dialog.show(((FragmentActivity)view.getContext()).getSupportFragmentManager(), "SongInfoDialogFragment");
        }


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
        public RelativeLayout songListItemLayout;

        public SongListViewHolder(View itemView) {
            super(itemView);

            artistNameTextView = (TextView) itemView.findViewById(R.id.song_list_item_artistname_tv);
            musicTitleTextView = (TextView) itemView.findViewById(R.id.song_list_item_musictitle_tv);
            playDateTextView = (TextView) itemView.findViewById(R.id.song_list_item_playdate_tv);
            songCoverImageView = (ImageView) itemView.findViewById(R.id.song_list_item_songimage_iv);
            songShareImageView = (ImageView) itemView.findViewById(R.id.song_list_item_share_url_iv);

            songListItemLayout = (RelativeLayout) itemView.findViewById(R.id.song_list_item_layout);

            songListItemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getLayoutPosition();

                    showWheelDialog(view, position);

                }
            });

            songShareImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final SongDTO songDTO = songDTOs.get(getLayoutPosition());

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(songDTO.getSongShareURL()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    parentContext.getApplicationContext().startActivity(intent);

                }
            });

            songListItemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showWheelDialog(view, getLayoutPosition());
                }
            });


            songListItemLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View view) {
                    vibrator.vibrate(100);

                    final SongDTO songDTO = songDTOs.get(getLayoutPosition());

                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setMessage("지우실래요?");
                    builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            songDAO.deleteSongData(songDTO.getId());
                            notifyDataSetChanged();
                            Toast.makeText(view.getContext().getApplicationContext(), "노래 기록을 지웠습니다^^", Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(view.getContext().getApplicationContext(), "취소 됬어요~", Toast.LENGTH_LONG).show();
                        }
                    });

                    builder.show();


                    return true;
                }
            });
        }
    }
}
