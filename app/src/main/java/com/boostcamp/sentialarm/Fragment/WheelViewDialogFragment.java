package com.boostcamp.sentialarm.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.boostcamp.sentialarm.Activity.MainActivity;
import com.boostcamp.sentialarm.Adapter.MainFragmentAdapter;
import com.boostcamp.sentialarm.R;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by 현기 on 2017-08-09.
 */

public class WheelViewDialogFragment extends DialogFragment {

    public WheelViewDialogListener mListener;

    public WheelView hourWheelView;
    public WheelView minuteWheelView;
    public TextView timeOkTextView;

    private static WheelViewDialogFragment wheelViewDialogFragment = null;

    public interface WheelViewDialogListener {
        public void onDialogPositiveClick(String sHour, String sMinute);
    }


    public static DialogFragment getWheelViewDialogFragmentIns() {

        if (wheelViewDialogFragment == null) {
            wheelViewDialogFragment = new WheelViewDialogFragment();
        }
        return wheelViewDialogFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


        Log.i("다이얼로그 리스너 attach", (WheelViewDialogListener) ((MainFragmentAdapter) ((MainActivity) getActivity()).getViewPager().getAdapter()).getItem(0) + "");
        try {
            mListener = (WheelViewDialogListener) ((MainFragmentAdapter) ((MainActivity) getActivity()).getViewPager().getAdapter()).getItem(0);

        } catch (ClassCastException e) {
            // The Fragment doesn't implement the interface, throw exception
            throw new ClassCastException("Fragment"
                    + " must implement WheelViewDialogListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View dialogView = inflater.inflate(R.layout.timepick_dialog, null);
        initWheel(dialogView);
        timeOkTextView = (TextView) dialogView.findViewById(R.id.wheel_time_enroll_tv);
        timeOkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sHour = hourWheelView.getSelectionItem().toString();
                String sMinute = minuteWheelView.getSelectionItem().toString();
                mListener.onDialogPositiveClick(sHour, sMinute);
                getDialog().dismiss();
            }
        });


        return dialogView;
    }


    private void initWheel(View view) {

        hourWheelView = (WheelView) view.findViewById(R.id.hour_wheelview);
        hourWheelView.setWheelAdapter(new ArrayWheelAdapter(getContext()));
        hourWheelView.setSkin(WheelView.Skin.Holo);
        hourWheelView.setWheelData(createHours());
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.holoBorderColor = Color.parseColor("#FFFFFF");
        style.backgroundColor = Color.parseColor("#00000000");
        style.selectedTextColor = Color.parseColor("#FFFFFFFF");
        style.textAlpha = 0.3f;
        style.textColor = Color.LTGRAY;
        style.selectedTextSize = 20;
        hourWheelView.setStyle(style);
        hourWheelView.setExtraText("시", Color.parseColor("#FFFFFF"), 40, 70);


        minuteWheelView = (WheelView) view.findViewById(R.id.minute_wheelview);
        minuteWheelView.setWheelAdapter(new ArrayWheelAdapter(getContext()));
        minuteWheelView.setSkin(WheelView.Skin.Holo);
        minuteWheelView.setWheelData(createMinutes());
        minuteWheelView.setStyle(style);
        minuteWheelView.setExtraText("분", Color.parseColor("#FFFFFF"), 40, 70);

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
