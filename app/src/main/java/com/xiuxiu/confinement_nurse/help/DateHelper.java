package com.xiuxiu.confinement_nurse.help;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc3;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class DateHelper {
    private DateHelper() {
    }

    public static int getDay() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int getMonth() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        return month;
    }

    public static int getYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    public static int get24Hour() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int get12Hour() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR);
    }

    public static int getMinute() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MINUTE);
    }

    public static int getSecond() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.SECOND);
    }

    /**
     * Parse the time in milliseconds into String with the format: hh:mm:ss or mm:ss
     *
     * @param duration The time needs to be parsed.
     */
    @SuppressLint("DefaultLocale")
    public static String formatDuration(long duration) {
        duration /= 1000; // milliseconds into seconds
        long minute = duration / 60;
        long hour = minute / 60;
        minute %= 60;
        long second = duration % 60;
        if (hour != 0) {
            return String.format("%2d:%02d:%02d", hour, minute, second);
        } else {
            return String.format("%02d:%02d", minute, second);
        }
    }

    public static String getPlayTime(long timeMills) {
        if (timeMills == 0) {
            return "00:00";
        }
        long second = timeMills / 1000;
        long minute = second / 60;
        long hour = minute / 60;
        long minuteValue = minute % 60;
        long secondValue = second % 60;
        java.text.DecimalFormat df = new java.text.DecimalFormat();
        df.applyPattern("00");
        return df.format(hour) + ":" + df.format(minuteValue) + ":" + df.format(secondValue);

    }

    public static String getScreensaverPlayTime(long timeMills) {
        if (timeMills == 0) {
            return "关闭";
        }
        long second = timeMills % 60;
        long minute = timeMills / 60;
        if (minute < 60) {
            if (minute == 0) {
                return second + "秒";
            } else {
                if (second == 0) {
                    return String.valueOf(minute).concat("分钟");
                } else {
                    return String.valueOf(minute).concat("分钟").concat(String.valueOf(second)).concat("秒");
                }
            }
        }
        return String.valueOf(minute).concat("分钟").concat(String.valueOf(second)).concat("秒");
    }


    /**
     * 将时间戳转换成 年月日
     *
     * @param s 单位是秒
     * @return
     */
