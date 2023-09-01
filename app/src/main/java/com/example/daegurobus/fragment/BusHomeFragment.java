package com.example.daegurobus.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.daegurobus.BusDetailActivity;
import com.example.daegurobus.BusStationDetailActivity;
import com.example.daegurobus.R;
import com.example.daegurobus.adapter.AutoScrolledViewPager;
import com.example.daegurobus.adapter.ImagePagerAdapter;
import com.example.daegurobus.adapter.RouteBookmarkAdapter;
import com.example.daegurobus.adapter.StationBookmarkAdapter;
import com.example.daegurobus.constant.LaunchIntent;
import com.example.daegurobus.model.CommonBoard;
import com.example.daegurobus.model.NoticeList;
import com.example.daegurobus.network.DEFINE;
import com.example.daegurobus.model.RequestBus;
import com.example.daegurobus.model.RouteBookmark;
import com.example.daegurobus.model.RouteStationBookmark;
import com.example.daegurobus.model.StationApi;
import com.example.daegurobus.model.StationBookmark;
import com.example.daegurobus.network.resultInterface.GetNoticeListInterface;
import com.example.daegurobus.network.resultInterface.mainBookmarkInterface;
import com.example.daegurobus.databinding.BusFragmentMainHomeBinding;
import com.example.daegurobus.util.BasicUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import retrofit2.Response;

public class BusHomeFragment extends BusBaseFragment {

    private BusFragmentMainHomeBinding binding;
    private FragmentStateAdapter pagerAdapter;
    private CommonBoard boardNotice;

    private String keyword = "1";
    private int num_page = 4;
    private final Handler handler = new Handler();
    private Runnable updateRunnable;
    private Parcelable recyclerViewState;
    private Parcelable recyclerViewState1;
    private ImagePagerAdapter imagePagerAdapter;
    private RouteBookmarkAdapter routeBookmarkAdapter;
    private StationBookmarkAdapter stationBookmarkAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bus_fragment_main_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding = BusFragmentMainHomeBinding.bind(getView());

        setInit();
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
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            handler.postDelayed(updateRunnable, 0, 30000);
        }
        requestBookmark(keyword);
    }

    private void initLayout() {
//        initEventViewPager();
        requestBookmark(keyword);
//        String savedKeyword = keyword;
        initAutoRefresh(keyword);
        binding.loNoticeText.setOnClickListener(view -> {
//            if (boardNotice != null) {
//                if ("Y".equals(boardNotice.getExtUrlYn())) {
//                    BasicUtil.launchWebsite(getContext(), boardNotice.getUrl2());
//                } else {
//                    Intent intent = new Intent(getContext(), CommonNoticeActivity.class);
//                    intent.putExtra(LaunchIntent.PROJECT, projectType);
//                    intent.putExtra(CommonNoticeActivity.SEQ, boardNotice.getSeq());
//                    intent.putExtra(CommonNoticeActivity.IS_SHOW_LIST_ICON, true);
//                    startActivity(intent);
//                }
//            } else {
//                showToast(getString(R.string.have_no_notice));
//            }
        });

//        binding.loEventText.setOnClickListener(view -> {
//            if (imagePagerAdapter.getItems() != null && imagePagerAdapter.getItems().size() > 0) {
//                CommonBoard commonBoard = imagePagerAdapter.getItem(0);
//
//                if ("Y".equals(commonBoard.getExtUrlYn())) {
//                    BasicUtil.launchWebsite(getContext(), commonBoard.getUrl2());
//                } else {
//                    Intent intent = new Intent(getContext(), CommonEventActivity.class);
//                    intent.putExtra(LaunchIntent.PROJECT, projectType);
//                    intent.putExtra(CommonEventActivity.SEQ, commonBoard.getSeq());
//                    startActivity(intent);
//                }
//            } else {
//                showToast(getString(R.string.have_no_event));
//            }
//        });

        binding.loBusinessAdditionalInfo.setVisibility(View.GONE);

        binding.loBusinessInfo.setOnClickListener(view -> {
            if (binding.loBusinessAdditionalInfo.getVisibility() == View.VISIBLE) {
                binding.loBusinessAdditionalInfo.setVisibility(View.GONE);
                binding.ivBusinessInfoCursor.setImageDrawable(getContext().getDrawable(R.drawable.common_cursor_bottom_16));
            } else {
                binding.loBusinessAdditionalInfo.setVisibility(View.VISIBLE);
                binding.ivBusinessInfoCursor.setImageDrawable(getContext().getDrawable(R.drawable.common_cursor_top_16));

                binding.svForm.post(() -> binding.svForm.fullScroll(ScrollView.FOCUS_DOWN));
            }
        });

    }

