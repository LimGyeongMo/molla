package com.example.daegurobus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daegurobus.R;
import com.example.daegurobus.databinding.BusListSearchBinding;
import com.example.daegurobus.model.BusResultSearch;

public class RecentKeywordAdapter extends BaseRecyclerViewAdapter<BusResultSearch, RecentKeywordAdapter.ViewHolder> {
    public RecentKeywordAdapter(Context context) {
        super(context);
    }

    @Override
    public void onBindView(ViewHolder holder, int position) {
        BusResultSearch item = getItem(position);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.bus_list_search, parent, false));


    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private BusListSearchBinding binding;

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
