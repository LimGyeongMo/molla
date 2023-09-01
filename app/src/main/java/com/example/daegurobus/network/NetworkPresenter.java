package com.example.daegurobus.network;

import android.content.Context;
import android.util.Log;


import com.example.daegurobus.model.NoticeList;
import com.example.daegurobus.model.SearchPathV2;
import com.example.daegurobus.network.resultInterface.GetNoticeListInterface;
import com.example.daegurobus.network.resultInterface.GetSearchPathV2Interface;
import com.example.daegurobus.network.resultInterface.RecentBusLocationInterface;
import com.example.daegurobus.model.StationDetailInfoSub;
import com.example.daegurobus.network.resultInterface.AuthBaseInterface;
import com.example.daegurobus.network.resultInterface.BusDetailInterface;
import com.example.daegurobus.network.resultInterface.BusStationAuto;
import com.example.daegurobus.network.resultInterface.BusStationDetailInterface;
import com.example.daegurobus.model.BusDetailInfo;
import com.example.daegurobus.model.BusDetailInfo1;
import com.example.daegurobus.model.BusDetailTop;
import com.example.daegurobus.model.BusResultSearch;
import com.example.daegurobus.model.RequestBus;
import com.example.daegurobus.model.StationApi;
import com.example.daegurobus.model.StationDetailInfo;
import com.example.daegurobus.model.StationDetailTop;
import com.example.daegurobus.model.StationNearByInfo;
import com.example.daegurobus.model.stationResult;
import com.example.daegurobus.network.resultInterface.StationSurroundInfo;
import com.example.daegurobus.network.resultInterface.mainBookmarkInterface;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkPresenter {

    private Context context;

    public NetworkPresenter(Context context) {
        this.context = context;
    }

    public void BusMainBookmark(RequestBus requestBus, mainBookmarkInterface anInterface) {

        Retrofit2Client
                .getClient(context)
                .busMainBookmark(requestBus)
                .enqueue(new Callback<StationApi>() {
                    @Override
                    public void onResponse(Call<StationApi> call, Response<StationApi> response) {
                        try {

                            anInterface.success(response);


                        } catch (Exception e) {
                            System.out.println(e);


                        }
                    }

                    @Override
                    public void onFailure(Call<StationApi> call, Throwable t) {

                    }
                });
    }


    public void GetBusList(RequestBus requestBus, BusInterface anInterface) {

        Retrofit2Client
                .getClient(context)
                .getBusList(requestBus)
                .enqueue(new Callback<StationApi>() {
                    @Override
                    public void onResponse(Call<StationApi> call, Response<StationApi> response) {
                        try {
                            String datalist = requestBus.getRequestData();
                            String[] gbn = datalist.split(DEFINE.FD_DELIMETER);

                            if ("1".equals(gbn[0])) {
                                StationApi result = response.body();
                                String str = result.getResultData();
                                String[] rows = str.split(DEFINE.ROW_DELIMETER);

                                ArrayList<BusResultSearch> busSearchList = new ArrayList<>();

                                for (int i = 0; i < rows.length; i++) {
                                    String row = rows[i];

                                    String[] route = row.split(DEFINE.DELIMETER); // [211, DGG, 618, "", 인]
                                    String ROUTE_CODE = route[0];
                                    String routeId = route[1];
                                    String ROUTE_NUM = route[2];
                                    String ROUTE_NUM2 = route[3];
                                    String STARTNODE_NAME = route[4];
                                    String ENDNODE_NAME = route[5];

                                    BusResultSearch busResultSearch = new BusResultSearch();
                                    busResultSearch.setRouteNum(ROUTE_NUM);
                                    busResultSearch.setRouteId(routeId);
                                    busResultSearch.setRouteNum2(ROUTE_NUM2);
                                    busResultSearch.setStartNodeName(STARTNODE_NAME);
                                    busResultSearch.setEndNodeName(ENDNODE_NAME);
                                    busSearchList.add(busResultSearch);
                                }

                                anInterface.sucsess(busSearchList);

                            } else {
                                StationApi result = response.body();
                                String str = result.getResultData();
                                String[] rows = str.split(DEFINE.ROW_DELIMETER);

                                ArrayList<BusResultSearch> busSearchList = new ArrayList<>();

                                for (int i = 0; i < rows.length; i++) {
                                    String row = rows[i];

                                    String[] route = row.split(DEFINE.DELIMETER); // [211, DGG, 618, "", 인]
                                    String ROUTE_CODE = route[0];
                                    String ROUTE_ID = route[1];
                                    String ROUTE_NUM = route[2];
                                    String ROUTE_NUM2 = route[3];
                                    String ROUTE_TP = route[4];
                                    String STARTNODE_NAME = route[5];
                                    String ENDNODE_NAME = route[6];
                                    String BOOK_MARK_YN = route[7];

                                    BusResultSearch busResultSearch = new BusResultSearch();
                                    busResultSearch.setRouteCode(ROUTE_CODE);
                                    busResultSearch.setRouteId(ROUTE_ID);
                                    busResultSearch.setRouteNum(ROUTE_NUM);
                                    busResultSearch.setRouteNum2(ROUTE_NUM2);
                                    busResultSearch.setStartNodeName(STARTNODE_NAME);
                                    busResultSearch.setEndNodeName(ENDNODE_NAME);
                                    busResultSearch.setRouteTp(ROUTE_TP);
                                    busResultSearch.setBookMark_yn(BOOK_MARK_YN);
                                    busSearchList.add(busResultSearch);
                                }

                                anInterface.sucsess(busSearchList);
                            }

                        } catch (Exception e) {
                            System.out.println(e);
                            anInterface.error("검색결과가 없습니다");
                        }
                    }

                    @Override
                    public void onFailure(Call<StationApi> call, Throwable t) {
                        anInterface.error("검색결과가 없습니다");
                    }
                });
    }

    public void getStationList(RequestBus requestBus, BusStationAuto anInterface) {

        Retrofit2Client
                .getClient(context)
                .busStationAuto(requestBus)
                .enqueue(new Callback<StationApi>() {
                    @Override
                    public void onResponse(Call<StationApi> call, Response<StationApi> response) {
                        try {

                            ArrayList<stationResult> stationList = new ArrayList<>();

                            String datalist = requestBus.getRequestData();
                            String[] gbn = datalist.split(DEFINE.FD_DELIMETER);

                            if ("1".equals(gbn[0])) {
                                StationApi result = response.body();
                                String str = result.getResultData();
                                String[] rows = str.split(DEFINE.ROW_DELIMETER);
                                for (int i = 0; i < rows.length; i++) {
                                    String row = rows[i];

                                    String[] route = row.split(DEFINE.DELIMETER); // [211, DGG, 618, "", 인]
                                    String stationcode = route[0];
                                    String stationId = route[1];
                                    String stationName = route[2];
                                    String stationDirection = route[3];

                                    stationResult stationResult = new stationResult();
                                    stationResult.setStationCode(stationcode);
                                    stationResult.setStationId(stationId);
                                    stationResult.setStationName(stationName);
                                    stationResult.setStationDirection(stationDirection);
                                    stationList.add(stationResult);
                                }
                                anInterface.success(stationList);

                            } else {
                                try {
                                    StationApi result = response.body();
                                    String str = result.getResultData();
                                    String[] rows = str.split(DEFINE.ROW_DELIMETER);

                                    for (int i = 0; i < rows.length; i++) {
                                        String row = rows[i];

                                        String[] route = row.split(DEFINE.DELIMETER); // [211, DGG, 618, "", 인]
                                        String STATION_CODE = route[0];
                                        String STATION_ID = route[1];
                                        String STATION_NAME = route[2];
                                        String STATION_NO = route[3];
                                        String STATION_DIRECTION = route[4];
                                        String KEYWORD = route[5];
                                        String SUBWAY_NUM = route[6];
                                        String BOOK_MARK_YN = route[7];

                                        stationResult busResultSearch = new stationResult();
                                        busResultSearch.setStationCode(STATION_CODE);
                                        busResultSearch.setStationId(STATION_ID);
                                        busResultSearch.setStationName(STATION_NAME);
                                        busResultSearch.setStationNo(STATION_NO);
                                        busResultSearch.setStationDirection(STATION_DIRECTION);
                                        busResultSearch.setKeyword(KEYWORD);
                                        busResultSearch.setSubwayNum(SUBWAY_NUM);
                                        busResultSearch.setBookMark(BOOK_MARK_YN);
                                        stationList.add(busResultSearch);

                                    }
                                    anInterface.success(stationList);
                                } catch (Exception e) {
                                    anInterface.error("검색결과가 없습니다");
                                }
                            }
                        } catch (Exception e) {
                            anInterface.error("검색결과가 없습니다");
                        }
                    }

                    @Override
                    public void onFailure(Call<StationApi> call, Throwable t) {
                    }
                });
    }
    public void busDetail(RequestBus requestBus, BusDetailInterface anInterface) {

        Retrofit2Client
                .getClient(context)
                .busDetail(requestBus)
                .enqueue(new Callback<StationApi>() {
                    @Override
                    public void onResponse(Call<StationApi> call, Response<StationApi> response) {
                        try {

                            StationApi result = response.body();
                            String str = result.getResultData();
                            String[] rows = str.split(DEFINE.TABLE_DELIMETER);

                            BusDetailTop busDetailTop = new BusDetailTop();
                            ArrayList<BusDetailInfo> item = new ArrayList<>();
                            ArrayList<BusDetailInfo1> items = new ArrayList<>();

                            String row1 = rows[0];

                            String[] top = row1.split(DEFINE.DELIMETER);// [211, DGG, 618, "", 인]
                            String ROUTE_CODE = top[0];
                            String ROUTE_ID = top[1];
                            String ROUTE_NUM = top[2];
                            String ROUTE_NUM2 = top[3];
                            String ROUTE_TP = top[4];
                            String ROUTE_TP1 = top[5];
                            String ROUTE_DIRECTION = top[6];
                            String STARTNODE_NAME = top[7];
                            String ENDNODE_NAME = top[8];
                            String STARTVIHICLE_TIME = top[9];
                            String ENDVIHICLE_TIME = top[10];
                            String INTERVAL_TIME = top[11];
                            String INTERVAL_SATURDAY = top[12];
                            String INTERVAL_SUNDAY = top[13];
                            String INTERVAL_HOLIDAY = top[14];
                            String BUS_BOOK_MARK_YN = top[15];

                            busDetailTop.setRouteCode(ROUTE_CODE);
                            busDetailTop.setRouteId(ROUTE_ID);
                            busDetailTop.setRouteNum(ROUTE_NUM);
                            busDetailTop.setRouteNum2(ROUTE_NUM2);
                            busDetailTop.setRouteTp(ROUTE_TP);
                            busDetailTop.setRouteTp1(ROUTE_TP1);
                            busDetailTop.setRouteDirection(ROUTE_DIRECTION);
                            busDetailTop.setStartnodeName(STARTNODE_NAME);
                            busDetailTop.setEndnodeName(ENDNODE_NAME);
                            busDetailTop.setStartVihicleTime(STARTVIHICLE_TIME);
                            busDetailTop.setEndVihicleTime(ENDVIHICLE_TIME);
                            busDetailTop.setIntervalTime(INTERVAL_TIME);
                            busDetailTop.setIntervalSaturday(INTERVAL_SATURDAY);
                            busDetailTop.setIntervalSunday(INTERVAL_SUNDAY);
                            busDetailTop.setIntervalHoliday(INTERVAL_HOLIDAY);
                            busDetailTop.setBookmarkYn(BUS_BOOK_MARK_YN);

                            String row2 = rows[1];
                            String[] info = row2.split(DEFINE.ROW_DELIMETER);

                            BusDetailInfo busDetailInfo[] = new BusDetailInfo[info.length];
                            HashMap<String, Integer> map = new HashMap<String, Integer>();
                            map.clear();

                            for (int a = 1; a < info.length; a++) {
                                String bus = info[a];

                                String[] detail = bus.split(DEFINE.DELIMETER);
                                String ROUTE_ID1 = detail[0];
                                String STATION_ID = detail[1];
                                String STATION_NAME = detail[2];
                                String STATION_NO = detail[3];
                                String UPDOWM_CD = detail[4];
                                String STATION_ORD = detail[5];
                                String STATION_DIRECTION = detail[6];
                                String STATION_LON = detail[7];
                                String STATION_LAT = detail[8];
                                String ROUTE_DATE = detail[9];
                                String STARTVEHICLE_TIME = detail[10];
                                String ENDVEHICLE_TIME = detail[11];
                                String SUBWAY_NUM = detail[12];
                                String TRAFFIC_LEVEL = detail[13];
                                String STATION_BOOK_MARK_YN = detail[14];
                                String STATION_WIFI_YN = detail[15];

                                busDetailInfo[a] = new BusDetailInfo();
                                busDetailInfo[a].setRouteId(ROUTE_ID1);
                                busDetailInfo[a].setStationId(STATION_ID);
                                busDetailInfo[a].setStationName(STATION_NAME);
                                busDetailInfo[a].setStationNo(STATION_NO);
                                busDetailInfo[a].setUpdownCd(UPDOWM_CD);
                                busDetailInfo[a].setStationOrd(STATION_ORD);
                                busDetailInfo[a].setStationDirection(STATION_DIRECTION);
                                busDetailInfo[a].setStationLon(STATION_LON);
                                busDetailInfo[a].setStationLat(STATION_LAT);
                                busDetailInfo[a].setRouteDate(ROUTE_DATE);
                                busDetailInfo[a].setStartvehicleTime(STARTVEHICLE_TIME);
                                busDetailInfo[a].setEndvehicleTime(ENDVEHICLE_TIME);
                                busDetailInfo[a].setSubwayNum(SUBWAY_NUM);
                                busDetailInfo[a].setTrafficLevel(TRAFFIC_LEVEL);
                                busDetailInfo[a].setBookmarkYn(STATION_BOOK_MARK_YN);
                                busDetailInfo[a].setStationWifiYn(STATION_WIFI_YN);
                                map.put(STATION_ID, a);
                            }

                            String row3 = rows[2];
                            String[] busInfo = row3.split(DEFINE.ROW_DELIMETER);
                            for (int b = 1; b < busInfo.length; b++) {
                                String recentBus = busInfo[b];

                                BusDetailInfo1 busDetailInfo1 = new BusDetailInfo1();
                                String[] detailBus = recentBus.split(DEFINE.DELIMETER);
                                String ROUTE_ID1 = detailBus[0];
                                String ROUTE_NUM1 = detailBus[1];
                                String BUS_NUM1 = detailBus[2];
                                String VEHICLE_TP = detailBus[3];
                                String ROUTE_WIFI_YN = detailBus[4];
                                String STATION_ID1 = detailBus[5];
                                String STATION_SORT = detailBus[6];
                                if (detailBus.length > 7) {
                                    String START_TIME = detailBus[7];
                                    busDetailInfo1.setStartTime(START_TIME);
                                    if (detailBus.length > 8) {
                                        String MOD_DATE = detailBus[8];
                                        busDetailInfo1.setModDate(MOD_DATE);
                                    } else {
                                        busDetailInfo1.setModDate(null);
                                    }
                                } else {
                                    busDetailInfo1.setModDate(null);
                                    busDetailInfo1.setStartTime(null);
                                }


                                busDetailInfo1.setRouteId(ROUTE_ID1);
                                busDetailInfo1.setRouteNm(ROUTE_NUM1);
                                busDetailInfo1.setBusNum(BUS_NUM1);
                                busDetailInfo1.setVehicleTp(VEHICLE_TP);
                                busDetailInfo1.setRouteWifiYn(ROUTE_WIFI_YN);
                                busDetailInfo1.setStationId1(STATION_ID1);
                                busDetailInfo1.setStationSort(STATION_SORT);
                                items.add(busDetailInfo1);

                                try {
                                    Integer index = map.get(STATION_ID1);

                                    if (index == null) {

                                        busDetailInfo[0].BusVisbleYn = "";
                                    } else if (index < 0 || index >= busDetailInfo.length) {

                                        busDetailInfo[0].BusVisbleYn = "";
                                    } else if (busDetailInfo[index] == null) {

                                        busDetailInfo[0].BusVisbleYn = "N";
                                    } else {
                                        busDetailInfo[index].BusVisbleYn = "Y";
                                        busDetailInfo[map.get(STATION_ID1)].setBusNum(BUS_NUM1);
                                        busDetailInfo[map.get(STATION_ID1)].RouteTp = busDetailTop.getRouteTp();
                                    }
                                } catch(Exception e) {
                                    System.out.println(e);
                                }


                            }

                            for (int a = 1; a < info.length; a++) {
                                item.add(busDetailInfo[a]);
                            }

                            anInterface.success(busDetailTop, item, items);

                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }

                    @Override
                    public void onFailure(Call<StationApi> call, Throwable t) {
                    }
                });
    }

    public void busStationDetail(RequestBus requestBus, BusStationDetailInterface anInterface) {

        Retrofit2Client
                .getClient(context)
                .busStationDetail(requestBus)
                .enqueue(new Callback<StationApi>() {
                    @Override
                    public void onResponse(Call<StationApi> call, Response<StationApi> response) {
                        try {

                            StationApi result = response.body();
                            String str = result.getResultData();
                            String[] rows = str.split(DEFINE.TABLE_DELIMETER);

                            StationDetailTop stationDetailTop = new StationDetailTop();
                            ArrayList<StationDetailInfo> items = new ArrayList<>();
                            ArrayList<StationDetailInfoSub> itemSub = new ArrayList<>();


                            String row1 = rows[0];

                            String[] top = row1.split(DEFINE.DELIMETER);
                            String STATION_CODE = top[0];
                            String stationId = top[1];
                            String stationName = top[2];
                            String STATION_NO = top[3];
                            String STATION_DIRECTION = top[4];
                            String KEYWORD = top[5];
                            String SUBWAY_NUM = top[6];
                            String STATION_LON = top[7];
                            String STATION_LAT = top[8];
                            String STATION_WIFI_YN = top[9];
                            String STATION_BOOK_MARK_YN = top[10];
                            if (top.length > 11) {
                                String COMMING_SOON = top[11];
                                stationDetailTop.setComingSoon(COMMING_SOON);
                            } else {
                                stationDetailTop.setComingSoon(null);
                            }
                            stationDetailTop.setStationCode(STATION_CODE);
                            stationDetailTop.setStationId(stationId);
                            stationDetailTop.setStationName(stationName);
                            stationDetailTop.setStationNo(STATION_NO);
                            stationDetailTop.setStationDirection(STATION_DIRECTION);
                            stationDetailTop.setKeyword(KEYWORD);
                            stationDetailTop.setSubwayNum(SUBWAY_NUM);
                            stationDetailTop.setStationLon(STATION_LON);
                            stationDetailTop.setStationLat(STATION_LAT);
                            stationDetailTop.setStationWifiYn(STATION_WIFI_YN);
                            stationDetailTop.setBookmarkYN(STATION_BOOK_MARK_YN);


                            String row2 = rows[1];
                            String[] info = row2.split(DEFINE.ROW_DELIMETER);

                            for (int a = 1; a < info.length; a++) {
                                String bus = info[a];

                                String[] busDetail = bus.split(DEFINE.DELIMETER);

                                StationDetailInfo detailInfo = new StationDetailInfo();

                                String routeId = busDetail[0];
                                String stationId1 = busDetail[1];
                                String upDowmCd = busDetail[2];
                                String stationOrd = busDetail[3];
                                String routeNum = busDetail[4];
                                String routeNum2 = busDetail[5];
                                String routeTp = busDetail[6];
                                String routeDirection = busDetail[7];
                                String startNodeName = busDetail[8];
                                String endNodeName = busDetail[9];
                                String startVehicleTime = busDetail[10];
                                String endVehicleTime = busDetail[11];
                                String intervalTime = busDetail[12];
                                String intervalSaturday = busDetail[13];
                                String intervalSunday = busDetail[14];
                                String intervalHoliday = busDetail[15];
                                String busBookmarkYn = busDetail[16];
                                if (busDetail.length > 17) {
                                    String minArrivalTime = busDetail[17];
                                    detailInfo.setMinArrivalTime(minArrivalTime);
                                } else {
                                    detailInfo.setMinArrivalTime(null);
                                }
                                if (busDetail.length > 18) {
                                    String routeInfo = busDetail[18];
                                    detailInfo.setRouteInfo(routeInfo);
                                } else {
                                    detailInfo.setRouteInfo(null);
                                }
                                detailInfo.setRouteId(routeId);
                                detailInfo.setStationId(stationId1);
                                detailInfo.setRouteNum(routeNum);
                                detailInfo.setRouteNum2(routeNum2);
                                detailInfo.setRouteTp(routeTp);
                                detailInfo.setRouteDirection(routeDirection);
                                detailInfo.setStartnodeName(startNodeName);
                                detailInfo.setEndnodeName(endNodeName);
                                detailInfo.setUpDownCd(upDowmCd);
                                detailInfo.setStationOrd(stationOrd);
                                detailInfo.setStartVehicleTime(startVehicleTime);
                                detailInfo.setEndVehicleTime(endVehicleTime);
                                detailInfo.setIntervalTime(intervalTime);
                                detailInfo.setIntervalSaturday(intervalSaturday);
                                detailInfo.setIntervalSunday(intervalSunday);
                                detailInfo.setIntervalHoliday(intervalHoliday);
                                detailInfo.setBusBookmarkYN(busBookmarkYn);
                                items.add(detailInfo);

                                StationDetailInfoSub infoSub = new StationDetailInfoSub();

                                if (detailInfo.getRouteInfo() == null) {
                                    infoSub.setRecentStationId(null);
                                    infoSub.setRecentStationName(null);
                                    infoSub.setRecentStationStopNum(null);
                                    infoSub.setStationTimeOfArrive(null);
                                    infoSub.setTimeTaken(null);
                                    infoSub.setRemainingStation(null);
                                    infoSub.setRouteLowFloorYn(null);
                                    infoSub.setRouteTrafficYn(null);
                                    infoSub.setRecentStationId1(null);
                                    infoSub.setRecentStationName1(null);
                                    infoSub.setRecentStationStopNum1(null);
                                    infoSub.setStationTimeOfArrive1(null);
                                    infoSub.setTimeTaken1(null);
                                    infoSub.setRemainingStation1(null);
                                    infoSub.setRouteLowFloorYn1(null);
                                    infoSub.setRouteTrafficYn1(null);
                                } else {
                                    String[] ROUTE_INFOs = detailInfo.getRouteInfo().split(DEFINE.SEMICOLON);
                                    String row3 = ROUTE_INFOs[0];

                                    String[] firstInfo = row3.split(DEFINE.COMMA);
                                    String recentStationId = firstInfo[0];
                                    String recentStationName = firstInfo[1];
                                    String recentStationStopNum = firstInfo[2];
                                    String StationTimeOfArrive = firstInfo[3];
                                    String timeTaken = firstInfo[4];
                                    String REMAINING_STATION = firstInfo[5];
                                    String routeLowFloorYn = firstInfo[6];
                                    if (firstInfo.length > 7) {
                                        String routeTrafficYn = firstInfo[7];
                                        infoSub.setRouteTrafficYn(routeTrafficYn);
                                    } else {
                                        infoSub.setRouteTrafficYn(null);
                                    }


                                    infoSub.setRecentStationId(recentStationId);
                                    infoSub.setRecentStationName(recentStationName);
                                    infoSub.setRecentStationStopNum(recentStationStopNum);
                                    infoSub.setStationTimeOfArrive(StationTimeOfArrive);
                                    infoSub.setTimeTaken(timeTaken);
                                    infoSub.setRemainingStation(REMAINING_STATION);
                                    infoSub.setRouteLowFloorYn(routeLowFloorYn);

                                    if (ROUTE_INFOs.length > 1) {
                                        String row4 = ROUTE_INFOs[1];
                                        String[] endInfo = row4.split(DEFINE.COMMA);
                                        String recentStationId1 = endInfo[0];
                                        String recentStationName1 = endInfo[1];
                                        String recentStationStopNum1 = endInfo[2];
                                        String StationTimeOfArrive1 = endInfo[3];
                                        String timeTaken1 = endInfo[4];
                                        String REMAINING_STATION1 = endInfo[5];
                                        String routeLowFloorYn1 = endInfo[6];
                                        if (endInfo.length > 7) {
                                            String routeTrafficYn1 = endInfo[7];
                                            infoSub.setRouteTrafficYn1(routeTrafficYn1);
                                        } else {
                                            infoSub.setRouteTrafficYn1(null);
                                        }

                                        infoSub.setRecentStationId1(recentStationId1);
                                        infoSub.setRecentStationName1(recentStationName1);
                                        infoSub.setRecentStationStopNum1(recentStationStopNum1);
                                        infoSub.setStationTimeOfArrive1(StationTimeOfArrive1);
                                        infoSub.setTimeTaken1(timeTaken1);
                                        infoSub.setRemainingStation1(REMAINING_STATION1);
                                        infoSub.setRouteLowFloorYn1(routeLowFloorYn1);

                                    } else {
                                        infoSub.setRecentStationId1(null);
                                        infoSub.setRecentStationName1(null);
                                        infoSub.setRecentStationStopNum1(null);
                                        infoSub.setStationTimeOfArrive1(null);
                                        infoSub.setTimeTaken1(null);
                                        infoSub.setRemainingStation1(null);
                                        infoSub.setRouteLowFloorYn1(null);
                                        infoSub.setRouteTrafficYn1(null);
                                    }
                                }
                                itemSub.add(infoSub);
                            }
                            anInterface.success(stationDetailTop, items, itemSub);

                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }

                    @Override
                    public void onFailure(Call<StationApi> call, Throwable t) {

                    }
                });
    }

    public void bookmarkInsert(RequestBus requestBus, AuthBaseInterface<String> anInterface) {

        Retrofit2Client
                .getClient(context)
                .bookmarkInsert(requestBus)
                .enqueue(new Callback<StationApi>() {
                    @Override
                    public void onResponse(Call<StationApi> call, Response<StationApi> response) {
                        try {
                            StationApi result = response.body();
                            anInterface.success(result.getResultCode());
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }

                    @Override
                    public void onFailure(Call<StationApi> call, Throwable t) {

                    }
                });

    }

    public void bookmarkDelete(RequestBus requestBus, AuthBaseInterface<String> anInterface) {


        Retrofit2Client
                .getClient(context)
                .bookmarkDelete(requestBus)
                .enqueue(new Callback<StationApi>() {
                    @Override
                    public void onResponse(Call<StationApi> call, Response<StationApi> response) {
                        StationApi result = response.body();
                        anInterface.success(result.getResultCode());
                    }

                    @Override
                    public void onFailure(Call<StationApi> call, Throwable t) {

                    }
                });

    }

    public void recentBusLocation(RequestBus requestBus, RecentBusLocationInterface anInterface) {

        Retrofit2Client
                .getClient(context)
                .recentBusLocation(requestBus)
                .enqueue(new Callback<StationApi>() {
                    @Override
                    public void onResponse(Call<StationApi> call, Response<StationApi> response) {
                        try {
                            StationApi result = response.body();
                            String str = result.getResultData();
                            String[] rows = str.split(DEFINE.TABLE_DELIMETER);

                            ArrayList<BusDetailInfo1> item = new ArrayList<>();

                            String infoList = rows[0];
                            String[] infoLists = infoList.split(DEFINE.ROW_DELIMETER);

                            for (int i = 0; i < infoLists.length; i++) {
                                String row = infoLists[i];

                                BusDetailInfo1 busDetailInfo1 = new BusDetailInfo1();
                                String[] detailBus = row.split(DEFINE.DELIMETER);
                                String routeId = detailBus[0];
                                String routeNum = detailBus[1];
                                String busNum = detailBus[2];
                                String vehicleTp = detailBus[3];
                                String routeWifiYn = detailBus[4];
                                String stationId = detailBus[5];
                                String stationSort = detailBus[6];
                                if (detailBus.length > 7) {
                                    String startTime = detailBus[7];
                                    busDetailInfo1.setStartTime(startTime);
                                } else {
                                    busDetailInfo1.setStartTime(null);
                                }
                                if (detailBus.length > 8) {
                                    String modDate = detailBus[8];
                                    busDetailInfo1.setModDate(modDate);
                                } else {
                                    busDetailInfo1.setModDate(null);
                                }

                                busDetailInfo1.setRouteId(routeId);
                                busDetailInfo1.setRouteNm(routeNum);
                                busDetailInfo1.setBusNum(busNum);
                                busDetailInfo1.setVehicleTp(vehicleTp);
                                busDetailInfo1.setRouteWifiYn(routeWifiYn);
                                busDetailInfo1.setStationId1(stationId);
                                busDetailInfo1.setStationSort(stationSort);
                                item.add(busDetailInfo1);
                            }

                            anInterface.success(item);

                        } catch (Exception e) {
                            anInterface.error("왜 팅기지 ?");
                        }
                    }

                    @Override
                    public void onFailure(Call<StationApi> call, Throwable t) {

                    }
                });
    }


    public void stationSurroundingInfo(RequestBus requestBus, StationSurroundInfo anInterface) {


        Retrofit2Client
                .getClient(context)
                .stationSurroundingInfo(requestBus)
                .enqueue(new Callback<StationApi>() {
                    @Override
                    public void onResponse(Call<StationApi> call, Response<StationApi> response) {
                        try {
                            StationApi result = response.body();
                            String str = result.getResultData();
                            //String stationSurroundingInfo = "["+str.replace("\u0019","\",\"")+"]";

                            ArrayList<StationNearByInfo> items = new ArrayList<>();

                            String dataList = requestBus.getRequestData();

                            String[] gbn = dataList.split(DEFINE.FD_DELIMETER);

                            String[] strAr = str.split(DEFINE.ROW_DELIMETER);
                            for (int i = 0; i < strAr.length; i++) {
                                String strAr1 = strAr[i];
                                String[] stationSurroundingInfoList = strAr1.split(DEFINE.DELIMETER);

                                StationNearByInfo stationNearByInfo = new StationNearByInfo();

                                if (gbn[0].equals("1")) {
                                    String stationId = stationSurroundingInfoList[0];
                                    String stationName = stationSurroundingInfoList[1];
                                    String stationNo = stationSurroundingInfoList[2];
                                    String stationLon = stationSurroundingInfoList[3];
                                    String stationLat = stationSurroundingInfoList[4];

                                    stationNearByInfo.setStationId(stationId);
                                    stationNearByInfo.setStationName(stationName);
                                    stationNearByInfo.setStationNo(stationNo);
                                    stationNearByInfo.setStationLon(stationLon);
                                    stationNearByInfo.setStationLat(stationLat);
                                } else {
                                    String locationName = stationSurroundingInfoList[0];
                                    String subwayName = stationSurroundingInfoList[1];
                                    String stationLon = stationSurroundingInfoList[2];
                                    String stationLat = stationSurroundingInfoList[3];

                                    stationNearByInfo.setLocationName(locationName);
                                    stationNearByInfo.setSubwayName(subwayName);
                                    stationNearByInfo.setStationLon(stationLon);
                                    stationNearByInfo.setStationLat(stationLat);
                                }
                                items.add(stationNearByInfo);
                            }
                            anInterface.success(items);
                        } catch (Exception e) {
                            anInterface.error("dd");
                        }

                    }

                    @Override
                    public void onFailure(Call<StationApi> call, Throwable t) {

                    }
                });
    }

    public void getSearchPathV2(RequestBus requestBus, GetSearchPathV2Interface anInterface) {

        Retrofit2Client
                .getClient(context)
                .getSearchPathV2(requestBus)
                .enqueue(new Callback<StationApi>() {
                    @Override
                    public void onResponse(Call<StationApi> call, Response<StationApi> response) {
                        try {
                            StationApi result = response.body();
                            String str = result.getResultData();
                            String[] strAr = str.split(DEFINE.ROW_DELIMETER);

                            ArrayList<SearchPathV2> searchPathV2List = new ArrayList<>();


                            for (int i = 0; i < strAr.length; i++) {
                                String strResultList = strAr[i];

                                SearchPathV2 searchPathV2 = new SearchPathV2();

                                String[] strResult = strResultList.split(DEFINE.DELIMETER);
                                String startStationID = strResult[0];
                                String endStationID = strResult[1];
                                String routeCount = strResult[2];
                                String routeGbn1= strResult[3];
                                String routeId1 = strResult[4];
                                String routeNo1= strResult[5];
                                String routeTp1= strResult[6];
                                String upDownCd1= strResult[7];
                                String route1StartStationId= strResult[8];
                                String route1StartStationName= strResult[9];
                                String route1StartStationOrd= strResult[10];
                                String route1EndStationId= strResult[11];
                                String route1EndStationName= strResult[12];
                                String route1EndStationOrd= strResult[13];
                                String routeGbn2= strResult[14];
                                String routeId2 = strResult[15];
                                String routeNo2= strResult[16];
                                String routeTp2= strResult[17];
                                String upDownCd2= strResult[18];
                                String route2StartStationId= strResult[19];
                                String route2StartStationName= strResult[20];
                                String route2StartStationOrd= strResult[21];
                                String route2EndStationId= strResult[22];
                                String route2EndStationName= strResult[23];
                                String route2EndStationOrd= strResult[24];
                                String currentInterval= strResult[25];
                                String avgInterval= strResult[26];
                                if (strResult.length > 27){
                                    String comingSoon= strResult[27];
                                    searchPathV2.setComingSoon(comingSoon);
                                }else {
                                    searchPathV2.setComingSoon(null);
                                }

                                searchPathV2.setStartStationID(startStationID);
                                searchPathV2.setEndStationID(endStationID);
                                searchPathV2.setRouteCount(routeCount);
                                searchPathV2.setRouteGbn1(routeGbn1);
                                searchPathV2.setRouteId1(routeId1);
                                searchPathV2.setRouteNo1(routeNo1);
                                searchPathV2.setRouteTp1(routeTp1);
                                searchPathV2.setUpDownCd1(upDownCd1);
                                searchPathV2.setRoute1StartStationId(route1StartStationId);
                                searchPathV2.setRoute1StartStationName(route1StartStationName);
                                searchPathV2.setRoute1StartStationOrd(route1StartStationOrd);
                                searchPathV2.setRoute1EndStationId(route1EndStationId);
                                searchPathV2.setRoute1EndStationName(route1EndStationName);
                                searchPathV2.setRoute1EndStationOrd(route1EndStationOrd);
                                searchPathV2.setRouteGbn2(routeGbn2);
                                searchPathV2.setRouteId2(routeId2);
                                searchPathV2.setRouteNo2(routeNo2);
                                searchPathV2.setRouteTp2(routeTp2);
                                searchPathV2.setUpDownCd2(upDownCd2);
                                searchPathV2.setRoute2StartStationId(route2StartStationId);
                                searchPathV2.setRoute2StartStationName(route2StartStationName);
                                searchPathV2.setRoute2StartStationOrd(route2StartStationOrd);
                                searchPathV2.setRoute2EndStationId(route2EndStationId);
                                searchPathV2.setRoute2EndStationName(route2EndStationName);
                                searchPathV2.setRoute2EndStationOrd(route2EndStationOrd);
                                searchPathV2.setCurrentInterval(currentInterval);
                                searchPathV2.setAvgInterval(avgInterval);
                                searchPathV2List.add(searchPathV2);

                            }
                            anInterface.success(searchPathV2List);
                        } catch (Exception e) {
                            anInterface.error("검색결과가 없습니다");
                        }

                    }

                    @Override
                    public void onFailure(Call<StationApi> call, Throwable t) {

                    }
                });
    }

    public void getNoticeList(RequestBus requestBus, GetNoticeListInterface anInterface){

        Retrofit2Client
                .getClient(context)
                .getNoticeList(requestBus)
                .enqueue(new Callback<StationApi>() {
                    @Override
                    public void onResponse(Call<StationApi> call, Response<StationApi> response) {
                        try {
                            StationApi result = response.body();
                            String str = result.getResultData();
                            String[] strAr = str.split(DEFINE.DELIMETER);
                            NoticeList noticeList = new NoticeList();

                            String rNum = strAr[0];
                            String noticeSeq = strAr[1];
                            String noticeTitle = strAr[2];
                            String noticeContents = strAr[3];
                            String noticeImage = strAr[4];
                            String noticeLinkUrl = strAr[5];
                            String linkSystemGbn = strAr[6];
                            String linkSystemSeq = strAr[7];
                            String extUrlYn = strAr[8];
                            String insDate = strAr[9];
//                            String hit = strAr[10];
//                            String readYn = strAr[11];

                            noticeList.setrNum(rNum);
                            noticeList.setNoticeSeq(noticeSeq);
                            noticeList.setNoticeTitle(noticeTitle);
                            noticeList.setNoticeContents(noticeContents);
                            noticeList.setNoticeImage(noticeImage);
                            noticeList.setNoticeLinkUrl(noticeLinkUrl);
                            noticeList.setLinkSystemGbn(linkSystemGbn);
                            noticeList.setNoticeSeq(linkSystemSeq);
                            noticeList.setExtUrlYn(extUrlYn);
                            noticeList.setInsDate(insDate);
//                            noticeList.setHit(hit);
//                            noticeList.setReadYn(readYn);

                            anInterface.success(noticeList);
                        }catch (Exception e){
                            System.out.println(e);
                        }
                    }

                    @Override
                    public void onFailure(Call<StationApi> call, Throwable t) {

                    }
                });
    }

}

