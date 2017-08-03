package com.boostcamp.sentialarm.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.boostcamp.sentialarm.DTO.AlarmDTO;
import com.boostcamp.sentialarm.R;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by 현기 on 2017-07-31.
 */

public class AlarmListAdapter extends RealmRecyclerViewAdapter<AlarmDTO, AlarmListAdapter.AlarmListViewHoler> {


    private Context context;
    AlarmDTO alarmDTO;


    public AlarmListAdapter(OrderedRealmCollection<AlarmDTO> data, Context context)  {
        super(data, true);
        setHasStableIds(true);
        this.context = context;

    }


    @Override
    public AlarmListViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AlarmListViewHoler(LayoutInflater.from(parent.getContext()).inflate(R.layout.alarmlist_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final AlarmListViewHoler holder, int position) {

        alarmDTO = getItem(position);

        holder.alarmDTO = alarmDTO;

        holder.onOffSwitch.setChecked(alarmDTO.isAlarmOnOff());

        holder.monCheck.setChecked(alarmDTO.isMonday());
        holder.tuesCheck.setChecked(alarmDTO.isTuesday());
        holder.wednesCheck.setChecked(alarmDTO.isWednesday());
        holder.thurCheck.setChecked(alarmDTO.isThursday());
        holder.friCheck.setChecked(alarmDTO.isFriday());
        holder.satCheck.setChecked(alarmDTO.isSaturday());
        holder.sunCheck.setChecked(alarmDTO.isSunday());

        holder.timeView.setText(alarmDTO.getAlarmtime());

        holder.alarmCardView.setOnLongClickListener(longClickCardView(alarmDTO.getId(), position));


    }

    // 뷰 길게 클릭 - 삭제 다이얼로그 띄우기
    public View.OnLongClickListener longClickCardView(final long id, final int position){

        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("지우실래요?");
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteAlarmData(id);
                        notifyItemChanged(position);
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

    // 램 데이터베이스에서 삭제
    private void deleteAlarmData(long id){

        Realm instance = Realm.getDefaultInstance();
        AlarmDTO alarmDTO = instance.where(AlarmDTO.class).equalTo("id", id).findFirst();
        if (alarmDTO != null) {
            instance.beginTransaction();
            alarmDTO.deleteFromRealm();
            instance.commitTransaction();
        }
        instance.close();

    }



    public class AlarmListViewHoler extends RecyclerView.ViewHolder{

        public TextView timeView;

        public CardView alarmCardView;

        public CheckBox monCheck;
        public CheckBox tuesCheck;
        public CheckBox wednesCheck;
        public CheckBox thurCheck;
        public CheckBox friCheck;
        public CheckBox satCheck;
        public CheckBox sunCheck;

        public Switch onOffSwitch;


        public AlarmDTO alarmDTO;

        public AlarmListViewHoler(View itemView) {
            super(itemView);

            alarmCardView = (CardView) itemView.findViewById(R.id.alarm_list_itemView);

            timeView = (TextView) itemView.findViewById(R.id.alarm_list_timeView);
            monCheck = (CheckBox) itemView.findViewById(R.id.alarm_list_view_monday);
            tuesCheck = (CheckBox) itemView.findViewById(R.id.alarm_list_view_tuesday);
            wednesCheck = (CheckBox) itemView.findViewById(R.id.alarm_list_view_wednesday);
            thurCheck = (CheckBox) itemView.findViewById(R.id.alarm_list_view_thursday);
            friCheck = (CheckBox) itemView.findViewById(R.id.alarm_list_view_friday);
            satCheck = (CheckBox) itemView.findViewById(R.id.alarm_list_view_saturday);
            sunCheck = (CheckBox) itemView.findViewById(R.id.alarm_list_view_sunday);

            onOffSwitch = (Switch) itemView.findViewById(R.id.alarm_switch);



        }


    }
}
