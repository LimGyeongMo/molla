package com.example.daegurobus.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    /**
     * 현재 시스템 날짜를 원하는 포멧형식으로 가져오기
     **/
    public static String getCurrentDateTime(String format) {
        return new SimpleDateFormat(format).format(new Date(System.currentTimeMillis()));
    }

    // 2021.04.20 15:06 > 2021.04.20
    public static String getDate(String date) {
        SimpleDateFormat beforeDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.KOREA); //같은 형식으로 맞춰줌
        SimpleDateFormat afterDateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA);

        try {
            return afterDateFormat.format(beforeDateFormat.parse(date));
        } catch (Exception e) {
            return "";
        }
    }

    public static String getDate(String beforeType, String afterType, String date) {
        SimpleDateFormat beforeDateFormat = new SimpleDateFormat(beforeType, Locale.KOREA);
        SimpleDateFormat afterDateFormat = new SimpleDateFormat(afterType, Locale.KOREA);

        try {
            return afterDateFormat.format(beforeDateFormat.parse(date));
        } catch (Exception e) {
            return "";
        }
    }

    // 2021.04.20 15:06 > 15:06
    public static String getTime(String date) {
        SimpleDateFormat originalDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.KOREA); //같은 형식으로 맞춰줌
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.KOREA);

        try {
            return timeFormat.format(originalDateFormat.parse(date));
        } catch (Exception e) {
            return "";
        }
    }

    // 20210420150622 > 15:06
    public static String getTimeFromTimeMile(String date) {
        SimpleDateFormat originalDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA); //같은 형식으로 맞춰줌
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.KOREA);

        try {
            return timeFormat.format(originalDateFormat.parse(date));
        } catch (Exception e) {
            return "";
        }
    }

    public static String getMonthAgoDate(int month) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA);
            Date date = new Date();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, -month);

            return simpleDateFormat.format(calendar.getTime());
        } catch (Exception e) {
            LogUtil.e(e.toString());
        }

        return "";
    }

    public static String getDayAgoDate(int day) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA);
            Date date = new Date();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR, -day);

            return simpleDateFormat.format(calendar.getTime());
        } catch (Exception e) {
            LogUtil.e(e.toString());
        }

        return "";
    }

    public static String changeReviewDate(String date) {
        SimpleDateFormat beforeDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        SimpleDateFormat afterDateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA);

        try {
            // 시간 상관없이 날짜로만 계산하기 위해
            String writeDay = afterDateFormat.format(beforeDateFormat.parse(date));
            String today = afterDateFormat.format(new Date(System.currentTimeMillis()));

            Date dWriteDay = afterDateFormat.parse(writeDay);
            Date dToday = afterDateFormat.parse(today);

            long diff = (dToday.getTime() - dWriteDay.getTime()) / (24 * 60 * 60 * 1000);

            if (diff == 0) {
                return "오늘";
            } else if (diff < 7) {
                return diff + "일전";
            } else {
                return writeDay;
            }
        } catch (Exception e) {

        }

        return "";
    }

    // 2021.04.20 15:06 > 오후 3시 6분
    public static String getTextDate(String date) {
        SimpleDateFormat beforeDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.KOREA); //같은 형식으로 맞춰줌
        SimpleDateFormat afterDateFormat = new SimpleDateFormat("a h시 m분", Locale.KOREA);

        try {
            return afterDateFormat.format(beforeDateFormat.parse(date));
        } catch (Exception e) {
            return "";
        }
    }

    public static String getMonth(int Month) {
        Calendar calendar = Calendar.getInstance();

        String sMonth = "";
        if (Month >= 0) {
            sMonth = (Month + 1) + "";
        } else {
            sMonth = (calendar.get(Calendar.MONTH) + 1) + "";
        }

        if (sMonth.length() == 1) sMonth = "0" + sMonth;

        return sMonth;


    }

    public static String getDay(int Day) {
        Calendar calendar = Calendar.getInstance();

        String sDay = "";
        if (Day >= 0) {
            sDay = Day + "";
        } else {
            sDay = calendar.get(Calendar.DAY_OF_MONTH) + "";
        }

        if (sDay.length() == 1) sDay = "0" + sDay;

        return sDay;
    }
}
