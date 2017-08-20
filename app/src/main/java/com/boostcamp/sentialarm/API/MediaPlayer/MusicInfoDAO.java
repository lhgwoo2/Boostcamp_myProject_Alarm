package com.boostcamp.sentialarm.API.MediaPlayer;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 현기 on 2017-08-09.
 */

public class MusicInfoDAO {

    public FirebaseDatabase database;
    public DatabaseReference myRef;
    public List<MusicInfoDTO> list = null;
    public List<MusicLocalDTO> localList = null;
    public long listCount = 0;


    public void initAlarmFirebase(){
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("SentiAlarm");
    }


    public void getSongListInFirebase(final String tag){

       list = new ArrayList<>();

        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                listCount = dataSnapshot.child(tag).getChildrenCount();

                // 파이어베이스에서 데이터를 가져올 동안 list 객체 동기화
                synchronized (list) {
                    for (DataSnapshot postSnapshot : dataSnapshot.child(tag).getChildren()) {

                        MusicInfoDTO musicInfoDTO = postSnapshot.getValue(MusicInfoDTO.class);
                        list.add(musicInfoDTO);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.w("Firebase error", "Failed to read value.", databaseError.toException());
            }
        });

    }
    public void getSongListInMyPhone(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        ArrayList<MusicLocalDTO> localList = new ArrayList<>();
        if (cursor == null) {
            // 쿼리 실패, 에러 처리
        } else if (!cursor.moveToFirst()) { // 미디어가 장치에 없음
        } else {
            int titleColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
            int durationColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int artistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);


            MusicLocalDTO musicLocalDTO=null;

            synchronized (localList) {
                do {
                    long thisId = cursor.getLong(idColumn);
                    String thisTitle = cursor.getString(titleColumn);
                    int thisDuration = cursor.getInt(durationColumn);
                    double seconds = thisDuration / 1000.0;
                    String thisArtist = cursor.getString(artistColumn);
                    // 목록을 처리
                    musicLocalDTO = new MusicLocalDTO();
                    musicLocalDTO.setArtist(thisArtist);
                    musicLocalDTO.setDuration((int)seconds);
                    musicLocalDTO.setId(thisId);
                    musicLocalDTO.setTitle(thisTitle);

                    localList.add(musicLocalDTO);

                } while (cursor.moveToNext());
            }
        }

        this.localList = localList;
    }



    public List<MusicInfoDTO> getMusicInfoList(){
        return list;
    }

    public List<MusicLocalDTO> getMusicLocalDTOList(){
        return localList;
    }




}
