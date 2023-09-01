package com.example.daegurobus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daegurobus.R;
import com.example.daegurobus.databinding.BusListDropDownBinding;
import com.example.daegurobus.model.CommonAddressSearched;
import com.example.daegurobus.model.stationResult;
import com.example.daegurobus.util.BasicUtil;

public class BusPathAtAdapter extends BaseRecyclerViewAdapter<CommonAddressSearched, BusPathAtAdapter.ViewHolder> {

    private String keyword;

    public BusPathAtAdapter(Context context) {
        super(context);
    }

    @Override
    public void onBindView(ViewHolder holder, int position){
        CommonAddressSearched item = getItem(position);

        if ("".equals(item.getPlaceName())) {
            holder.binding.tvRouteNum.setVisibility(View.GONE);
        } else {
            holder.binding.tvRouteNum.setVisibility(View.VISIBLE);
            holder.binding.tvRouteNum.setText(BasicUtil.convertHighlightText(item.getPlaceName(), keyword,context.getColor(R.color.color_01CAFF)));
        }



        if ("".equals(item.getNewAddress())){
            holder.binding.tvStart.setText(BasicUtil.convertHighlightText(item.getNewAddress(), keyword,context.getColor(R.color.color_01CAFF)));
        }else{
            holder.binding.tvStart.setText(BasicUtil.convertHighlightText(item.getOldAddress(), keyword,context.getColor(R.color.color_01CAFF)));
        }




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
