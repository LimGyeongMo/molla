package com.example.daegurobus;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;

import com.example.daegurobus.adapter.BusDetailAdapter;
import com.example.daegurobus.network.DEFINE;
import com.example.daegurobus.network.resultInterface.AuthBaseInterface;
import com.example.daegurobus.network.resultInterface.BusDetailInterface;
import com.example.daegurobus.databinding.ActivityBusDetailBinding;
import com.example.daegurobus.model.BusDetailInfo;
import com.example.daegurobus.model.BusDetailInfo1;
import com.example.daegurobus.model.BusDetailTop;
import com.example.daegurobus.model.Point;
import com.example.daegurobus.model.RequestBus;
import com.example.daegurobus.util.GpsUtil;
import com.example.daegurobus.widget.BusBaseActivity;
import com.example.daegurobus.widget.CommonNoticeDialog;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.naver.maps.geometry.LatLng;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BusDetailActivity extends BusBaseActivity {

    private ActivityBusDetailBinding binding;
    private BusDetailAdapter routeListAdapter;
    private final String GAN = "1";
    private final String JI = "2";
    private final String SUN = "3";
    private final String FAST = "4";
    private final String WORK = "5";
    private final String gunwi = "6";

    private List<Point> stationPointList;
    private HashMap<String, Integer> lonLat;
    private BusDetailTop bus;
    private ArrayList<BusDetailInfo> routeInfo;
    private Intent intent;
    private String savedKeyword;
    private String keyword;

    private Parcelable recyclerViewState;
    private GpsUtil gpsUtil;
    private int selectedPosition;

    private final Handler handler = new Handler();
    private Runnable updateRunnable;
    private boolean wasClicked = false;
    private boolean isClickable = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bus_detail);

        initData();
        initLayout();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (handler != null && updateRunnable != null) {
            handler.removeCallbacks(updateRunnable);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            handler.postDelayed(updateRunnable, 0, 30000);
        }
    }

    private void initData() {
        intent = getIntent();
        savedKeyword = intent.getStringExtra("routeId");
        keyword = intent.getStringExtra("routeTp");
        requestBusDetail(savedKeyword);

        gpsUtil = new GpsUtil(this, new GpsUtil.Callback() {
            @Override
            public void getLastLocation(LatLng latLng) {
            }
            @Override
            public void getCurrentLocation(LatLng latLng) {

                ArrayList<BusDetailInfo> itemList = routeListAdapter.getItems();
                double dis = Double.MAX_VALUE;
                double temp = Double.MAX_VALUE;
                int idx = 0;

                for (int i = 0; i < itemList.size(); i++) {
                    temp = itemList.get(i).distanceTo(latLng.longitude, latLng.latitude);
                    if (dis > temp) {
                        dis = temp;
                        idx = i;
                    }
                }
                selectedPosition = idx;

            }

            @Override
            public void getCurrentLocationTimeout() {
                runOnUiThread(() -> {
                    hideLoadingAll(binding.loLoading);
                });
            }

            @Override
            public void stopLocationUpdate() {
            }
        });

        gpsUtil.startLocationUpdates();
    }

    private void settingData() {
        if ("".equals(bus.getRouteNum2())) {
            binding.tvBusName.setText(bus.getRouteNum());
        } else {
            binding.tvBusName.setText(bus.getRouteNum() + "(" + bus.getRouteNum2() + ")");
        }

        initBackground(bus.getRouteTp());
        binding.tvTp.setText(bus.getRouteTp1());

        int time = Integer.valueOf(bus.getIntervalTime()) / 60; // 평일 간격
        int sun = Integer.valueOf(bus.getIntervalSaturday()) / 60; // 일요일 간격
        int sat = Integer.valueOf(bus.getIntervalSaturday()) / 60; // 토요일 간격

        binding.tvIntervalTime.setText("평일 " + time + "분, 토요일 " + sat + "분, 일요일" + sun + "분 간격");
        binding.tvNodename.setText(bus.getStartnodeName() + "↔" + bus.getEndnodeName());


        if ("Y".equals(bus.getBookmarkYn())) {
            binding.imBookmark.setImageResource(R.drawable.ic_on_star_36_pt);
        } else {
            binding.imBookmark.setImageResource(R.drawable.ic_off_line_star_20_pt);
        }

    }

    private void initLayout() {
        showLoadingView();
        initBackground(keyword);
        initTitleView();
        initAutoRefresh(savedKeyword);
    }

    private void initTitleView() {
        binding.ivBusLeft1.setOnClickListener(view -> finish());

        binding.tvBusName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        binding.tvBusName.setSelected(true);

        binding.ivBusRight2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClickable) {
                    showLoadingView();
                    recyclerViewState = binding.recyclerview.getLayoutManager().onSaveInstanceState();
                    requestBusDetail(savedKeyword);

                    // 5초 동안 클릭을 안되게
                    isClickable = false;
                    binding.ivBusRight2.setEnabled(false);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isClickable = true;
                            binding.ivBusRight2.setEnabled(true);
                        }
                    }, 5000);

                }
            }
        });

        binding.ivBusRight1.setOnClickListener(view -> startActivity(new Intent(BusDetailActivity.this, BusMainActivity.class)));

        binding.rlTabMap.setOnClickListener(View -> {
            Intent intent;
            intent = new Intent(View.getContext(), BusRouteMapActivity.class);
            intent.putExtra("busDetailTop", bus);
            intent.putExtra("routeId", bus.getRouteId());
            intent.putExtra("busDetailInfo", routeInfo);
            View.getContext().startActivity(intent);

        });
        String gbn = "1";
        String stationId = "";
        binding.rlTabBookmark.setOnClickListener(view -> {
            if ("Y".equals(bus.getBookmarkYn())) {
                requestBookmarkDelete(gbn, bus.getRouteId(), stationId);
            } else {
                requestBookmarkInsert(gbn, bus.getRouteId(), stationId);
            }
        });


        binding.rlTabNearbyStops.setOnClickListener(View -> {
            binding.recyclerview.stopScroll();
            wasClicked = true;
            if (selectedPosition > 0) {
                scrollSelectedItemToCenter(selectedPosition);
                routeListAdapter.setSelectedPosition(selectedPosition);
            } else {
                addressSelectByGps();

            }
        });

        binding.ivFloating.setOnClickListener(view -> scrollToTop());

    }

    @SuppressLint("NonConstantResourceId")
    private void initRecyclerView() {

            routeListAdapter = new BusDetailAdapter(this);
            routeListAdapter.setOnItemClickListener((View, Position) -> {
                BusDetailInfo items = routeListAdapter.getItem(Position);
//                switch (View.getId()) {
//                    case R.id.lo_form:
//
//                }
                Intent intent;
                intent = new Intent(View.getContext(), BusStationDetailActivity.class);
                intent.putExtra("data", items);
                View.getContext().startActivity(intent);

            });

            binding.recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    refreshFloatingButton();
                }
            });
            if (wasClicked){
                routeListAdapter.setSelectedPosition(selectedPosition);
            }
            binding.recyclerview.setLayoutManager(new LinearLayoutManager(BusDetailActivity.this, LinearLayoutManager.VERTICAL, false));
            binding.recyclerview.getLayoutManager().onRestoreInstanceState(recyclerViewState);
            binding.recyclerview.setAdapter(routeListAdapter);


    }

    private void addressSelectByGps() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            TedPermission.with(getApplicationContext())
                    .setPermissionListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted() {

                            gpsUtil.startLocationUpdates();
                            scrollSelectedItemToCenter(selectedPosition);
                        }

                        @Override
                        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        }
                    })
                    .setDeniedMessage(getString(R.string.ted_permission_denied_message))
                    .setGotoSettingButtonText(getString(R.string.ted_permission_go_to_setting_button_text))
                    .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                    .check();
        } else {
            new CommonNoticeDialog(this)
                    .setMessage(getString(R.string.msg_need_gps_setting))
                    .setPositiveText(getString(R.string.do_setting))
                    .setCallbackListener(new CommonNoticeDialog.CallbackListener() {
                        @Override
                        public void positive() {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }

                        @Override
                        public void negative() {

                        }
                    }).show();
        }

    }
    private void showLoadingView() {
        //allViewGone();
        showLoading(binding.loLoading);
    }

    private void allViewGone() {
        hideLoadingAll(binding.loLoading);
    }

    private void initBackground(String tp) {

        switch (tp) {
            case GAN:
                binding.titleView.setBackgroundResource(R.color.color_2971F6);
                binding.loInfoPage.setBackgroundResource(R.color.color_2971F6);
                break;
            case JI:
                binding.titleView.setBackgroundResource(R.color.color_F7B744);
                binding.loInfoPage.setBackgroundResource(R.color.color_F7B744);
                break;
            case SUN:
                binding.titleView.setBackgroundResource(R.color.color_008142);
                binding.loInfoPage.setBackgroundResource(R.color.color_008142);
                break;
            case FAST:
                binding.titleView.setBackgroundResource(R.color.color_EB0220);
                binding.loInfoPage.setBackgroundResource(R.color.color_EB0220);
                break;
            case WORK:
                binding.titleView.setBackgroundResource(R.color.color_8AD644);
                binding.loInfoPage.setBackgroundResource(R.color.color_8AD644);
                break;
            case gunwi:
                binding.titleView.setBackgroundResource(R.color.color_183290);
                binding.loInfoPage.setBackgroundResource(R.color.color_183290);
                break;

        }
    }

    private void requestBusDetail(String query) {

        RequestBus requestBus = new RequestBus(DEFINE.MCODE, DEFINE.GET_BUS_DETAIL);
        requestBus.addParam("1");
        requestBus.addParam(query);
        requestBus.commit();

        busNetworkPresenter.busDetail(requestBus, new BusDetailInterface() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void success(BusDetailTop busDetailTop, ArrayList<BusDetailInfo> busDetailInfoList, ArrayList<BusDetailInfo1> busDetailInfo1) {
                try {

                    bus = busDetailTop;
                    routeInfo = busDetailInfoList;

                    settingData();

                    initRecyclerView();
                    routeListAdapter.initItems(busDetailInfoList);
//                    routeListAdapter.updateDiffCallbackListItems(busDetailInfoList);

                    lonLat = new HashMap<String, Integer>();

                    stationPointList = new ArrayList<>();

                    for (BusDetailInfo item : busDetailInfoList) {
                        double stationLon = Double.parseDouble(item.getStationLon());
                        double stationLat = Double.parseDouble(item.getStationLat());
                        lonLat.put(item.getStationLon(), busDetailInfoList.size());

                        Point point = new Point(stationLon, stationLat);
                        stationPointList.add(point);
                    }

                    allViewGone();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });
    }

    private void requestBookmarkInsert(String gbn, String routeId, String stationId) {
        RequestBus requestBus = new RequestBus(DEFINE.MCODE, DEFINE.INSERT_BOOKMARK);
        requestBus.addParam(gbn);
        requestBus.addParam(routeId);
        requestBus.addParam(stationId);
        requestBus.addParam("");
        requestBus.addParam("");
        requestBus.commit();

        busNetworkPresenter.bookmarkInsert(requestBus, new AuthBaseInterface<String>() {
            @Override
            public void success(String item) {
                bus.setBookmarkYn("Y");
                settingData();
            }

            @Override
            public void error(String message) {
            }

            @Override
            public void errorAuth() {
            }
        });

    }

    private void requestBookmarkDelete(String gbn, String routeId, String stationId) {
        RequestBus requestBus = new RequestBus(DEFINE.MCODE, DEFINE.DELETE_BOOKMARK);
        requestBus.addParam(gbn);
        requestBus.addParam(routeId);
        requestBus.addParam(stationId);
        requestBus.addParam("");
        requestBus.addParam("");
        requestBus.commit();

        busNetworkPresenter.bookmarkDelete(requestBus, new AuthBaseInterface<String>() {
            @Override
            public void success(String item) {
                bus.setBookmarkYn("N");
                settingData();
            }

            @Override
            public void error(String message) {
            }

            @Override
            public void errorAuth() {
            }
        });
    }

    // 천천히 이동
    private void SmoothScrollSelectedItemToCenter(int selectedPosition) {
        RecyclerView.LayoutManager layoutManager = binding.recyclerview.getLayoutManager();

        if (layoutManager instanceof LinearLayoutManager) {
            RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(this) {
                @Override
                protected int getVerticalSnapPreference() {
                    return LinearSmoothScroller.SNAP_TO_START;
                }

                @Override
                protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                    // 이 값을 조절하여 스크롤 속도를 변경가능
                    return super.calculateSpeedPerPixel(displayMetrics) * 0.2f;
                }

                @Override
                public int calculateDtToFit(int viewStart, int viewEnd, int boxStart, int boxEnd, int snapPreference) {
                    // 리사이클러뷰 높이
                    int centerOffset = (boxEnd - boxStart) / 2;
                    //스크롤될 아이템 높이
                    int itemHeight = viewEnd - viewStart;

                    return boxStart + centerOffset - (viewStart + itemHeight / 2);
                }
            };

            smoothScroller.setTargetPosition(selectedPosition);
            layoutManager.startSmoothScroll(smoothScroller);
        }
    }

    // 바로 슝 이동
    private void scrollSelectedItemToCenter(int selectedPosition) {
        RecyclerView.LayoutManager layoutManager = binding.recyclerview.getLayoutManager();

        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;

            int recyclerViewHeight = binding.recyclerview.getHeight();

            int itemHeightPx = dpToPx(68);

            // 스크린 높이의 절반에서 아이템 높이의 절반을 뺀 위치로 스크롤하기 위한 offset 계산
            int offset = (recyclerViewHeight / 2) - (itemHeightPx/ 2);

            // 선택된 아이템을 수직 방향으로 중앙에 위치시킴
            linearLayoutManager.scrollToPositionWithOffset(selectedPosition, offset);
        }
    }

    public void refreshFloatingButton() {
        try {
            LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) binding.recyclerview.getLayoutManager());

            // 첫번째 아이템이 안보일때
            if (linearLayoutManager.findFirstVisibleItemPosition() > 0 /*&& linearLayoutManager.findLastVisibleItemPosition() != (routeListAdapter.getItemCount() - 1)*/) {
                binding.ivFloating.setVisibility(View.VISIBLE);
            } else {
                binding.ivFloating.setVisibility(View.GONE);
            }
        } catch (Exception e) {

        }
    }

    public void scrollToTop() {
        binding.recyclerview.stopScroll();
        binding.recyclerview.getLayoutManager().scrollToPosition(0);
        binding.ivFloating.setVisibility(View.GONE);
    }

    private void initAutoRefresh(String routeId) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            handler.postDelayed(updateRunnable, 0, 30000);
        }

        updateRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    recyclerViewState = binding.recyclerview.getLayoutManager().onSaveInstanceState();
                    requestBusDetail(routeId);
                    handler.postDelayed(this, 30000);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        };
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}