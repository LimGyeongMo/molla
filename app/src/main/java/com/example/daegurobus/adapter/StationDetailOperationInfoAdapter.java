package com.example.daegurobus.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daegurobus.R;
import com.example.daegurobus.network.DEFINE;
import com.example.daegurobus.network.NetworkPresenter;
import com.example.daegurobus.model.RequestBus;
import com.example.daegurobus.network.resultInterface.AuthBaseInterface;
import com.example.daegurobus.databinding.BusStationDetailInfoListBinding;
import com.example.daegurobus.model.StationDetailInfo;

public class StationDetailOperationInfoAdapter extends BaseRecyclerViewAdapter<StationDetailInfo, StationDetailOperationInfoAdapter.ViewHolder> {

    private NetworkPresenter presenter;

    private final String GAN = "1";
    private final String JI = "2";
    private final String SUN = "3";
    private final String FAST = "4";
    private final String WORK = "5";
    private final String GUNWI = "6";

    private int intervalTime;
    private int intervalSaturday;

    private int intervalSunday;
    private int intervalHoliday;




    public StationDetailOperationInfoAdapter(Context context) {
        super(context);
    }


    @Override
    public void onBindView(ViewHolder holder, int position) {
        StationDetailInfo item = getItem(position);
        holder.binding.tvRouteNum.setText(item.getRouteNum());
        if ("".equals(item.getRouteNum2())) {
            holder.binding.tvRouteNum.setText(item.getRouteNum());
        } else {
            holder.binding.tvRouteNum.setText(item.getRouteNum() + "(" + item.getRouteNum2() + ")");
        }
        if (holder.binding.tvRouteNum.getText().length() >10){
            holder.binding.tvRouteNum.setEllipsize(TextUtils.TruncateAt.END);
        }

        holder.binding.tvStartVihicle.setText("첫차" + item.getStartVehicleTime().substring(0, 2) + "시" + item.getStartVehicleTime().substring(3) + "분");
        holder.binding.tvEndVihicle.setText("막차" + item.getEndVehicleTime().substring(0, 2) + "시" + item.getEndVehicleTime().substring(3) + "분");

        switch (item.getRouteTp()) {
            case GAN:
                holder.binding.tvRuoteTp.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_018EEC)));
                holder.binding.tvRuoteTp.setText("간선");
                break;
            case JI:
                holder.binding.tvRuoteTp.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_F7B744)));
                holder.binding.tvRuoteTp.setText("지선");
                break;
            case SUN:
                holder.binding.tvRuoteTp.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_008142)));
                holder.binding.tvRuoteTp.setText("순환");
                break;
            case FAST:
                holder.binding.tvRuoteTp.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_EB0220)));
                holder.binding.tvRuoteTp.setText("급행");
                break;
            case WORK:
                holder.binding.tvRuoteTp.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_8AD644)));
                holder.binding.tvRuoteTp.setText("출근");
                break;
            case GUNWI:
                holder.binding.tvRuoteTp.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_183290)));
                holder.binding.tvRuoteTp.setText("군위");
                break;
        }


        intervalTime = Integer.parseInt(item.getIntervalTime()) / 60;
        intervalSaturday = Integer.parseInt(item.getIntervalSaturday()) / 60;
        intervalSunday = Integer.parseInt(item.getIntervalSunday()) / 60;
        // intervalHoliday = Integer.parseInt(item.getIntervalHoliday())/ 60;

        holder.binding.tvInterval.setText("평일" + intervalTime + "분 간격");
        holder.binding.tvIntervalSaturday.setText("토요일 " + intervalSaturday + " 분 간격");
        holder.binding.tvIntervalSunday.setText("일요일 " + intervalSunday + " 분 간격");
        // holder.binding.tvIntervalHoliday.setText("공휴일 " + intervalHoliday + " 분 간격");

        presenter = new NetworkPresenter(context);
        holder.binding.imBookmark.setOnClickListener(new View.OnClickListener() {
            boolean isButtonPressed = false;

            @Override
            public void onClick(View v) {
                if (isButtonPressed) {

                    RequestBus requestBus = new RequestBus(DEFINE.MCODE, DEFINE.INSERT_BOOKMARK);
                    requestBus.addParam("3");
                    requestBus.addParam(item.getRouteId());
                    requestBus.addParam(item.getStationId());
                    requestBus.addParam("");
                    requestBus.addParam("");
                    requestBus.commit();
                    presenter.bookmarkInsert(requestBus, new AuthBaseInterface<String>() {
                        @Override
                        public void success(String item) {
                            holder.binding.imBookmark.setImageResource(R.drawable.common_on_star_40);
                        }

                        @Override
                        public void error(String message) {
                        }

                        @Override
                        public void errorAuth() {
                        }
                    });
                } else {
                    RequestBus requestBus = new RequestBus(DEFINE.MCODE, DEFINE.DELETE_BOOKMARK);
                    requestBus.addParam("3");
                    requestBus.addParam(item.getRouteId());
                    requestBus.addParam(item.getStationId());
                    requestBus.addParam("");
                    requestBus.addParam("");
                    requestBus.commit();
                    presenter.bookmarkDelete(requestBus, new AuthBaseInterface<String>() {
                        @Override
                        public void success(String item) {
                            holder.binding.imBookmark.setImageResource(R.drawable.common_off_star_40);
                        }

                        @Override
                        public void error(String message) {
                        }

                        @Override
                        public void errorAuth() {
                        }
                    });
                }
                isButtonPressed = !isButtonPressed;
            }
        });

        switch (item.getBusBookmarkYN()) {
            case "y":
                holder.binding.imBookmark.setImageResource(R.drawable.common_on_star_40);
                break;
            case "N":
                holder.binding.imBookmark.setImageResource(R.drawable.common_off_star_40);
                break;
        }


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new StationDetailOperationInfoAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bus_station_detail_info_list, viewGroup, false)); // 뷰 홀더를 생성하여 리턴
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private BusStationDetailInfoListBinding binding;

        ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}


