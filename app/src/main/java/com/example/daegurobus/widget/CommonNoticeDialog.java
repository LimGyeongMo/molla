package com.example.daegurobus.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.example.daegurobus.R;
import com.example.daegurobus.databinding.CommonDialogNoticeBinding;
import com.example.daegurobus.util.BasicUtil;
import com.example.daegurobus.util.CustomRoundedCornersTransformation;
import com.example.daegurobus.util.LogUtil;

public class CommonNoticeDialog extends BaseDialog {
    private CommonDialogNoticeBinding binding;

    private boolean isCancelable = true;

    private Object img;
    private String linkUrl;
    private String title;
    private String message;
    private boolean isShowNegativeButton = true;

    private String positiveText;
    private int positiveTextColor;

    private String negativeText;

    private CallbackListener callbackListener;

    // 디버그 모드 접속 시 사용
    private int titleTouchCount = 0;
    private int contentsTouchCount = 0;
    private DebugCallbackListener debugCallbackListener;

    public CommonNoticeDialog(Context context) {
        super(context);
    }

    public CommonNoticeDialog setCancelPossible(boolean b) {
        this.isCancelable = b;
        return this;
    }

    public CommonNoticeDialog setImg(Object img) {
        if (img instanceof String) {
            String imageUrl = (String) img;

            if (imageUrl.length() == 0) {
                img = null;
            }
        }

        this.img = img;
        return this;
    }

    public CommonNoticeDialog setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
        return this;
    }

    public CommonNoticeDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public CommonNoticeDialog setMessage(String message) {
        this.message = message;
        return this;
    }

    public CommonNoticeDialog setShowNegativeButton(boolean b) {
        this.isShowNegativeButton = b;
        return this;
    }

    public CommonNoticeDialog setPositiveText(String text) {
        this.positiveText = text;
        return this;
    }

    public CommonNoticeDialog setNegativeText(String text) {
        this.negativeText = text;
        return this;
    }

    public CommonNoticeDialog setPositiveTextColor(int color) {
        this.positiveTextColor = color;
        return this;
    }

    public CommonNoticeDialog setCallbackListener(CallbackListener callbackListener) {
        this.callbackListener = callbackListener;
        return this;
    }

    public CommonNoticeDialog setDebugCallbackListener(DebugCallbackListener debugCallbackListener) {
        this.debugCallbackListener = debugCallbackListener;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.common_dialog_notice, null, false);
        setContentView(binding.getRoot());

        setCancelable(isCancelable);

        if (img == null) {
            binding.ivImage.setVisibility(View.GONE);
        } else {
            if (img instanceof String) {
                Glide.with(getContext())
                        .load(img)
                        .error(R.drawable.common_image_default_1_1)
                        .transform(new MultiTransformation<>(new CenterInside(), new CustomRoundedCornersTransformation(getContext(), (int) BasicUtil.dpToPx(getContext(), 6), 0, CustomRoundedCornersTransformation.CornerType.TOP)))
                        .into(binding.ivImage);
            } else if (img instanceof Drawable) {
                binding.ivImage.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        if (binding.ivImage.getMeasuredWidth() > 0) {
                            binding.ivImage.getViewTreeObserver().removeOnPreDrawListener(this);

                            int height = ((Drawable) img).getIntrinsicHeight();
                            int width = ((Drawable) img).getIntrinsicWidth();
                            int resetHeight = (int) (height * ((double) binding.ivImage.getMeasuredWidth() / width));

                            // 이미지 비율에 맞게 이미지뷰 높이 설정
                            ViewGroup.LayoutParams layoutParams = binding.ivImage.getLayoutParams();
                            layoutParams.height = resetHeight;
                            binding.ivImage.setLayoutParams(layoutParams);

                            Glide.with(getContext())
                                    .load(img)
                                    .error(R.drawable.common_image_default_1_1)
                                    .transform(new MultiTransformation<>(new CenterInside(), new CustomRoundedCornersTransformation(getContext(), (int) BasicUtil.dpToPx(getContext(), 6), 0, CustomRoundedCornersTransformation.CornerType.TOP)))
                                    .into(binding.ivImage);
                        }

                        return false;
                    }
                });
            }

            binding.ivImage.setClipToOutline(true);
            binding.ivImage.setOnClickListener(view -> {
                if (linkUrl != null && linkUrl.length() > 0) {
                    BasicUtil.launchWebsite(getContext(), linkUrl);
                }
            });
        }

        if (title == null || title.length() == 0) {
            binding.tvTitle.setVisibility(View.GONE);
        } else {
            binding.tvTitle.setText(title);

            if (debugCallbackListener != null) {
                binding.tvTitle.setOnClickListener(v -> {
                    titleTouchCount++;

                    debugModeCheck();
                });
            }
        }

        if (message == null || message.length() == 0) {
            binding.tvMessage.setVisibility(View.GONE);
        } else {
            binding.tvMessage.setText(message.replace(" ", "\u00A0"));

            if (debugCallbackListener != null) {
                binding.tvMessage.setOnClickListener(v -> {
                    contentsTouchCount++;

                    debugModeCheck();
                });
            }
        }

        if ((title == null || title.length() == 0) && (message == null || message.length() == 0)) {
            binding.loMessage.setVisibility(View.GONE);
        }

        if (positiveText != null) {
            binding.tvPositive.setText(positiveText);
        }

        if (positiveTextColor != 0) {
            binding.tvPositive.setTextColor(positiveTextColor);
        }

        if (negativeText != null) {
            binding.tvNegative.setText(negativeText);
        }

        binding.tvPositive.setOnClickListener(v -> {
            if (callbackListener != null) {
                callbackListener.positive();
            }

            dismiss();
        });

        if (isShowNegativeButton) {
            binding.tvNegative.setOnClickListener(v -> {
                if (callbackListener != null) {
                    callbackListener.negative();
                }

                dismiss();
            });
        } else {
            binding.tvNegative.setVisibility(View.GONE);
            binding.viewDivider.setVisibility(View.GONE);
        }
    }

    private void debugModeCheck() {
        LogUtil.i(titleTouchCount + " " + contentsTouchCount);

        if (titleTouchCount == 3 && contentsTouchCount == 7) {
            debugCallbackListener.debug();
        }
    }

    public interface CallbackListener {
        void positive();

        void negative();
    }

    public interface DebugCallbackListener {
        void debug();
    }
}