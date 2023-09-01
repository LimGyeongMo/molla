package com.example.daegurobus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.daegurobus.adapter.BusMainPagerAdapter;
import com.example.daegurobus.adapter.CommonPagerAdapter;

import com.example.daegurobus.fragment.BusEventFragment;
import com.example.daegurobus.fragment.BusHomeFragment;
import com.example.daegurobus.databinding.BusActivityMainBinding;
import com.example.daegurobus.fragment.BusInquiryFragment;
import com.example.daegurobus.fragment.BusNoticelistFragment;
import com.example.daegurobus.widget.BusBaseActivity;


public class BusMainActivity extends BusBaseActivity {
    public BusActivityMainBinding binding;
    private BusHomeFragment fragment;
    private Handler handler = new Handler();
    private boolean isClickable = true;

    private BusMainPagerAdapter pagerAdapter;

    private String query = "1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.bus_activity_main);
        initLayout();

    }

    private void initLayout() {

        initViewpager();
        binding.titleView.getIconLeft1().setOnClickListener(view -> startActivity(new Intent(BusMainActivity.this, BusSearchInfoActivity.class)));
        binding.titleView.getIconRight1().setBackgroundResource(R.drawable.ic_search_36_dp_touch);
        binding.titleView.getIconRight1().setOnClickListener(view -> startActivity(new Intent(BusMainActivity.this, BusSearchInfoActivity.class)));
       //새로 고침 버튼
        binding.titleView.getIconRight2().setVisibility(View.VISIBLE);
        binding.titleView.getIconRight2().setImageResource(R.drawable.ic_refresh_20_pt);
        binding.titleView.getIconRight2().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClickable) {
                    fragment.refreshTab(query);

                    // 5초 동안 버튼 클릭을 방지
                    isClickable = false;
                    binding.titleView.getIconRight2().setEnabled(false);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isClickable = true;
                            binding.titleView.getIconRight2().setEnabled(true);
                        }
                    }, 5000);
                }
            }
        });
        initBottomTabLayout();
    }

    private void initBottomTabLayout() {
        binding.tabHome.setOnClickListener(view -> {
            setTabView(BusMainPagerAdapter.TAB_HOME);
        });
        binding.tabAnnouncement.setOnClickListener(view -> {
            setTabView(BusMainPagerAdapter.TAB_ANNOUNCEMENT);
        });
        binding.tabEvent.setOnClickListener(view -> {
            setTabView(BusMainPagerAdapter.TAB_EVENT);
        });
        binding.tabInquiry.setOnClickListener(view -> {
            setTabView(BusMainPagerAdapter.TAB_INQUIRY);
        });
    }

//    private View.OnClickListener onClickListener = view -> {
//        switch (view.getId()) {
//            case R.id.tab_home:
//
//                break;
//
//            case R.id.tab_announcement:
//                setTabView(BusMainPagerAdapter.TAB_ANNOUNCEMENT);
//                break;
//
//            case R.id.tab_event:
//                setTabView(BusMainPagerAdapter.TAB_EVENT);
//                break;
//
//            case R.id.tab_inquiry:
//                setTabView(BusMainPagerAdapter.TAB_INQUIRY);
//                break;
//        }
//    };

    public int tabNo;

    public void setTabView(int num) {
//        if (!myPreferencesManager.isLogined() && num == DeliveryMainPagerAdapter.TAB_BENEFIT) {
//            showToast(getString(R.string.service_need_login));
//            Intent intent = new Intent(DeliveryMainActivity.this, CommonLoginActivity.class);
//            startActivity(intent);
//            return;
//        }
        pagerAdapter.setCurrentPosition(num);


        binding.tabHome.unSelect();
        binding.tabAnnouncement.unSelect();
        binding.tabEvent.unSelect();
        binding.tabInquiry.unSelect();
        ;

        binding.viewPager.setCurrentItem(num);
        binding.titleView.getTitle().setVisibility(View.GONE);
        binding.titleView.getIconLeft1().setVisibility(View.INVISIBLE);
        binding.titleView.getIconRight1().setVisibility(View.INVISIBLE);
//        binding.vTitleBottomLine.setVisibility(View.GONE);

        tabNo = num;

        switch (num) {
            case BusMainPagerAdapter.TAB_HOME:

                binding.titleView.getIconLeft1().setVisibility(View.VISIBLE);
                binding.titleView.getBottomLine().setVisibility(View.VISIBLE);
                binding.titleView.getIconRight1().setVisibility(View.VISIBLE);
                binding.titleView.getIconRight1().setImageDrawable(ContextCompat.getDrawable(this, R.drawable.common_search_20));

                binding.tabHome.select();
                break;

            case BusMainPagerAdapter.TAB_ANNOUNCEMENT:
                binding.titleView.getTitle().setVisibility(View.VISIBLE);
                binding.titleView.getTitle().setText(getString(R.string.announcement));

                binding.tabAnnouncement.select();
//                ((BusNoticelistFragment) pagerAdapter.getItem(BusMainPagerAdapter.TAB_ANNOUNCEMENT)).pageSelected();
                break;

            case BusMainPagerAdapter.TAB_EVENT:
                binding.titleView.getTitle().setVisibility(View.VISIBLE);
                binding.titleView.getTitle().setText(getString(R.string.event));

                binding.tabEvent.select();
                ((BusEventFragment) pagerAdapter.getItem(BusMainPagerAdapter.TAB_EVENT)).pageSelected();
                break;

            case BusMainPagerAdapter.TAB_INQUIRY:
                binding.titleView.getTitle().setVisibility(View.VISIBLE);
                binding.titleView.getTitle().setText(getString(R.string.inquiry));
                binding.titleView.getBottomLine().setVisibility(View.VISIBLE);

                binding.titleView.getTitle().setVisibility(View.VISIBLE);

                binding.tabInquiry.select();
                break;
        }
    }

    private void initViewpager() {
        pagerAdapter = new BusMainPagerAdapter(getSupportFragmentManager());

        binding.viewPager.setOffscreenPageLimit(4);
        binding.viewPager.setPageMarginDrawable(R.color.white);
        binding.viewPager.setAdapter(pagerAdapter);
        binding.viewPager.setCurrentItem(BusMainPagerAdapter.TAB_HOME);

//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        fragment = new BusHomeFragment();
//        transaction.replace(R.id.viewPager, fragment);
//        transaction.commit();

    }

}

