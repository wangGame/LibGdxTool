package com.kw.gdx.utils;

import com.kw.gdx.constant.Constant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {
    public static String[] momthEng = {
            "January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December"
    };

    //显示月，需要偏移几天
    /**
     * 当前月从周几开始
     * @param year
     * @param month
     * @return
     */
    public static int monthStartDays(int year, int month){
        //1900年到今天一共多少天
        int days = countDays(year, month);
        int offDay =  1 + days % 7;
        if (offDay == 7){
            offDay = 0;
        }
        return offDay;
    }

    /**
     * -1 : 过去月
     * 0：当前月
     * 1：未来月
     * @return
     */
    public static int compareYear(int targetYear){
        StdDateBean bean = currentDateBean();
        return compareYear(bean.getYear(),targetYear);
    }

    /**
     *
     * @param tarGetYear
     * @param stdYear
     * @return
     */
    public static int compareYear(int stdYear,int tarGetYear){
        if (tarGetYear>stdYear) {
            return 1;
        }else if (tarGetYear<stdYear){
            return -1;
        }else {
            return 0;
        }
    }

    public static int compareYearMonth(int stdYear,int stdMonth,int targetYear,int targetMonth){
        int i = compareYear(stdYear,targetYear);
        if (i == 0){
            return compareMonth(stdMonth,targetMonth);
        }else {
            return i;
        }
    }

    public static int compareYearMonth(int year,int month){
        StdDateBean bean = currentDateBean();
        return compareYearMonth(bean.getYear(),bean.getMonth(),year,month);
    }

    public static int compareMonth(int stdMonth,int targetMonth){
        if (targetMonth > stdMonth) {
            return 1;
        }else if (targetMonth< stdMonth){
            return -1;
        }else {
            return 0;
        }
    }

    public static int compareMonth(int month){
        StdDateBean bean = currentDateBean();
        return compareMonth(bean.getMonth(),month);
    }

    //next month or premonth.
    public static StdDateBean jisuanDate(int year,int month,int type) {
        month = month + type;
        if (month>11){
            month = 0;
            year = year + 1;
        }
        if (month < 0){
            month = 11;
            year = year - 1;
        }
        StdDateBean bean = new StdDateBean();
        bean.setYear(year);
        bean.setMonth(month);
        return bean;
    }

    //count day
    public static int countDays(int year,int month){
        int totalDays = 0; //累計天數
        for (int i = 1900; i < year; i++) {
            if ((i % 4 == 0 && i % 100 != 0) || (i % 400 == 0)) {
                totalDays = totalDays + 366;
            } else {
                totalDays = totalDays + 365;
            }
        }
        //根據月份判斷天數
        for (int j = 0; j <month; j++) {
            totalDays += currentMonthDay(year, j);
        }
        return totalDays;
    }

    //rui year
    public static boolean isLeap(int year){
        boolean isLeap = false;
        if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
            isLeap = true;
        }
        return isLeap;
    }

    //get current day;
    public static int currentDateDay(){

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int i = calendar.get(Calendar.MONTH);
        int i1 = calendar.get(Calendar.YEAR);
        if (i1<2023){
            return 1;
        }else if (i1 == 2023){
            if ((i <2)) {
                return 1;
            }
        }
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static StdDateBean bean;
    public static StdDateBean currentDateBean(boolean vv){
        StdDateBean bean = new StdDateBean();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        bean.setMonth(calendar.get(Calendar.MONTH));
        bean.setYear(calendar.get(Calendar.YEAR));
        bean.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        bean.setWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
        return bean;
    }

    public static StdDateBean currentDateBean(){
        if (bean == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            StdDateBean bean = new StdDateBean();
            bean.setMonth(calendar.get(Calendar.MONTH));
            bean.setYear(calendar.get(Calendar.YEAR));
            bean.setDay(calendar.get(Calendar.DAY_OF_MONTH));
            bean.setWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
        }

//        if (Constant.realseDebug){
//            StdDateBean bean = new StdDateBean();
//            bean.setMonth(10);
//            bean.setYear(2023);
//            bean.setDay(29);
//            bean.setWeekDay(1);
//            DailyUtils.bean = bean;
//        }
        return bean;
    }

    //get current month day;
    public static int currentMonthDay(int year,int month){
        boolean isLeap = isLeap(year);
        //根據月份判斷天數
        int days = 0;
        switch (month) {
            case 0:
            case 2:
            case 4:
            case 6:
            case 7:
            case 9:
            case 11:
                days = 31;
                break;
            case 3:
            case 5:
            case 8:
            case 10:
                days = 30;
                break;
            case 1:
                if (isLeap) {
                    days = 29;
                } else {
                    days = 28;
                }
                break;
        }
        return days;
    }

    //is current year month day
    public static boolean isCurrentMonth(int year ,int month) {
        return compareYearMonth(year,month) == 0;
    }

    public static String formatTime(Long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        return null;
    }

    public static boolean setDate() {
        int year = -1;
        int month = -1;
        int day = -1;
        if (bean !=null){
            year     = bean.getYear();
            month    = bean.getMonth();
            day      = bean.getDay();
        }
        Calendar calendar = Calendar.getInstance();
        StdDateBean bean = StdDateBean(calendar);
        if (year!=-1){
            if (day != bean.getDay()) {
                return true;
            }
            if (month != bean.getMonth()) {
                return true;
            }
            if (year != bean.getYear()) {
                return true;
            }
        }

        return false;
    }

    private static StdDateBean StdDateBean(Calendar calendar) {
        StdDateBean bean = new StdDateBean();
        bean.setMonth(calendar.get(Calendar.MONTH));
        bean.setYear(calendar.get(Calendar.YEAR));
        bean.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        bean.setWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
        return bean;
    }

    public static int offSet(){
        TimeZone timeZone = TimeZone.getDefault();
        int offset = timeZone.getRawOffset() / 3600000;
        return offset;
    }


    public static long converServerTime(String str){
        // 创建用于解析日期字符串的格式化程序
        try {
//            String str = "Fri, 23 Feb 2024 08:14:55 GMT";
            SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
            Date date = formatter.parse(str);

            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return System.currentTimeMillis();
    }

    public static float fenNum = 0;

    public static class PhbDateBean{
        private int day;
        private int hour;
        private int fen;
        private int miao;

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public int getHour() {
            return hour;
        }

        public void setHour(int hour) {
            this.hour = hour;
        }

        public int getFen() {
            return fen;
        }

        public void setFen(int fen) {
            this.fen = fen;
        }

        public void setMiao(int miao) {
            this.miao = miao;
        }

        public int getMiao() {
            return miao;
        }

        @Override
        public String toString() {
            return "PhbDateBean{" +
                    "day=" + day +
                    ", hour=" + hour +
                    ", fen=" + fen +
                    ", miao=" + miao +
                    '}';
        }
    }

    private static Calendar now;
    private static Calendar nextMonday;


    public static PhbDateBean lastTime(long time){
        PhbDateBean phbDateBean = new PhbDateBean();
        try {
            // 获取当前日期时间
            if (now == null) {
                now = Calendar.getInstance(Locale.US);
            }
            if (!Constant.realseDebug) {
                now.setTimeInMillis(time);
            }else {
                now.setTimeInMillis(System.currentTimeMillis());
            }
            // 计算距离下一个周一还有多少时间
            if (nextMonday == null){
                nextMonday = (Calendar) now.clone();
            }
            if (!Constant.realseDebug) {
                nextMonday.setTimeInMillis(time);
            }else {
                nextMonday.setTimeInMillis(System.currentTimeMillis());
            }
            int daysUntilNextMonday = (Calendar.SATURDAY - now.get(Calendar.DAY_OF_WEEK) + 2) % 7;
            if (daysUntilNextMonday == 0){
                daysUntilNextMonday = 7;
            }
            nextMonday.add(Calendar.DAY_OF_YEAR, daysUntilNextMonday);
            nextMonday.set(Calendar.HOUR_OF_DAY, 0);
            nextMonday.set(Calendar.MINUTE, 0);
            nextMonday.set(Calendar.SECOND, 0);
            nextMonday.set(Calendar.MILLISECOND, 0);
            long timeUntilNextMonday = nextMonday.getTimeInMillis() - now.getTimeInMillis();
            // 将时间间隔格式化为 xxh xxm 的格式
            long hours = timeUntilNextMonday / (1000 * 60 * 60);
            long minutes = (timeUntilNextMonday / (1000 * 60)) % 60;
            long l = timeUntilNextMonday / 1000;
            long miao = l%60;
            phbDateBean.setDay((int) (hours/24));
            phbDateBean.setHour((int)(hours%24));
            phbDateBean.setFen((int)minutes);
            phbDateBean.setMiao((int) miao);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(
                    "---------------异常"
            );
        }
        return phbDateBean;
    }

    public static void dispose(){
        c1 = null;
        nextMonday = null;
        now  = null;
    }

    private static Calendar c1;
//    public static NumWeekBean numWeek(long serverTime){
//        int currentYear = 0;
//        int weekOfYear= 0;
//        try {
//
//            // 获取当前日期
//            if (c1 == null) {
//                c1 = Calendar.getInstance(Locale.US);
//            }
//            if (!Constant.realseDebug) {
//                c1.setTimeInMillis(serverTime);
//            }else {
//                c1.setTimeInMillis(System.currentTimeMillis());
//            }
//            // 获取当前日期所在年份
//            currentYear = c1.get(Calendar.YEAR);
//            // 获取当前日期是当年的第几周
//            c1.setFirstDayOfWeek(Calendar.MONDAY);  // 设置一周的第一天为周一
//            c1.setMinimalDaysInFirstWeek(4);  // 设置一年中第一周最少包含的天数
//
//            weekOfYear = c1.get(Calendar.WEEK_OF_YEAR);
//
//            int i1 = c1.get(Calendar.DAY_OF_WEEK);//星期
//            if(i1 == 1){
//                long time = c1.getTimeInMillis();
//                c1.setTimeInMillis(time - 24 * 60 * 60 * 1000L);
//                int weekOfYear1 = c1.get(Calendar.WEEK_OF_YEAR);
//                weekOfYear = weekOfYear1;
//            }
//
//        }catch (Exception e){
//
//        }
//        return new NumWeekBean(currentYear, weekOfYear);
//    }
}
