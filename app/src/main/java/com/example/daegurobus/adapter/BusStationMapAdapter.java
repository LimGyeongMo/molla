package com.example.daegurobus.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daegurobus.R;
import com.example.daegurobus.databinding.BusFilterListBinding;
import com.example.daegurobus.model.BusFliter;

import java.util.ArrayList;
import java.util.List;

public class BusStationMapAdapter extends RecyclerView.Adapter<BusStationMapAdapter.ViewHolder> {

    ArrayList<BusFliter> data;
    private Context context;

    private int selectedPosition = -1;

    private final int station = 1;
    private final int subway = 2;
    private final int train = 3;
    private final int terminal = 4;
    private final int airport = 5;

    public BusStationMapAdapter(Context context, ArrayList<BusFliter> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.bus_filter_list, parent, false);
        return new BusStationMapAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BusFliter item = data.get(position);
        if (position == selectedPosition) {
            //선택된 아이템
            switch (item.getFilter()) {
                case station:
                    holder.binding.flitering.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_306FD9)));
                    holder.binding.imFliter.setImageResource(R.drawable.ic_busstop_white_22_pt);
                    holder.binding.tvFliter.setText(R.string.bus_station);
                    holder.binding.tvFliter.setTextColor(context.getResources().getColor(R.color.color_FFFFFF));
                    break;
                case subway:
                    holder.binding.flitering.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_5f6062)));
                    holder.binding.imFliter.setImageResource(R.drawable.ic_metro_white_20_pt);
                    holder.binding.tvFliter.setText(R.string.subway);
                    holder.binding.tvFliter.setTextColor(context.getResources().getColor(R.color.color_FFFFFF));
                    break;
                case train:
                    holder.binding.flitering.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_790252)));
                    holder.binding.imFliter.setImageResource(R.drawable.common_train_20);
                    holder.binding.tvFliter.setText(R.string.train_station);
                    holder.binding.tvFliter.setTextColor(context.getResources().getColor(R.color.color_FFFFFF));
                    break;
                case terminal:
                    holder.binding.flitering.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_5d8233)));
                    holder.binding.imFliter.setImageResource(R.drawable.common_bus_20);
                    holder.binding.tvFliter.setText(R.string.bus_terminal);
                    holder.binding.tvFliter.setTextColor(context.getResources().getColor(R.color.color_FFFFFF));
                    break;
                case airport:
                    holder.binding.flitering.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_B61F00)));
                    holder.binding.imFliter.setImageResource(R.drawable.common_airplane_20);
                    holder.binding.tvFliter.setText(R.string.airport);
                    holder.binding.tvFliter.setTextColor(context.getResources().getColor(R.color.color_FFFFFF));
                    break;
            }

        } else {
            // 아이템을 일반 상태로 돌려 놓기
            switch (item.getFilter()) {
                case station:
                    holder.binding.flitering.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white)));
                    holder.binding.imFliter.setImageResource(R.drawable.ic_busstop_gray_22_pt);
                    holder.binding.tvFliter.setText(R.string.bus_station);
                    holder.binding.tvFliter.setTextColor(context.getResources().getColor(R.color.color_999999));
                    break;
                case subway:
                    holder.binding.flitering.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white)));
                    holder.binding.imFliter.setImageResource(R.drawable.ic_metro_20_pt);
                    holder.binding.tvFliter.setText(R.string.subway);
                    holder.binding.tvFliter.setTextColor(context.getResources().getColor(R.color.color_999999));
                    break;
                case train:
                    holder.binding.flitering.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white)));
                    holder.binding.imFliter.setImageResource(R.drawable.ic_train_20_pt);
                    holder.binding.tvFliter.setText(R.string.train_station);
                    holder.binding.tvFliter.setTextColor(context.getResources().getColor(R.color.color_999999));
                    break;
                case terminal:
                    holder.binding.flitering.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white)));
                    holder.binding.imFliter.setImageResource(R.drawable.ic_bus_gray_20_pt);
                    holder.binding.tvFliter.setText(R.string.bus_terminal);
                    holder.binding.tvFliter.setTextColor(context.getResources().getColor(R.color.color_999999));
                    break;
                case airport:
                    holder.binding.flitering.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white)));
                    holder.binding.imFliter.setImageResource(R.drawable.ic_airplane_gray_20_pt);
                    holder.binding.tvFliter.setText(R.string.airport);
                    holder.binding.tvFliter.setTextColor(context.getResources().getColor(R.color.color_999999));
                    break;
            }
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int previousSelectedPosition = selectedPosition;
                selectedPosition = holder.getAdapterPosition();

                mListener.onItemClick(v, item.getFilter());

                // 이전 선택된 아이템과 현재 선택된 아이템만 업데이트
                notifyItemChanged(previousSelectedPosition);
                notifyItemChanged(selectedPosition);


            }
        });


    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private BusFilterListBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);

        }
    }

    public BusFliter getItem(int position) {
        if (data == null) {
            return null;
        }

        return data.get(position);
    }


    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int nearByInfo);
    }

    private void tabSelect(ImageView imageView, TextView textView, View view) {
        textView.setTypeface(ResourcesCompat.getFont(context, R.font.font_noto_sans_cj_kkr_bold));
        textView.setTextColor(ContextCompat.getColor(context, R.color.color_FFFFFF));
    }

    private void tabUnSelect(ImageView imageView, TextView textView, View view) {
        view.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_FFFFFF)));
        textView.setTypeface(ResourcesCompat.getFont(context, R.font.font_noto_sans_cj_kkr_medium));
        textView.setTextColor(ContextCompat.getColor(context, R.color.color_999999));
    }

}
