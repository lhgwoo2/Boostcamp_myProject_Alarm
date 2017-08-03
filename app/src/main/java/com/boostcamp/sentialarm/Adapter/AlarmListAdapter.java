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

import com.boostcamp.sentialarm.R;
import com.boostcamp.sentialarm.VO.AlarmVO;

import java.util.List;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by 현기 on 2017-07-31.
 */

public class AlarmListAdapter extends RealmRecyclerViewAdapter<AlarmVO, AlarmListAdapter.AlarmListViewHoler> {

    //private List<AlarmVO> alarmArray;
    private Context context;
    AlarmVO alarmVO;
    Realm realm;


    public AlarmListAdapter(OrderedRealmCollection<AlarmVO> data, Context context)  {
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

        alarmVO = getItem(position);

        holder.alarmVO = alarmVO;

        holder.onOffSwitch.setChecked(alarmVO.isAlarmOnOff());

        holder.monCheck.setChecked(alarmVO.isMonday());
        holder.tuesCheck.setChecked(alarmVO.isTuesday());
        holder.wednesCheck.setChecked(alarmVO.isWednesday());
        holder.thurCheck.setChecked(alarmVO.isThursday());
        holder.friCheck.setChecked(alarmVO.isFriday());
        holder.satCheck.setChecked(alarmVO.isSaturday());
        holder.sunCheck.setChecked(alarmVO.isSunday());

        holder.timeView.setText(alarmVO.getAlarmtime());

        holder.alarmCardView.setOnLongClickListener(longClickCardView());

    }

    // 뷰 길게 클릭 - 삭제 다이얼로그 띄우기
    public View.OnLongClickListener longClickCardView(){

        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("지우실래요?");
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
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
    public void deleteAlarmData(){
        real
        alarmVO.deleteFromRealm();

    }



    public void setAlarmListData(List<AlarmVO> alarmArray){


        //this.alarmArray = alarmArray;
    }


/* @Override
    public int getItemCount() {
        return alarmArray.size();
    }*/

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


        public AlarmVO alarmVO;

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
