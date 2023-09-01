package com.example.daegurobus.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.databinding.DataBindingUtil;

import com.example.daegurobus.R;
import com.example.daegurobus.databinding.CommonClearEditTextBinding;

public class CommonClearEditText extends RelativeLayout {
    public static final int STATE_FOCUS_IN = 1;
    public static final int STATE_FOCUS_OUT = 2;
    public static final int STATE_ERROR = 3;
    public static final int STATE_DISABLE = 4;

    public boolean isUseClear;
    public boolean isUseTextLimit;
    public boolean isInputBlock;

    private CommonClearEditTextBinding binding;

    private int state = STATE_FOCUS_IN;
    private int maxLength;

    public CommonClearEditText(Context context) {
        super(context);
        initView();
    }

    public CommonClearEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        getAttrs(attrs);
    }

    public CommonClearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        getAttrs(attrs, defStyleAttr);
    }

    private void initView() {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.common_clear_edit_text, null, false);
        addView(binding.getRoot());

        binding.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.editText.setOnFocusChangeListener(onFocusChangeListener);

        binding.clear.setOnClickListener(view -> {
            binding.editText.setText("");
            binding.clear.setVisibility(GONE);
        });
        binding.clear.setVisibility(GONE);

        binding.iconRight1.setVisibility(GONE);
    }

    public CommonClearEditTextBinding getBinding() {
        return binding;
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CommonEditText);
        setTypeArray(typedArray);
    }

    private void getAttrs(AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CommonEditText, defStyleAttr, 0);
        setTypeArray(typedArray);
    }

    private void setTypeArray(TypedArray typeArray) {
        String text = typeArray.getString(R.styleable.CommonEditText_text);
        binding.editText.setText(text);

        String hint = typeArray.getString(R.styleable.CommonEditText_hint);
        binding.editText.setHint(hint);

        int imeOptions = typeArray.getInt(R.styleable.CommonEditText_imeOptions, EditorInfo.IME_ACTION_DONE);
        binding.editText.setImeOptions(imeOptions);

        setMaxLength(typeArray.getInt(R.styleable.CommonEditText_maxLength, 100));

        isUseTextLimit = typeArray.getBoolean(R.styleable.CommonEditText_is_use_text_limit, true);

        if (!isUseTextLimit) {
            binding.tvTextLimit.setVisibility(GONE);
        }

        Drawable iconRight1 = typeArray.getDrawable(R.styleable.CommonEditText_icon_right1);

        if (iconRight1 != null) {
            binding.iconRight1.setImageDrawable(iconRight1);
            binding.iconRight1.setVisibility(VISIBLE);
        }

        isUseClear = typeArray.getBoolean(R.styleable.CommonEditText_is_use_clear, true);

        if (!isUseClear) {
            binding.clear.setVisibility(GONE);
        }

        int lines = typeArray.getInt(R.styleable.CommonEditText_lines, 1);
        binding.editText.setLines(lines);

        if (lines > 1) {
            binding.editText.setMaxLines(lines * 2);
            binding.editText.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
            binding.editText.setVerticalScrollBarEnabled(true);
            binding.editText.setHorizontallyScrolling(false);
        }

        int gravity = typeArray.getInt(R.styleable.CommonEditText_gravity, Gravity.NO_GRAVITY);
        binding.editText.setGravity(gravity);

        int inputType = typeArray.getInt(R.styleable.CommonEditText_inputType, InputType.TYPE_CLASS_TEXT);
        binding.editText.setInputType(inputType);

        String digits = typeArray.getString(R.styleable.CommonEditText_digits);

        if (digits != null) {
            binding.editText.setKeyListener(DigitsKeyListener.getInstance(digits));
        }

        String privateImeOptions = typeArray.getString(R.styleable.CommonEditText_privateImeOptions);
        binding.editText.setPrivateImeOptions(privateImeOptions);

        isInputBlock = typeArray.getBoolean(R.styleable.CommonEditText_is_input_block, false);

        if (isInputBlock) {
            binding.wrapper.setVisibility(VISIBLE);
        }

        int state = typeArray.getInt(R.styleable.CommonEditText_state, STATE_FOCUS_OUT);
        setState(state);

        typeArray.recycle();
    }

