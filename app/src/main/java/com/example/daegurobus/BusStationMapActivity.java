package com.example.daegurobus;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.example.daegurobus.adapter.BusStationMapAdapter;
import com.example.daegurobus.network.DEFINE;
import com.example.daegurobus.network.resultInterface.BusStationAuto;
import com.example.daegurobus.network.resultInterface.StationSurroundInfo;

import com.example.daegurobus.network.NetworkPresenter;
import com.example.daegurobus.databinding.ActivityBusStationMapBinding;
import com.example.daegurobus.model.BusFliter;
import com.example.daegurobus.model.RequestBus;
import com.example.daegurobus.model.StationNearByInfo;
import com.example.daegurobus.model.stationResult;
import com.example.daegurobus.util.GpsUtil;
import com.example.daegurobus.widget.CommonNoticeDialog;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.naver.maps.geometry.LatLng;

import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;

import java.util.ArrayList;
import java.util.List;

public class BusStationMapActivity extends BaseActivity {

    private ActivityBusStationMapBinding binding;
    private BusStationMapAdapter Adapter;
    private Intent intent;
    private String gbn;
    private double stationLat;
    private double stationLon;
    private NetworkPresenter presenter;
    private String stationId;
    private int infoSize;
    private List<Marker> markers = new ArrayList<>();
    private MapView mapView;
    private NaverMap naverMap;
    private UiSettings uiSettings;

