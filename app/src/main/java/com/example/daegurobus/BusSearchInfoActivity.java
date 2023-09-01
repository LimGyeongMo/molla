package com.example.daegurobus;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.InputType;
import android.view.KeyboardShortcutGroup;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.daegurobus.fragment.BusPathFragment;
import com.example.daegurobus.fragment.BusSearchFragment;
import com.example.daegurobus.fragment.BusStationFragment;
import com.example.daegurobus.adapter.CommonPagerAdapter;
import com.example.daegurobus.databinding.BusActivitySearchKeywordBinding;
import com.example.daegurobus.widget.BusBaseActivity;


public class BusSearchInfoActivity extends BusBaseActivity {

    private static final int TAB_BUS = 0;
    private static final int TAB_STATION = 1;
    private static final int TAB_FIND = 2;
    private int currentTabNo = TAB_BUS;

    private String Dash = "-";
    private BusActivitySearchKeywordBinding binding;

    private CommonPagerAdapter pagerAdapter;

    private Fragment[] fragments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.bus_activity_search_keyword);

        fragments = new Fragment[]{new BusSearchFragment(), new BusStationFragment(), new BusPathFragment()};

        initLayout();

    }


    private void initLayout() {
        binding.titleView.getIconLeft1().setOnClickListener(view -> finish());
        binding.titleView.getTitle();

        initTabLayout();
        initViewPager();
    }


    public void moveTab(int tabNo) {

        currentTabNo =tabNo;

        switch (tabNo) {
            case TAB_BUS:
                binding.loTabBus.callOnClick();
                break;

            case TAB_STATION:

                binding.loTabStation.callOnClick();
                break;
                // 경로찾기
            case TAB_FIND:
                binding.loTabPath.callOnClick();
                break;
        }
    }



    private void initTabLayout() {
        binding.loTabBus.setOnClickListener(v -> binding.viewPager.setCurrentItem(TAB_BUS));
        binding.loTabStation.setOnClickListener(v -> binding.viewPager.setCurrentItem(TAB_STATION));
        binding.loTabPath.setOnClickListener(v -> binding.viewPager.setCurrentItem(TAB_FIND)); // 경로찾기

        selectTabLayout(currentTabNo);
    }

    private void selectTabLayout(int num) {
        tabUnSelect(binding.imTabBus, binding.tvTabBus, binding.vTabBusBottomLine);
        tabUnSelect(binding.imTabStation,binding.tvTabStation, binding.vTabStationBottomLine);
        tabUnSelect(binding.imTabPath,binding.tvTabPath, binding.vTabPathBottomLine);

        switch (num) {
            case TAB_BUS:
                tabSelect(binding.imTabBus,binding.tvTabBus, binding.vTabBusBottomLine);
                binding.titleView.getTitle().setText(R.string.findBus);
                binding.imTabBus.setImageDrawable(getDrawable(R.drawable.common_bus_off_20));
                binding.imTabStation.setImageDrawable(getDrawable(R.drawable.common_busstop_22));
                binding.imTabPath.setImageDrawable(getDrawable(R.drawable.ic_arrow_20_off));
                break;

            case TAB_STATION:
                tabSelect(binding.imTabStation,binding.tvTabStation, binding.vTabStationBottomLine);
                binding.titleView.getTitle().setText(R.string.findStation);
                binding.imTabBus.setImageDrawable(getDrawable(R.drawable.common_bus_on_20));
                binding.imTabStation.setImageDrawable(getDrawable(R.drawable.common_on_busstop_22));
                binding.imTabPath.setImageDrawable(getDrawable(R.drawable.ic_arrow_20_off));
                break;

            // 경로찾기
            case TAB_FIND:
                tabSelect(binding.imTabPath,binding.tvTabPath, binding.vTabPathBottomLine);
                binding.titleView.getTitle().setText(R.string.findRoute);
                binding.imTabStation.setImageDrawable(getDrawable(R.drawable.common_busstop_22));
                binding.imTabBus.setImageDrawable(getDrawable(R.drawable.common_bus_on_20));
                binding.imTabPath.setImageDrawable(getDrawable(R.drawable.ic_arrow_20_on));
                break;
        }
    }

    private void initViewPager() {
        pagerAdapter = new CommonPagerAdapter(getSupportFragmentManager(), fragments);

        binding.viewPager.setOffscreenPageLimit(1);
        binding.viewPager.setPageMargin(0);
        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                hideKeyBoard(binding.viewPager);
            }

            @Override
            public void onPageSelected(int position) {
                selectTabLayout(position);
                hideKeyBoard(binding.viewPager);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        binding.viewPager.setAdapter(pagerAdapter);
        binding.viewPager.setCurrentItem(currentTabNo);
    }


    private void tabSelect(ImageView imageView, TextView textView, View view) {
        textView.setTypeface(ResourcesCompat.getFont(this, R.font.font_noto_sans_cj_kkr_bold));
        textView.setTextColor(this.getColor(R.color.color_01CAFF));
        view.setVisibility(View.VISIBLE);
    }
    private void tabUnSelect(ImageView imageView, TextView textView, View view) {
        //imageView.setColorFilter(R.color.color_999999);
        textView.setTypeface(ResourcesCompat.getFont(this, R.font.font_noto_sans_cj_kkr_medium));
        textView.setTextColor(this.getColor(R.color.color_999999));
        view.setVisibility(View.INVISIBLE);
    }

    public void hideKeyBoard(View view) {
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
