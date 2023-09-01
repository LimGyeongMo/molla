package com.example.daegurobus.widget;


import androidx.databinding.DataBindingUtil;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.daegurobus.R;
import com.example.daegurobus.databinding.BusTitleViewBinding;

public class BusTitleView extends RelativeLayout {
    private BusTitleViewBinding binding;

    public BusTitleView(Context context) {
        super(context);
        initView();
    }

    public BusTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        getAttrs(attrs);
    }

    public BusTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        getAttrs(attrs, defStyleAttr);
    }

    private void initView() {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.bus_title_view, null, false);
        addView(binding.getRoot());
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.BusTitleView);
        setTypeArray(typedArray);
    }

    private void getAttrs(AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.BusTitleView, defStyleAttr, 0);
        setTypeArray(typedArray);
    }


    private void setTypeArray(TypedArray typeArray) {
        String text = typeArray.getString(R.styleable.BusTitleView_text);

        if (text != null) {
            binding.busTitle.setText(text);
        }

        Drawable iconLeft1 = typeArray.getDrawable(R.styleable.BusTitleView_icon_bus_left1);

        if (iconLeft1 != null) {
            binding.ivBusLeft1.setImageDrawable(iconLeft1);
        }

        Drawable iconRight1 = typeArray.getDrawable(R.styleable.BusTitleView_icon_right1);

        if (iconRight1 != null) {
            binding.ivBusRight1.setImageDrawable(iconRight1);
        }

        Drawable iconRight2 = typeArray.getDrawable(R.styleable.BusTitleView_icon_right2);

        if (iconRight2 != null) {
            binding.ivBusRight2.setImageDrawable(iconRight2);
            binding.loBusRight2.setVisibility(VISIBLE);
        }

        int bottomLineVisibility = typeArray.getInt(R.styleable.BusTitleView_bottom_line_visibility, View.VISIBLE);
        binding.vBusBottomLine.setVisibility(bottomLineVisibility);


        typeArray.recycle();
    }

    public BusTitleViewBinding getBinding() {return binding;
    }

    public TextView getTitle() {return binding.busTitle;}

    public ImageView getIconLeft1() {return binding.ivBusLeft1;}

    public ImageView getIconRight1() { return binding.ivBusRight1;}

    public ImageView getIconRight2() {
        return binding.ivBusRight2;
    }

    public void setIconRight1(Drawable drawable) {
        binding.ivBusRight1.setImageDrawable(drawable);
    }

    public void setIconRight2(Drawable drawable) {
        binding.ivBusRight2.setImageDrawable(drawable);
    }

    public View getBottomLine() {
        return binding.vBusBottomLine;
    }


}