//    public void setTelMode(){
//        binding.text.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                binding.tvTextLimit.setText("(" + binding.text.getText().length() + "/" + maxLength + ")");
//
//                String hyphenNumber = phoneNumberHyphenAdd(s.toString());
//
//                if (!s.toString().equals(hyphenNumber)) {
//                    holder.binding.telNo.getEditText().setText(hyphenNumber);
//                    holder.binding.telNo.getEditText().setSelection(holder.binding.telNo.getEditText().length());
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;

        binding.editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        binding.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.tvTextLimit.setText("(" + binding.editText.getText().length() + "/" + maxLength + ")");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public String getEditTextString() {
        return binding.editText.getText().toString();
    }

    public void setEditTextString(String str) {
        binding.editText.setText(str);
        binding.tvTextLimit.setVisibility(GONE);
        binding.clear.setVisibility(GONE);  // setString 후에 clear버튼이 나타난다.
    }

    public EditText getEditText() {
        return binding.editText;
    }

    public void showClearIcon() {
        if (isUseClear) {
            binding.clear.setVisibility(VISIBLE);
        }
    }

    public ImageView getIconRight1() {
        return binding.iconRight1;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;

//        switch (state) {
//            case STATE_FOCUS_IN:
//                binding.round.setBackground(getContext().getDrawable(R.drawable.bg_round_line_6_01caff_ffffff));
//                binding.editText.setTextColor(getContext().getColor(R.color.color_333333));
//                binding.editText.setOnFocusChangeListener(onFocusChangeListener);
//                binding.editText.setEnabled(true);
//                break;
//
//            case STATE_FOCUS_OUT:
//                binding.round.setBackground(getContext().getDrawable(R.drawable.bg_round_area_6_999999_ffffff));
//                binding.editText.setTextColor(getContext().getColor(R.color.color_333333));
//                binding.editText.setOnFocusChangeListener(onFocusChangeListener);
//                binding.editText.setEnabled(true);
//                break;
//
//            case STATE_ERROR:
//                binding.round.setBackground(getContext().getDrawable(R.drawable.bg_round_area_6_e30613_ffffff));
//                binding.editText.setTextColor(getContext().getColor(R.color.color_333333));
//                binding.editText.setOnFocusChangeListener(onFocusChangeListener);
//                binding.editText.setEnabled(true);
//                break;
//
//            case STATE_DISABLE:
//                binding.round.setBackground(getContext().getDrawable(R.drawable.bg_round_area_6_999999_f2f2f2));
//                binding.editText.setTextColor(getContext().getColor(R.color.color_999999));
//                binding.editText.setOnFocusChangeListener(null);
//                binding.editText.setEnabled(false);
//
//                binding.tvTextLimit.setVisibility(GONE);
//                binding.clear.setVisibility(GONE);
//                break;
//        }
    }

    public void textChanged() {
        if (binding.editText.getText().toString().length() > 0) {
            if (isUseTextLimit) {
                binding.tvTextLimit.setVisibility(VISIBLE);
            }

            binding.tvTextLimit.setText("(" + binding.editText.getText().length() + "/" + maxLength + ")");

            if (isUseClear) {
                binding.clear.setVisibility(VISIBLE);
            }
        } else {
            binding.tvTextLimit.setVisibility(GONE);
            binding.clear.setVisibility(GONE);
        }

        setState(CommonClearEditText.STATE_FOCUS_IN);
        binding.error.setVisibility(View.GONE);
        binding.error.setText("");
    }

    public void setError(String message) {
        setState(CommonClearEditText.STATE_ERROR);
        binding.error.setVisibility(View.VISIBLE);
        binding.error.setText(message);
    }

    public void setErrorClear() {
        setState(CommonClearEditText.STATE_FOCUS_OUT);
        binding.error.setVisibility(View.GONE);
        binding.error.setText("");
    }

    public void setLayoutByFocusedState() {
        if (binding.editText.isFocused()) {
            setState(CommonClearEditText.STATE_FOCUS_IN);
        } else {
            setState(CommonClearEditText.STATE_FOCUS_OUT);
        }

        binding.tvTextLimit.setVisibility(View.GONE);
        binding.error.setVisibility(View.GONE);
    }

    public void setWrapperClickListener(OnClickListener onClickListener) {
        binding.wrapper.setOnClickListener(onClickListener);
    }

    private OnFocusChangeListener onFocusChangeListener = (view, b) -> {
        // 에러, 비활성 상태일때는 포커스리스너 호출하지 않기 위해
        switch (state) {
            case STATE_FOCUS_IN:
            case STATE_FOCUS_OUT:
                if (b) {
                    setState(STATE_FOCUS_IN);

                    if (binding.editText.getText().toString().length() > 0) {
                        if (isUseTextLimit) {
                            binding.tvTextLimit.setVisibility(VISIBLE);
                        }

                        if (isUseClear) {
                            binding.clear.setVisibility(VISIBLE);
                        }
                    }
                } else {
                    setState(STATE_FOCUS_OUT);
                    binding.tvTextLimit.setVisibility(GONE);
                    binding.clear.setVisibility(GONE);
                }

                break;
        }
    };
}