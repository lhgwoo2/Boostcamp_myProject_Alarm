package com.boostcamp.sentialarm.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boostcamp.sentialarm.Adapter.AlarmListAdapters;
import com.boostcamp.sentialarm.DAO.AlarmDAO;
import com.boostcamp.sentialarm.DTO.AlarmDTO;
import com.boostcamp.sentialarm.R;

import io.realm.RealmResults;

/**
 * Created by 현기 on 2017-07-26.
 */

public class AlarmListFragment extends Fragment {

    private TextView alarmlistEmptyTextView;
    private RecyclerView alarmlistRecyclerView;
    private LinearLayoutManager linearLayoutManager;

    public AlarmListAdapters alarmListAdapter;
    private AlarmDAO alarmDAO;

    private static AlarmListFragment alarmListFragment;

    public static AlarmListFragment getAlarmListFragmentIns() {
        if (alarmListFragment == null) {
            alarmListFragment = new AlarmListFragment();
        }

        return alarmListFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.alarmlist_fragment, container, false);
        alarmlistRecyclerView = (RecyclerView) view.findViewById(R.id.alarm_list_recycler);
        alarmlistEmptyTextView = (TextView) view.findViewById(R.id.alarm_list_empty_tv);

        alarmDAO = new AlarmDAO();
        alarmDAO.creatAlarmRealm();
        if (alarmDAO.isEmptyAlarmList()) {
            alarmlistRecyclerView.setVisibility(View.GONE);
            alarmlistEmptyTextView.setVisibility(View.VISIBLE);
        } else {
            alarmlistRecyclerView.setVisibility(View.VISIBLE);
            alarmlistEmptyTextView.setVisibility(View.GONE);

            alarmlistRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

            linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

            // 어댑터의 내용이 리사이클러뷰의 크기의 영향을 끼치면 false 영향이 없으면 true, - true 일때 리사이클러뷰 최적화가 가능.
            alarmlistRecyclerView.setHasFixedSize(false);
            alarmlistRecyclerView.setLayoutManager(linearLayoutManager);
            alarmlistRecyclerView.setItemAnimator(new DefaultItemAnimator());


            RealmResults<AlarmDTO> alarmDTOs = alarmDAO.getAllAlarm();


            alarmListAdapter = new AlarmListAdapters(getContext().getApplicationContext());
            alarmListAdapter.setList(alarmDTOs);
            alarmListAdapter.setAlarmDAO(alarmDAO);
            alarmlistRecyclerView.setAdapter(alarmListAdapter);

        }

        return view;
    }
    public void refresh(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }


    @Override
    public void onDestroyView() {
        alarmDAO.closeAlarmRealm();
        super.onDestroyView();
    }
}
