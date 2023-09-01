package com.example.daegurobus.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import com.example.daegurobus.R;
import com.example.daegurobus.databinding.CommonRowTextBinding;

public class CommonRowText extends RelativeLayout {
    private CommonRowTextBinding binding;

    public CommonRowText(Context context) {
        super(context);
        initView();
    }

    public CommonRowText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        getAttrs(attrs);
    }

    public CommonRowText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        getAttrs(attrs, defStyleAttr);
    }

    private void initView() {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.common_row_text, null, false);
        addView(binding.getRoot());
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CommonRowText);
        setTypeArray(typedArray);
    }

    private void getAttrs(AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CommonRowText, defStyleAttr, 0);
        setTypeArray(typedArray);
    }

    private void setTypeArray(TypedArray typeArray) {
        String text = typeArray.getString(R.styleable.CommonRowText_text);
        binding.text.setText(text);

        String textSub = typeArray.getString(R.styleable.CommonRowText_text_sub);

        if (textSub != null) {
            binding.textSub.setVisibility(VISIBLE);
            binding.textSub.setText(textSub);
        }

        Drawable iconRes = typeArray.getDrawable(R.styleable.CommonRowText_icon_res);

        if (iconRes != null) {
            binding.rightIcon.setImageDrawable(iconRes);
        }

        int bottomLineVisibility = typeArray.getInt(R.styleable.CommonRowText_bottom_line_visibility, View.VISIBLE);
        binding.vBottomLine.setVisibility(bottomLineVisibility);

        typeArray.recycle();
    }

    public TextView getTextSub(){
        return binding.textSub;
    }

    public String getTextString() {
        return binding.text.getText().toString();
    }

    public void setTextString(String str) {
        binding.text.setText(str);
    }
}