//    private void initEventViewPager() {
//        imagePagerAdapter = new ImagePagerAdapter(getContext(), getLayoutInflater());
//        imagePagerAdapter.setOnItemClickListener((view, position) -> {
//            NoticeList noticeList = imagePagerAdapter.getItem(position);
//
//            if ("Y".equals(noticeList.getExtUrlYn())) {
//                BasicUtil.launchWebsite(getContext(), noticeList.getNoticeLinkUrl());
//            } else {
//                startEventActivity(noticeList.getNoticeSeq());
//            }
//        });
//
//        binding.vpEvent.setScrollDurationFactor(AutoScrolledViewPager.SCROLL_DURATION_TIME);
//        binding.vpEvent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                setEventPageIndex();
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                binding.vpEvent.setCurrentPosition(position);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//                switch (state) {
//                    case ViewPager.SCROLL_STATE_IDLE:   // 스크롤이 끝났다거나, 아니면 현재 멈춰져 있을 경우 오토 스크롤 시작.
//                        binding.vpEvent.autoScrollStart();
//                        break;
//
//                    case ViewPager.SCROLL_STATE_DRAGGING:  // 사용자가 손으로 스크롤 시 오토 스크롤 잠깐 정지.
//                        binding.vpEvent.autoScrollStop();
//                        break;
//                }
//            }
//        });
//        binding.vpEvent.setAdapter(imagePagerAdapter);
//        binding.vpEvent.autoScrollStart();
//
//        binding.loViewEventAll.setOnClickListener(view -> {
//            Intent intent = new Intent(getContext(), CommonEventListActivity.class);
//            intent.putExtra(LaunchIntent.PROJECT, projectType);
//            startActivity(intent);
//        });
//    }

    //노선 정보
    private void rvRouteBookmark() {
        routeBookmarkAdapter = new RouteBookmarkAdapter(getContext());
        routeBookmarkAdapter.setOnItemClickListener((View, Position) -> {
            RouteBookmark items = routeBookmarkAdapter.getItem(Position);

            Intent intent;
            intent = new Intent(getActivity(), BusDetailActivity.class);
            intent.putExtra("routeId", items.getRouteId());
            intent.putExtra("routeTp", items.getRouteTp());
            getActivity().startActivity(intent);
        });
        binding.rvRouteBookmark.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.rvRouteBookmark.getLayoutManager().onRestoreInstanceState(recyclerViewState);
        binding.rvRouteBookmark.setAdapter(routeBookmarkAdapter);

    }

    //정류장 정보(정류장 노선 정보)
    private void rvStationBookmark() {
        stationBookmarkAdapter = new StationBookmarkAdapter(getContext());
        stationBookmarkAdapter.setOnItemClickListener((View, Position) -> {
            StationBookmark items = stationBookmarkAdapter.getItem(Position);
            Intent intent;
            intent = new Intent(getActivity(), BusStationDetailActivity.class);
            intent.putExtra("stationId", items.getStationId());
            getActivity().startActivity(intent);

        });
        binding.rvStationBookmark.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.rvStationBookmark.setNestedScrollingEnabled(false);
        binding.rvStationBookmark.getLayoutManager().onRestoreInstanceState(recyclerViewState1);
        binding.rvStationBookmark.setAdapter(stationBookmarkAdapter);


    }

    private void initEamptyView() {
        try {
            if (routeBookmarkAdapter.getItemCount() > 0 || stationBookmarkAdapter.getItemCount() > 0) {
                binding.loBookmarkEmpty.setVisibility(View.GONE);
                binding.loBookmarkRv.setVisibility(View.VISIBLE);

            } else {
                binding.loBookmarkEmpty.setVisibility(View.VISIBLE);
                binding.loBookmarkRv.setVisibility(View.GONE);
            }
        } catch (Exception ex) {
            binding.loBookmarkEmpty.setVisibility(View.VISIBLE);
            binding.loBookmarkRv.setVisibility(View.GONE);
        }


    }


    private void requestBookmark(String query) {
        RequestBus requestBus = new RequestBus(DEFINE.MCODE, DEFINE.GET_MAIN_DATA);
        requestBus.addParam(query);
        requestBus.commit();
        networkPresenter.BusMainBookmark(requestBus, new mainBookmarkInterface() {
            @Override
            public void success(Response<StationApi> response) {

                StationApi result = response.body();
                String str = result.getResultData();
                String[] rows = str.split(DEFINE.TABLE_DELIMETER);

                String row1 = rows[0];

                String[] top = row1.split(DEFINE.ROW_DELIMETER);

                ArrayList<RouteBookmark> routeBookmarkList = new ArrayList<>();
                ArrayList<StationBookmark> stationBookmarkList = new ArrayList<>();
                ArrayList<RouteStationBookmark> routeStationBookmarkList = new ArrayList<>();


                for (int a = 0; a < top.length; a++) {
                    String bus = top[a];

                    String[] route = bus.split(DEFINE.DELIMETER);
                    String routeId = route[0];
                    String routeNum = route[1];
                    String routeNum2 = route[2];
                    String routeTp = route[3];
                    String startNodeName = route[4];
                    String endNodeName = route[5];

                    RouteBookmark routeBookmark = new RouteBookmark();
                    routeBookmark.setRouteId(routeId);
                    routeBookmark.setRouteNum(routeNum);
                    routeBookmark.setRouteNum2(routeNum2);
                    routeBookmark.setRouteTp(routeTp);
                    routeBookmark.setStartnodeName(startNodeName);
                    routeBookmark.setEndtnodeName(endNodeName);
                    routeBookmarkList.add(routeBookmark);
                }

                String row3 = rows[2];
                String[] bottom = row3.split(DEFINE.ROW_DELIMETER);


                for (int a = 1; a < bottom.length; a++) {
                    String bus = bottom[a];
                    RouteStationBookmark routeStationBookmark = new RouteStationBookmark();

                    String[] routeStation = bus.split(DEFINE.DELIMETER);

                    if (routeStation.length > 10) {
                        String routeId = routeStation[0];
                        String routeNum = routeStation[1];
                        String routeNum2 = routeStation[2];
                        String routeTp = routeStation[3];
                        String routeDirection = routeStation[4];
                        String stationId = routeStation[5];
                        String upDowmCd = routeStation[6];
                        String stationOrd = routeStation[7];
                        String stationName = routeStation[8];
                        String stationNo = routeStation[9];
                        String subStationline = routeStation[10];
                        String minArriveTime = routeStation[11];

                        if (routeStation.length > 12) {
                            String ROUTE_INFO = routeStation[12];
                            routeStationBookmark.setRouteInfo(ROUTE_INFO);

                        } else {
                            routeStationBookmark.setRouteInfo(null);
                        }
                        routeStationBookmark.setRouteId(routeId);
                        routeStationBookmark.setRouteNum(routeNum);
                        routeStationBookmark.setRouteNum2(routeNum2);
                        routeStationBookmark.setRouteTp(routeTp);
                        routeStationBookmark.setRouteDirection(routeDirection);
                        routeStationBookmark.setStationId(stationId);
                        routeStationBookmark.setUpDownCd(upDowmCd);
                        routeStationBookmark.setStationOrd(stationOrd);
                        routeStationBookmark.setStationName(stationName);
                        routeStationBookmark.setStationNo(stationNo);
                        routeStationBookmark.setSubStationLine(subStationline);
                        routeStationBookmark.setMinArriveTime(minArriveTime);
                        routeStationBookmarkList.add(routeStationBookmark);
                    } else {

                        String routeId = routeStation[0];
                        String routeNum = routeStation[1];
                        String routeNum2 = routeStation[2];
                        String routeTp = routeStation[3];
                        String routeDirection = routeStation[4];
                        String stationId = routeStation[5];
                        String stationName = routeStation[6];
                        String stationOrd = routeStation[7];
//                    String stationName = routeStation[8];
//                    String stationNo = routeStation[9];
                        String subStationline = routeStation[8];
                        String minArriveTime = routeStation[9];

//                    if (routeStation.length > 12) {
//                        String ROUTE_INFO = routeStation[12];
//                        routeStationBookmark.setRouteInfo(ROUTE_INFO);
//
//                    } else {
//                        routeStationBookmark.setRouteInfo(null);
//                    }
                        routeStationBookmark.setRouteId(routeId);
                        routeStationBookmark.setRouteNum(routeNum);
                        routeStationBookmark.setRouteNum2(routeNum2);
                        routeStationBookmark.setRouteTp(routeTp);
                        routeStationBookmark.setRouteDirection(routeDirection);
                        routeStationBookmark.setStationId(stationId);
//                    routeStationBookmark.setUpDownCd(upDowmCd);
                        routeStationBookmark.setStationOrd(stationOrd);
                        routeStationBookmark.setStationName(stationName);
//                    routeStationBookmark.setStationNo(stationNo);
                        routeStationBookmark.setSubStationLine(subStationline);
                        routeStationBookmark.setMinArriveTime(minArriveTime);
                        routeStationBookmarkList.add(routeStationBookmark);

                    }
                }

                String row2 = rows[1];
                String[] middle = row2.split(DEFINE.ROW_DELIMETER);

                for (int a = 1; a < middle.length; a++) {
                    String bus = middle[a];

                    StationBookmark stationBookmark = new StationBookmark();
                    String[] station = bus.split(DEFINE.DELIMETER);
                    String stationId = station[0];
                    String stationName = station[1];
                    String stationNo = station[2];
                    String stationDirection = station[3];

                    if (station.length > 4) {
                        String SUBWAY_NUM = station[4];
                        stationBookmark.setSubwayNum(SUBWAY_NUM);
                    } else {
                        stationBookmark.setSubwayNum(null);
                    }

                    stationBookmark.setStationId(stationId);
                    stationBookmark.setStationName(stationName);
                    stationBookmark.setStationNo(stationNo);
                    stationBookmark.setStationDirection(stationDirection);
                    //stationBookmark.routeStationBookmarks = new ArrayList<>(routeStationBookmarkList);
                    stationBookmarkList.add(stationBookmark);
                }

                rvRouteBookmark();
                routeBookmarkAdapter.initItems(routeBookmarkList);

                rvStationBookmark();
                stationBookmarkAdapter.initItems(routeStationBookmarkList, stationBookmarkList);

                initEamptyView();
            }

        });

    }


