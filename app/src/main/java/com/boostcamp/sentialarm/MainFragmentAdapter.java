package com.boostcamp.sentialarm;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.boostcamp.sentialarm.fragment.AlarmListFragment;
import com.boostcamp.sentialarm.fragment.EnrollFragment;
import com.boostcamp.sentialarm.fragment.SongListFragment;

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
                EnrollFragment enrollFragment = new EnrollFragment();
                return enrollFragment;
            case 1 :
                AlarmListFragment alarmListFragment = new AlarmListFragment();
                return alarmListFragment;
            case 2 :
                SongListFragment songListFragment = new SongListFragment();
                return songListFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return FRAGMENT_COUNT;
    }
}
