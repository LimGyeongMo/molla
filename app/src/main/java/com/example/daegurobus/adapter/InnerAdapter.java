package com.example.daegurobus.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daegurobus.BusDetailActivity;
import com.example.daegurobus.BusRouteMapActivity;
import com.example.daegurobus.R;
import com.example.daegurobus.network.DEFINE;
import com.example.daegurobus.model.RouteStationBookmark;
import com.example.daegurobus.databinding.BusListMainStationSubBinding;
import com.example.daegurobus.model.StationDetailInfoSub;

import java.util.Timer;
import java.util.TimerTask;

public class InnerAdapter extends BaseRecyclerViewAdapter<RouteStationBookmark, InnerAdapter.ViewHolder> {

    private Handler handler = new Handler();
    private final String GAN = "1";
    private final String JI = "2";
    private final String SUN = "3";
    private final String FAST = "4";
    private final String WORK = "5";
    private final String GUNWI = "6";


    public InnerAdapter(Context context) {
        super(context);

    }

    public void onBindView(ViewHolder holder, int position) {
        RouteStationBookmark item = getItem(position);

        switch (item.getRouteTp()) {
            case GAN:
                holder.binding.tvTp.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(holder.binding.tvTp.getContext(), R.color.color_018EEC)));
                holder.binding.tvTp.setText("간선");
                break;
            case JI:
                holder.binding.tvTp.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(holder.binding.tvTp.getContext(), R.color.color_F7B744)));
                holder.binding.tvTp.setText("지선");
                break;
            case SUN:
                holder.binding.tvTp.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(holder.binding.tvTp.getContext(), R.color.color_008142)));
                holder.binding.tvTp.setText("순환");
                break;
            case FAST:
                holder.binding.tvTp.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(holder.binding.tvTp.getContext(), R.color.color_EB0220)));
                holder.binding.tvTp.setText("급행");
                break;
            case WORK:
                holder.binding.tvTp.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(holder.binding.tvTp.getContext(), R.color.color_8AD644)));
                holder.binding.tvTp.setText("출근");
                break;
            case GUNWI:
                holder.binding.tvTp.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(holder.binding.tvTp.getContext(), R.color.color_183290)));
                holder.binding.tvTp.setText("군위");
                break;
        }

        if ("".equals(item.getRouteNum2())) {
            holder.binding.tvRouteNum.setText(item.getRouteNum());
        } else {
            holder.binding.tvRouteNum.setText(item.getRouteNum() + "(" + item.getRouteNum2() + ")");
        }

        holder.binding.tvDirection.setText(item.getRouteDirection());

        StationDetailInfoSub timeInfo = new StationDetailInfoSub();

        if (item.getRouteInfo() == null) {
            holder.binding.tvFirstTime.setText("정보없음");
            holder.binding.tvStationBefore.setText("[도착정보없음]");
            holder.binding.tvSecondTime.setText("정보없음");
            holder.binding.tvStationBefore1.setText("[도착정보없음]");

        } else {
            String[] ROUTE_INFOs = item.getRouteInfo().split(DEFINE.SEMICOLON);
            String row4 = ROUTE_INFOs[0];

            String[] firstInfo = row4.split(DEFINE.COMMA);
            String recentStationId = firstInfo[0];
            String recentStationName = firstInfo[1];
            String recentStationStopNum = firstInfo[2];
            String StationTimeOfArrive = firstInfo[3];
            String timeTaken = firstInfo[4];
            String REMAINING_STATION = firstInfo[5];
            String routeLowFloorYn = firstInfo[6];
            String routeTrafficYn = firstInfo[7];

            timeInfo.setRecentStationId(recentStationId);
            timeInfo.setRecentStationName(recentStationName);
            timeInfo.setRecentStationStopNum(recentStationStopNum);
            timeInfo.setStationTimeOfArrive(StationTimeOfArrive);
            timeInfo.setTimeTaken(timeTaken);
            timeInfo.setRemainingStation(REMAINING_STATION);
            timeInfo.setRouteLowFloorYn(routeLowFloorYn);
            timeInfo.setRouteTrafficYn(routeTrafficYn);

            if (ROUTE_INFOs.length > 1) {
                String row5 = ROUTE_INFOs[1];
                String[] endInfo = row5.split(DEFINE.COMMA);
                String recentStationId1 = endInfo[0];
                String recentStationName1 = endInfo[1];
                String recentStationStopNum1 = endInfo[2];
                String StationTimeOfArrive1 = endInfo[3];
                String timeTaken1 = endInfo[4];
                String REMAINING_STATION1 = endInfo[5];
                String routeLowFloorYn1 = endInfo[6];
                String routeTrafficYn1 = endInfo[7];

                timeInfo.setRecentStationId1(recentStationId1);
                timeInfo.setRecentStationName1(recentStationName1);
                timeInfo.setRecentStationStopNum1(recentStationStopNum1);
                timeInfo.setStationTimeOfArrive1(StationTimeOfArrive1);
                timeInfo.setTimeTaken1(timeTaken1);
                timeInfo.setRemainingStation1(REMAINING_STATION1);
                timeInfo.setRouteLowFloorYn1(routeLowFloorYn1);
                timeInfo.setRouteTrafficYn1(routeTrafficYn1);
            } else {
                timeInfo.setRecentStationId1(null);
                timeInfo.setRecentStationName1(null);
                timeInfo.setRecentStationStopNum1(null);
                timeInfo.setStationTimeOfArrive1(null);
                timeInfo.setTimeTaken1(null);
                timeInfo.setRemainingStation1(null);
                timeInfo.setRouteLowFloorYn1(null);
                timeInfo.setRouteTrafficYn1(null);
            }
        }

        if ("1".equals(timeInfo.getRouteLowFloorYn())) {
            holder.binding.tvLowYn.setVisibility(View.VISIBLE);
        }
        if ("1".equals(timeInfo.getRouteLowFloorYn1())) {
            holder.binding.tvLowYn1.setVisibility(View.VISIBLE);
        }

        if ("1".equals(timeInfo.getRouteTrafficYn())) {
            holder.binding.tvLateYn.setVisibility(View.VISIBLE);
        }
        if ("1".equals(timeInfo.getRouteTrafficYn1())) {
            holder.binding.tvLateYn1.setVisibility(View.VISIBLE);
        }

        try {
            //첫번째 버스
            if (timeInfo.getTimeTaken() == null) {
                holder.binding.tvFirstTime.setText("정보없음");
            } else if ("1".equals(timeInfo.getRemainingStation())) {

                holder.binding.tvFirstTime.setText("곧 도착");
                holder.binding.tvFirstTime.setTextColor(ContextCompat.getColor(holder.binding.tvFirstTime.getContext(),R.color.color_F22100));

            } else if (timeInfo.getTimeTaken().length() > 0) {

                item.firstTime = Integer.valueOf(timeInfo.getTimeTaken());
                holder.timer = new Timer();
                holder.timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        if (item.firstTime > 0) {
                            item.firstTime--;
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    holder.binding.tvFirstTime.setText(item.firstTime / 60 + "분 " + item.firstTime % 60 + "초");
                                }
                            });
                        } else {
                            this.cancel();
                        }
                    }
                };
                holder.timer.scheduleAtFixedRate(holder.timerTask, 0l, 1000);

            } else {
                holder.binding.tvFirstTime.setText("정보없음");
            }

            if (timeInfo.getRemainingStation() == null) {

                holder.binding.tvStationBefore.setText("[도착정보없음]");
            } else if ("".equals(timeInfo.getRemainingStation())) {

                holder.binding.tvStationBefore.setText("[도착정보없음]");
            } else {
                holder.binding.tvStationBefore.setText("[" + timeInfo.getRemainingStation() + "번째 전]");
            }
        }catch (Exception e){
            System.out.println(e);
        }

        try {
            //두번째 버스
            if (timeInfo.getTimeTaken1() == null) {
                holder.binding.tvSecondTime.setText("정보없음");
            } else if ("1".equals(timeInfo.getRemainingStation1())) {

                holder.binding.tvSecondTime.setText("곧 도착");
                holder.binding.tvSecondTime.setTextColor(ContextCompat.getColor(holder.binding.tvSecondTime.getContext(),R.color.color_F22100));

            } else if (timeInfo.getTimeTaken1().length() > 0) {

                item.secondTime = Integer.valueOf(timeInfo.getTimeTaken1());
                holder.timer1 = new Timer();
                holder.timerTask1 = new TimerTask() {
                    @Override
                    public void run() {
                        if (item.secondTime > 0) {
                            item.secondTime--;
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    holder.binding.tvSecondTime.setText(item.secondTime / 60 + "분 " + item.secondTime % 60 + "초");
                                }
                            });
                        } else {
                            this.cancel();
                        }
                    }
                };
                holder.timer1.scheduleAtFixedRate(holder.timerTask1, 0l, 1000);

            } else {
                holder.binding.tvSecondTime.setText("정보없음");
            }
            if (timeInfo.getRemainingStation1() == null) {
                holder.binding.tvStationBefore1.setText("[도착정보없음]");

            } else if ("".equals(timeInfo.getRemainingStation1())) {
                holder.binding.tvStationBefore1.setText("[도착정보없음]");

            } else {
                holder.binding.tvStationBefore1.setText("[" + timeInfo.getRemainingStation1() + "번째 전]");
            }

        }catch (Exception e){
            System.out.println(e);
        }

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bus_list_main_station_sub, viewGroup, false);
        return new ViewHolder(view);

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private BusListMainStationSubBinding binding;
        private Timer timer;
        private Timer timer1;
        private TimerTask timerTask;
        private TimerTask timerTask1;

        ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);

        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onItemClick(view, getLayoutPosition());
        }
    }


}
