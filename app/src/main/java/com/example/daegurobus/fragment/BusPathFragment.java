package com.example.daegurobus.fragment;

import static android.app.Activity.RESULT_OK;
import static androidx.core.content.ContextCompat.getSystemService;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.VerifiedInputEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daegurobus.BusAddressMapSelectActivity;
import com.example.daegurobus.BusSearchInfoActivity;
import com.example.daegurobus.R;
import com.example.daegurobus.adapter.BusPathAtAdapter;
import com.example.daegurobus.adapter.CommonAddressSearchedAdapter;
import com.example.daegurobus.adapter.SearchPathAdapter;
import com.example.daegurobus.databinding.BusActivityAddressMapSelectBinding;
import com.example.daegurobus.model.BusAddressSearched;
import com.example.daegurobus.model.CommonAddress;
import com.example.daegurobus.model.CommonAddressSearched;
import com.example.daegurobus.model.PathAddress;
import com.example.daegurobus.model.SearchPathV2;
import com.example.daegurobus.network.DEFINE;
import com.example.daegurobus.network.kakao.model.Document;
import com.example.daegurobus.network.kakao.response.ResponseKeyword;
import com.example.daegurobus.network.kakao.resultInterface.KeywordInterface;
import com.example.daegurobus.network.naver.model.geocode.NaverGeoAddress;
import com.example.daegurobus.network.naver.model.reversegeocode.NaverAddress;
import com.example.daegurobus.network.naver.request.RequestGeocoding;
import com.example.daegurobus.network.naver.request.RequestReverseGeocoding;
import com.example.daegurobus.network.naver.resultInterface.GeocodingInterface;
import com.example.daegurobus.network.naver.resultInterface.ReverseGeocodingInterface;
import com.example.daegurobus.network.resultInterface.BusStationAuto;
import com.example.daegurobus.databinding.ActivityBusPathFragmentBinding;
import com.example.daegurobus.model.RequestBus;
import com.example.daegurobus.model.stationResult;
import com.example.daegurobus.network.resultInterface.GetSearchPathV2Interface;
import com.example.daegurobus.util.BasicUtil;
import com.example.daegurobus.util.CommonAddressUtil;
import com.example.daegurobus.util.GpsUtil;
import com.example.daegurobus.widget.CommonNoticeDialog;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;

import java.util.ArrayList;

public class BusPathFragment extends BusBaseFragment {
    public static final String IS_ADDRESS_CHANGED = "IS_ADDRESS_CHANGED";
    private ActivityBusPathFragmentBinding binding;

    private String viewType;

    private static final int REQUEST_ADDRESS_MAP_SELECT_ACTIVITY = 1;
    private static final int REQUEST_ADDRESS_SEARCH_LIST_ACTIVITY = 2;

    private static final String VIEW_TYPE_RECENT_ADDRESS = "VIEW_TYPE_RECENT_ADDRESS";
    private static final String VIEW_TYPE_MAP_SELECT = "VIEW_TYPE_MAP_SELECT";

    private static final String VIEW_TYPE_START_SEARCH = "VIEW_TYPE_START_SEARCH";
    private static final String VIEW_TYPE_END_SEARCH = "VIEW_TYPE_END_SEARCH";

    private BusPathAtAdapter autoComlicationAdapter;

    private String savedKeyword;
    private String CURRENT_ADDRESS = "CURRENT_ADDRESS";
    private String gbn = "1";
    private GpsUtil gpsUtil;
    private PathAddress address = new PathAddress();

    private CommonAddress commonAddress;
    private BusAddressSearched busAddressSearched;
    private CommonAddress passedAddress;
    private String startDirection = "0";
    private String endDirection = "1";

    private CommonAddressSearchedAdapter addressSearchedAdapter;
    private SearchPathAdapter searchPathAdapter;

