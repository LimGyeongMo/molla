package com.example.daegurobus;

import android.Manifest;
import android.content.Context;
import android.content.Intent;

import android.location.LocationManager;
import android.os.Bundle;

import android.view.View;


import androidx.annotation.NonNull;

import androidx.databinding.DataBindingUtil;

import com.example.daegurobus.databinding.BusActivityAddressMapSelectBinding;

import com.example.daegurobus.model.CommonAddress;
import com.example.daegurobus.network.naver.model.reversegeocode.NaverAddress;
import com.example.daegurobus.util.CommonAddressUtil;
import com.example.daegurobus.util.GpsUtil;
import com.example.daegurobus.widget.BusBaseActivity;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.example.daegurobus.widget.CommonNoticeDialog;

import com.example.daegurobus.network.naver.request.RequestReverseGeocoding;
import com.example.daegurobus.network.naver.resultInterface.ReverseGeocodingInterface;

import com.example.daegurobus.util.LogUtil;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.UiSettings;

import java.util.ArrayList;


public class BusAddressMapSelectActivity extends BusBaseActivity {
    public static final String MODE = "MODE";
    public static final int MODE_MY_ADDRESS_SAVE = 0;
    public static final int MODE_GET_ADDRESS = 1;
    private int mode = MODE_MY_ADDRESS_SAVE;

    public static final String ADDRESS = "ADDRESS";

    public static final int VIEW_TYPE_MAP = 1;
    public static final int VIEW_TYPE_DETAIL_INPUT = 2;
    private int viewType = VIEW_TYPE_MAP;

    public static final String CURRENT_ADDRESS = "CURRENT_ADDRESS";

    private BusActivityAddressMapSelectBinding binding;

    private CommonAddress address;
    private CommonAddress passedAddress;    // 이전 화면에서 전달받은 주소정보(건물명 정보 추가하기 위해)
    private String direction;
    private MapView mapView;
    private NaverMap naverMap;
    private UiSettings uiSettings;
    private GpsUtil gpsUtil;


