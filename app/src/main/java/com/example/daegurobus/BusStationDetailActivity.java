package com.example.daegurobus;

import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;

import com.example.daegurobus.adapter.StationDetailArriveInfoAdapter;
import com.example.daegurobus.adapter.StationDetailOperationInfoAdapter;
import com.example.daegurobus.network.DEFINE;
import com.example.daegurobus.model.BusDetailInfo;
import com.example.daegurobus.model.RequestBus;
import com.example.daegurobus.model.StationDetailInfoSub;
import com.example.daegurobus.network.resultInterface.AuthBaseInterface;
import com.example.daegurobus.network.resultInterface.BusStationDetailInterface;
import com.example.daegurobus.databinding.ActivityStationDetailBinding;
import com.example.daegurobus.model.StationDetailInfo;
import com.example.daegurobus.model.StationDetailTop;
import com.example.daegurobus.widget.BusBaseActivity;
import com.example.daegurobus.widget.stationDetailDialog;
import com.example.daegurobus.widget.stationDetailOperationDialog;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BusStationDetailActivity extends BusBaseActivity implements NestedScrollView.OnScrollChangeListener {

    private ActivityStationDetailBinding binding;
    private StationDetailOperationInfoAdapter stationDetailOperationInfoAdapter;
    private StationDetailArriveInfoAdapter stationDetailArriveInfoAdapter;

    private String viewType = VIEW_TYPE_NON_OPERATION_MODE;

    private static final String VIEW_TYPE_OPERATION_MODE = "VIEW_TYPE_OPERATION_MODE";
    private static final String VIEW_TYPE_NON_OPERATION_MODE = "VIEW_TYPE_NON_OPERATION_MODE";

    private Intent intent;
    private BusDetailInfo data;

    private double stationLon;
    private double stationLat;
    private String stationName;
    private String gbn;
    private String savedKeyword;
    private StationDetailTop station;
    private ArrayList<StationDetailInfo> stationInfo;

    private Parcelable recyclerViewState;
    private Parcelable recyclerViewState1;

    private final Handler handler = new Handler();
    private boolean isClickable = true;
    private Runnable updateRunnable;

    public static final String SORT_BY_ROUTE = "1";
    public static final String SORT_BY_TIME = "2";
    public static final String SORT_BY_KIND = "3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_station_detail);

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
        if (intent.hasExtra("data")) {
            data = (BusDetailInfo) intent.getSerializableExtra("data");
            savedKeyword = data.getStationId();
        } else {
            savedKeyword = intent.getStringExtra("stationId");
        }

    }

    private void settingData() {
        // tp 중복 제거후 표시
        List<String> tpList = new ArrayList<>();

        for (StationDetailInfo item : stationInfo) {
            switch (item.getRouteTp()) {
                case "1":
                    tpList.add("간선");
                    break;
                case "2":
                    tpList.add("지선");
                    break;
                case "3":
                    tpList.add("순환");
                    break;
                case "4":
                    tpList.add("급행");
                    break;
                case "5":
                    tpList.add("출근");
                    break;
//                        case "6":
//                            tpList.add("군위");
//                            break;
            }
        }
        Set<String> set = new HashSet<>(tpList);
        List<String> tpListDeduplication = new ArrayList<>(set);
        String tpListRemoveSpace = TextUtils.join(",", tpListDeduplication);
        binding.tvTp.setText(tpListRemoveSpace);

        switch (station.getSubwayNum()) {
            case "1":
                binding.tvNearbySubway.setText("1호선");
                break;
            case "2":
                binding.tvNearbySubway.setText("2호선");
                break;
            case "3":
                binding.tvNearbySubway.setText("3호선");
                break;
            case "1,2":
                binding.tvNearbySubway.setText("1호선,2호선");
                break;
            case "1,3":
                binding.tvNearbySubway.setText("1호선,3호선");
                break;
            case "2,3":
                binding.tvNearbySubway.setText("2호선,3호선");
                break;
            case "":
                binding.tvNearbySubway.setText("없음");
                break;
        }

        binding.tvStationName.setText(station.getStationName());
        binding.tvStationNext.setText(station.getStationDirection());
        binding.tvStationNo.setText(station.getStationNo());
        binding.tvKeyword.setText(station.getKeyword());
        binding.tvComming.setText(station.getComingSoon().replace(" ", ","));

        if ("Y".equals(station.getBookmarkYN())) {
            binding.imBookmark.setImageResource(R.drawable.ic_on_star_36_pt);
        } else {
            binding.imBookmark.setImageResource(R.drawable.ic_off_line_black_star_20_pt);
        }

        stationName = station.getStationName();
        stationLat = Double.parseDouble(station.getStationLat());
        stationLon = Double.parseDouble(station.getStationLon());
    }


    private void initLayout() {

        try {

            binding.rlTabNearbyStops.setOnClickListener(new View.OnClickListener() {

                boolean isButtonPressed = false;

                @Override
                public void onClick(View v) {

                    if (isButtonPressed) {

                        viewType = VIEW_TYPE_NON_OPERATION_MODE;
                        initNonOperationMode();
                    } else {



                        viewType = VIEW_TYPE_OPERATION_MODE;
                        initOperationMode();
                    }
                    isButtonPressed = !isButtonPressed;
                }
            });
        } catch (Exception e) {
            System.out.println(e);
        }

        initTitleView();
        initFloatingActionButton();
        requestStationDetail(gbn);
        initDialog(viewType);

    }

    private void initTitleView() {
        binding.ivBusLeft1.setOnClickListener(view -> finish());

        binding.tvStationName.setSelected(true);
        binding.tvStationName.setEllipsize(TextUtils.TruncateAt.MARQUEE);

        //새로고침 버튼
        binding.ivBusRight2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClickable) {
                    refreshButton();

                    // 5초 동안 텍스트뷰 클릭을 방지
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

        binding.ivBusRight1.setOnClickListener(View -> startActivity(new Intent(BusStationDetailActivity.this, BusMainActivity.class)));

        binding.rlTabMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(v.getContext(), BusStationMapActivity.class);
                intent.putExtra("stationName", stationName);
                intent.putExtra("stationLat", stationLat);
                intent.putExtra("stationLon", stationLon);
                v.getContext().startActivity(intent);
            }
        });

        binding.rlBookmark.setOnClickListener(view -> {
            if ("Y".equals(station.getBookmarkYN())) {
                requestBookmarkDelete(station.getStationId());
            } else {
                requestBookmarkInsert(station.getStationId());
            }
        });

    }

    // 버스 운행 정보
    private void rvOperationInfo(ArrayList<StationDetailInfo> stationDetailInfo) {
        stationDetailOperationInfoAdapter = new StationDetailOperationInfoAdapter(BusStationDetailActivity.this);
        binding.recyclerviewRouteInfo.setLayoutManager(new LinearLayoutManager(BusStationDetailActivity.this, LinearLayoutManager.VERTICAL, false));
        binding.recyclerviewRouteInfo.setAdapter(stationDetailOperationInfoAdapter);
        recyclerViewState = binding.recyclerviewRouteInfo.getLayoutManager().onSaveInstanceState();
        stationDetailOperationInfoAdapter.initItems(stationDetailInfo);

    }

    // 버스 실시간 도착 정보
    private void rvArriveInfo(ArrayList<StationDetailInfoSub> stationDetailInfoSub, ArrayList<StationDetailInfo> stationDetailInfo) {
        stationDetailArriveInfoAdapter = new StationDetailArriveInfoAdapter(BusStationDetailActivity.this);
        binding.recyclerviewArrriveTime.setLayoutManager(new LinearLayoutManager(BusStationDetailActivity.this, LinearLayoutManager.VERTICAL, false));
        binding.recyclerviewArrriveTime.setAdapter(stationDetailArriveInfoAdapter);
        recyclerViewState1 = binding.recyclerviewArrriveTime.getLayoutManager().onSaveInstanceState();
        stationDetailArriveInfoAdapter.initItems(stationDetailInfoSub, stationDetailInfo);
    }


    private void showLoadingView() {
        showLoading(binding.loLoading);

    }

    private void allViewGone() {
        hideLoadingAll(binding.loLoading);
    }


    private void initFloatingActionButton() {

        binding.nestScrollView.setOnScrollChangeListener(this);
        binding.ivFloating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.appBarLayout.setExpanded(true, true);
                binding.nestScrollView.smoothScrollTo(0, 0);
            }
        });
    }

    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int
            oldScrollY) {
        if (scrollY > oldScrollY) {
            // Scroll down -> hide FloatingActionButton
            binding.ivFloating.setVisibility(View.VISIBLE);
        } else {
            // Scroll up -> show FloatingActionButton
            binding.ivFloating.setVisibility(View.INVISIBLE);
        }
    }

    private void initDialog(String viewType) {
        binding.loDialog.setOnClickListener(View -> {

            switch (viewType) {
                case VIEW_TYPE_OPERATION_MODE:
                    new stationDetailOperationDialog(BusStationDetailActivity.this, gbn)
                            .setCallbackListener(new stationDetailOperationDialog.CallbackListener() {
                                @Override
                                public void click(String a) {

                                    switch (a) {
                                        case SORT_BY_ROUTE:
                                            binding.tvDialogKind.setText("번호순");
                                            gbn = SORT_BY_ROUTE;
                                            break;
                                        case SORT_BY_KIND:
                                            binding.tvDialogKind.setText("유형순");
                                            gbn = SORT_BY_KIND;
                                            break;
                                    }

                                    requestStationDetail(a);
                                }
                            })
                            .show();
                    break;
                case VIEW_TYPE_NON_OPERATION_MODE:
                    new stationDetailDialog(BusStationDetailActivity.this, gbn)
                            .setCallbackListener(new stationDetailDialog.CallbackListener() {
                                @Override
                                public void click(String a) {

                                    switch (a) {
                                        case SORT_BY_ROUTE:
                                            binding.tvDialogKind.setText("번호순");
                                            gbn = SORT_BY_ROUTE;
                                            break;
                                        case SORT_BY_TIME:
                                            binding.tvDialogKind.setText("시간순");
                                            gbn = SORT_BY_TIME;
                                            break;
                                        case SORT_BY_KIND:
                                            binding.tvDialogKind.setText("유형순");
                                            gbn = SORT_BY_KIND;
                                            break;
                                    }


                                    requestStationDetail(a);
                                }
                            })
                            .show();
                    break;
            }
        });
    }

    private void requestStationDetail(String gbn) {
        showLoadingView();
        if (gbn == null) {
            gbn = SORT_BY_ROUTE;
        }

        RequestBus requestBus = new RequestBus(DEFINE.MCODE, DEFINE.STATION_DETAIL);
        requestBus.addParam(gbn);
        requestBus.addParam(savedKeyword);
        requestBus.commit();
        busNetworkPresenter.busStationDetail(requestBus, new BusStationDetailInterface() {
            @Override
            public void success(StationDetailTop stationDetailTop, ArrayList<StationDetailInfo> stationDetailInfo, ArrayList<StationDetailInfoSub> stationDetailInfoSub) {

                station = stationDetailTop;
                stationInfo = stationDetailInfo;

                rvOperationInfo(stationDetailInfo);

                rvArriveInfo(stationDetailInfoSub, stationDetailInfo);

                settingData();
                initAutoRefresh();
                allViewGone();
            }
        });
    }

    private void requestBookmarkInsert(String stationId) {
        RequestBus requestBus = new RequestBus(DEFINE.MCODE, DEFINE.INSERT_BOOKMARK);
        requestBus.addParam("2");
        requestBus.addParam("");
        requestBus.addParam(stationId);
        requestBus.addParam("");
        requestBus.addParam("");
        requestBus.commit();

        busNetworkPresenter.bookmarkInsert(requestBus, new AuthBaseInterface<String>() {
            @Override
            public void success(String item) {
                station.setBookmarkYN("Y");
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

    private void requestBookmarkDelete(String stationId) {
        RequestBus requestBus = new RequestBus(DEFINE.MCODE, DEFINE.DELETE_BOOKMARK);
        requestBus.addParam("2");
        requestBus.addParam("");
        requestBus.addParam(stationId);
        requestBus.addParam("");
        requestBus.addParam("");
        requestBus.commit();

        busNetworkPresenter.bookmarkDelete(requestBus, new AuthBaseInterface<String>() {
            @Override
            public void success(String item) {
                station.setBookmarkYN("N");
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

    private void initNonOperationMode() {
        binding.titleView.setBackgroundResource(R.color.white);
        binding.ivBusLeft1.setImageTintList(ColorStateList.valueOf(getColor(R.color.color_333333)));
        binding.tvStationName.setTextColor(getColor(R.color.black));
        binding.ivBusRight2.setImageTintList(ColorStateList.valueOf(getColor(R.color.color_333333)));
        binding.ivBusRight1.setImageTintList(ColorStateList.valueOf(getColor(R.color.color_333333)));

        binding.appBarLayout.setBackgroundResource(R.color.white);

        binding.loBusInfo.setBackgroundResource(R.color.white);
        binding.tvStationNext.setTextColor(getColor(R.color.color_333333));
        binding.imTabNearbyStops.setImageTintList(ColorStateList.valueOf(getColor(R.color.color_333333)));
        binding.tvTabNearbyStops.setTextColor(getColor(R.color.color_333333));
        binding.imTabMap.setImageTintList(ColorStateList.valueOf(getColor(R.color.color_333333)));
        binding.tvTabMap.setTextColor(getColor(R.color.color_333333));

        if ("N".equals(station.getBookmarkYN())) {
            binding.imBookmark.setImageResource(R.drawable.ic_off_line_black_star_20_pt);
            binding.imBookmark.setImageTintList(ColorStateList.valueOf(getColor(R.color.color_333333)));
        }

        binding.tvTabBookmark.setTextColor(getColor(R.color.color_333333));
        binding.vToolbarBottomShadow.setBackgroundResource(R.color.color_E6E6E6);
        binding.vToolbarBottomLine.setBackgroundResource(R.color.color_F2F2F2);
        binding.vMiddleBottomLine.setBackgroundResource(R.color.color_F2F2F2);

        binding.tvTp.setTextColor(getColor(R.color.color_333333));
        binding.tvNearbySubway.setTextColor(getColor(R.color.color_333333));
        binding.tvKeyword.setTextColor(getColor(R.color.color_333333));

        binding.loTabMiddle.setBackgroundResource(R.color.white);
        binding.tvComming.setTextColor(getColor(R.color.color_333333));
        binding.vDialogLeftLine.setBackgroundResource(R.color.color_333333_30);
        binding.tvDialogKind.setTextColor(getColor(R.color.color_333333));
        binding.tvDialogCursor.setImageTintList(ColorStateList.valueOf(getColor(R.color.color_333333)));

        binding.vMiddleBottomLine.setBackgroundResource(R.color.color_F2F2F2);

        binding.flNestScrollView.setBackgroundResource(R.color.white);

        binding.recyclerviewArrriveTime.setVisibility(View.VISIBLE);
        binding.recyclerviewRouteInfo.setVisibility(View.GONE);
        viewType = VIEW_TYPE_NON_OPERATION_MODE;
        initDialog(VIEW_TYPE_NON_OPERATION_MODE);

    }

    private void initOperationMode() {
        binding.titleView.setBackgroundResource(R.color.color_333333);
        binding.ivBusLeft1.setImageTintList(ColorStateList.valueOf(getColor(R.color.white)));
        binding.tvStationName.setTextColor(getColor(R.color.white));
        binding.ivBusRight2.setImageTintList(ColorStateList.valueOf(getColor(R.color.white)));
        binding.ivBusRight1.setImageTintList(ColorStateList.valueOf(getColor(R.color.white)));

        binding.appBarLayout.setBackgroundResource(R.color.color_333333);

        binding.loBusInfo.setBackgroundResource(R.color.color_333333);
        binding.tvStationNext.setTextColor(getColor(R.color.white));
        binding.imTabNearbyStops.setImageTintList(ColorStateList.valueOf(getColor(R.color.color_01CAFF)));
        binding.tvTabNearbyStops.setTextColor(getColor(R.color.color_01CAFF));
        binding.imTabMap.setImageTintList(ColorStateList.valueOf(getColor(R.color.white)));
        binding.tvTabMap.setTextColor(getColor(R.color.white));


        if ("N".equals(station.getBookmarkYN())) {
            binding.imBookmark.setImageResource(R.drawable.ic_off_line_black_star_20_pt);
            binding.imBookmark.setImageTintList(ColorStateList.valueOf(getColor(R.color.white)));
        }


        binding.tvTabBookmark.setTextColor(getColor(R.color.white));

        binding.vToolbarBottomShadow.setBackgroundResource(R.color.color_F2F2F2_5);
        binding.vToolbarBottomLine.setBackgroundResource(R.color.color_F2F2F2_10);
        binding.vMiddleBottomLine.setBackgroundResource(R.color.color_CCCCCC_5);

        binding.tvTp.setTextColor(getColor(R.color.white));
        binding.tvNearbySubway.setTextColor(getColor(R.color.white));
        binding.tvKeyword.setTextColor(getColor(R.color.white));

        binding.loTabMiddle.setBackgroundResource(R.color.color_333333);
        binding.tvComming.setTextColor(getColor(R.color.white));
        binding.vDialogLeftLine.setBackgroundResource(R.color.color_F2F2F2_30);
        binding.tvDialogKind.setTextColor(getColor(R.color.white));
        binding.tvDialogCursor.setImageTintList(ColorStateList.valueOf(getColor(R.color.white)));

        binding.flNestScrollView.setBackgroundResource(R.color.color_999999);

        binding.recyclerviewArrriveTime.setVisibility(View.GONE);
        binding.recyclerviewRouteInfo.setVisibility(View.VISIBLE);
        viewType = VIEW_TYPE_OPERATION_MODE;
        initDialog(VIEW_TYPE_OPERATION_MODE);
    }


    private void refreshButton() {
        requestStationDetail(gbn);
        binding.recyclerviewRouteInfo.getLayoutManager().onRestoreInstanceState(recyclerViewState);
        stationDetailOperationInfoAdapter.notifyDataSetChanged();
        binding.recyclerviewArrriveTime.getLayoutManager().onRestoreInstanceState(recyclerViewState1);
        stationDetailArriveInfoAdapter.notifyDataSetChanged();
    }

    private void initAutoRefresh() {

        updateRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    requestStationDetail(gbn);
                    binding.recyclerviewRouteInfo.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                    stationDetailOperationInfoAdapter.notifyDataSetChanged();
                    binding.recyclerviewArrriveTime.getLayoutManager().onRestoreInstanceState(recyclerViewState1);
                    stationDetailArriveInfoAdapter.notifyDataSetChanged();
                    handler.postDelayed(this, 30000);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            handler.postDelayed(updateRunnable, 0, 30000);
        }

    }
}