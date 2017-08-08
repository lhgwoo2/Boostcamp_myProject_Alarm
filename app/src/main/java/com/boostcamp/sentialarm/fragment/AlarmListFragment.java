package com.boostcamp.sentialarm.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boostcamp.sentialarm.Alarm.AlarmDAO;
import com.boostcamp.sentialarm.Alarm.AlarmListAdapter;
import com.boostcamp.sentialarm.R;

/**
 * Created by 현기 on 2017-07-26.
 */

public class AlarmListFragment extends Fragment {

    private RecyclerView alarmlistRecyclerView;
    private LinearLayoutManager linearLayoutManager;

    private AlarmDAO alarmDAO;

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

        alarmDAO = new AlarmDAO();
        alarmDAO.creatAlarmRealm();

        AlarmListAdapter alarmListAdapter = new AlarmListAdapter(alarmDAO.getAllAlarm(),this.getContext());
        alarmlistRecyclerView.setAdapter(alarmListAdapter);


        return view;
    }

}
