package com.boostcamp.sentialarm.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.boostcamp.sentialarm.MainActivity;
import com.boostcamp.sentialarm.R;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by 현기 on 2017-08-09.
 */

public class WheelViewDialogFragment extends DialogFragment {


    public WheelView hourWheelView;
    public WheelView minuteWheelView;

    private static WheelViewDialogFragment wheelViewDialogFragment=null;

    public interface WheelViewDialogListener {
        public void onDialogPositiveClick(String sHour, String sMinute);
    }

    WheelViewDialogListener mListener;

    public static DialogFragment getWheelViewDialogFragmentIns(){

        if(wheelViewDialogFragment==null){
            wheelViewDialogFragment = new WheelViewDialogFragment();
        }
        return wheelViewDialogFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (WheelViewDialogListener) ((MainActivity)getActivity()).getSupportFragmentManager().getFragments().get(0);
        } catch (ClassCastException e) {
            // The Fragment doesn't implement the interface, throw exception
            throw new ClassCastException("Fragment"
                    + " must implement WheelViewDialogListener");
        }

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.timepick_dialog, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(dialogView)
                // Add action buttons
                .setPositiveButton(R.string.confrim_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                       String sHour = hourWheelView.getSelectionItem().toString();
                        String sMinute = minuteWheelView.getSelectionItem().toString();
                        dialog.dismiss();
                        mListener.onDialogPositiveClick(sHour, sMinute);
                    }
                });

        initWheel(dialogView);

        return builder.create();
    }




    private void initWheel(View view) {

        hourWheelView = (WheelView) view.findViewById(R.id.hour_wheelview);
        hourWheelView.setWheelAdapter(new ArrayWheelAdapter(getContext()));
        hourWheelView.setSkin(WheelView.Skin.Holo);
        hourWheelView.setWheelData(createHours());
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.selectedTextColor = Color.parseColor("#0288ce");
        style.textColor = Color.GRAY;
        style.selectedTextSize = 20;
        hourWheelView.setStyle(style);
        hourWheelView.setExtraText("시", Color.parseColor("#0288ce"), 40, 70);


        minuteWheelView = (WheelView) view.findViewById(R.id.minute_wheelview);
        minuteWheelView.setWheelAdapter(new ArrayWheelAdapter(getContext()));
        minuteWheelView.setSkin(WheelView.Skin.Holo);
        minuteWheelView.setWheelData(createMinutes());
        minuteWheelView.setStyle(style);
        minuteWheelView.setExtraText("분", Color.parseColor("#0288ce"), 40, 70);

        Calendar calendar = Calendar.getInstance();

        hourWheelView.setSelection(calendar.get(Calendar.HOUR_OF_DAY));
        minuteWheelView.setSelection(calendar.get(Calendar.MINUTE));

    }

    // 알람시간 선택 - 시간 휠뷰
    private ArrayList<String> createHours() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                list.add("0" + i);
            } else {
                list.add("" + i);
            }
        }
        return list;
    }

    // 알람시간 선택 - 분 휠뷰
    private ArrayList<String> createMinutes() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < 60; i += 1) {
            if (i < 10) {
                list.add("0" + i);
            } else {
                list.add("" + i);
            }
        }
        return list;
    }




}