    private boolean isProcessing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.bus_activity_address_map_select);

        initialize();
        initLayout();
        initData();
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
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

        mapView.onLowMemory();
    }


    private void initialize() {
        mode = getIntent().getExtras().getInt(MODE, MODE_MY_ADDRESS_SAVE);

        address = getIntent().getExtras().getParcelable(ADDRESS);

        passedAddress = new CommonAddress();
        passedAddress.setLongitude(address.getLongitude());
        passedAddress.setLatitude(address.getLatitude());
        passedAddress.setRoadDestBuilding(address.getRoadDestBuilding());

       direction = getIntent().getStringExtra("locationPoint");
    }

    private void initLayout() {
        binding.titleView.getIconLeft1().setOnClickListener(v -> finish());
        initNaverMap();

        binding.ivFloating.setOnClickListener(view -> {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                TedPermission.with(getApplicationContext())
                        .setPermissionListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted() {
                                showLoadingDark(binding.loLoading);
                                setViewProcessing();
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
                new CommonNoticeDialog(BusAddressMapSelectActivity.this)
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

        if (direction.equals("0")){
            binding.tvSelectButton.setText(R.string.bus_start_select);
        }else{
            binding.tvSelectButton.setText(R.string.bus_end_select);
        }


        binding.loSelectButton.setOnClickListener(view -> {
            completeFinish();
        });

    }

    private void initNaverMap() {
        mapView = binding.mapView;
        mapView.getMapAsync(mNaverMap -> {
            naverMap = mNaverMap;
            naverMap.addOnCameraChangeListener((i, b) -> {
                if (i != 0) {
                    binding.tvTopBadge.setVisibility(View.GONE);
                    setViewProcessing();
                }
            });
            naverMap.setCameraPosition(new CameraPosition(new LatLng(address.getLatitude(), address.getLongitude()), 18));
            naverMap.addOnCameraIdleListener(() -> {
                isProcessing = false;
                LogUtil.e("addOnCameraIdleListener");
                requestReverseGeocoding();
            });

            naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

            uiSettings = naverMap.getUiSettings();
            uiSettings.setCompassEnabled(false);
            uiSettings.setScaleBarEnabled(false);
            uiSettings.setZoomControlEnabled(false);
            uiSettings.setLocationButtonEnabled(false);
        });
    }

    private void initData() {
        gpsUtil = new GpsUtil(this, new GpsUtil.Callback() {
            @Override
            public void getLastLocation(LatLng latLng) {

            }

            @Override
            public void getCurrentLocation(LatLng latLng) {
                hideLoadingAll(binding.loLoading);
                naverMap.moveCamera(CameraUpdate.scrollTo(latLng));
                showToast(getString(R.string.fail_search_current_gps));
            }

            @Override
            public void getCurrentLocationTimeout() {
                runOnUiThread(() -> {
                    hideLoadingAll(binding.loLoading);
                    showToast(getString(R.string.fail_search_current_gps));
                });
            }

            @Override
            public void stopLocationUpdate() {

            }
        });
    }


    private void setViewProcessing() {
        isProcessing = true;

        address = new CommonAddress();

        binding.tvOldAddress.setText(getString(R.string.processing_search_gps));
        binding.tvNewAddress.setText(getString(R.string.processing_search_gps));
    }

    private boolean isValidateBottom(boolean isShowErrorMessage) {
        boolean isValidate = false;
        String message = "";


        if (isProcessing) { // 카메라 움직이는 중
            message = getString(R.string.gps_searching);
        } else if (!CommonAddressUtil.isAddressUsable(address)) { // 올바른 주소인지 체크
            message = getString(R.string.msg_address_not_usable);
        } else {
            isValidate = true;
        }


        if (!isValidate && isShowErrorMessage) {
            showToast(message);
        }

        return isValidate;
    }

    // 좌표값으로 주소 검색(카메라 움직임이 끝났을때 호출)
    private void requestReverseGeocoding() {
        CameraPosition cameraPosition = naverMap.getCameraPosition();
        double longitude = cameraPosition.target.longitude;
        double latitude = cameraPosition.target.latitude;
        LogUtil.i("getReverseGeocoding: " + longitude + " " + latitude);

        RequestReverseGeocoding request = new RequestReverseGeocoding();
        request.setLongitude(longitude);
        request.setLatitude(latitude);

        // 앱자체 호출
//        if (ResponseVersion.MAP_GBN_SERVER.equals(myPreferencesManager.getResponseVersion().getMapGbn())) {
//            // showToast("서버 호출: " + myPreferencesManager.getResponseVersion().getMapGbn());
//
//            // 서버호출
//            networkPresenter.mapReverseGeocode(longitude, latitude, new ReverseGeocodingInterface() {
//                @Override
//                public void success(ArrayList<NaverAddress> naverAddresses) {
//                    setAddressGeo(naverAddresses, longitude, latitude);
//
//                    binding.tvOldAddress.setText(CommonAddressUtil.getOldAddress(address, true, true));
//                    binding.tvNewAddress.setText(CommonAddressUtil.getNewAddress(address, true, true));
//
//                    refreshBottom();
//                }
//
//                @Override
//                public void error(String message) {
//                    showToast(message);
//                }
//            });
//        } else {
        // showToast("앱 호출: " + myPreferencesManager.getResponseVersion().getMapGbn());

        naverNetworkPresenter.getReverseGeocoding(request, new ReverseGeocodingInterface() {
            @Override
            public void success(ArrayList<NaverAddress> naverAddresses) {
                setAddressGeo(naverAddresses, longitude, latitude);

                binding.tvOldAddress.setText(CommonAddressUtil.getOldAddress(address, true, true));
                binding.tvNewAddress.setText(CommonAddressUtil.getNewAddress(address, true, true));

            }

            @Override
            public void error(String message) {
                showToast("좌표값으로 주소검색에 실패하였습니다.");
            }
        });
//        }
    }

    private void setAddressGeo(ArrayList<NaverAddress> naverAddresses, double longitude, double latitude) {
        address = new CommonAddress();
        address.setLongitude(longitude);
        address.setLatitude(latitude);

        // 1. 구주소(법정동) 주소
        NaverAddress oldNaverAddress = naverAddresses.get(0);

        String sanJibun;

        switch (oldNaverAddress.getLand().getType()) {
            case "2": {
                sanJibun = "산 " + oldNaverAddress.getLand().getNumber1();
                break;
            }

            case "1":   // 일반토지
            default: {
                sanJibun = oldNaverAddress.getLand().getNumber1();
                break;
            }
        }

        address.setSido(oldNaverAddress.getRegion().getArea1().getName());
        address.setGungu(oldNaverAddress.getRegion().getArea2().getName());
        address.setDong(oldNaverAddress.getRegion().getArea3().getName());
        address.setRi(oldNaverAddress.getRegion().getArea4().getName());

        if (address.getRi().length() > 0) {
            address.setDong(address.getDong() + " " + address.getRi());
        }

        address.setJibun(sanJibun);

        if (!"".equals(oldNaverAddress.getLand().getNumber2())) {
            address.setJibun(address.getJibun() + "-" + oldNaverAddress.getLand().getNumber2());
        }

        // 2. 구주소(행정동) 주소
        if (naverAddresses.size() >= 2) {
            address.setHangDong(naverAddresses.get(1).getRegion().getArea3().getName());

            if (address.getRi().length() > 0) {
                address.setHangDong(address.getHangDong() + " " + address.getRi());
            }
        }

        // 3. 신주소
        if (naverAddresses.size() >= 3) {
            NaverAddress newNaverAddress = naverAddresses.get(2);

            address.setRoadName(newNaverAddress.getLand().getName());
            address.setRoadNum(newNaverAddress.getLand().getNumber1());

            if (!"".equals(newNaverAddress.getLand().getNumber2())) {
                address.setRoadNum(address.getRoadNum() + "-" + newNaverAddress.getLand().getNumber2());
            }
        }

        // 4. 건물명 추가(좌표값이 같을 경우 동일한 주소라고 판단)
        if (String.format("%.6f", address.getLongitude()).equals(String.format("%.6f", passedAddress.getLongitude()))
                && String.format("%.6f", address.getLatitude()).equals(String.format("%.6f", passedAddress.getLatitude()))
                && !"".equals(passedAddress.getRoadDestBuilding())) {
            address.setRoadDestBuilding(passedAddress.getRoadDestBuilding());
        }
    }

    private void completeFinish() {
        Intent intent = new Intent();
        intent.putExtra(ADDRESS, address);
        if (binding.tvNewAddress.getText().length() > 0 ){
            intent.putExtra(CURRENT_ADDRESS, binding.tvNewAddress.getText());
        }else{
            intent.putExtra(CURRENT_ADDRESS, binding.tvNewAddress.getText());
        }
        setResult(RESULT_OK, intent);
        finish();
    }

}