    private ActivityResultLauncher<Intent> resultLauncher;


    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.activity_bus_path_fragment, container, false);
        initLayout();
        initData();


        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_ADDRESS_MAP_SELECT_ACTIVITY:
            case REQUEST_ADDRESS_SEARCH_LIST_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    commonAddress = data.getExtras().getParcelable(BusAddressMapSelectActivity.ADDRESS);
                    String currentAddress = data.getStringExtra(CURRENT_ADDRESS);

                    if (binding.etKeyword.hasFocus()) {
                        address.setStartLon(Double.toString(commonAddress.getLongitude()));
                        address.setStartLat(Double.toString(commonAddress.getLatitude()));
                        binding.etKeyword.setText(currentAddress);
                    } else {
                        address.setEndLon(Double.toString(commonAddress.getLongitude()));
                        address.setEndLat(Double.toString(commonAddress.getLatitude()));
                        binding.etKeywordEnd.setText(currentAddress);
                    }

                }
                allViewGone();
                availableSearchPath();

                break;
        }
    }

    private void initLayout() {
        initStartEdit();
        initEndEdit();
        initScrollTextView();
        initLoBtn();
    }

    private void initData() {
        gpsUtil = new GpsUtil(getContext(), new GpsUtil.Callback() {
            @Override
            public void getLastLocation(LatLng latLng) {
            }

            @Override
            public void getCurrentLocation(LatLng latLng) {
                hideLoadingAll(binding.loLoading);
                switch (viewType) {
                    case VIEW_TYPE_RECENT_ADDRESS:
                        requestReverseGeocoding(latLng.longitude, latLng.latitude);
                        break;
                    case VIEW_TYPE_MAP_SELECT:
                        CommonAddress address = new CommonAddress();
                        address.setLongitude(latLng.longitude);
                        address.setLatitude(latLng.latitude);

                        if (getActivity() == null) {
                            Log.e("FragmentError", "Fragment가 아직 Activity에 연결되지 않았습니다.");
                        } else {
                            Intent intent = new Intent(getActivity(), BusAddressMapSelectActivity.class);
                            intent.putExtra(BusAddressMapSelectActivity.ADDRESS, address);
                            if (binding.etKeyword.hasFocus()){
                                intent.putExtra("locationPoint", startDirection);
                            }else{
                                intent.putExtra("locationPoint", endDirection);
                            }

                            startActivityForResult(intent, REQUEST_ADDRESS_MAP_SELECT_ACTIVITY);
                        }
                        break;
                }
            }

            @Override
            public void getCurrentLocationTimeout() {
                Activity activity = getActivity();
                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideLoadingAll(binding.loLoading);
                        }
                    });
                }
            }

            @Override
            public void stopLocationUpdate() {

            }
        });
    }


    private void initStartEdit() {

        binding.etKeyword.addTextChangedListener(new TextWatcher() {
            int prevLength = 0;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                prevLength = charSequence.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                refreshInputClearView();
                String startLocation = s.toString();
//                requestGeocodingAuto(startLocation);
                if (startLocation.length() == 0) {
                    binding.rvSearchPath.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() < prevLength) {
                    binding.startKeyWord.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.color_01CAFF)));
                    binding.endKeyWord.setVisibility(View.GONE);
                    binding.igChangeBtnBig.setVisibility(View.GONE);
                    binding.loBtn.setVisibility(View.VISIBLE);
                    binding.etKeywordEnd.setCursorVisible(true);
                    binding.rvSearchPath.setVisibility(View.GONE);
                }
                prevLength = editable.length();
            }
        });

        binding.etKeyword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    binding.startKeyWord.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.color_01CAFF)));
                    binding.igChangeBtnBig.setVisibility(View.GONE);
                    binding.endKeyWord.setVisibility(View.GONE);
                    binding.loBtn.setVisibility(View.VISIBLE);
                    binding.etKeyword.setCursorVisible(true);

                } else {
                    binding.startKeyWord.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.color_E6E6E6)));
                    binding.etKeyword.setCursorVisible(false);
                    allViewGone();
