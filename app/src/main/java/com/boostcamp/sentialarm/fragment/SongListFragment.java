package com.boostcamp.sentialarm.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boostcamp.sentialarm.AlarmSong.SongDAO;
import com.boostcamp.sentialarm.AlarmSong.SongDTO;
import com.boostcamp.sentialarm.R;

import io.realm.RealmResults;

/**
 * Created by 현기 on 2017-07-26.
 */

public class SongListFragment extends Fragment {
    private RecyclerView songListRecyclerView;
    private LinearLayoutManager mLayoutManager;
    public SongListAdapter songListAdapter;

    private SongDAO songDAO;

    private static SongListFragment songListFragment;

    public static SongListFragment getSongListFragmentIns(){
        if(songListFragment==null){
            songListFragment=new SongListFragment();
        }
        return songListFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);


        View rootView = inflater.inflate(R.layout.songlist_fragment, container, false);

        songListRecyclerView = (RecyclerView) rootView.findViewById(R.id.song_list_recyclerView);
        songListRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        songListRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(this.getContext());
        songListRecyclerView.setLayoutManager(mLayoutManager);
        songListRecyclerView.setItemAnimator(new DefaultItemAnimator());

        songListAdapter = new SongListAdapter(getContext());

        songDAO = new SongDAO();
        songDAO.createSongRealm();
        RealmResults<SongDTO> songDTOs = songDAO.getSongListFindAll();

        songListAdapter.setSongDAO(songDAO);
        songListAdapter.setSongDTOs(songDTOs);
        songListRecyclerView.setAdapter(songListAdapter);

        return rootView;
    }




    @Override
    public void onDestroyView() {

        songDAO.closeSongRealm();
        super.onDestroyView();
    }
}
