package com.example.daegurobus.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daegurobus.R;

import com.example.daegurobus.databinding.BusStationDetailArriveListBinding;
import com.example.daegurobus.network.DEFINE;
import com.example.daegurobus.network.NetworkPresenter;
import com.example.daegurobus.model.RequestBus;
import com.example.daegurobus.model.StationDetailInfoSub;
import com.example.daegurobus.network.resultInterface.AuthBaseInterface;
import com.example.daegurobus.model.StationDetailInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class StationDetailArriveInfoAdapter extends BaseRecyclerViewAdapter<StationDetailInfo, StationDetailArriveInfoAdapter.ViewHolder> {


    private ArrayList<StationDetailInfoSub> dataList;
    private StationDetailInfo savedItem;

    private NetworkPresenter presenter;



    private Handler handler = new Handler();
    private final String GAN = "1";
    private final String JI = "2";
    private final String SUN = "3";
    private final String FAST = "4";
    private final String WORK = "5";

    private String gbn = "3";


    public StationDetailArriveInfoAdapter(Context context) {
        super(context);
    }


    @Override
    public void onBindView(ViewHolder holder, int position) {
        StationDetailInfo item = getItem(position);
        StationDetailInfoSub itemSub = dataList.get(position);
        savedItem = item;
        if ("".equals(item.getRouteNum2())) {
            holder.binding.tvBusRouteNum.setText(item.getRouteNum());

        } else {
            holder.binding.tvBusRouteNum.setText(item.getRouteNum() + item.getRouteNum2());
        }

        if (holder.timerTask != null) {
            holder.timerTask.cancel();
            holder.timer = null;
            holder.timerTask = null;
        }

        if (holder.timerTask1 != null) {
            holder.timerTask1.cancel();
            holder.timer1 = null;
            holder.timerTask1 = null;
        }
        holder.binding.tvCurrentStation.setText(item.getRouteDirection());

        presenter = new NetworkPresenter(context);
        holder.binding.imBookmark.setOnClickListener(View ->  {

           if (item.getBusBookmarkYN().equals("Y")){
               RequestBus requestBus = new RequestBus(DEFINE.MCODE, DEFINE.DELETE_BOOKMARK);
               requestBus.addParam(gbn);
               requestBus.addParam(item.getRouteId());
               requestBus.addParam(item.getStationId());
               requestBus.addParam(item.getUpDownCd());
               requestBus.addParam(item.getStationOrd());
               requestBus.commit();
               presenter.bookmarkDelete(requestBus, new AuthBaseInterface<String>() {

                   @Override
                   public void success(String item) {
                       if (item.equals("00")) {
                           savedItem.setBusBookmarkYN("N");
                           holder.binding.imBookmark.setImageResource(R.drawable.common_off_star_40);
                       }else {
                           holder.binding.imBookmark.setImageResource(R.drawable.common_on_star_40);
                       }
                   }

                   @Override
                   public void error(String message) {
                   }
                   @Override
                   public void errorAuth() {
                   }
               });
           }else{
               RequestBus requestBus = new RequestBus(DEFINE.MCODE, DEFINE.INSERT_BOOKMARK);
               requestBus.addParam(gbn);
               requestBus.addParam(item.getRouteId());
               requestBus.addParam(item.getStationId());
               requestBus.addParam(item.getUpDownCd());
               requestBus.addParam(item.getStationOrd());
               requestBus.commit();
               presenter.bookmarkInsert(requestBus, new AuthBaseInterface<String>() {
                   @Override
                   public void success(String item) {
                       if (item.equals("00")){
                           savedItem.setBusBookmarkYN("Y");
                           holder.binding.imBookmark.setImageResource(R.drawable.common_on_star_40);
                       }else{
                           holder.binding.imBookmark.setImageResource(R.drawable.common_off_star_40);
                       }
                   }
                   @Override
                   public void error(String message) {
                   }
                   @Override
                   public void errorAuth() {
                   }
               });
           }
        });

        if ("Y".equals(savedItem.getBusBookmarkYN())){
            holder.binding.imBookmark.setImageResource(R.drawable.common_on_star_40);
        }else{
               holder.binding.imBookmark.setImageResource(R.drawable.common_off_star_40);
        }

        switch (item.getRouteTp()) {
            case GAN:
                holder.binding.tvRouteTp.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_018EEC)));
                holder.binding.tvRouteTp.setText("간선");
                break;
            case JI:
                holder.binding.tvRouteTp.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_F7B744)));
                holder.binding.tvRouteTp.setText("지선");
                break;
            case SUN:
                holder.binding.tvRouteTp.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_008142)));
                holder.binding.tvRouteTp.setText("순환");
                break;
            case FAST:
                holder.binding.tvRouteTp.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_EB0220)));
                holder.binding.tvRouteTp.setText("급행");
                break;
            case WORK:
                holder.binding.tvRouteTp.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_8AD644)));
                holder.binding.tvRouteTp.setText("출근");
                break;
        }

        if ("1".equals(itemSub.getRouteLowFloorYn())) {
            holder.binding.tvVihicleTp.setVisibility(View.VISIBLE);
        } else {
            holder.binding.tvVihicleTp.setVisibility(View.GONE);
        }
        if ("1".equals(itemSub.getRouteLowFloorYn1())) {
            holder.binding.tvVihicleTp1.setVisibility(View.VISIBLE);
        } else {
            holder.binding.tvVihicleTp1.setVisibility(View.GONE);
        }

        if ("1".equals(itemSub.getRouteLowFloorYn())) {
            holder.binding.tvVihicleLate.setVisibility(View.VISIBLE);
        } else {
            holder.binding.tvVihicleLate.setVisibility(View.GONE);
        }
        if ("1".equals(itemSub.getRouteLowFloorYn1())) {
            holder.binding.tvVihicleLate1.setVisibility(View.VISIBLE);
        } else {
            holder.binding.tvVihicleLate1.setVisibility(View.GONE);
        }

        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            String dataTime = dateFormat.format(new Date());

            Date endVehicleTime = new Date(dateFormat.parse(item.getEndVehicleTime()).getTime());
            Date recentTime = new Date(dateFormat.parse(dataTime).getTime());

            int compare = endVehicleTime.compareTo(recentTime);

            if (compare < 0){
                if (itemSub.getTimeTaken1().length() == 0 && itemSub.getTimeTaken().length() == 0) {
                    holder.binding.loClosed.setVisibility(View.VISIBLE);
                    holder.binding.tvOpenMessage.setText("내일 오전 " + item.getStartVehicleTime() + " 운행");
                }
            }
        }catch (Exception e){

        }

        //첫번째 버스 시간
        if("1".equals(itemSub.getRemainingStation())){
            holder.binding.tvStartVihicle.setText("곧 도착");
            holder.binding.tvStartVihicle.setTextColor(context.getResources().getColor(R.color.color_F22100));
        }else {
            if (itemSub.getTimeTaken().length() > 0) {
                itemSub.time = Integer.valueOf(itemSub.getTimeTaken());
                holder.timer = new Timer();
                holder.timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        if (itemSub.time > 0) {
                            itemSub.time--;
                            // UI 스레드에서 TextView를 업데이트.
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    holder.binding.tvStartVihicle.setText(itemSub.time / 60 + "분 " + itemSub.time % 60 + "초");
                                }
                            });
                        } else {
                            this.cancel();
                        }

                    }
                };
                holder.timer.scheduleAtFixedRate(holder.timerTask, 0l, 1000);


            } else {
                holder.binding.tvStartVihicle.setText("도착 정보 없음");
            }
        }

        if ("".equals(itemSub.getRemainingStation())){
            holder.binding.tvRemainingStation.setText("남은 정거장 도착 정보 없음");
        }else{
            holder.binding.tvRemainingStation.setText("[" + itemSub.getRemainingStation() + "번째 전]");
        }

        if (itemSub.getRecentStationName().equals("")) {
            holder.binding.tvRecentStationName.setText("도착 정보 없음");
        } else {
            holder.binding.tvRecentStationName.setText(itemSub.getRecentStationName());
        }

        //두번째 버스 시간
        if (itemSub.getTimeTaken1().length() > 0) {
            itemSub.time2 = Integer.valueOf(itemSub.getTimeTaken1());
            holder.timer1 = new Timer();
            holder.timerTask1 = new TimerTask() {
                @Override
                public void run() {
                    if (itemSub.time2 > 0) {
                        itemSub.time2--;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                holder.binding.tvEndVihicle.setText(itemSub.time2 / 60 + "분 " + itemSub.time2 % 60 + "초");
                            }
                        });
                    } else {
                        this.cancel();
                    }
                }
            };
            holder.timer1.scheduleAtFixedRate(holder.timerTask1, 0l, 1000);

        } else {
            holder.binding.tvEndVihicle.setText("도착 정보 없음");
        }

        if ("".equals(itemSub.getRemainingStation1())) {
            holder.binding.tvRemainingStationEnd.setText("남은 정거장 도착 정보 없음");
        } else {
            holder.binding.tvRemainingStationEnd.setText("[" + itemSub.getRemainingStation1() + "번째 전]");
        }

        if ("".equals(itemSub.getRecentStationName1())) {
            holder.binding.tvRecentStationNameEnd.setText("도착 정보 없음");
        } else {
            holder.binding.tvRecentStationNameEnd.setText(itemSub.getRecentStationName1());
        }

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bus_station_detail_arrive_list, viewGroup, false)); // 뷰 홀더를 생성하여 리턴
    }

    public void initItems(ArrayList<StationDetailInfoSub> dataList, ArrayList<StationDetailInfo> list) {
        this.dataList = dataList;
        initItems(list);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private BusStationDetailArriveListBinding binding;

        private Timer timer;
        private Timer timer1;
        private TimerTask timerTask;
        private TimerTask timerTask1;

        ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}