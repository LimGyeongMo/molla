package com.example.daegurobus.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.example.daegurobus.R;
import com.example.daegurobus.databinding.BusFragmentMainInquiryBinding;
import com.example.daegurobus.util.BasicUtil;

public class BusInquiryFragment extends BusBaseFragment{


    private BusFragmentMainInquiryBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bus_fragment_main_inquiry, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding = BusFragmentMainInquiryBinding.bind(getView());


        initLayout();
    }

    private void initLayout() {

        binding.frequentlyAskedQuestions.setOnClickListener(view -> {
            switch (projectType) {

            }
        });

        binding.kakaoChannel.setOnClickListener(view -> BasicUtil.launchWebsite(getContext(), getString(R.string.kakao_channel_link_customer_center)));

        binding.customerCounselingCenter.setTextString(getString(R.string.customer_counseling_center) + " 1661-3773"  /*myPreferencesManager.getOperationInfo().getTelNo()*/);
        binding.customerCounselingCenter.getTextSub().setText("운영시간: 10:00 ~ 20:00 (일요일, 공휴일 휴무)" /*myPreferencesManager.getOperationInfo().getOperationTime()*/);
        binding.customerCounselingCenter.setOnClickListener(view -> {
            try {
                BasicUtil.sendCall(getContext(), getString(R.string.customer_center_tel));
            } catch (Exception e) {
                showToast("전화걸기 실패: " + e.toString());
            }
        });
    }

}
