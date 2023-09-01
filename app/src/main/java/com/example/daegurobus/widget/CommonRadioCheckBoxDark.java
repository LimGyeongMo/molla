package com.example.daegurobus.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.databinding.DataBindingUtil;

import com.example.daegurobus.R;
import com.example.daegurobus.databinding.CommonRadioCheckBoxBlackBinding;


public class
CommonRadioCheckBoxDark extends RelativeLayout {
        private CommonRadioCheckBoxBlackBinding binding;

        public static final int STATE_DISABLE = -1;
        public static final int STATE_UN_CHECK = 0;
        public static final int STATE_CHECK = 1;

        private int state;

        private boolean isChecked;

    public CommonRadioCheckBoxDark(Context context) {
            super(context);
            initView();
        }

    public CommonRadioCheckBoxDark(Context context, AttributeSet attrs) {
            super(context, attrs);
            initView();
            getAttrs(attrs);
        }

    public CommonRadioCheckBoxDark(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            initView();
            getAttrs(attrs, defStyleAttr);
        }

        private void initView() {
            binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.common_radio_check_box_black, null, false);
            addView(binding.getRoot());

            setUnCheck();
        }

        private void getAttrs(AttributeSet attrs) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CommonRadioCheckBox);
            setTypeArray(typedArray);
        }

        private void getAttrs(AttributeSet attrs, int defStyleAttr) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CommonRadioCheckBox, defStyleAttr, 0);
            setTypeArray(typedArray);
        }

        private void setTypeArray(TypedArray typeArray) {
            String text = typeArray.getString(R.styleable.CommonRadioCheckBox_text);

            if (text == null) {
                binding.text.setVisibility(GONE);
            } else {
                binding.text.setText(text);
            }

            Drawable drawable = typeArray.getDrawable(R.styleable.CommonRadioCheckBox_text_icon);

            if (drawable != null) {
                binding.logo.setImageDrawable(drawable);
            }

            int checkState = typeArray.getInt(R.styleable.CommonRadioCheckBox_checkState, STATE_UN_CHECK);
            setState(checkState);

            typeArray.recycle();
        }

        public void setText(String text) {
            binding.text.setText(text);
        }

        public void setCheck() {
            isChecked = true;
            binding.form.setEnabled(true);

            binding.circleLine.getForeground().setColorFilter(getContext().getColor(R.color.color_01CAFF), PorterDuff.Mode.SRC_IN);
            binding.circleLine.getBackground().setColorFilter(getContext().getColor(R.color.color_333333), PorterDuff.Mode.SRC_IN);
            binding.circleDot.setVisibility(View.VISIBLE);
            binding.text.setTextColor(getContext().getColor(R.color.color_FFFFFF));
        }

        public void setCheck(int nameColor) {
            setCheck();
            binding.text.setTextColor(nameColor);
        }

        public void setUnCheck() {
            isChecked = false;
            binding.form.setEnabled(true);

            binding.circleLine.getForeground().setColorFilter(getContext().getColor(R.color.color_E6E6E6), PorterDuff.Mode.SRC_IN);
            binding.circleLine.getBackground().setColorFilter(getContext().getColor(R.color.color_333333), PorterDuff.Mode.SRC_IN);
            binding.circleDot.setVisibility(View.GONE);
            binding.text.setTextColor(getContext().getColor(R.color.color_FFFFFF));
        }

        public void setDisable() {
            isChecked = false;
            binding.form.setEnabled(false);

            binding.circleLine.getForeground().setColorFilter(getContext().getColor(R.color.color_E6E6E6), PorterDuff.Mode.SRC_IN);
            binding.circleLine.getBackground().setColorFilter(getContext().getColor(R.color.color_F2F2F2), PorterDuff.Mode.SRC_IN);
            binding.circleDot.setVisibility(View.GONE);
            binding.text.setTextColor(getContext().getColor(R.color.color_FFFFFF));
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setState(int state) {
            this.state = state;

            switch (this.state) {
                case STATE_DISABLE:
                    setDisable();
                    break;

                case STATE_UN_CHECK:
                    setUnCheck();
                    break;

                case STATE_CHECK:
                    setCheck();
                    break;
            }
        }

    }
