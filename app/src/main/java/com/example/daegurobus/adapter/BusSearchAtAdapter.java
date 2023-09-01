package com.example.daegurobus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daegurobus.R;
import com.example.daegurobus.model.BusResultSearch;
import com.example.daegurobus.databinding.BusListDropDownBinding;
import com.example.daegurobus.util.BasicUtil;


import java.util.ArrayList;

public class BusSearchAtAdapter extends BaseRecyclerViewAdapter<BusResultSearch, BusSearchAtAdapter.ViewHolder> {

    private String keyword;



    public BusSearchAtAdapter(Context context) {
        super(context);
    }

    @Override
    public void onBindView(ViewHolder holder, int position){
        BusResultSearch item = getItem(position);

        if (position == items.size() -1 ){
            holder.binding.loForm.setBackground(context.getDrawable(R.drawable.bg_round_area_6_bottom));
            holder.binding.vBottomLine.setVisibility(View.GONE);
        }

        if ("".equals(item.getRouteNum2())) {
            holder.binding.tvRouteNum.setText(BasicUtil.convertHighlightText(item.getRouteNum(), keyword,context.getColor(R.color.color_01CAFF)));

        } else {
            holder.binding.tvRouteNum.setText(BasicUtil.convertHighlightText(item.getRouteNum() + "(" + item.getRouteNum2() + ")", keyword,context.getColor(R.color.color_01CAFF)));
        }

        holder.binding.tvStart.setText(item.getStartNodeName() + " ↔ " + item.getEndNodeName());

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bus_list_drop_down, viewGroup, false));
    }

//    private OnItemClickListener mListener ;
//
//    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
//    public void setOnItemClickListener(OnItemClickListener listener) {
//        this.mListener = listener ;
//    }
//
//    public interface OnItemClickListener {
//        void onItemClick(View v, String keyword, String routeId) ;
//    }


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







