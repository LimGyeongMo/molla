package com.example.daegurobus;

import static java.nio.file.Paths.get;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;

import com.example.daegurobus.network.DEFINE;
import com.example.daegurobus.network.NetworkPresenter;
import com.example.daegurobus.network.resultInterface.RecentBusLocationInterface;
import com.example.daegurobus.databinding.BusRouteInfoWindowBinding;
import com.example.daegurobus.model.BusDetailInfo;
import com.example.daegurobus.model.BusDetailInfo1;
import com.example.daegurobus.model.BusDetailTop;
import com.example.daegurobus.geoClient.GeoNetworkPresenter;
import com.example.daegurobus.geoClient.StationLocationInterface;
import com.example.daegurobus.databinding.ActivityBusRouteMapBinding;
import com.example.daegurobus.model.RequestBus;
import com.example.daegurobus.util.GpsUtil;
import com.example.daegurobus.widget.BusBaseActivity;
import com.example.daegurobus.widget.CommonNoticeDialog;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.LatLngBounds;
import com.naver.maps.geometry.Utmk;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.overlay.PathOverlay;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BusRouteMapActivity extends BusBaseActivity {

    private ActivityBusRouteMapBinding binding;

    private MapView mapView;
    private NaverMap naverMap;
    private UiSettings uiSettings;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;

    private FusedLocationSource locationSource;
    private GeoNetworkPresenter geoPresenter;
    private NetworkPresenter Presenter;

    private String routeId;
    private String SavedKeyword;
    private int infoSize;

    private GpsUtil gpsUtil;

    private Context mContext;

    private Marker currentSelectedMarker = null;

    private final Handler handler = new Handler();
    private boolean isClickable = true;
    private Runnable updateRunnable;

    private final String GAN = "1";
    private final String JI = "2";
    private final String SUN = "3";
    private final String FAST = "4";
    private final String WORK = "5";

    private List<Marker> markers = new ArrayList<>();

    private ArrayList<BusDetailInfo> busDetailInfo;
    private BusDetailTop busDetailTop;
    private String gbn = "1";

    private boolean wasClicked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bus_route_map);
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            handler.postDelayed(updateRunnable, 0, 30000);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        if (handler != null && updateRunnable != null) {
            handler.removeCallbacks(updateRunnable);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) {
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            } else {
                naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initData() {
        Intent intent = getIntent();
        routeId = intent.getStringExtra("routeId");
        busDetailTop = (BusDetailTop) intent.getSerializableExtra("busDetailTop");
        busDetailInfo = (ArrayList<BusDetailInfo>) intent.getSerializableExtra("busDetailInfo");
        SavedKeyword = routeId.substring(3);

        gpsUtil = new GpsUtil(this, new GpsUtil.Callback() {
            @Override
            public void getLastLocation(LatLng latLng) {

            }

            @Override
            public void getCurrentLocation(LatLng latLng) {
                if (wasClicked){
                    hideLoadingAll(binding.loLoading);
                    CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(latLng.latitude, latLng.longitude))
                            .animate(CameraAnimation.Fly, 1000)
                            .zoomTo(16);
                    naverMap.moveCamera(cameraUpdate);

                    naverMap.moveCamera(CameraUpdate.scrollTo(latLng));
                }

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

    private void initLayout() {
        geoPresenter = new GeoNetworkPresenter(this);
        Presenter = new NetworkPresenter(this);

        initNaverMap();
        initTitleView();
        initAutoRefresh();

        binding.ivFloating.setOnClickListener(view -> {
            wasClicked = true;
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
                new CommonNoticeDialog(BusRouteMapActivity.this)
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

    private void initTitleView() {
        binding.ivBusLeft1.setOnClickListener(view -> finish());
        binding.ivBusRight1.setOnClickListener(view -> startActivity(new Intent(BusRouteMapActivity.this, BusMainActivity.class)));
        binding.ivFloating.setOnClickListener(View -> {
        });
        binding.tvBusRouteNum.setText(busDetailTop.getRouteNum());

        switch (busDetailTop.getRouteTp()) {
            case GAN:
                binding.tvBusTp.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.color_018EEC)));
                binding.tvBusTp.setText("간선");
                break;
            case JI:
                binding.tvBusTp.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.color_F7B744)));
                binding.tvBusTp.setText("지선");
                break;
            case SUN:
                binding.tvBusTp.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.color_008142)));
                binding.tvBusTp.setText("순환");
                break;
            case FAST:
                binding.tvBusTp.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.color_EB0220)));
                binding.tvBusTp.setText("급행");
                break;
            case WORK:
                binding.tvBusTp.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.color_8AD644)));
                binding.tvBusTp.setText("출근");
                break;
        }

        int time = Integer.valueOf(busDetailTop.getIntervalTime()) / 60; // 평일 간격
        int sun = Integer.valueOf(busDetailTop.getIntervalSaturday()) / 60; // 일요일 간격
        int sat = Integer.valueOf(busDetailTop.getIntervalSaturday()) / 60; // 토요일 간격

        binding.tvIntervalTime.setText("평일 " + time + "분 간격");
        binding.tvNodeName.setText(busDetailTop.getStartnodeName() + "↔" + busDetailTop.getEndnodeName());
        binding.loBusRight2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClickable) {
                    busLocation(gbn);

                    // 5초 동안 텍스트뷰 클릭을 방지
                    isClickable = false;
                    binding.loBusRight2.setEnabled(false);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isClickable = true;
                            binding.loBusRight2.setEnabled(true);
                        }
                    }, 5000);
                }
            }
        });
    }


    private void initNaverMap() {
        mapView = binding.mapView;
        mapView.getMapAsync(mNaverMap -> {
            naverMap = mNaverMap;


            stationMarker();
            initRouteLine();

            naverMap.setLocationSource(locationSource);
            uiSettings = naverMap.getUiSettings();
//            naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
            uiSettings.setScaleBarEnabled(false);
            uiSettings.setZoomControlEnabled(false);
            uiSettings.setLogoClickEnabled(false);
            uiSettings.setRotateGesturesEnabled(false);
            uiSettings.setTiltGesturesEnabled(false);
            uiSettings.setLogoGravity(Gravity.LEFT | Gravity.TOP);

        });

    }


    private void stationMarker() {
        infoSize = busDetailInfo.size();

        double[] stationLat = new double[infoSize];
        double[] stationLon = new double[infoSize];

        for (int i = 0; i < infoSize; i++) {
            String lat = busDetailInfo.get(i).getStationLat();
            String lon = busDetailInfo.get(i).getStationLon();

            stationLat[i] = Double.parseDouble(lat);
            stationLon[i] = Double.parseDouble(lon);
        }

        Marker firstMarkerUp = null;
        Marker firstMarkerDown = null;

        for (int i = 0; i < infoSize; i++) {
            Marker markerUp = new Marker();
            Marker upBookMarker = new Marker();
            Marker markerDown = new Marker();
            Marker downBookMarker = new Marker();


            if ("0".equals(busDetailInfo.get(i).getUpdownCd())) {
                if ("Y".equals(busDetailInfo.get(i).getBookmarkYn())) {
                    upBookMarker.setPosition(new LatLng(stationLat[i], stationLon[i]));
                    upBookMarker.setIcon(OverlayImage.fromResource(R.drawable.ic_f_d_up_stop_25_pt));
                    upBookMarker.setMap(naverMap);
                    upBookMarker.setTag("upBookMarker");
                    upBookMarker.setOnClickListener(markerClickListener);
                } else {
                    markerUp.setPosition(new LatLng(stationLat[i], stationLon[i]));
                    markerUp.setIcon(OverlayImage.fromResource(R.drawable.ic_d_up_stop_25pt));
                    markerUp.setMap(naverMap);
                    markerUp.setTag("markerUp");
                    markerUp.setOnClickListener(markerClickListener);

                    //첫번쨰 상행 저장
                    if (firstMarkerUp == null) {
                        firstMarkerUp = markerUp;
                    }
                    if (markerUp != firstMarkerUp) {
                        markerUp.setMinZoom(14);
                        markerUp.setMinZoomInclusive(false);
                    }
                }
            } else {
                if ("Y".equals(busDetailInfo.get(i).getBookmarkYn())) {
                    downBookMarker.setPosition(new LatLng(stationLat[i], stationLon[i]));
                    downBookMarker.setIcon(OverlayImage.fromResource(R.drawable.ic_f_d_down_stop_25_pt));
                    downBookMarker.setMap(naverMap);
                    downBookMarker.setTag("downBookMarker");
                    downBookMarker.setOnClickListener(markerClickListener);

                } else {
                    markerDown.setPosition(new LatLng(stationLat[i], stationLon[i]));
                    markerDown.setIcon(OverlayImage.fromResource(R.drawable.ic_d_down_stop_25_pt));
                    markerDown.setMap(naverMap);
                    markerDown.setTag("markerDown");
                    markerDown.setOnClickListener(markerClickListener);

                    //첫번쨰 하행 저장
                    if (firstMarkerDown == null) {
                        firstMarkerDown = markerDown;
                    }
                    if (markerDown != firstMarkerDown) {
                        markerDown.setMinZoom(14);
                        markerDown.setMinZoomInclusive(false);
                    }
                }
            }

        }

        busLocation(gbn);
    }

    private void busLocation(String gbn) {
        RequestBus requestBus = new RequestBus(DEFINE.MCODE, DEFINE.GET_BUS_LOCATION_LIST);
        requestBus.addParam(gbn);
        requestBus.addParam(routeId);
        requestBus.commit();

        Presenter.recentBusLocation(requestBus, new RecentBusLocationInterface() {
            @Override
            public void success(ArrayList<BusDetailInfo1> items) {
                clearAllMarkers();

                HashMap<String, BusDetailInfo1> vehicleLocationMap = new HashMap<>();

                for (BusDetailInfo1 item : items) {
                    vehicleLocationMap.put(item.getStationId1(), item);
                }

                for (BusDetailInfo stationInfo : busDetailInfo) {
                    if (!vehicleLocationMap.containsKey(stationInfo.getStationId())) {
                        continue;
                    }

                    BusDetailInfo1 item = vehicleLocationMap.get(stationInfo.getStationId());

                    String lat = stationInfo.getStationLat();
                    String lon = stationInfo.getStationLon();

                    InfoWindow infoWindow = new InfoWindow();
                    infoWindow.setAdapter(new InfoWindow.ViewAdapter() {

                        @Override
                        public View getView(@NonNull InfoWindow infoWindow) {
                            BusRouteInfoWindowBinding binding = BusRouteInfoWindowBinding.inflate(getLayoutInflater());
                            binding.tvBusNum.setText(item.getBusNum().substring(5));

                            return binding.getRoot();
                        }
                    });

                    Marker busMarker = new Marker();
                    busMarker.setPosition(new LatLng(Double.parseDouble(lat), Double.parseDouble(lon)));

                    switch (busDetailTop.getRouteTp()) {
                        case GAN:
                            busMarker.setIcon(OverlayImage.fromResource(R.drawable.ic_bus_color_36_pt_blue));
                            break;
                        case JI:
                            busMarker.setIcon(OverlayImage.fromResource(R.drawable.ic_bus_color_36_pt_yellow));
                            break;
                        case SUN:
                            busMarker.setIcon(OverlayImage.fromResource(R.drawable.ic_bus_color_36_pt_green));
                            break;
                        case FAST:
                            busMarker.setIcon(OverlayImage.fromResource(R.drawable.ic_bus_color_36_pt_red));
                            break;
                        case WORK:
                            busMarker.setIcon(OverlayImage.fromResource(R.drawable.ic_bus_color_36_pt_light_green));
                            break;
                    }

                    busMarker.setMinZoom(14);
                    busMarker.setMinZoomInclusive(false);
                    busMarker.setMap(naverMap);
                    markers.add(busMarker);
                    infoWindow.open(busMarker);
                }
            }

            public void error(String msg) {
                System.out.println(msg);
            }

        });
    }

    private Overlay.OnClickListener markerClickListener = new Overlay.OnClickListener() {
        @Override
        public boolean onClick(@NonNull Overlay overlay) {

            Marker clickedMarker = (Marker) overlay;
            // 마커 원래대로 변경
            if (currentSelectedMarker != null) {
                if (currentSelectedMarker.getTag().equals("markerUp")) {
                    currentSelectedMarker.setIcon(OverlayImage.fromResource(R.drawable.ic_d_down_stop_25_pt));
                } else if (currentSelectedMarker.getTag().equals("markerDown")) {
                    currentSelectedMarker.setIcon(OverlayImage.fromResource(R.drawable.ic_d_up_stop_25pt));
                } else if (currentSelectedMarker.getTag().equals("upBookMarker")) {
                    currentSelectedMarker.setIcon(OverlayImage.fromResource(R.drawable.ic_f_d_up_stop_25_pt));
                } else if (currentSelectedMarker.getTag().equals("downBookMarker")) {
                    currentSelectedMarker.setIcon(OverlayImage.fromResource(R.drawable.ic_f_d_down_stop_25_pt));
                }
            }

            // 클릭된 마커 변경
            if (clickedMarker.getTag().equals("markerUp")) {
                clickedMarker.setIcon(OverlayImage.fromResource(R.drawable.ic_select_stop_40_pt));
                LatLng markerPosition = clickedMarker.getPosition();
                double clickedLatitude = markerPosition.latitude;
                double clickedLongitude = markerPosition.longitude;
                stationInfo(clickedLatitude, clickedLongitude);
                naverMap.moveCamera(CameraUpdate.scrollTo(markerPosition));
            } else if (clickedMarker.getTag().equals("markerDown")) {
                clickedMarker.setIcon(OverlayImage.fromResource(R.drawable.ic_select_stop_40_pt));
                LatLng markerPosition = clickedMarker.getPosition();
                double clickedLatitude = markerPosition.latitude;
                double clickedLongitude = markerPosition.longitude;
                stationInfo(clickedLatitude, clickedLongitude);

                naverMap.moveCamera(CameraUpdate.scrollTo(markerPosition));
            } else if (clickedMarker.getTag().equals("upBookMarker")) {
                clickedMarker.setIcon(OverlayImage.fromResource(R.drawable.ic_f_select_stop_40_pt));
                LatLng markerPosition = clickedMarker.getPosition();
                double clickedLatitude = markerPosition.latitude;
                double clickedLongitude = markerPosition.longitude;
                stationInfo(clickedLatitude, clickedLongitude);

                naverMap.moveCamera(CameraUpdate.scrollTo(markerPosition));
            } else if (clickedMarker.getTag().equals("downBookMarker")) {
                clickedMarker.setIcon(OverlayImage.fromResource(R.drawable.ic_f_select_stop_40_pt));
                LatLng markerPosition = clickedMarker.getPosition();
                double clickedLatitude = markerPosition.latitude;
                double clickedLongitude = markerPosition.longitude;
                stationInfo(clickedLatitude, clickedLongitude);

                naverMap.moveCamera(CameraUpdate.scrollTo(markerPosition));
            }

            // 현재 선택된 마커를 업데이트
            currentSelectedMarker = clickedMarker;
            return true;

        }

        private void stationInfo(double clickedLatitude, double clickedLongitude) {

            for (int i = 0; i < busDetailInfo.size(); i++) {
                String lat = busDetailInfo.get(i).getStationLat();
                String lon = busDetailInfo.get(i).getStationLon();

                if (lat.equals(String.valueOf(clickedLatitude)) && lon.equals(String.valueOf(clickedLongitude))) {

                    binding.tvNodeName.setText(busDetailInfo.get(i).getStationName());
                    binding.tvVehicleTime.setText(busDetailInfo.get(i).getStationNo());
                    binding.tvIntervalTime.setText(busDetailInfo.get(i).getStationDirection());
                    binding.vTimeDot.setVisibility(View.GONE);
                    binding.tvBreaktime.setVisibility(View.GONE);

                    switch (busDetailInfo.get(i).getSubwayNum()) {
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

                    if ("Y".equals(busDetailInfo.get(i).getBookmarkYn())) {
                        binding.igBookmark.setImageResource(R.drawable.common_on_star_40);
                    } else {
                        binding.igBookmark.setImageResource(R.drawable.common_off_star_40);
                    }

                    String stationId = busDetailInfo.get(i).getStationId();

                    binding.loDetailBtn.setVisibility(View.VISIBLE);
                    binding.loDetailBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent;
                            intent = new Intent(v.getContext(), BusStationDetailActivity.class);
                            intent.putExtra("stationId", stationId);
                            v.getContext().startActivity(intent);
                        }
                    });
                }

            }
        }
    };

    private void initRouteLine() {

        geoPresenter.RouteLine(SavedKeyword, new StationLocationInterface() {
            @Override
            public void success(ArrayList<ArrayList<Double>> routeAll, ArrayList<ArrayList<Double>> Downward, ArrayList<ArrayList<Double>> uphill) {

                ArrayList<LatLng> latLngAll = new ArrayList<>();
                ArrayList<LatLng> latLngUphill = new ArrayList<>();
                ArrayList<LatLng> latLngDownward = new ArrayList<>();

                if (routeAll != null) {
                    for (ArrayList<Double> route : routeAll) {
                        Utmk utmk = new Utmk(route.get(0), route.get(1));
                        latLngAll.add(utmk.toLatLng());
                    }
                } else {
                    for (ArrayList<Double> route : uphill) {
                        Utmk utmk = new Utmk(route.get(0), route.get(1));
                        latLngAll.add(utmk.toLatLng());
                    }
                }

                if (uphill != null) {
                    for (int i = 0; i < uphill.size(); i++) {
                        double routeLat = uphill.get(i).get(0);
                        double routeLon = uphill.get(i).get(1);

                        Utmk utmk = new Utmk(routeLat, routeLon);
                        LatLng latLng = utmk.toLatLng();
                        latLngUphill.add(latLng);;
                    }

                    PathOverlay pathTop = new PathOverlay();

                    pathTop.setCoords(latLngUphill);
                    pathTop.setWidth(30);
                    pathTop.setColor(getResources().getColor(R.color.color_00A1E9));
                    pathTop.setPatternImage(OverlayImage.fromResource(R.drawable.ic_path_arrow_8_pt));
                    pathTop.setPatternInterval(40);
                    pathTop.setOutlineColor(getResources().getColor(R.color.transparent));
                    pathTop.setMap(naverMap);
                }

                for (int i = 0; i < Downward.size(); i++) {
                    double routeLat = Downward.get(i).get(0);
                    double routeLon = Downward.get(i).get(1);

                    Utmk utmk = new Utmk(routeLat, routeLon);
                    LatLng latLng = utmk.toLatLng();
                    latLngDownward.add(latLng);
                }

                PathOverlay pathBottom = new PathOverlay();

                pathBottom.setCoords(latLngDownward);
                pathBottom.setWidth(30);
                pathBottom.setColor(getResources().getColor(R.color.color_0049AF));
                pathBottom.setPatternImage(OverlayImage.fromResource(R.drawable.ic_path_arrow_8_pt));
                pathBottom.setPatternInterval(40);
                pathBottom.setOutlineColor(getResources().getColor(R.color.transparent));
                pathBottom.setMap(naverMap);

                naverCamera(latLngAll);

            }
        });
    }

    private void naverCamera(ArrayList<LatLng> routeAll) {
        ArrayList<Double> lat = new ArrayList<Double>();
        ArrayList<Double> lon = new ArrayList<Double>();

        for (LatLng latLng : routeAll) {
            lat.add(latLng.latitude);
            lon.add(latLng.longitude);
        }

        double maxLat = 0;
        double minLat = 1000;

        double maxLon = 0;
        double minLon = 1000;

        for (double x : lat) {
            if (minLat > x) {
                minLat = x;
            }

            if (maxLat < x) {
                maxLat = x;
            }
        }

        for (double y : lon) {
            if (minLon > y) {
                minLon = y;
            }

            if (maxLon < y) {
                maxLon = y;
            }
        }

        LatLng southWest = new LatLng(minLat, minLon);
        LatLng northEast = new LatLng(maxLat, maxLon);
        LatLngBounds bounds = new LatLngBounds(southWest, northEast);

        CameraUpdate cameraUpdate = CameraUpdate.fitBounds(bounds, 50)
                .animate(CameraAnimation.Fly, 1000);
        naverMap.moveCamera(cameraUpdate);
    }


    private void initAutoRefresh() {
        //30초 마다 반복
        updateRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    busLocation(gbn);
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

    private void clearAllMarkers() {
        // 마커를 지도에서 제거
        for (Marker marker : markers) {
            marker.setMap(null);
        }
        markers.clear();
    }
}


