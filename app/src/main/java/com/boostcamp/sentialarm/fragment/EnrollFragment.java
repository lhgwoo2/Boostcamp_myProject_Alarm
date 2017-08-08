package com.boostcamp.sentialarm.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boostcamp.sentialarm.Alarm.AlarmDAO;
import com.boostcamp.sentialarm.Alarm.AlarmDTO;
import com.boostcamp.sentialarm.Alarm.AlarmScheduler;
import com.boostcamp.sentialarm.MainActivity;
import com.boostcamp.sentialarm.R;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EnrollFragment extends Fragment {

    WheelView hourWheelView;
    WheelView minuteWheelView;

    TextView textViewtime;

    CheckBox checkMonEnroll;
    CheckBox checkTuesEnroll;
    CheckBox checkWednesEnroll;
    CheckBox checkThursEnroll;
    CheckBox checkFriEnroll;
    CheckBox checkSatEnroll;
    CheckBox checkSunEnroll;

    ImageButton enrollButton;

    View.OnTouchListener timeTouchListener;

    Animation ani;

    AlarmDAO alarmDAO;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.enroll_fragment, container, false);

        alarmDAO = new AlarmDAO();
        alarmDAO.creatAlarmRealm();

        initView(view);
        selectTimeAreaSetting(view);
        initTime();
        setEnrollButton();

        return view;
    }

    private void initView(View view) {
        textViewtime = (TextView) view.findViewById(R.id.tv_time);

        checkMonEnroll = (CheckBox) view.findViewById(R.id.check_mon_enroll);
        checkTuesEnroll = (CheckBox) view.findViewById(R.id.check_tues_enroll);
        checkWednesEnroll = (CheckBox) view.findViewById(R.id.check_wednes_enroll);
        checkThursEnroll = (CheckBox) view.findViewById(R.id.check_thurs_enroll);
        checkFriEnroll = (CheckBox) view.findViewById(R.id.check_fri_enroll);
        checkSatEnroll = (CheckBox) view.findViewById(R.id.check_sat_enroll);
        checkSunEnroll = (CheckBox) view.findViewById(R.id.check_sun_enroll);

        enrollButton = (ImageButton) view.findViewById(R.id.enroll_button);
    }


    private void setEnrollButton(){

        enrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ani = AnimationUtils.loadAnimation(getContext(), R.anim.button_animation);
                enrollButton.startAnimation(ani);

                ani.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}
                    //애니메이션이 끝나고 화면 이동
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Log.i("등록애니","애니작동");


                        String time = textViewtime.getText().toString();
                        String[] times = time.split(":");
                        // 알람 저장
                        AlarmDTO alarmDTO = alarmDAO.setEnrollAlarm(Integer.valueOf(times[0].trim()), Integer.valueOf(times[1].trim()),true,
                                checkMonEnroll.isChecked(),checkTuesEnroll.isChecked(),checkWednesEnroll.isChecked(),
                                checkThursEnroll.isChecked(),checkFriEnroll.isChecked(),checkSatEnroll.isChecked(),checkSunEnroll.isChecked());

                        // 알람 등록
                        AlarmScheduler.registerAlarm(getContext().getApplicationContext(),alarmDTO.getId(),alarmDTO.getAlarmHour(),alarmDTO.getAlarmMinute());


                        ((MainActivity)getActivity()).getViewPager().setCurrentItem(1);
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });
            }
        });
    }

    //현재시간 초기 설정
    private void initTime() {
        Date nowDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("HH : mm");
        String nowTime = formatter.format(nowDate);

        textViewtime.setText(" " + nowTime + " ");
    }


    //시간 영역 선택시 다이얼로그 띄우기
    private void selectTimeAreaSetting(View view) {

        LinearLayout timeSelectLinearLayout = (LinearLayout) view.findViewById(R.id.time_layout);

        timeTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        showDialog();
                        break;
                    default:
                        break;
                }
                return false;
            }
        };

        timeSelectLinearLayout.setOnTouchListener(timeTouchListener);
    }


    // 알람 시간 선택 다이얼로그
    private void showDialog() {
        LayoutInflater dialog = LayoutInflater.from(getContext());
        final View dialogLayout = dialog.inflate(R.layout.timepick_dialog, null);
        final Dialog myDialog = new Dialog(getContext());

        initWheel(dialogLayout);

        myDialog.setTitle(R.string.alarm_dialog_title);
        myDialog.setContentView(dialogLayout);
        myDialog.show();

        Button button_time_dialog_ok = (Button) dialogLayout.findViewById(R.id.button_time_dialog_ok);

        button_time_dialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTimeWheelview();
                myDialog.cancel();
            }
        });

    }

    // 다이얼로그 시간 확정하기
    private void getTimeWheelview() {
        String timeHourValue = hourWheelView.getSelectionItem().toString();
        String timeMinuteValue = minuteWheelView.getSelectionItem().toString();

        String timeString = timeHourValue +" : "+timeMinuteValue;

        textViewtime.setText(timeString);
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
        for (int i = 0; i < 60; i += 5) {
            if (i < 10) {
                list.add("0" + i);
            } else {
                list.add("" + i);
            }
        }
        return list;
    }

    @Override
    public void onDestroy() {
        //realm 해제
        alarmDAO.closeAlarmRealm();
        super.onDestroy();
    }
}
