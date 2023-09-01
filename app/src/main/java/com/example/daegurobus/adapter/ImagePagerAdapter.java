package com.example.daegurobus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.daegurobus.R;
import com.example.daegurobus.model.NoticeList;

public class ImagePagerAdapter extends BasePagerAdapter<NoticeList> {
    public ImagePagerAdapter(Context context, LayoutInflater layoutInflater) {
        super(context, layoutInflater);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.common_image_pager, null);

        ImageView iv = (ImageView) view.findViewById(R.id.imageView);

        int index = position % getItems().size();

        final NoticeList item = items.get(index);

        Glide.with(context)
                .load(item.getNoticeLinkUrl())
                .placeholder(R.drawable.common_image_default_1_1)
                .error(R.drawable.common_image_default_1_1)
                .centerCrop()
                .into(iv);

        view.setOnClickListener(view1 -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(view1, position);
            }
        });

        container.addView(view);
        return view;
    }

    @Override
    public NoticeList getItem(int position) {
        // RealIndex와 ViewIndex의 차이 때문에(ex. RealIndex = 2500, ViewIndex = 5. IndexOutOfBoundsException)
        return super.getItem(position % getItems().size());
    }

    @Override
    public int getCount() {
        if (getItems() == null) {
            return 0;
        } else {
            if (getItems().size() > 1) {
                return AutoScrolledViewPager.IMAGE_ADAPTER_LOOP_COUNT * getItems().size();
            } else {
                return getItems().size();
            }
        }
    }
}
