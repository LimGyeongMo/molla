package com.example.daegurobus.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.daegurobus.app.MyPreferencesManager;
import com.example.daegurobus.databinding.CommonLoadingBinding;
import com.example.daegurobus.BaseActivity;
import com.example.daegurobus.util.CommonToastUtil;
import com.example.daegurobus.util.LogUtil;
import com.example.daegurobus.network.NetworkPresenter;

public class BaseFragment extends Fragment {
    protected String projectType;

    protected com.example.daegurobus.network.naver.NetworkPresenter naverNetworkPresenter;
    protected com.example.daegurobus.network.kakao.NetworkPresenter kakaoNetworkPresenter;

    protected MyPreferencesManager myPreferencesManager;
    protected NetworkPresenter networkPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myPreferencesManager = MyPreferencesManager.getInstance(getContext());
        networkPresenter = new NetworkPresenter(getContext());
        naverNetworkPresenter= new com.example.daegurobus.network.naver.NetworkPresenter(getContext());
        kakaoNetworkPresenter = new com.example.daegurobus.network.kakao.NetworkPresenter(getContext());

    }

    protected void showToast(String message) {
        try {
            LogUtil.i(getContext() + " " + getLayoutInflater() + " " + message);
            CommonToastUtil.showToast(getContext(), getLayoutInflater(), message);
        } catch (Exception e) {

        }
    }

//    protected void showToast(String message) {
//        try {
//            LogUtil.i(getContext() + " " + getLayoutInflater() + " " + message);
//            CommonToastUtil.showToast(getContext(), getLayoutInflater(), message);
//        } catch (Exception e) {
//
//        }
//    }

    /**
     * 뷰 포커스 요청
     **/
    protected void requestFocus(View view) {
        view.clearFocus();
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }

    /**
     * 키보드 보이기
     **/
    public void showKeyboard(View view) {
        InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * 키보드 숨기기
     **/
    public void hideKeyboard(View view) {
        InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

//    protected void appRestart() {
//        Intent intent = new Intent(getContext(), CommonSplashActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//    }

//    protected void showAuthExpiredDialog() {
//        new CommonNoticeDialog(getContext())
//                .setCancelPossible(false)
//                .setShowNegativeButton(false)
//                .setMessage("인증이 만료되어서 앱을 재실행 합니다.")
//                .setCallbackListener(new CommonNoticeDialog.CallbackListener() {
//                    @Override
//                    public void positive() {
//                        appRestart();
//                    }
//
//                    @Override
//                    public void negative() {
//
//                    }
//                }).show();
//    }

    protected void showLoadingDark(CommonLoadingBinding binding) {
        binding.loViewDark.setVisibility(View.VISIBLE);
        ((BaseActivity) getActivity()).isBackpressedLock = true;
    }

    protected void showLoading(CommonLoadingBinding binding) {
        binding.loView.setVisibility(View.VISIBLE);
        ((BaseActivity) getActivity()).isBackpressedLock = true;
    }

    protected void hideLoadingAll(CommonLoadingBinding binding) {
        try {
            binding.loView.setVisibility(View.GONE);
            binding.loViewDark.setVisibility(View.GONE);
            ((BaseActivity) getActivity()).isBackpressedLock = false;
        } catch (Exception e) {
            LogUtil.i(e.toString());
        }
    }
}