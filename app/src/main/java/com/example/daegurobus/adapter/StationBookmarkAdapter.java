package com.example.daegurobus.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daegurobus.BusStationDetailActivity;
import com.example.daegurobus.R;
import com.example.daegurobus.model.RouteStationBookmark;
import com.example.daegurobus.model.StationBookmark;
import com.example.daegurobus.databinding.BusListMainStationBookmarkBinding;

import java.util.ArrayList;

public class StationBookmarkAdapter extends BaseRecyclerViewAdapter<StationBookmark, StationBookmarkAdapter.ViewHolder> {

    // private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private Context context;
    private ArrayList<RouteStationBookmark> dataList;

    private InnerAdapter inneradapter;

    private Parcelable recyclerViewState;


    public StationBookmarkAdapter(Context context) {
        super(context);

    }


    public void onBindView(ViewHolder holder, int position) {
        StationBookmark item = getItem(position);

        if (position == getItemCount() - 1) {
            holder.binding.loBottomLine.setVisibility(View.GONE);
        }


        holder.binding.tvStationName.setText(item.getStationName());
        holder.binding.tvStationNo.setText(item.getStationNo());
        holder.binding.tvDirection.setText(item.getStationDirection());
        switch (item.getSubwayNum()) {
            case "1":
                holder.binding.tvSubwayNum1.setVisibility(View.VISIBLE);
                holder.binding.tvSubwayNum2.setVisibility(View.GONE);
                holder.binding.tvSubwayNum3.setVisibility(View.GONE);
                break;
            case "2":
                holder.binding.tvSubwayNum1.setVisibility(View.GONE);
                holder.binding.tvSubwayNum2.setVisibility(View.VISIBLE);
                holder.binding.tvSubwayNum3.setVisibility(View.GONE);
                break;
            case "3":
                holder.binding.tvSubwayNum1.setVisibility(View.GONE);
                holder.binding.tvSubwayNum2.setVisibility(View.GONE);
                holder.binding.tvSubwayNum3.setVisibility(View.VISIBLE);
                break;
            case "1,2":
                holder.binding.tvSubwayNum1.setVisibility(View.VISIBLE);
                holder.binding.tvSubwayNum2.setVisibility(View.VISIBLE);
                holder.binding.tvSubwayNum3.setVisibility(View.GONE);
                break;
            case "1,3":
                holder.binding.tvSubwayNum1.setVisibility(View.VISIBLE);
                holder.binding.tvSubwayNum2.setVisibility(View.GONE);
                holder.binding.tvSubwayNum3.setVisibility(View.VISIBLE);
                break;
            case "2,3":
                holder.binding.tvSubwayNum1.setVisibility(View.GONE);
                holder.binding.tvSubwayNum2.setVisibility(View.VISIBLE);
                holder.binding.tvSubwayNum3.setVisibility(View.VISIBLE);
                break;
            case "":
                holder.binding.tvSubwayNum1.setVisibility(View.GONE);
                holder.binding.tvSubwayNum2.setVisibility(View.GONE);
                holder.binding.tvSubwayNum3.setVisibility(View.GONE);
                break;
        }
        ArrayList<RouteStationBookmark> matchingData = new ArrayList<>();

        for (int i = 0; i < dataList.size(); i++) {
            if (item.getStationId().equals(dataList.get(i).getStationId())) {
                matchingData.add(dataList.get(i));
            }
        }


        if (matchingData.size() > 0) {
            inneradapter = new InnerAdapter(context);
            holder.binding.rvInnerBookmark.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            holder.binding.rvInnerBookmark.setAdapter(inneradapter);
            recyclerViewState = holder.binding.rvInnerBookmark.getLayoutManager().onSaveInstanceState();
//            holder.binding.rvInnerBookmark.setRecycledViewPool(viewPool);
            inneradapter.initItems(matchingData);
        } else {
            holder.binding.rvInnerBookmark.setVisibility(View.GONE);
        }


    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bus_list_main_station_bookmark, viewGroup, false);
        return new ViewHolder(view);

    }

    public void initItems(ArrayList<RouteStationBookmark> dataList, ArrayList<StationBookmark> list) {
        this.dataList = dataList;
        initItems(list);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private BusListMainStationBookmarkBinding binding;

        ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);

            binding.rvInnerBookmark.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onItemClick(view, getLayoutPosition());
        }

    }
}