//    private void requestNoticeList(){
//        RequestBus requestBus = new RequestBus(DEFINE.MCODE, DEFINE.GET_NOTICE_LIST);
//        networkPresenter.getNoticeList( requestBus, new GetNoticeListInterface() {
//            @Override
//            public void success(NoticeList noticeList) {
//                imagePagerAdapter.initItems(noticeList);
//            }
//        });
//    }


    public void refreshTab(String query) {
        recyclerViewState = binding.rvRouteBookmark.getLayoutManager().onSaveInstanceState();
        recyclerViewState1 = binding.rvRouteBookmark.getLayoutManager().onSaveInstanceState();
        requestBookmark(query);
    }


    private void initAutoRefresh(String savedQuery) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            handler.postDelayed(updateRunnable, 30000);
        }

        updateRunnable = new Runnable() {

            @Override
            public void run() {
                try {
                    recyclerViewState = binding.rvRouteBookmark.getLayoutManager().onSaveInstanceState();
                    recyclerViewState1 = binding.rvRouteBookmark.getLayoutManager().onSaveInstanceState();
                    requestBookmark(savedQuery);
                    handler.postDelayed(this, 30000);

                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        };

    }

    //    private void startEventActivity(String seq) {
//        Intent intent = new Intent(getContext(), CommonEventActivity.class);
//        intent.putExtra(LaunchIntent.PROJECT, projectType);
//        intent.putExtra(CommonEventActivity.SEQ, seq);
//        startActivity(intent);
//    }
//
//    private void setEventPageIndex() {
//        binding.tvEventCurrent.setText(String.valueOf((binding.vpEvent.getCurrentPosition() % imagePagerAdapter.getItems().size()) + 1));
//        binding.tvEventTotal.setText(String.valueOf(imagePagerAdapter.getItems().size()));
//    }
    private void setInit() {

    }

}