//    public static String stampToDate(String s) {
//        if (TextUtils.isEmpty(s)) {
//            return "";
//        }
////        String res;
////        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
////        long lt = Long.parseLong(s + "000");
////        Date date = new Date(lt);
////        res = simpleDateFormat.format(date);
//
//        return toDayString(new Date(Long.parseLong(s + "000")));
//    }
   final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    /**
     * 日期格式转时间错
     *
     * @param s
     * @return
     * @throws ParseException
     */
    public static String dateToStamp(String s) {
        String res;

        Date date = null;
        try {
            date = simpleDateFormat.parse(s);
        } catch (ParseException e) {
            return "";
        }
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    public static String stampToDate3(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
        //如果它本来就是long类型的,则不用写这一步
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }


    /**
     * 时间戳转日期
     *
     * @param s
     * @return
     */
    public static String stampToDate(String s) {
        String res;
        //如果它本来就是long类型的,则不用写这一步
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * 时间戳转日期
     *
     * @param s
     * @return
     */
    public static String stampToDate2(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        //如果它本来就是long类型的,则不用写这一步
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static String stampToDate(Date s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        res = simpleDateFormat.format(s);
        return res;
    }

    public static String stampToDate2(Date s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        res = simpleDateFormat.format(s);
        return res;
    }

    public static String toDayString(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);

        int hour = c.get(Calendar.HOUR);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        int dayweek = c.get(Calendar.DAY_OF_WEEK);
        String dayweek2 = dayweek == 0 ? "日" : dayweek + "";

        return year + "-" + month + "-" + day;
//        System.out.println(year + "年" + month + "月" + day + "日" + hour + "时" + minute + "分"
//                + second + "秒" + "周" + dayweek2);
    }


    /**
     * 根据一天的时间获取类型
     *
     * @return
     */
    public static int getTimeTypeByDay() {
        long l = System.currentTimeMillis();
        Date date = new Date(l);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        if (hour < 6) {
            return 0;
        }
        if (hour < 11) {
            return 1;
        }
        if (hour < 13) {
            return 2;
        }
        if (hour < 18) {
            return 3;
        }
        if (hour < 22) {
            return 4;
        }
        return 5;
    }


    /**
     * 根据一天的时间获取类型
     *
     * @return
     */
    public static int getTimeTypeByWeek() {
        long l = System.currentTimeMillis();
        Date date = new Date(l);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int week = c.get(Calendar.DAY_OF_WEEK);//1--7的值,对应：星期日，星期一，星期二，星期三....星期六
        return week - 1;
    }

    public static String getTimeDescription(long time) {
        Date date = new Date(time);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        String msg;
        if (hour > 12) {
            msg = "下午";
        } else {
            msg = "上午";
        }
        int minute = c.get(Calendar.MINUTE);
        return msg + " " + hour + ":" + minute;
    }


    public static final long ONE_DAY_MS = 24 * 60 * 60 * 1000;

    /**
     * 计算两个日期之间的日期
     *
     * @param startTime
     * @param endTime
     */
    public static void betweenDays(long startTime, long endTime, XFunc3<Integer, Integer, Integer> callback) {
        Date date_start = new Date(startTime);
        Date date_end = new Date(endTime);
        //计算日期从开始时间于结束时间的0时计算
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(date_start);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(date_end);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);
        int s = (int) ((toCalendar.getTimeInMillis() - fromCalendar.getTimeInMillis()) / (ONE_DAY_MS));
        if (s > 0) {
            for (int i = 0; i <= s; i++) {
                long todayDate = fromCalendar.getTimeInMillis() + i * ONE_DAY_MS;
                /**
                 * yyyy-MM-dd E :2012-09-01
                 */


                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(todayDate);

                int endMonth = c.get(Calendar.MONTH) + 1;
                int endYear = c.get(Calendar.YEAR);
                int endDay = c.get(Calendar.DAY_OF_MONTH);

                Log.i("打印日期", "-->" + endYear + "-->" + endMonth + "-->" + endDay);

                callback.call(endYear, endMonth, endDay);

            }
        } else {//此时在同一天之内
            Log.i("打印日期", getCustonFormatTime(startTime, "yyyy-MM-dd HH:mm:ss"));
        }
    }

    /**
     * 格式化传入的时间
     *
     * @param time      需要格式化的时间
     * @param formatStr 格式化的格式
     * @return
     */
    public static String getCustonFormatTime(long time, String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        Date d1 = new Date(time);
        return format.format(d1);
    }

    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     * @param time1 带有时间格式的
     * @param time2
     * @return
     */
    public static int differentDaysByMillisecond(String mFormat1,String mFormat2) {
        String res;
        Date date = null;
        try {
            date = simpleDateFormat.parse(mFormat1);
            long time1 = date.getTime();
            date=simpleDateFormat.parse(mFormat2);
            long time2=date.getTime();
            int days = (int) ((time1 - time2) / (1000*3600*24))+1;
            return days;
        } catch (ParseException|NullPointerException e) {

        }
        return 0;
    }




    private static String birthday;
    private static String ageStr;
    private static int age;
    //出生年、月、日
    private static int year;
    private static int month;
    private static int day;
    public static String BirthdayToAge(String birthday1) {
        birthday = birthday1;
        stringToInt(birthday, "yyyy-MM-dd HH:mm:ss");
        // 得到当前时间的年、月、日
        Calendar cal = Calendar.getInstance();
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayNow = cal.get(Calendar.DATE);
        // 用当前年月日减去出生年月日
        int yearMinus = yearNow - year;
        int monthMinus = monthNow - month;
        int dayMinus = dayNow - day;
        age = yearMinus;// 先大致赋值
        if (yearMinus <= 0) {
            age = 0;
            ageStr = String.valueOf(age) ;
            return ageStr;
        }
        if (monthMinus < 0) {
            age = age - 1;
        } else if (monthMinus == 0) {
            if (dayMinus < 0) {
                age = age - 1;
            }
        }
        ageStr = String.valueOf(age) ;
        return ageStr;
    }

    /**
     * String类型转换成date类型
     * strTime: 要转换的string类型的时间，
     * formatType: 要转换的格式yyyy-MM-dd HH:mm:ss
     * //yyyy年MM月dd日 HH时mm分ss秒，
     * strTime的时间格式必须要与formatType的时间格式相同
     */
    private static Date stringToDate(String strTime, String formatType) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(formatType);
            Date date;
            date = formatter.parse(strTime);
            return date;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * String类型转换为long类型
     * .............................
     * strTime为要转换的String类型时间
     * formatType时间格式
     * formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
     * strTime的时间格式和formatType的时间格式必须相同
     */
    private static void stringToInt(String strTime, String formatType) {
        try {
            //String类型转换为date类型
            Calendar calendar = Calendar.getInstance();
            Date date = stringToDate(strTime, formatType);
            if (date==null) {
                date=stringToDate(strTime,"yyyy-MM-dd");
          }

            if (date == null) {
            } else {
                calendar.setTime(date);
                //date类型转成long类型
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH) + 1;
                day = calendar.get(Calendar.DAY_OF_MONTH);
            }
        } catch (Exception e) {
        }
    }


}
