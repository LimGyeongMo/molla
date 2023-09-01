package com.example.daegurobus.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import androidx.databinding.DataBindingUtil;

import com.example.daegurobus.R;
import com.example.daegurobus.databinding.CommonMainTabBinding;

public class CommonMainTab extends RelativeLayout {
    private CommonMainTabBinding binding;

    public CommonMainTab(Context context) {
        super(context);
        initView();
    }

    public CommonMainTab(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        getAttrs(attrs);
    }

    public CommonMainTab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        getAttrs(attrs, defStyleAttr);
    }

    private void initView() {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.common_main_tab, null, false);
        addView(binding.getRoot());
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CommonMainTab);
        setTypeArray(typedArray);
    }

    private void getAttrs(AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CommonMainTab, defStyleAttr, 0);
        setTypeArray(typedArray);
    }

    private void setTypeArray(TypedArray typeArray) {
        String text = typeArray.getString(R.styleable.CommonMainTab_text);
        binding.text.setText(text);

        Drawable iconRes = typeArray.getDrawable(R.styleable.CommonMainTab_icon_res);

        if (iconRes != null) {
            binding.icon.setImageDrawable(iconRes);
        }

        typeArray.recycle();
    }

    public void select() {
        binding.icon.setColorFilter(getContext().getColor(R.color.color_01CAFF));
        binding.text.setTextColor(getContext().getColor(R.color.color_01CAFF));
    }

    public void unSelect() {
        binding.icon.setColorFilter(getContext().getColor(R.color.color_333333));
        binding.text.setTextColor(getContext().getColor(R.color.color_333333));
    }

    public void setBadgeVisibility(int visibility) {
        binding.badge.setVisibility(visibility);
    }
}