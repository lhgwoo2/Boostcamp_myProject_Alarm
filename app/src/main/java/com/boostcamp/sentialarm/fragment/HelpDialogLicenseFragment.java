package com.boostcamp.sentialarm.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boostcamp.sentialarm.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by 현기 on 2017-08-24.
 */

public class HelpDialogLicenseFragment extends Fragment {

    public static HelpDialogLicenseFragment helpDialogLisenseFragment=null;

    private RecyclerView lisenseRecyclerView;
    private HelpDialogLicenseListAdapter lisenseAdapter;

    private ArrayList<String> licenseNames;
    private ArrayList<String> licenseContents;

    public static HelpDialogLicenseFragment getHelpDialogLicenseFragmentIns(){
        if(helpDialogLisenseFragment == null){
            helpDialogLisenseFragment = new HelpDialogLicenseFragment();
        }

        return helpDialogLisenseFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.help_dialog_lisense_fragment, container, false);
        lisenseRecyclerView = (RecyclerView) rootView.findViewById(R.id.help_dialog_lisense_recyclerview);
        lisenseAdapter = new HelpDialogLicenseListAdapter();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lisenseRecyclerView.setLayoutManager(layoutManager);
        setList();
        lisenseAdapter.setLisenseList(licenseNames, licenseContents);
        lisenseRecyclerView.setAdapter(lisenseAdapter);


        return rootView;
    }

    private void setList(){
        licenseContents = new ArrayList<>();
        licenseNames = new ArrayList<>();
        Collections.addAll(licenseNames, getResources().getStringArray(R.array.lisensename));
        Collections.addAll(licenseContents, getResources().getStringArray(R.array.lisensecontents));
    }
}
