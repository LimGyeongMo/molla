package com.example.daegurobus.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import com.example.daegurobus.R;
import com.example.daegurobus.databinding.CommonTitleViewBinding;


public class CommonTitleView extends RelativeLayout {
    private CommonTitleViewBinding binding;

    public CommonTitleView(Context context) {
        super(context);
        initView();
    }

    public CommonTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        getAttrs(attrs);
    }

    public CommonTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        getAttrs(attrs, defStyleAttr);
    }

    private void initView() {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.common_title_view, null, false);
        addView(binding.getRoot());
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CommonTitleView);
        setTypeArray(typedArray);
    }

    private void getAttrs(AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CommonTitleView, defStyleAttr, 0);
        setTypeArray(typedArray);
    }


    private void setTypeArray(TypedArray typeArray) {
        String text = typeArray.getString(R.styleable.CommonTitleView_text);

        if (text != null) {
            binding.title.setText(text);
        }

        Drawable iconLeft1 = typeArray.getDrawable(R.styleable.CommonTitleView_icon_left1);

        if (iconLeft1 != null) {
            binding.ivLeft1.setImageDrawable(iconLeft1);
        }

        Drawable iconRight1 = typeArray.getDrawable(R.styleable.CommonTitleView_icon_right1);

        if (iconRight1 != null) {
            binding.ivRight1.setImageDrawable(iconRight1);
        }

        Drawable iconRight2 = typeArray.getDrawable(R.styleable.CommonTitleView_icon_right2);

        if (iconRight2 != null) {
            binding.ivRight2.setImageDrawable(iconRight2);
            binding.loRight2.setVisibility(VISIBLE);
        }

        int bottomLineVisibility = typeArray.getInt(R.styleable.CommonTitleView_bottom_line_visibility, View.VISIBLE);
        binding.vBottomLine.setVisibility(bottomLineVisibility);

        typeArray.recycle();
    }

    public CommonTitleViewBinding getBinding(){
        return binding;
    }

    public TextView getTitle(){
        return binding.title;
    }

    public ImageView getIconLeft1(){
        return binding.ivLeft1;
    }

    public ImageView getIconRight1(){
        return binding.ivRight1;
    }

    public ImageView getIconRight2(){
        return binding.ivRight2;
    }

    public void setRight2Badge(int visibility){
        binding.ivRight2Badge.setVisibility(visibility);
    }

    public void setIconRight1(Drawable drawable){
        binding.ivRight1.setImageDrawable(drawable);
    }

    public void setIconRight2(Drawable drawable){
        binding.ivRight2.setImageDrawable(drawable);
    }

    public View getBottomLine(){
        return binding.vBottomLine;
    }
}