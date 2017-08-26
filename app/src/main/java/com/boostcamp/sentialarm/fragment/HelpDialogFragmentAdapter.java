package com.boostcamp.sentialarm.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by 현기 on 2017-08-24.
 */

public class HelpDialogFragmentAdapter extends FragmentStatePagerAdapter {

    final int FRAGMENT_COUNT = 2;

    public HelpDialogFragmentAdapter(FragmentManager fm) {
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
                HelpDialogInfoFragment helpDialogInfoFragment = HelpDialogInfoFragment.getHelpDialogInfoFragmentIns();
                return helpDialogInfoFragment;
            case 1 :
                HelpDialogLicenseFragment helpDialogLicenseFragment = HelpDialogLicenseFragment.getHelpDialogLicenseFragmentIns();
                return helpDialogLicenseFragment;
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
