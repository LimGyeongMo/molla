package com.example.daegurobus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daegurobus.R;
import com.example.daegurobus.databinding.CommonListKeywordBinding;

public class CommonKeywordAdapter extends BaseRecyclerViewAdapter<String, CommonKeywordAdapter.ViewHolder> {
    public CommonKeywordAdapter(Context context) {
        super(context);
    }

    @Override
    public void onBindView(ViewHolder holder, int position) {
        String keyword = getItem(position);
        holder.binding.text.setText("#" + keyword);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.common_list_keyword, parent, false));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CommonListKeywordBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}