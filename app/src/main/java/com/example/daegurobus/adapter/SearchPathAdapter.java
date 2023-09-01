package com.example.daegurobus.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.sax.EndElementListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daegurobus.R;
import com.example.daegurobus.databinding.BusPathListBinding;
import com.example.daegurobus.databinding.BusPathTransferListBinding;
import com.example.daegurobus.model.SearchPathV2;
import com.example.daegurobus.network.DEFINE;

import java.util.ArrayList;

public class SearchPathAdapter extends BaseRecyclerViewAdapter<SearchPathV2, RecyclerView.ViewHolder> {


    private final String GAN = "1";
    private final String JI = "2";
    private final String SUN = "3";
    private final String FAST = "4";
    private final String WORK = "5";


    public SearchPathAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemViewType(int position) {
        int transfer = Integer.valueOf(getItem(position).getRouteCount());
        return transfer; // 2 또는 1을 직접 반환
    }

    @Override
    public void onBindView(RecyclerView.ViewHolder holder, int position) {
        SearchPathV2 item = getItem(position);
        if (holder instanceof standardViewHolder) {
            int currentInterval = Integer.valueOf(item.getCurrentInterval());
//            int hour = currentInterval / 3600;
//            int minute = (currentInterval % 3600) / 60;
            int minute = currentInterval / 60;
            int second = (minute) / 60;

            if (second > 0) {
                minute++;
            }
            ((standardViewHolder) holder).binding.tvCurrentInterval.setText( minute + "분 ");

            switch (item.getRouteTp1()) {
                case GAN:
                    ((standardViewHolder) holder).binding.igBus.setImageResource(R.drawable.ic_bus_color_20_pt_blue);
                    ((standardViewHolder) holder).binding.vTrafficLine.setBackgroundColor(context.getColor(R.color.color_018EEC));
                    ((standardViewHolder) holder).binding.tvTp.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_018EEC)));
                    ((standardViewHolder) holder).binding.tvTp.setText("간선");
                    break;
                case JI:
                    ((standardViewHolder) holder).binding.igBus.setImageResource(R.drawable.ic_bus_color_20_pt_yellow);
                    ((standardViewHolder) holder).binding.vTrafficLine.setBackgroundColor(context.getColor(R.color.color_F7B744));
                    ((standardViewHolder) holder).binding.tvTp.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_F7B744)));
                    ((standardViewHolder) holder).binding.tvTp.setText("지선");
                    break;
                case SUN:
                    ((standardViewHolder) holder).binding.igBus.setImageResource(R.drawable.ic_bus_color_20_pt_green);
                    ((standardViewHolder) holder).binding.vTrafficLine.setBackgroundColor(context.getColor(R.color.color_008142));
                    ((standardViewHolder) holder).binding.tvTp.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_008142)));
                    ((standardViewHolder) holder).binding.tvTp.setText("순환");
                    break;
                case FAST:
                    ((standardViewHolder) holder).binding.igBus.setImageResource(R.drawable.ic_bus_color_20_pt_dark_red);
                    ((standardViewHolder) holder).binding.vTrafficLine.setBackgroundColor(context.getColor(R.color.color_EB0220));
                    ((standardViewHolder) holder).binding.tvTp.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_EB0220)));
                    ((standardViewHolder) holder).binding.tvTp.setText("급행");
                    break;
                case WORK:
                    ((standardViewHolder) holder).binding.igBus.setImageResource(R.drawable.ic_bus_color_20_pt_light_green);
                    ((standardViewHolder) holder).binding.vTrafficLine.setBackgroundColor(context.getColor(R.color.color_8AD644));
                    ((standardViewHolder) holder).binding.tvTp.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_8AD644)));
                    ((standardViewHolder) holder).binding.tvTp.setText("출근");
                    break;
            }

            ((standardViewHolder) holder).binding.tvRouteName.setText(item.getRouteNo1());
            if (((standardViewHolder) holder).binding.tvRouteName.getText().length() > 6){
                ((standardViewHolder) holder).binding.tvRouteName.setEllipsize(TextUtils.TruncateAt.END);
            }
            ((standardViewHolder) holder).binding.tvStartStation.setText(item.getRoute1StartStationName());


            if (item.getComingSoon() == null) {
                ((standardViewHolder) holder).binding.tvTimeTaken.setText("정보 없음");
                ((standardViewHolder) holder).binding.tvStationBefore.setText("[도착 정보 없음]");

            } else {
                String[] comingSoon = item.getComingSoon().split(DEFINE.COMMA);
                String timeTaken = comingSoon[0];
                String beforeStationCount = comingSoon[1];
                String arriveTime = comingSoon[2];

                int times = Integer.valueOf(timeTaken);

                ((standardViewHolder) holder).binding.tvTimeTaken.setText(times / 60 + "분" + times % 60 + "초");
                ((standardViewHolder) holder).binding.tvStationBefore.setText("[" + beforeStationCount + "번째 전]");
            }
       // 환승 버스 정보
        } else if (holder instanceof transferViewHolder) {
            int currentInterval = Integer.valueOf(item.getCurrentInterval());
//            int hour = currentInterval / 3600;
//            int minute = (currentInterval % 3600) / 60;
            int minute = currentInterval / 60;
            int second = (minute) / 60;

            if (second > 0) {
                minute++;
            }
            ((transferViewHolder) holder).binding.tvCurrentInterval.setText( minute + "분 ");

            switch (item.getRouteTp1()) {
                case GAN:
                    ((transferViewHolder) holder).binding.igBus1.setImageResource(R.drawable.ic_bus_color_20_pt_blue);
                    ((transferViewHolder) holder).binding.vTrafficLine1.setBackgroundColor(context.getColor(R.color.color_018EEC));
                    ((transferViewHolder) holder).binding.tvTp1.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_018EEC)));
                    ((transferViewHolder) holder).binding.tvTp1.setText("간선");
                    break;
                case JI:
                    ((transferViewHolder) holder).binding.igBus1.setImageResource(R.drawable.ic_bus_color_20_pt_yellow);
                    ((transferViewHolder) holder).binding.vTrafficLine1.setBackgroundColor(context.getColor(R.color.color_F7B744));
                    ((transferViewHolder) holder).binding.tvTp1.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_F7B744)));
                    ((transferViewHolder) holder).binding.tvTp1.setText("지선");
                    break;
                case SUN:
                    ((transferViewHolder) holder).binding.igBus1.setImageResource(R.drawable.ic_bus_color_20_pt_green);
                    ((transferViewHolder) holder).binding.vTrafficLine1.setBackgroundColor(context.getColor(R.color.color_008142));
                    ((transferViewHolder) holder).binding.tvTp1.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_008142)));
                    ((transferViewHolder) holder).binding.tvTp1.setText("순환");
                    break;
                case FAST:
                    ((transferViewHolder) holder).binding.igBus1.setImageResource(R.drawable.ic_bus_color_20_pt_dark_red);
                    ((transferViewHolder) holder).binding.vTrafficLine1.setBackgroundColor(context.getColor(R.color.color_EB0220));
                    ((transferViewHolder) holder).binding.tvTp1.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_EB0220)));
                    ((transferViewHolder) holder).binding.tvTp1.setText("급행");
                    break;
                case WORK:
                    ((transferViewHolder) holder).binding.igBus1.setImageResource(R.drawable.ic_bus_color_20_pt_light_green);
                    ((transferViewHolder) holder).binding.vTrafficLine1.setBackgroundColor(context.getColor(R.color.color_8AD644));
                    ((transferViewHolder) holder).binding.tvTp1.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_8AD644)));
                    ((transferViewHolder) holder).binding.tvTp1.setText("출근");
                    break;
            }
            ((transferViewHolder) holder).binding.tvRouteName1.setText(item.getRouteNo1());
            if (((transferViewHolder) holder).binding.tvRouteName1.getText().length() > 6){
                ((transferViewHolder) holder).binding.tvRouteName1.setEllipsize(TextUtils.TruncateAt.END);
            }
            ((transferViewHolder) holder).binding.tvStartStation.setText(item.getRoute1StartStationName());

            if (item.getComingSoon() == null) {
                ((transferViewHolder) holder).binding.tvTimeTaken.setText("정보 없음");
                ((transferViewHolder) holder).binding.tvStationBefore.setText("[도착 정보 없음]");

            } else {
                String[] comingSoon = item.getComingSoon().split(DEFINE.COMMA);
                String timeTaken = comingSoon[0];
                String beforeStationCount = comingSoon[1];
                String arriveTime = comingSoon[2];

                int times = Integer.valueOf(timeTaken);

                ((transferViewHolder) holder).binding.tvTimeTaken.setText(times / 60 + "분" + times % 60 + "초");
                ((transferViewHolder) holder).binding.tvStationBefore.setText("[" + beforeStationCount + "번째 전]");
            }

             switch (item.getRouteTp2()) {
                 case GAN:
                     ((transferViewHolder) holder).binding.igBus2.setImageResource(R.drawable.ic_bus_color_20_pt_blue);
                     ((transferViewHolder) holder).binding.vTrafficLine2.setBackgroundColor(context.getColor(R.color.color_018EEC));
                     ((transferViewHolder) holder).binding.tvTp2.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_018EEC)));
                     ((transferViewHolder) holder).binding.tvTp2.setText("간선");
                     break;
                 case JI:
                     ((transferViewHolder) holder).binding.igBus2.setImageResource(R.drawable.ic_bus_color_20_pt_yellow);
                     ((transferViewHolder) holder).binding.vTrafficLine2.setBackgroundColor(context.getColor(R.color.color_F7B744));
                     ((transferViewHolder) holder).binding.tvTp2.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_F7B744)));
                     ((transferViewHolder) holder).binding.tvTp2.setText("지선");
                     break;
                 case SUN:
                     ((transferViewHolder) holder).binding.igBus2.setImageResource(R.drawable.ic_bus_color_20_pt_green);
                     ((transferViewHolder) holder).binding.vTrafficLine2.setBackgroundColor(context.getColor(R.color.color_008142));
                     ((transferViewHolder) holder).binding.tvTp2.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_008142)));
                     ((transferViewHolder) holder).binding.tvTp2.setText("순환");
                     break;
                 case FAST:
                     ((transferViewHolder) holder).binding.igBus2.setImageResource(R.drawable.ic_bus_color_20_pt_dark_red);
                     ((transferViewHolder) holder).binding.vTrafficLine2.setBackgroundColor(context.getColor(R.color.color_EB0220));
                     ((transferViewHolder) holder).binding.tvTp2.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_EB0220)));
                     ((transferViewHolder) holder).binding.tvTp2.setText("급행");
                     break;
                 case WORK:
                     ((transferViewHolder) holder).binding.igBus2.setImageResource(R.drawable.ic_bus_color_20_pt_light_green);
                     ((transferViewHolder) holder).binding.vTrafficLine2.setBackgroundColor(context.getColor(R.color.color_8AD644));
                     ((transferViewHolder) holder).binding.tvTp2.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_8AD644)));
                     ((transferViewHolder) holder).binding.tvTp2.setText("출근");
                     break;
             }
             ((transferViewHolder) holder).binding.tvRouteName2.setText(item.getRouteNo2());
            ((transferViewHolder) holder).binding.tvMidleStation.setText(item.getRoute2StartStationName());
            ((transferViewHolder) holder).binding.tvEndStation.setText(item.getRoute2EndStationName());
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == 1) {
            return new standardViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bus_path_list, viewGroup, false));
        } else if (viewType == 2) {
            return new transferViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bus_path_transfer_list, viewGroup, false));
        }
        throw new IllegalArgumentException("Unsupported data count");
    }

    public class standardViewHolder extends RecyclerView.ViewHolder {
        private BusPathListBinding binding;

        public standardViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

    }

    public class transferViewHolder extends RecyclerView.ViewHolder {
        private BusPathTransferListBinding binding;

        public transferViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

    }

}
