package com.boostcamp.sentialarm.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boostcamp.sentialarm.Adapter.AlarmListAdapter;
import com.boostcamp.sentialarm.DTO.AlarmDTO;
import com.boostcamp.sentialarm.MainActivity;
import com.boostcamp.sentialarm.R;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by 현기 on 2017-07-26.
 */

public class AlarmListFragment extends Fragment {

    private RecyclerView alarmlistRecyclerView;
    private LinearLayoutManager linearLayoutManager;

    private Realm realm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.alarmlist_fragment, container, false);
        alarmlistRecyclerView = (RecyclerView) view.findViewById(R.id.alarm_list_recycler);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        alarmlistRecyclerView.setHasFixedSize(true);
        alarmlistRecyclerView.setLayoutManager(linearLayoutManager);

        realm = ((MainActivity)getActivity()).realm;

        AlarmListAdapter alarmListAdapter = new AlarmListAdapter(setData(),this.getContext());
        alarmlistRecyclerView.setAdapter(alarmListAdapter);


        return view;
    }

    private RealmResults<AlarmDTO> setData(){
        RealmResults<AlarmDTO> alarms = realm.where(AlarmDTO.class)
                .findAllSorted("id");


        return alarms;
    }

}
