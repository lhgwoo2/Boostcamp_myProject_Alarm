package com.boostcamp.sentialarm.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boostcamp.sentialarm.R;

/**
 * Created by 현기 on 2017-08-24.
 */

public class HelpDialogInfoFragment extends Fragment {

    public static HelpDialogInfoFragment helpDialogInfoFragment=null;

    private TextView infoTextview;


    public static HelpDialogInfoFragment getHelpDialogInfoFragmentIns(){
        if(helpDialogInfoFragment == null){
            helpDialogInfoFragment = new HelpDialogInfoFragment();
        }

        return helpDialogInfoFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.help_dialog_info_fragment, container, false);
        infoTextview = (TextView) rootView.findViewById(R.id.help_dialog_info_textview);


        return rootView;
    }
}
