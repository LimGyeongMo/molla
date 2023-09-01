package com.example.daegurobus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daegurobus.R;
import com.example.daegurobus.databinding.BusPathSearchListBinding;
import com.example.daegurobus.model.BusAddressSearched;
import com.example.daegurobus.model.CommonAddressSearched;
import com.example.daegurobus.util.BasicUtil;

public class CommonAddressSearchedAdapter extends BaseRecyclerViewAdapter<BusAddressSearched, CommonAddressSearchedAdapter.ViewHolder> {
    private String keyword;

    public CommonAddressSearchedAdapter(Context context) {
        super(context);
    }

    @Override
    public void onBindView(ViewHolder holder, int position) {
        BusAddressSearched item = getItem(position);

        if ("".equals(item.getPlaceName())) {
            holder.binding.tvPlaceName.setVisibility(View.GONE);
        } else {
            holder.binding.tvPlaceName.setVisibility(View.VISIBLE);
            holder.binding.tvPlaceName.setText(BasicUtil.convertHighlightText(item.getPlaceName(), keyword, context.getColor(R.color.color_01CAFF)));
        }

        if (item.getNewAddress().equals("")) {
            holder.binding.tvNewAddress.setText(BasicUtil.convertHighlightText(item.getOldAddress(), keyword, context.getColor(R.color.color_01CAFF)));
        } else {
            holder.binding.tvNewAddress.setText(BasicUtil.convertHighlightText(item.getNewAddress(), keyword, context.getColor(R.color.color_01CAFF)));
        }

        if (item.getDirection().equals("0")) {
            holder.binding.tvDirectionPoint.setText("출발");
        }else{
            holder.binding.tvDirectionPoint.setText("도착");
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.bus_path_search_list, parent, false));
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private BusPathSearchListBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}