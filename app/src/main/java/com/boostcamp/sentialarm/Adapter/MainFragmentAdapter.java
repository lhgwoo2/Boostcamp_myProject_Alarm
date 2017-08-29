package com.boostcamp.sentialarm.Adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.boostcamp.sentialarm.Fragment.AlarmListFragment;
import com.boostcamp.sentialarm.Fragment.EnrollFragment;
import com.boostcamp.sentialarm.Fragment.SongListFragment;

/**
 * Created by 현기 on 2017-07-26.
 */

public class MainFragmentAdapter extends FragmentStatePagerAdapter {


    final int FRAGMENT_COUNT = 3;

    public MainFragmentAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public int getItemPosition(Object object) {
        Fragment fragment = (Fragment)object;

        return super.getItemPosition(object);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0 :
                EnrollFragment enrollFragment = EnrollFragment.getEnrollFragmentIns();
                return enrollFragment;
            case 1 :
                AlarmListFragment alarmListFragment = AlarmListFragment.getAlarmListFragmentIns();
                return alarmListFragment;
            case 2 :
                SongListFragment songListFragment = SongListFragment.getSongListFragmentIns();
                return songListFragment;
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {


        return super.getPageTitle(position);
    }

    @Override
    public int getCount() {
        return FRAGMENT_COUNT;
    }
}
