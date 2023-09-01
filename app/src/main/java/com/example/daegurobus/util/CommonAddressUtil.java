package com.example.daegurobus.util;

import android.content.Context;

import com.example.daegurobus.R;
import com.example.daegurobus.model.CommonAddress;

public class CommonAddressUtil {
    public static boolean isAddressUsable(CommonAddress address) {
        if (address.getSido().length() > 0 && address.getGungu().length() > 0   // 시도 군구는 무조건 있어야 하고
                && (address.getDong().length() > 0 && address.getHangDong().length() > 0 || address.getRoadName().length() > 0 && address.getRoadNum().length() > 0)) { // 구주소가 있거나 신주소가 있을 경우
            return true;
        }

        return false;
    }

    public static String getOldAddress(CommonAddress address, boolean isShowSido, boolean isShowDetail) {
        String oldAddress = "";

        if (isShowSido) {
            oldAddress = address.getSido() + " ";
        }

        oldAddress += address.getGungu() + " " + address.getDong();

        if (!address.getDong().equals(address.getHangDong()) && address.getHangDong().length() > 0) {
            oldAddress += "(" + address.getHangDong() + ")";
        }

        if (address.getJibun().length() > 0) {
            oldAddress += " " + address.getJibun();
        }

        if (address.getRoadDestBuilding().length() > 0) {
            oldAddress += " " + address.getRoadDestBuilding();
        }

        if (isShowDetail && address.getDetail().length() > 0) {
            oldAddress += " " + address.getDetail();
        }

        return oldAddress;
    }

    public static String getNewAddress(CommonAddress address, boolean isShowSido, boolean isShowDetail) {
        if (isEmptyNewAddress(address)) {
            return getOldAddress(address, isShowSido, isShowDetail);
        } else {
            String newAddress = "";

            if (isShowSido) {
                newAddress = address.getSido() + " ";
            }

            newAddress += address.getGungu();

            if (address.getGungu().length() > 0) {
                String gunLast = Character.toString(address.getGungu().charAt(address.getGungu().length() - 1));

                if ("군".equals(gunLast)) { // 대구에는 달성군만 존재
                    String dong;

                    if (address.getHangDong().length() > 0) {
                        dong = address.getHangDong();
                    } else {
                        dong = address.getDong();
                    }

                    if (dong.length() > 0) {    // 동이 없을때가 있다(?)
                        String dongLast = Character.toString(dong.charAt(dong.length() - 1));

                        if (!"동".equals(dongLast)) {
                            newAddress += " " + dong.split(" ", -1)[0];
                        }
                    }
                }
            }

            newAddress += " " + address.getRoadName() + " " + address.getRoadNum();

            if (address.getRoadDestBuilding().length() > 0) {
                newAddress += " " + address.getRoadDestBuilding();
            }

            if (isShowDetail && address.getDetail().length() > 0) {
                newAddress += " " + address.getDetail();
            }

            return newAddress;
        }
    }

    public static boolean isSidoDaegu(Context context, String sidoName) {
        return context.getString(R.string.daegu_sido_name).equals(sidoName);
    }

    private static boolean isEmptyNewAddress(CommonAddress address) {
        if (address.getRoadName().equals("") && address.getRoadNum().equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isAddressEqual(CommonAddress address1, CommonAddress address2) {
        if (address1 != null && address2 != null
                && address1.getCustomName().equals(address2.getCustomName())
                && address1.getSido().equals(address2.getSido())
                && address1.getGungu().equals(address2.getGungu())
                && address1.getDong().equals(address2.getDong())
                && address1.getHangDong().equals(address2.getHangDong())
                && address1.getJibun().equals(address2.getJibun())
                && address1.getRoadDestBuilding().equals(address2.getRoadDestBuilding())
                && address1.getDetail().equals(address2.getDetail())) {
//                && address1.getLongitude() == address2.getLongitude()
//                    && address1.getLatitude() == address2.getLatitude()
            return true;
        }

        return false;
    }
}