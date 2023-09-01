package com.example.daegurobus.util;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.example.daegurobus.R;
import com.example.daegurobus.databinding.CommonToastBinding;

public class CommonToastUtil {
    private static Toast toast;

    public static void showToast(Context context, LayoutInflater inflater, String message) {
        CommonToastBinding binding = DataBindingUtil.inflate(inflater, R.layout.common_toast, null, false);

        if (toast != null) {
            toast.cancel();
        }

        toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(binding.getRoot());
        binding.tvMessage.setText(message.replace(" ", "\u00A0"));
        toast.show();
    }

    public static float dpToPx(Context context, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}