    private GpsUtil gpsUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bus_station_map);


        initData();
        initLayout();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void initData() {
        intent = getIntent();// 인텐트 받아오기

        stationId = intent.getStringExtra("stationName");
        stationLat = intent.getDoubleExtra("stationLat", 0);
        stationLon = intent.getDoubleExtra("stationLon", 0);


    }


    private void initLayout() {
        presenter = new NetworkPresenter(this);

        binding.ivLeft1.setOnClickListener(View -> finish());
        binding.ivRight1.setOnClickListener(View -> startActivity(new Intent(BusStationMapActivity.this, BusMainActivity.class)));

        initStationInfo();
        initNaverView();
        initViewPager();

        binding.loDetailBtn.setOnClickListener(View -> startActivity(new Intent(BusStationMapActivity.this, BusStationMapActivity.class)));
        binding.ivFloating.setOnClickListener(view -> {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                TedPermission.with(getApplicationContext())
                        .setPermissionListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted() {
                                showLoadingDark(binding.loLoading);
                                gpsUtil.startLocationUpdates();
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
                new CommonNoticeDialog(BusStationMapActivity.this)
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
        });
    }


    private void initNaverView() {
        mapView = binding.mapView;
        mapView.getMapAsync(mNaverMap -> {
            naverMap = mNaverMap;

            Marker marker = new Marker();
            marker.setPosition(new LatLng(stationLat, stationLon));
            marker.setIcon(OverlayImage.fromResource(R.drawable.ic_select_busstop_60_pt));
            marker.setMap(naverMap);
            surroundInfo(gbn);

            naverMap.setCameraPosition(new CameraPosition(new LatLng(stationLat, stationLon), 14));
            uiSettings = naverMap.getUiSettings();
            uiSettings.setScaleBarEnabled(false);
            uiSettings.setZoomControlEnabled(false);
            uiSettings.setLogoClickEnabled(false);
            uiSettings.setRotateGesturesEnabled(false);
            uiSettings.setTiltGesturesEnabled(false);
            uiSettings.setLogoGravity(Gravity.LEFT | Gravity.TOP);

        });

    }

    private void initViewPager() {

        binding.ivLeft1.setOnClickListener(View -> finish());
        binding.ivRight1.setOnClickListener(view -> startActivity(new Intent(BusStationMapActivity.this, BusMainActivity.class)));

        binding.viewpager2.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) binding.viewpager2.getLayoutManager();
            }
        });

        ArrayList<BusFliter> data = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            data.add(new BusFliter(i));

        }
        Adapter = new BusStationMapAdapter(this, data);
        binding.viewpager2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        Adapter.setOnItemClickListener(new BusStationMapAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int nearByInfo) {
                gbn = Integer.toString(nearByInfo);
                surroundInfo(gbn);
            }

        });
        binding.viewpager2.setAdapter(Adapter);
        Adapter.notifyDataSetChanged();


    }


    private void surroundInfo(String jobCode) {

        String lon = Double.toString(stationLon);
        String lat = Double.toString(stationLat);
        if (gbn == null) {
            jobCode = "0";

        }

        RequestBus requestBus = new RequestBus(DEFINE.MCODE, DEFINE.GET_NEARBY_PLACE_LIST);
        requestBus.addParam(jobCode);
        requestBus.addParam(lon);
        requestBus.addParam(lat);
        requestBus.commit();
        presenter.stationSurroundingInfo(requestBus, new StationSurroundInfo() {
            @Override
            public void success(ArrayList<StationNearByInfo> items) {
                clearAllMarkers();

                infoSize = items.size();

                double[] stationLat = new double[infoSize];
                double[] stationLon = new double[infoSize];

                for (int i = 0; i < infoSize; i++) {
                    String lat = items.get(i).getStationLat();
                    String lon = items.get(i).getStationLon();

                    stationLat[i] = Double.parseDouble(lat);
                    stationLon[i] = Double.parseDouble(lon);
                }

                for (int i = 0; i < infoSize; i++) {
                    Marker surroundMarker = new Marker();
                    surroundMarker.setPosition(new LatLng (stationLat[i], stationLon[i]));
                    switch (gbn) {
                        case "1":
                            surroundMarker.setIcon(OverlayImage.fromResource(R.drawable.ic_busstop_36_blue));
                            break;
                        case "2":
                            surroundMarker.setIcon(OverlayImage.fromResource(R.drawable.ic_subway_36_pt));
                            break;
                        case "3":
                            surroundMarker.setIcon(OverlayImage.fromResource(R.drawable.ic_train_36_pt));
                            break;
                        case "4":
                            surroundMarker.setIcon(OverlayImage.fromResource(R.drawable.ic_terminal_36_pt));
                            break;
                        case "5":
                            surroundMarker.setIcon(OverlayImage.fromResource(R.drawable.ic_airport_36_pt));
                            break;
                    }
                    surroundMarker.setMap(naverMap);
                    markers.add(surroundMarker);
                }
            }

            @Override
            public void error(String message) {
                clearAllMarkers();
            }

        });
    }

    private void initStationInfo() {


        RequestBus requestBus = new RequestBus(DEFINE.MCODE, DEFINE.GET_STATION_LIST);
        requestBus.addParam("3");
        requestBus.addParam(stationId);
        requestBus.commit();
        presenter.getStationList(requestBus, new BusStationAuto() {
            @Override
            public void success(ArrayList<stationResult> stationList) {
                binding.tvStationName.setText(stationList.get(0).getStationName());
                binding.tvStationNo.setText(stationList.get(0).getStationNo());
                binding.tvStationNext.setText(stationList.get(0).getStationDirection());

                switch (stationList.get(0).getSubwayNum()) {
                    case "1":
                        binding.tvSubwayNum1.setVisibility(View.VISIBLE);
                        binding.tvSubwayNum2.setVisibility(View.GONE);
                        binding.tvSubwayNum3.setVisibility(View.GONE);
                        break;
                    case "2":
                        binding.tvSubwayNum1.setVisibility(View.GONE);
                        binding.tvSubwayNum2.setVisibility(View.VISIBLE);
                        binding.tvSubwayNum3.setVisibility(View.GONE);
                        break;
                    case "3":
                        binding.tvSubwayNum1.setVisibility(View.GONE);
                        binding.tvSubwayNum2.setVisibility(View.GONE);
                        binding.tvSubwayNum3.setVisibility(View.VISIBLE);
                        break;
                    case "1,2":
                        binding.tvSubwayNum1.setVisibility(View.VISIBLE);
                        binding.tvSubwayNum2.setVisibility(View.VISIBLE);
                        binding.tvSubwayNum3.setVisibility(View.GONE);
                        break;
                    case "1,3":
                        binding.tvSubwayNum1.setVisibility(View.VISIBLE);
                        binding.tvSubwayNum2.setVisibility(View.GONE);
                        binding.tvSubwayNum3.setVisibility(View.VISIBLE);
                        break;
                    case "2,3":
                        binding.tvSubwayNum1.setVisibility(View.GONE);
                        binding.tvSubwayNum2.setVisibility(View.VISIBLE);
                        binding.tvSubwayNum3.setVisibility(View.VISIBLE);
                        break;
                    case "":
                        binding.tvSubwayNum1.setVisibility(View.GONE);
                        binding.tvSubwayNum2.setVisibility(View.GONE);
                        binding.tvSubwayNum3.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void error(String msg) {

            }
        });
    }

    private void clearAllMarkers() {
        // 마커를 지도에서 제거
        for (Marker marker : markers) {
            marker.setMap(null);
        }
        markers.clear();
    }
}
