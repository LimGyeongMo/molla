package com.example.daegurobus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daegurobus.R;
import com.example.daegurobus.databinding.BusListDropDownBinding;
import com.example.daegurobus.model.BusStation;
import com.example.daegurobus.model.stationResult;
import com.example.daegurobus.util.BasicUtil;

import java.util.ArrayList;

public class BusStationAtAdapter extends BaseRecyclerViewAdapter<stationResult, BusStationAtAdapter.ViewHolder> {

    
    private String keyword;


    public BusStationAtAdapter(Context context) {
        super(context);
    }

    @Override
    public void onBindView(ViewHolder holder, int position){
        stationResult item = getItem(position);

        holder.binding.tvRouteNum.setText(item.getStationName());
        holder.binding.tvStart.setText(item.getStationDirection());
        holder.binding.tvRouteNum.setText(BasicUtil.convertHighlightText(item.getStationName(), keyword,context.getColor(R.color.color_01CAFF)));
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                item.getStationName();
//
//                mListener.onItemClick(v, item.getStationName(), item.getStationId());
//
//            }
//        });

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bus_list_drop_down, viewGroup, false));
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private BusListDropDownBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);

            binding = DataBindingUtil.bind(itemView);
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onItemClick(view, getLayoutPosition());
        }
    }
}



