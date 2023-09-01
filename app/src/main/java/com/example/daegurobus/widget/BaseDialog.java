package com.example.daegurobus.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.example.daegurobus.util.BasicUtil;

public class BaseDialog extends AlertDialog {
    protected BaseDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setSideMargin(18);
    }

    protected void setSideMargin(int sideMargin) {
        DisplayMetrics dm = getContext().getApplicationContext().getResources().getDisplayMetrics();
        float widthDp = BasicUtil.pxToDp(getContext(), dm.widthPixels);
        getWindow().setLayout((int) BasicUtil.dpToPx(getContext(), widthDp - (sideMargin * 2)), LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    protected void showKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInputFromInputMethod(view.getWindowToken(), InputMethodManager.SHOW_FORCED);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        view.requestFocus();
    }

    protected void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }
}