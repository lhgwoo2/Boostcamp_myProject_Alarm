package com.boostcamp.sentialarm.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.TextView;

import com.boostcamp.sentialarm.DAO.AlarmDAO;
import com.boostcamp.sentialarm.DTO.AlarmDTO;
import com.boostcamp.sentialarm.Alarm.AlarmScheduler;
import com.boostcamp.sentialarm.Activity.MainActivity;
import com.boostcamp.sentialarm.Adapter.MainFragmentAdapter;
import com.boostcamp.sentialarm.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EnrollFragment extends Fragment implements WheelViewDialogFragment.WheelViewDialogListener {

    private TextView textViewtime;

    private CheckBox checkMonEnroll;
    private CheckBox checkTuesEnroll;
    private CheckBox checkWednesEnroll;
    private CheckBox checkThursEnroll;
    private CheckBox checkFriEnroll;
    private CheckBox checkSatEnroll;
    private CheckBox checkSunEnroll;

    private TextView enrollButton;

    private View.OnClickListener timeClickListener;

    private DialogFragment wheelDialog = null;

    private Animation ani;

    private AlarmDAO alarmDAO;

    private Vibrator vibrator;

    private static EnrollFragment enrollFragment;

    public static EnrollFragment getEnrollFragmentIns(){
        if(enrollFragment==null){
            enrollFragment = new EnrollFragment();
        }

        return enrollFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.enroll_fragment, container, false);

        vibrator = (Vibrator) view.getContext().getSystemService(Context.VIBRATOR_SERVICE);

        alarmDAO = new AlarmDAO();

        initView(view);
        initTime();
        selectTimeAreaSetting(view);
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

        enrollButton = (TextView) view.findViewById(R.id.enroll_button);



    }


    private void setEnrollButton() {

        enrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ani = AnimationUtils.loadAnimation(getContext(), R.anim.button_animation);
                enrollButton.startAnimation(ani);

                ani.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    //애니메이션이 끝나고 화면 이동
                    @Override
                    public void onAnimationEnd(Animation animation) {

                        String time = textViewtime.getText().toString();
                        String[] times = time.split(":");
                        // 알람 저장
                        alarmDAO.creatAlarmRealm();
                        AlarmDTO alarmDTO = alarmDAO.setEnrollAlarm(Integer.valueOf(times[0].trim()), Integer.valueOf(times[1].trim()), true,
                                checkMonEnroll.isChecked(), checkTuesEnroll.isChecked(), checkWednesEnroll.isChecked(),
                                checkThursEnroll.isChecked(), checkFriEnroll.isChecked(), checkSatEnroll.isChecked(), checkSunEnroll.isChecked());
                        alarmDAO.closeAlarmRealm();

                        // 리스트 데이터 변경
                       // ((AlarmListFragment) getFragmentManager().getFragments().get(2)).alarmListAdapter.notifyDataSetChanged();
                        AlarmListFragment f = (AlarmListFragment)((MainFragmentAdapter) ((MainActivity) getActivity()).getViewPager().getAdapter()).getItem(1);
                        //f.alarmListAdapter.notifyDataSetChanged();
                        f.refresh();
                        Log.i("알람등록", "디버깅, 등록화면 등록된 알람 id:" + alarmDTO.getId());

                        // 알람 등록
                        AlarmScheduler.registerAlarm(getContext().getApplicationContext(), alarmDTO.getId(), alarmDTO.getAlarmHour(), alarmDTO.getAlarmMinute());
                        ((MainActivity) getActivity()).getViewPager().setCurrentItem(1);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
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

    @Override
    public void onDialogPositiveClick(String sHour, String sMinute) {
        String timeString = sHour + " : " + sMinute;
        textViewtime.setText(timeString);
    }

    //시간 영역 선택시 다이얼로그 띄우기
    private void selectTimeAreaSetting(View view) {

        timeClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(100);
                showWheelDialog();
            }
        };

        textViewtime.setOnClickListener(timeClickListener);
    }


    public void showWheelDialog() {
        // Create an instance of the dialog fragment and show it

        wheelDialog = WheelViewDialogFragment.getWheelViewDialogFragmentIns();
        List<Fragment> fList = getActivity().getSupportFragmentManager().getFragments();
        if (!fList.contains(wheelDialog)) {
            wheelDialog.show(getActivity().getSupportFragmentManager(), "WheelViewDialogFragment");
        }

    }


    @Override
    public void onDestroy() {

        super.onDestroy();
    }
}
