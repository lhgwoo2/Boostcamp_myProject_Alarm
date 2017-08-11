package com.boostcamp.sentialarm.API.MediaPlayer;

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

                Log.i("test", listCount+"");
                Log.i("test","파이어베이스 데이터 가져오기");

                // 파이어베이스에서 데이터를 가져올 동안 list 객체 동기화
                synchronized (list) {
                    for (DataSnapshot postSnapshot : dataSnapshot.child(tag).getChildren()) {

                        MusicInfoDTO musicInfoDTO = postSnapshot.getValue(MusicInfoDTO.class);
                        Log.i("파이어베이스데이터", musicInfoDTO.getName());
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

    public List<MusicInfoDTO> getMusicInfoList(){
        return list;
    }





}
