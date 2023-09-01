package com.example.daegurobus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daegurobus.R;
import com.example.daegurobus.databinding.CommonListNoticeBinding;
import com.example.daegurobus.model.NoticeList;


import java.text.SimpleDateFormat;

public class CommonNoticeAdapter extends BaseRecyclerViewAdapter<NoticeList, CommonNoticeAdapter.ViewHolder> {
    private SimpleDateFormat beforeDateFormat;
    private SimpleDateFormat afterDateFormat;
    private String date;

    public CommonNoticeAdapter(Context context) {
        super(context);
        beforeDateFormat = new SimpleDateFormat("yyyyMMddhhmm");
        afterDateFormat = new SimpleDateFormat("yyyy.MM.dd");
    }

    @Override
    public void onBindView(ViewHolder holder, int position) {
        NoticeList item = getItem(position);

        holder.binding.tvName.setText(item.getNoticeTitle());

        try {
            date = afterDateFormat.format(beforeDateFormat.parse(item.getInsDate()));
        } catch (Exception e) {
            date = "format error";
        }

        holder.binding.tvDate.setText(date);

//        if (date.equals(DateUtil.getCurrentDateTime("yyyy.MM.dd"))) {
//            holder.binding.ivNew.setVisibility(View.VISIBLE);
//        } else {
//            holder.binding.ivNew.setVisibility(View.GONE);
//        }

//        holder.binding.ivNew.setVisibility("N".equals(item.getReadYn()) ? View.VISIBLE : View.GONE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.common_list_notice, parent, false));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CommonListNoticeBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}