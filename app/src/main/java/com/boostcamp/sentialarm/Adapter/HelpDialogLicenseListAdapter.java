package com.boostcamp.sentialarm.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boostcamp.sentialarm.R;

import java.util.ArrayList;

/**
 * Created by 현기 on 2017-08-24.
 */

public class HelpDialogLicenseListAdapter extends RecyclerView.Adapter<HelpDialogLicenseListAdapter.HelpLisenseListViewHolder> {

    private ArrayList<String> licenseNames;
    private ArrayList<String> licenseContents;

    @Override
    public HelpLisenseListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HelpLisenseListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.licenselist_item, parent, false));
    }

    @Override
    public void onBindViewHolder(HelpLisenseListViewHolder holder, int position) {

        String[] contents = spiltContents(licenseContents.get(position));

        holder.lisenseNameTextView.setText(licenseNames.get(position));
        holder.lisenseContentsWebTextView.setText(contents[0]);
        holder.lisenseContentsRightTextView.setText(contents[1]);
        holder.lisenseContentsAphachTextView.setText(contents[2]);

    }

    private String[] spiltContents(String str){

        return str.split("\n");
    }

    @Override
    public int getItemCount() {
        return licenseNames.size();
    }

    public void setLisenseList(ArrayList<String> names, ArrayList<String> contents) {
        licenseNames = names;
        licenseContents = contents;

        notifyDataSetChanged();
    }

    public class HelpLisenseListViewHolder extends RecyclerView.ViewHolder {

        public TextView lisenseNameTextView;
        public TextView lisenseContentsWebTextView;
        public TextView lisenseContentsRightTextView;
        public TextView lisenseContentsAphachTextView;

        public HelpLisenseListViewHolder(View itemView) {
            super(itemView);

            lisenseNameTextView = (TextView) itemView.findViewById(R.id.help_dialog_lisense_name_tv);
            lisenseContentsWebTextView = (TextView) itemView.findViewById(R.id.help_dialog_lisense_contents_1_tv);
            lisenseContentsRightTextView = (TextView) itemView.findViewById(R.id.help_dialog_lisense_contents_2_tv);
            lisenseContentsAphachTextView = (TextView) itemView.findViewById(R.id.help_dialog_lisense_contents_3_tv);
        }
    }
}


