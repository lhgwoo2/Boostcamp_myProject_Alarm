package com.boostcamp.sentialarm.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boostcamp.sentialarm.Adapter.AlarmListAdapter;
import com.boostcamp.sentialarm.MainActivity;
import com.boostcamp.sentialarm.R;
import com.boostcamp.sentialarm.VO.AlarmVO;

import java.util.ArrayList;
import java.util.List;

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
        //alarmListAdapter.setAlarmListData();
        alarmlistRecyclerView.setAdapter(alarmListAdapter);


        return view;
    }

    private RealmResults<AlarmVO> setData(){
        RealmResults<AlarmVO> alarms = realm.where(AlarmVO.class)
                .findAll();

        Log.i("알람데이터","디비에서가져온 알람시간 : "+ alarms.get(0).getAlarmtime());
        return alarms;
    }

    public List<AlarmVO> testSetVO(){
        List<AlarmVO> list = new ArrayList<>();

        AlarmVO vo = new AlarmVO();
        vo.setAlarmOnOff(true);
        vo.setAlarmtime("오전 12시 12분");
        vo.setFriday(false);
        vo.setMonday(false);
        vo.setSaturday(true);
        vo.setSunday(true);
        vo.setThursday(false);
        vo.setTuesday(true);
        vo.setWednesday(true);

        list.add(vo);


        vo = new AlarmVO();
        vo.setAlarmOnOff(true);
        vo.setAlarmtime("오후 1시 30분");
        vo.setFriday(false);
        vo.setMonday(true);
        vo.setSaturday(true);
        vo.setSunday(true);
        vo.setThursday(false);
        vo.setTuesday(true);
        vo.setWednesday(true);

        list.add(vo);



        return list;
    }








}
