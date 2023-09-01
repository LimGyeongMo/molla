package com.example.daegurobus;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.daegurobus.databinding.CommonLoadingBinding;
import com.example.daegurobus.app.MyPreferencesManager;

import com.example.daegurobus.network.NetworkPresenter;
import com.example.daegurobus.util.CommonToastUtil;


public class BaseActivity extends AppCompatActivity {
    protected String projectType;

    protected MyPreferencesManager myPreferencesManager;
    protected NetworkPresenter commonNetworkPresenter;
    protected com.example.daegurobus.network.NetworkPresenter busNetworkPresenter;
    protected com.example.daegurobus.network.naver.NetworkPresenter naverNetworkPresenter;

    public boolean isBackpressedLock = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialize();
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!isBackpressedLock) {
            super.onBackPressed();
        }
    }

    private void initialize() {

        busNetworkPresenter = new com.example.daegurobus.network.NetworkPresenter(this);
        naverNetworkPresenter= new com.example.daegurobus.network.naver.NetworkPresenter(this);
        myPreferencesManager = MyPreferencesManager.getInstance(this);
    }
    protected void showToast(String message) {
        CommonToastUtil.showToast(this, getLayoutInflater(), message);
    }

    protected void requestFocus(View view) {
        view.clearFocus();
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }

    protected void showKeyboard(View view) {
        requestFocus(view);

        new Handler().postDelayed(() -> {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }, 50);
    }

    protected void hideKeyboard(View view) {
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }



    protected void showLoadingDark(CommonLoadingBinding binding) {
        binding.loViewDark.setVisibility(View.VISIBLE);
        isBackpressedLock = true;
    }

    protected void showLoading(CommonLoadingBinding binding) {
        binding.loView.setVisibility(View.VISIBLE);
        isBackpressedLock = true;
    }

    protected void hideLoadingAll(CommonLoadingBinding binding) {
        binding.loView.setVisibility(View.GONE);
        binding.loViewDark.setVisibility(View.GONE);
        isBackpressedLock = false;
    }

    public void tabUnSelect(TextView textView, View view, int textColorId) {
        textView.setTypeface(ResourcesCompat.getFont(this, R.font.font_noto_sans_cj_kkr_medium));
        textView.setTextColor(getColor(textColorId));
        view.setVisibility(View.INVISIBLE);
    }

    public void tabUnSelect(TextView textView, View view) {
        tabUnSelect(textView, view, R.color.color_999999);
    }

    public void tabSelect(TextView textView, View view, int textColorId) {
        textView.setTypeface(ResourcesCompat.getFont(this, R.font.font_noto_sans_cj_kkr_bold));
        textView.setTextColor(getColor(textColorId));
        view.setVisibility(View.VISIBLE);
    }

    public void tabSelect(TextView textView, View view) {
        tabSelect(textView, view, R.color.color_01CAFF);
    }

    public String getProjectType() {
        return projectType;
    }
}