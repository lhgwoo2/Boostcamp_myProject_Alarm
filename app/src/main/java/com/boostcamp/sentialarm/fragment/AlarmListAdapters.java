package com.boostcamp.sentialarm.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boostcamp.sentialarm.Alarm.AlarmDAO;
import com.boostcamp.sentialarm.Alarm.AlarmDTO;
import com.boostcamp.sentialarm.Alarm.AlarmScheduler;
import com.boostcamp.sentialarm.R;
import com.github.lguipeng.library.animcheckbox.AnimCheckBox;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by 현기 on 2017-08-11.
 */

public class AlarmListAdapters extends RecyclerView.Adapter<AlarmListAdapters.ViewHolder> {

    private RealmResults<AlarmDTO> list;
    private Context context;
    private AlarmDAO alarmDAO;
    private Vibrator vibrator;

    public AlarmListAdapters(Context context) {
        super();
        this.context = context;
        vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.alarmlist_item, parent, false));


    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.alarmListView.setOnLongClickListener(null);

        holder.monCheck.setOnCheckedChangeListener(null);
        holder.tuesCheck.setOnCheckedChangeListener(null);
        holder.wednesCheck.setOnCheckedChangeListener(null);
        holder.thurCheck.setOnCheckedChangeListener(null);
        holder.friCheck.setOnCheckedChangeListener(null);
        holder.satCheck.setOnCheckedChangeListener(null);
        holder.sunCheck.setOnCheckedChangeListener(null);

        holder.onOffCheck.setOnCheckedChangeListener(null);

        AlarmDTO alarmDTO = list.get(position);



        holder.onOffCheck.setChecked(alarmDTO.isAlarmOnOff());

        holder.monCheck.setChecked(alarmDTO.isMonday());
        holder.tuesCheck.setChecked(alarmDTO.isTuesday());
        holder.wednesCheck.setChecked(alarmDTO.isWednesday());
        holder.thurCheck.setChecked(alarmDTO.isThursday());
        holder.friCheck.setChecked(alarmDTO.isFriday());
        holder.satCheck.setChecked(alarmDTO.isSaturday());
        holder.sunCheck.setChecked(alarmDTO.isSunday());


        holder.timeView.setText(presentFullTime(alarmDTO.getAlarmHour(), alarmDTO.getAlarmMinute()));
        holder.alarmListView.setOnLongClickListener(longClickView(alarmDTO.getId(), position));

        holder.monCheck.setOnCheckedChangeListener(checkedChangeListener(alarmDTO));
        holder.tuesCheck.setOnCheckedChangeListener(checkedChangeListener(alarmDTO));
        holder.wednesCheck.setOnCheckedChangeListener(checkedChangeListener(alarmDTO));
        holder.thurCheck.setOnCheckedChangeListener(checkedChangeListener(alarmDTO));
        holder.friCheck.setOnCheckedChangeListener(checkedChangeListener(alarmDTO));
        holder.satCheck.setOnCheckedChangeListener(checkedChangeListener(alarmDTO));
        holder.sunCheck.setOnCheckedChangeListener(checkedChangeListener(alarmDTO));

        holder.onOffCheck.setOnCheckedChangeListener(switchOnCheckedChangeListener(alarmDTO));


    }

    public void setList(RealmResults<AlarmDTO> list) {
        this.list = list;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    // 시간을 00:00의 형태로 표현
    public String presentFullTime(int hour, int minute) {

        String sHour = String.valueOf(hour);
        String sMinute = String.valueOf(minute);

        if (hour < 10) {
            sHour = String.format("0%d", hour);
        }
        if (minute < 10) {
            sMinute = String.format("0%d", minute);
        }

        return String.format("%s : %s", sHour, sMinute);
    }

    //스위치 리스너로 알람 해제
    public AnimCheckBox.OnCheckedChangeListener switchOnCheckedChangeListener(final AlarmDTO alarmDTO) {

        return new AnimCheckBox.OnCheckedChangeListener() {
            @Override
            public void onChange(AnimCheckBox animCheckBox, boolean isChecked) {

                alarmDAO.realm.beginTransaction();
                alarmDTO.setAlarmOnOff(isChecked);
                alarmDAO.realm.commitTransaction();


                //알람을 끌 경우
                if (!isChecked) {
                    AlarmScheduler.releaseAlarm(alarmDTO.getId(), context);
                } else { // 알람을 킬 경우
                    // 알람 등록
                    AlarmScheduler.registerAlarm(context, alarmDTO.getId(), alarmDTO.getAlarmHour(), alarmDTO.getAlarmMinute());
                }
                notifyDataSetChanged();
            }

        };
    }


    public CheckBox.OnCheckedChangeListener checkedChangeListener(final AlarmDTO alarmDTO) {

        return new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int checkId = compoundButton.getId();

                checkBoxValueChange(checkId, alarmDTO);
                notifyDataSetChanged();
            }
        };
    }

    private void checkBoxValueChange(final int checkId, final AlarmDTO alarmDTO) {

        alarmDAO.realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                AlarmDTO alarm = realm.where(AlarmDTO.class).equalTo("id", alarmDTO.getId()).findFirst();
                switch (checkId) {
                    case R.id.alarm_list_view_monday:
                        if (alarmDTO.isMonday() == false) {
                            alarm.setMonday(true);
                        } else {
                            alarm.setMonday(false);
                        }
                        break;
                    case R.id.alarm_list_view_tuesday:
                        if (alarmDTO.isTuesday() == false) {
                            alarm.setTuesday(true);
                        } else {
                            alarm.setTuesday(false);
                        }
                        break;
                    case R.id.alarm_list_view_wednesday:
                        if (alarmDTO.isWednesday() == false) {
                            alarm.setWednesday(true);
                        } else {
                            alarm.setWednesday(false);
                        }
                        break;
                    case R.id.alarm_list_view_thursday:
                        if (alarmDTO.isThursday() == false) {
                            alarm.setThursday(true);
                        } else {
                            alarm.setThursday(false);
                        }
                        break;
                    case R.id.alarm_list_view_friday:
                        if (alarmDTO.isFriday() == false) {
                            alarm.setFriday(true);
                        } else {
                            alarm.setFriday(false);
                        }
                        break;
                    case R.id.alarm_list_view_saturday:
                        if (alarmDTO.isSaturday() == false) {
                            alarm.setSaturday(true);
                        } else {
                            alarm.setSaturday(false);
                        }
                        break;
                    case R.id.alarm_list_view_sunday:
                        if (alarmDTO.isSunday() == false) {
                            alarm.setSunday(true);
                        } else {
                            alarm.setSunday(false);
                        }
                        break;
                }
            }
        });

    }


    // 뷰 길게 클릭 - 삭제 다이얼로그 띄우기
    public View.OnLongClickListener longClickView(final long id, final int position) {



        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                vibrator.vibrate(100);
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("지우실래요?");
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        alarmDAO.deleteAlarmData(id);
                        notifyDataSetChanged();

                        Toast.makeText(context.getApplicationContext(), "알람을 지웠습니다^^", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(context.getApplicationContext(), "취소 됬어요~", Toast.LENGTH_LONG).show();
                    }
                });

                builder.show();

                return true;
            }
        };
    }

    public void setAlarmDAO(AlarmDAO alarmDAO) {
        this.alarmDAO = alarmDAO;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        public TextView timeView;

        public LinearLayout alarmListView;

        public CheckBox monCheck;
        public CheckBox tuesCheck;
        public CheckBox wednesCheck;
        public CheckBox thurCheck;
        public CheckBox friCheck;
        public CheckBox satCheck;
        public CheckBox sunCheck;

        public AnimCheckBox onOffCheck;

        public ViewHolder(View itemView) {
            super(itemView);

            alarmListView = (LinearLayout) itemView.findViewById(R.id.alarm_list_itemView);

            timeView = (TextView) itemView.findViewById(R.id.alarm_list_timeView);
            monCheck = (CheckBox) itemView.findViewById(R.id.alarm_list_view_monday);
            tuesCheck = (CheckBox) itemView.findViewById(R.id.alarm_list_view_tuesday);
            wednesCheck = (CheckBox) itemView.findViewById(R.id.alarm_list_view_wednesday);
            thurCheck = (CheckBox) itemView.findViewById(R.id.alarm_list_view_thursday);
            friCheck = (CheckBox) itemView.findViewById(R.id.alarm_list_view_friday);
            satCheck = (CheckBox) itemView.findViewById(R.id.alarm_list_view_saturday);
            sunCheck = (CheckBox) itemView.findViewById(R.id.alarm_list_view_sunday);

            onOffCheck = (AnimCheckBox) itemView.findViewById(R.id.alarm_on_off_checkbox);

        }
    }
}
