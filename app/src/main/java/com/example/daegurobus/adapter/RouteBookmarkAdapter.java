package com.example.daegurobus.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daegurobus.BusDetailActivity;
import com.example.daegurobus.R;
import com.example.daegurobus.model.RouteBookmark;
import com.example.daegurobus.databinding.BusListMainRouteBookmarkBinding;

import java.util.ArrayList;

//public class RouteBookmarkAdapter extends RecyclerView.Adapter<RouteBookmarkAdapter.ViewHolder> {
public class RouteBookmarkAdapter extends BaseRecyclerViewAdapter<RouteBookmark, RouteBookmarkAdapter.ViewHolder> {

    private final String GAN = "1";
    private final String JI = "2";
    private final String SUN = "3";
    private final String FAST = "4";
    private final String WORK = "5";
    private final String GUNWI = "6";

    /*
    public RouteBookmarkAdapter(Context context,ArrayList<RouteBookmark> data){
        this.context = context;
        this.data = data;
    }
    */
    public RouteBookmarkAdapter(Context context) {
        super(context);

    }

    @Override
    public void onBindView(ViewHolder holder, int position) {
        RouteBookmark item = getItem(position);

        if("".equals(item.getRouteNum2())){
            holder.binding.tvRouteNum.setText(item.getRouteNum());
        }else{
            holder.binding.tvRouteNum.setText(item.getRouteNum()+"("+item.getRouteNum2()+")");
        }

        switch (item.getRouteTp()) {
            case GAN:
                holder.binding.tvTp.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_018EEC)));
                holder.binding.tvTp.setText("간선");
                break;
            case JI:
                holder.binding.tvTp.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_F7B744)));
                holder.binding.tvTp.setText("지선");
                break;
            case SUN:
                holder.binding.tvTp.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_008142)));
                holder.binding.tvTp.setText("순환");
                break;
            case FAST:
                holder.binding.tvTp.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_EB0220)));
                holder.binding.tvTp.setText("급행");
                break;
            case WORK:
                holder.binding.tvTp.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_8AD644)));
                holder.binding.tvTp.setText("출근");
                break;
            case GUNWI:
                holder.binding.tvTp.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_183290)));
                holder.binding.tvTp.setText("군위");
                break;
        }

        holder.binding.tvNodeName.setText(item.getStartnodeName()+" ↔ "+item.getEndtnodeName());

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.bus_list_main_route_bookmark, parent, false));

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private BusListMainRouteBookmarkBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public void onClick(View view) {
            onItemClickListener.onItemClick(view, getLayoutPosition());
        }
    }

}