//                    if (binding.etKeyword.getText().toString().length() > 0) {
//                        binding.ivClear.setVisibility(View.VISIBLE);
//                    } else {
//                        binding.ivClear.setVisibility(View.INVISIBLE);
//                    }
                }
            }
        });


        binding.etKeyword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // 엔터키가 눌렸을 때 동작 구현
                    binding.etKeyword.clearFocus();
                    if (binding.etKeyword.getText().length() > 0) {
                        requestGeocoding(binding.etKeyword.getText().toString(), startDirection);
                    }
                    binding.rvAutoSearch.setVisibility(View.GONE);
                    binding.rvAdressSearch.setVisibility(View.VISIBLE);
                    binding.ivClear.setVisibility(View.GONE);
                    requestFocus(v);
                    hideKeyboard(v);
                }
                return false;
            }
        });

        binding.ivClear.setOnClickListener(view -> {
            binding.etKeyword.setText("");
            binding.etKeyword.clearFocus();
            refreshInputClearView();
            showKeyboard(binding.etKeyword);
        });
    }

    private void initEndEdit() {

        binding.etKeywordEnd.addTextChangedListener(new TextWatcher() {
            int prevLength = 0;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                prevLength = charSequence.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                String endLocation = s.toString();
//                requestGeocodingAuto(endLocation);
                if (endLocation.length() == 0) {
                    binding.rvSearchPath.setVisibility(View.GONE);
                }
                refreshInputClearViewEnd();

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() < prevLength) {
                    binding.endKeyWord.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.color_01CAFF)));
                    binding.startKeyWord.setVisibility(View.GONE);
                    binding.igChangeBtnBig.setVisibility(View.GONE);
                    binding.loBtn.setVisibility(View.VISIBLE);
                    binding.etKeywordEnd.setCursorVisible(true);
                    binding.rvSearchPath.setVisibility(View.GONE);
                }
                prevLength = editable.length();
            }
        });

        binding.etKeywordEnd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    binding.endKeyWord.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.color_01CAFF)));
                    binding.startKeyWord.setVisibility(View.GONE);
                    binding.igChangeBtnBig.setVisibility(View.GONE);
                    binding.loBtn.setVisibility(View.VISIBLE);
                    binding.etKeywordEnd.setCursorVisible(true);

                } else {
                    binding.endKeyWord.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.color_E6E6E6)));
                    binding.etKeywordEnd.setCursorVisible(false);
                    allViewGone();
                }
            }
        });


        binding.etKeywordEnd.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // 엔터키가 눌렸을 때 동작 구현
                    binding.etKeywordEnd.clearFocus();
                    requestGeocoding(binding.etKeywordEnd.getText().toString(), endDirection);
                    binding.rvAutoSearch.setVisibility(View.GONE);
                    binding.rvAdressSearch.setVisibility(View.VISIBLE);
                    binding.ivClearEnd.setVisibility(View.GONE);
                    requestFocus(v);
                    hideKeyboard(v);
                }
                return false;
            }
        });

        binding.ivClearEnd.setOnClickListener(view -> {
            binding.etKeywordEnd.setText("");
            binding.etKeywordEnd.clearFocus();
            refreshInputClearViewEnd();
            showKeyboard(binding.etKeywordEnd);
        });
    }

    private void initLoBtn() {
        binding.loRecentAddress.setOnClickListener(View -> {
            viewType = VIEW_TYPE_RECENT_ADDRESS;
            addressSelectByGps();
        });

        binding.loMapSelect.setOnClickListener(View -> {
            viewType = VIEW_TYPE_MAP_SELECT;
            addressSelectByGps();
        });

        binding.igChangeBtnBig.setOnClickListener(View -> {
            changeBtn();
        });
    }

    private void initScrollTextView() {
        binding.loScrollSearchText.setOnClickListener(View -> allViewGone());
        binding.igChangeBtnSmall.setOnClickListener(View -> {
            changeBtn();
        });
    }

    private void changeBtn() {
        //좌표 바꿈
        String startLon = address.getStartLon();
        String startLat = address.getStartLat();
        String endLon = address.getEndLon();
        String endLat = address.getEndLat();

        address.setStartLon(endLon);
        address.setStartLat(endLat);
        address.setEndLon(startLon);
        address.setEndLat(startLat);
        // 주소 바꿈
        String startKeyword = binding.etKeyword.getText().toString();
        String endKeyword = binding.etKeywordEnd.getText().toString();

        binding.etKeyword.setText(endKeyword);
        binding.etKeywordEnd.setText(startKeyword);

        binding.etKeyword.clearFocus();
        binding.etKeywordEnd.clearFocus();

        availableSearchPath();
    }

    private void rvAutocompletion(String keyword) {

        autoComlicationAdapter = new BusPathAtAdapter(getContext());
        autoComlicationAdapter.setOnItemClickListener((View, Position) -> {
            CommonAddressSearched items = autoComlicationAdapter.getItem(Position);

            if (binding.etKeyword.hasFocus()) {
                binding.etKeyword.setText(items.getPlaceName());
            } else {
                binding.etKeywordEnd.setText(items.getPlaceName());
            }
            binding.rvAutoSearch.setVisibility(View.GONE);
//            requestGeocodingAuto(items.getPlaceName());

            hideKeyboard(View);
            requestFocus(View);
        });

        binding.rvAutoSearch.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.rvAutoSearch.setAdapter(autoComlicationAdapter);
        autoComlicationAdapter.setKeyword(keyword);

    }

    private void initAddressSearch() {
        addressSearchedAdapter = new CommonAddressSearchedAdapter(getContext());
        addressSearchedAdapter.setOnItemClickListener((view, position) -> {
            BusAddressSearched item = addressSearchedAdapter.getItem(position);

            busAddressSearched = item;
            // 출발지
            if (item.getDirection().equals("0")) {
                if (item.getNewAddress().length() > 0) {
                    binding.etKeyword.setText(item.getNewAddress());
                } else {
                    binding.etKeyword.setText(item.getOldAddress());
                }
                address.setStartLon(Double.toString(item.getLongitude()));
                address.setStartLat(Double.toString(item.getLatitude()));
            } else {
                //도착지
                if (item.getNewAddress().length() > 0) {
                    binding.etKeywordEnd.setText(item.getNewAddress());
                } else {
                    binding.etKeywordEnd.setText(item.getOldAddress());
                }
                address.setEndLon(Double.toString(item.getLongitude()));
                address.setEndLat(Double.toString(item.getLatitude()));
            }

            hideKeyboard(binding.etKeyword);
            hideKeyboard(binding.etKeywordEnd);
            requestFocus(view);

            if (address.getStartLon() != null && address.getEndLon() != null) {
                binding.rvSearchPath.setVisibility(View.GONE);
                binding.rvAdressSearch.setVisibility(View.VISIBLE);
                requestSearchPath();
            }
            allViewGone();

        });
        binding.rvAdressSearch.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.rvAdressSearch.setAdapter(addressSearchedAdapter);
    }

    private void initSearchPath() {
        searchPathAdapter = new SearchPathAdapter(getContext());
        binding.rvSearchPath.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.rvSearchPath.setAdapter(searchPathAdapter);
        binding.rvSearchPath.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    binding.loScrollSearchView.setVisibility(View.VISIBLE);
                    binding.tvStartLocation.setText(binding.etKeyword.getText());
                    binding.tvEndLocation.setText(binding.etKeywordEnd.getText());
                    binding.loSearchView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void availableSearchPath() {
        if (address.getStartLon().length() > 0 && address.getEndLon().length() > 0) {
            requestSearchPath();
        }
    }

    private void refreshInputClearView() {
        binding.ivClear.setVisibility(binding.etKeyword.getText().toString().length() > 0 ? View.VISIBLE : View.GONE);
    }

    private void refreshInputClearViewEnd() {
        binding.ivClearEnd.setVisibility(binding.etKeywordEnd.getText().toString().length() > 0 ? View.VISIBLE : View.GONE);
    }


    private void showNormalView() {
        allViewGone();
        binding.rvAdressSearch.setVisibility(View.VISIBLE);
    }

    private void showLoadingView() {
        allViewGone();
        showLoading(binding.loLoading);
    }

    private void allViewGone() {
        binding.loSearchView.setVisibility(View.VISIBLE);
        binding.loScrollSearchView.setVisibility(View.GONE);
        binding.loBtn.setVisibility(View.GONE);
        binding.igChangeBtnBig.setVisibility(View.VISIBLE);
        binding.startKeyWord.setVisibility(View.VISIBLE);
        binding.endKeyWord.setVisibility(View.VISIBLE);
        binding.rvAdressSearch.setVisibility(View.GONE);
        binding.rvAutoSearch.setVisibility(View.GONE);
        binding.loResultEmpty.loView.setVisibility(View.GONE);
        hideLoadingAll(binding.loLoading);
    }

    private void showFailView(int iconRes, String failMessage, String failSubMessage) {
        allViewGone();
        binding.loResultEmpty.loView.setVisibility(View.VISIBLE);
//        binding.loResultEmpty.loView.setBackgroundColor(getContext().getColor(R.color.white));
        binding.loResultEmpty.ivIcon.setImageDrawable(getContext().getDrawable(iconRes));
        binding.loResultEmpty.tvFailMessage.setText(failMessage);
        binding.loResultEmpty.tvFailSubMessage.setText(failSubMessage);
        binding.loResultEmpty.ivFailSubCursor1.setVisibility(View.GONE);
        binding.loResultEmpty.ivFailSubCursor2.setVisibility(View.GONE);
    }


    private void requestGeocoding(String keyword, String direction) {
        showLoadingView();

        double lon = Double.parseDouble(getString(R.string.daegu_city_hall_longitude));
        double lat = Double.parseDouble(getString(R.string.daegu_city_hall_latitude));

        // 대구시청 위치로 통일
//        if (myPreferencesManager.getCurrentAddress() == null) {
//            lon = Double.parseDouble(getString(R.string.daegu_city_hall_longitude));
//            lat = Double.parseDouble(getString(R.string.daegu_city_hall_latitude));
//        } else {
//            lon = myPreferencesManager.getCurrentAddress().getLongitude();
//            lat = myPreferencesManager.getCurrentAddress().getLatitude();
//        }

        RequestGeocoding request = new RequestGeocoding();
        request.setLongitude(lon);
        request.setLatitude(lat);
        request.setQuery(keyword);

        naverNetworkPresenter.getGeocoding(request, new GeocodingInterface() {
            @SuppressLint("ResourceType")
            @Override
            public void success(ArrayList<NaverGeoAddress> naverAddresses) {
                ArrayList<BusAddressSearched> items = new ArrayList<>();

                for (int i = 0; i < naverAddresses.size(); i++) {
                    NaverGeoAddress naverGeoAddress = naverAddresses.get(i);

                    BusAddressSearched item = new BusAddressSearched();
                    item.setNewAddress(naverGeoAddress.getRoadAddress());
                    item.setOldAddress(naverGeoAddress.getJibunAddress());
                    item.setLongitude(BasicUtil.safeParseDouble(naverGeoAddress.getX()));
                    item.setLatitude(BasicUtil.safeParseDouble(naverGeoAddress.getY()));
                    item.setDirection(direction);
                    items.add(item);
                }

                if (items.size() > 0) {
                    initAddressSearch();
                    addressSearchedAdapter.setKeyword(keyword);
                    addressSearchedAdapter.initItems(items);
                    showNormalView();
                } else {
                    kakaoNetworkPresenter.keyword(keyword, 1, 15, lon, lat, new KeywordInterface() {
                        @Override
                        public void success(ResponseKeyword response) {
                            for (int i = 0; i < response.getDocuments().size(); i++) {
                                Document document = response.getDocuments().get(i);

                                BusAddressSearched item = new BusAddressSearched();
                                item.setPlaceName(document.getPlaceName());
                                item.setNewAddress(document.getRoadAddressName());
                                item.setOldAddress(document.getAddressName());
                                item.setLongitude(BasicUtil.safeParseDouble(document.getX()));
                                item.setLatitude(BasicUtil.safeParseDouble(document.getY()));
                                item.setDirection(direction);
                                items.add(item);
                            }

                            if (items.size() > 0) {
                                initAddressSearch();
                                addressSearchedAdapter.setKeyword(keyword);
                                addressSearchedAdapter.initItems(items);
                                showNormalView();
                            } else {

                                showFailView(R.drawable.img_dalsoo_none_1, "검색결과가 없습니다.", "");
                            }
                        }

                        @Override
                        public void error(String message) {
                            showFailView(R.drawable.img_dalsoo_none_1, "검색결과가 없습니다.", "");
                        }
                    });
                }
            }

            @Override
            public void error(String message) {
                showFailView(R.drawable.img_dalsoo_none_1, "검색결과가 없습니다.", "");
            }
        });
    }

//    private void requestGeocodingAuto(String keyword) {
//        showLoadingView();
//
//        double lon = Double.parseDouble(getString(R.string.daegu_city_hall_longitude));
//        double lat = Double.parseDouble(getString(R.string.daegu_city_hall_latitude));
//
//        // 대구시청 위치로 통일
////        if (myPreferencesManager.getCurrentAddress() == null) {
////            lon = Double.parseDouble(getString(R.string.daegu_city_hall_longitude));
////            lat = Double.parseDouble(getString(R.string.daegu_city_hall_latitude));
////        } else {
////            lon = myPreferencesManager.getCurrentAddress().getLongitude();
////            lat = myPreferencesManager.getCurrentAddress().getLatitude();
////        }
//
//        RequestGeocoding request = new RequestGeocoding();
//        request.setLongitude(lon);
//        request.setLatitude(lat);
//        request.setQuery(keyword);
//
//        naverNetworkPresenter.getGeocoding(request, new GeocodingInterface() {
//            @SuppressLint("ResourceType")
//            @Override
//            public void success(ArrayList<NaverGeoAddress> naverAddresses) {
//                ArrayList<CommonAddressSearched> items = new ArrayList<>();
//
//                for (int i = 0; i < naverAddresses.size(); i++) {
//                    NaverGeoAddress naverGeoAddress = naverAddresses.get(i);
//
//                    CommonAddressSearched item = new CommonAddressSearched();
//                    item.setNewAddress(naverGeoAddress.getRoadAddress());
//                    item.setOldAddress(naverGeoAddress.getJibunAddress());
//                    item.setLongitude(BasicUtil.safeParseDouble(naverGeoAddress.getX()));
//                    item.setLatitude(BasicUtil.safeParseDouble(naverGeoAddress.getY()));
//                    items.add(item);
//                }
//
//                if (items.size() > 0) {
//                    autoComlicationAdapter.setKeyword(keyword);
//                    autoComlicationAdapter.initItems(items);
//                    showNormalView();
//                } else {
//                    kakaoNetworkPresenter.keyword(keyword, 1, 15, lon, lat, new KeywordInterface() {
//                        @Override
//                        public void success(ResponseKeyword response) {
//                            for (int i = 0; i < response.getDocuments().size(); i++) {
//                                Document document = response.getDocuments().get(i);
//
//                                CommonAddressSearched item = new CommonAddressSearched();
//                                item.setPlaceName(document.getPlaceName());
//                                item.setNewAddress(document.getRoadAddressName());
//                                item.setOldAddress(document.getAddressName());
//                                item.setLongitude(BasicUtil.safeParseDouble(document.getX()));
//                                item.setLatitude(BasicUtil.safeParseDouble(document.getY()));
//                                items.add(item);
//                            }
//
//                            if (items.size() > 0) {
//                                autoComlicationAdapter.setKeyword(keyword);
//                                autoComlicationAdapter.initItems(items);
//                                showNormalView();
//                            } else {
//                                SpannableStringBuilder ssb = new SpannableStringBuilder("'" + keyword + "'에 대한\n검색결과가 없습니다.");
//                                ssb.setSpan(new ForegroundColorSpan(Color.parseColor(getResources().getString(R.color.color_01CAFF))), 0, keyword.length() + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
////                                showFailView(R.drawable.common_no_result, ssb, "");
//                            }
//                        }
//
//                        @Override
//                        public void error(String message) {
////                            showFailView(R.drawable.common_no_result, message, "");
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void error(String message) {
//                showFailView(R.drawable.common_no_result, message, "");
//            }
//        });
//    }

    private void requestSearchPath() {
        showLoadingView();

        RequestBus requestBus = new RequestBus("2", DEFINE.GET_SEARCH_PATH_V2);
        requestBus.addParam("1");
        requestBus.addParam(address.getStartLon());
        requestBus.addParam(address.getStartLat());
        requestBus.addParam(address.getEndLon());
        requestBus.addParam(address.getEndLat());
        requestBus.commit();
        busNetworkPresenter.getSearchPathV2(requestBus, new GetSearchPathV2Interface() {
            @Override
            public void success(ArrayList<SearchPathV2> searchPathV2List) {

                if (searchPathV2List == null) {
                    binding.rvSearchPath.setVisibility(View.GONE);
                } else {
                    initSearchPath();
                    searchPathAdapter.initItems(searchPathV2List);
                    binding.rvSearchPath.setVisibility(View.VISIBLE);
                }

                hideLoadingAll(binding.loLoading);
            }

            @Override
            public void error(String msg) {
                showFailView(R.drawable.img_dalsoo_none_1, "검색결과가 없습니다.", "");
            }

        });
    }


    private void requestReverseGeocoding(double longitude, double latitude) {
        commonAddress = new CommonAddress();
        RequestReverseGeocoding request = new RequestReverseGeocoding();
        request.setLongitude(longitude);
        request.setLatitude(latitude);

        naverNetworkPresenter.getReverseGeocoding(request, new ReverseGeocodingInterface() {
            @Override
            public void success(ArrayList<NaverAddress> naverAddresses) {
                setAddressGeo(naverAddresses, longitude, latitude);

                if (binding.etKeyword.hasFocus()) {
                    address.setStartLon(Double.toString(longitude));
                    address.setStartLat(Double.toString(latitude));
                    binding.etKeyword.setText(CommonAddressUtil.getNewAddress(commonAddress, true, true));
                    binding.etKeyword.clearFocus();
                } else if (binding.etKeywordEnd.hasFocus()) {
                    address.setEndLon(Double.toString(longitude));
                    address.setEndLat(Double.toString(latitude));
                    binding.etKeywordEnd.setText(CommonAddressUtil.getNewAddress(commonAddress, true, true));
                    binding.etKeywordEnd.clearFocus();
                }
                availableSearchPath();
            }

            @Override
            public void error(String message) {
                showFailView(R.drawable.img_dalsoo_none_1, "검색결과가 없습니다.", "");
            }
        });
    }

    private void addressSelectByGps() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            TedPermission.with(getContext())
                    .setPermissionListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted() {
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
            new CommonNoticeDialog(getContext())
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

    private void setAddressGeo(ArrayList<NaverAddress> naverAddresses, double longitude, double latitude) {
        commonAddress = new CommonAddress();
        commonAddress.setLongitude(longitude);
        commonAddress.setLatitude(latitude);

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

        commonAddress.setSido(oldNaverAddress.getRegion().getArea1().getName());
        commonAddress.setGungu(oldNaverAddress.getRegion().getArea2().getName());
        commonAddress.setDong(oldNaverAddress.getRegion().getArea3().getName());
        commonAddress.setRi(oldNaverAddress.getRegion().getArea4().getName());

        if (commonAddress.getRi().length() > 0) {
            commonAddress.setDong(commonAddress.getDong() + " " + commonAddress.getRi());
        }

        commonAddress.setJibun(sanJibun);

        if (!"".equals(oldNaverAddress.getLand().getNumber2())) {
            commonAddress.setJibun(commonAddress.getJibun() + "-" + oldNaverAddress.getLand().getNumber2());
        }

        // 2. 구주소(행정동) 주소
        if (naverAddresses.size() >= 2) {
            commonAddress.setHangDong(naverAddresses.get(1).getRegion().getArea3().getName());

            if (commonAddress.getRi().length() > 0) {
                commonAddress.setHangDong(commonAddress.getHangDong() + " " + commonAddress.getRi());
            }
        }

        // 3. 신주소
        if (naverAddresses.size() >= 3) {
            NaverAddress newNaverAddress = naverAddresses.get(2);

            commonAddress.setRoadName(newNaverAddress.getLand().getName());
            commonAddress.setRoadNum(newNaverAddress.getLand().getNumber1());

            if (!"".equals(newNaverAddress.getLand().getNumber2())) {
                commonAddress.setRoadNum(commonAddress.getRoadNum() + "-" + newNaverAddress.getLand().getNumber2());
            }
        }

        passedAddress = new CommonAddress();

        // 4. 건물명 추가(좌표값이 같을 경우 동일한 주소라고 판단)
        if (String.format("%.6f", commonAddress.getLongitude()).equals(String.format("%.6f", passedAddress.getLongitude()))
                && String.format("%.6f", commonAddress.getLatitude()).equals(String.format("%.6f", passedAddress.getLatitude()))
                && !"".equals(passedAddress.getRoadDestBuilding())) {
            commonAddress.setRoadDestBuilding(passedAddress.getRoadDestBuilding());
        }
    }


}
