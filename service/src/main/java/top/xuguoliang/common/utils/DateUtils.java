package top.xuguoliang.common.utils;

import org.apache.log4j.Logger;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtils {

    /*
     * 获取年月日
     */
    public static String getyyyyMMdd(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    /*
     * 获取时分秒
     */
    public static String getHHmmss(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(date);
    }

    /**
     * 获取一个月的最后一天
     *
     * @param year
     * @param month
     * @return
     */
    public static int getMaxDayByYearMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        return calendar.getActualMaximum(Calendar.DATE);
    }

    private static Logger logger = Logger.getLogger(DateUtils.class);

    public static String dateStr(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日 hh:mm");
        String str = format.format(date);
        return str;
    }

    public static String dateStr(Date date, String f) {
        SimpleDateFormat format = new SimpleDateFormat(f);
        String str = format.format(date);
        return str;
    }

    public static Date getDate3(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            logger.error(e);
        }
        return date;

    }

    /**
     * 将2017-08-12这种结束时间变为 2017-08-12 23:59:59
     */
    public static String toEndDayTime(String endTime) {
        return endTime + " 23:59:59";
    }

    public static String tostartDayTime(String endTime) {
        return endTime + " 00:00:01";
    }

    public static String dateStr2(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String str = format.format(date);
        return str;
    }

    public static String newdateStr2(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String str = format.format(date);
        return str;
    }

    public static String newdateStr6(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        String str = format.format(date);
        return str;
    }

    public static String newdateStr3(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyMMdd");
        String str = format.format(date);
        return str;
    }

    public static String dateStr3(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String str = format.format(date);
        return str;
    }

    public static String dateStr4(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = format.format(date);
        return str;

    }

    public static String dateStr5(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        String str = format.format(date);
        return str;

    }

    public static Date getDate(String time, String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        try {
            return format.parse(time);
        } catch (ParseException e) {
            logger.error(e);
        }
        return null;
    }

    public static String getCurDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String sDate = df.format(date);
        return sDate;
    }

    /**
     * 将秒转换成时间
     *
     * @param times
     * @return
     */
    public static Date getDate(String times) {
        long time = Long.parseLong(times);
        return new Date(time * 1000);
    }

    public static String dateStr(String times) {
        return dateStr(getDate(times));
    }

    public static String dateStr2(String times) {
        return dateStr2(getDate(times));
    }

    public static String dateStr3(String times) {
        return dateStr3(getDate(times));
    }

    public static String dateStr4(String times) {
        return dateStr4(getDate(times));
    }

    public static long getTime(Date date) {
        return date.getTime() / 1000;
    }

    public static int getDay(Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public static int getMonth(Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        return cal.get(Calendar.MONTH);
    }

    public static int getYear(Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        return cal.get(Calendar.YEAR);
    }

    /**
     * s - 表示 "yyyy-mm-dd" 形式的日期的 String 对象
     *
     * @param
     * @return
     */
    public static Date valueOf(String s) {
        final int YEAR_LENGTH = 4;
        final int MONTH_LENGTH = 2;
        final int DAY_LENGTH = 2;
        final int MAX_MONTH = 12;
        final int MAX_DAY = 31;
        int firstDash;
        int secondDash;
        Date d = null;

        if (s == null) {
            throw new IllegalArgumentException();
        }

        firstDash = s.indexOf('-');
        secondDash = s.indexOf('-', firstDash + 1);
        if ((firstDash > 0) && (secondDash > 0) && (secondDash < s.length() - 1)) {
            String yyyy = s.substring(0, firstDash);
            String mm = s.substring(firstDash + 1, secondDash);
            String dd = s.substring(secondDash + 1);
            if (yyyy.length() == YEAR_LENGTH && mm.length() == MONTH_LENGTH &&
                    dd.length() == DAY_LENGTH) {
                int year = Integer.parseInt(yyyy);
                int month = Integer.parseInt(mm);
                int day = Integer.parseInt(dd);
                if (month >= 1 && month <= MAX_MONTH) {
                    int maxDays = MAX_DAY;
                    switch (month) {
                        // February determine if a leap year or not
                        case 2:
                            if ((year % 4 == 0 && !(year % 100 == 0)) || (year % 400 == 0)) {
                                maxDays = MAX_DAY - 2; // leap year so 29 days in February
                            } else {
                                maxDays = MAX_DAY - 3; //  not a leap year so 28 days in February
                            }
                            break;
                        // April, June, Sept, Nov 30 day months
                        case 4:
                        case 6:
                        case 9:
                        case 11:
                            maxDays = MAX_DAY - 1;
                            break;
                    }
                    if (day >= 1 && day <= maxDays) {
                        Calendar cal = Calendar.getInstance();
                        cal.set(year, month - 1, day, 0, 0, 0);
                        cal.set(Calendar.MILLISECOND, 0);
                        d = cal.getTime();
                    }
                }
            }
        }
        if (d == null) {
            throw new IllegalArgumentException();
        }
        return d;
    }

    /**
     * @param Begin
     * @param end   传入开始时间 和 结束时间 格式如：2012-09-07
     * @return 返回Map  获取相隔多少年 get("YEAR")及为俩个时间年只差，月 天，类推
     * Key ：
     * YEAR MONTH DAY
     * 如果开始时间 晚于 结束时间 return null；
     * @author lijie
     */

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Map getApartTime(String Begin, String end) {
        String[] temp = Begin.split("-");
        String[] temp2 = end.split("-");
        if (temp.length > 1 && temp2.length > 1) {
            Calendar ends = Calendar.getInstance();
            Calendar begin = Calendar.getInstance();

            begin.set(NumberUtils.getInt(temp[0]),
                    NumberUtils.getInt(temp[1]), NumberUtils.getInt(temp[2]));
            ends.set(NumberUtils.getInt(temp2[0]),
                    NumberUtils.getInt(temp2[1]), NumberUtils.getInt(temp2[2]));
            if (begin.compareTo(ends) < 0) {
                Map map = new HashMap();
                ends.add(Calendar.YEAR, -NumberUtils.getInt(temp[0]));
                ends.add(Calendar.MONTH, -NumberUtils.getInt(temp[1]));
                ends.add(Calendar.DATE, -NumberUtils.getInt(temp[2]));
                map.put("YEAR", ends.get(Calendar.YEAR));
                map.put("MONTH", ends.get(Calendar.MONTH) + 1);
                map.put("DAY", ends.get(Calendar.DATE));
                return map;
            }
        }
        return null;
    }

    public static Date rollDay(Date d, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.DAY_OF_MONTH, day);
        return cal.getTime();
    }

    public static Date rollMon(Date d, int mon) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.MONTH, mon);
        return cal.getTime();
    }

    public static Date rollYear(Date d, int year) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.YEAR, year);
        return cal.getTime();
    }

    public static Date rollDate(Date d, int year, int mon, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.YEAR, year);
        cal.add(Calendar.MONTH, mon);
        cal.add(Calendar.DAY_OF_MONTH, day);
        return cal.getTime();
    }


    /**
     * 获取当前时间的秒数字符串
     *
     * @return
     */
    public static String getNowTimeStr() {
        String str = Long.toString(System.currentTimeMillis() / 1000);
        return str;
    }

    public static String getTimeStr(Date time) {
        long date = time.getTime();
        String str = Long.toString(date / 1000);
        return str;
    }

    public static String rollMonth(String addtime, String time_limit) {
        Date t = DateUtils.rollDate(DateUtils.getDate(addtime), 0, NumberUtils.getInt(time_limit), 0);
        return t.getTime() / 1000 + "";
    }

    public static String rollDay(String addtime, String time_limit_day) {
        Date t = DateUtils.rollDate(DateUtils.getDate(addtime), 0, 0, NumberUtils.getInt(time_limit_day));
        return t.getTime() / 1000 + "";
    }

    public static Date getIntegralTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getSecIntegralTime(Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(d.getTime());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getLastIntegralTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getLastSecIntegralTime(Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(d.getTime());
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getFirstDayTime(Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(d.getTime());
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DATE));
        return cal.getTime();
    }

    public static Date getLastDayTime(Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(d.getTime());
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DATE));
        return cal.getTime();
    }

    public static long getTime(String format) {
        long t = 0;
        if (StringUtils.isBlank(format)) {
            return t;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        try {
            date = sdf.parse(format);
            t = date.getTime() / 1000;
        } catch (ParseException e) {
            logger.error(e);
        }
        return t;
    }

    public static Date getDate2(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            logger.error(e);
        }
        return date;

    }

    public static Time getTime1(String str) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Time time = null;
        try {
            Date date = format.parse(str);
            time = new Time(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static Date getDateYYYYMMdd(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            logger.error(e);
        }
        return date;

    }

    /**
     * 获取下一天 默认 格式 yyyy-MM-dd 00:00:00
     *
     * @param date
     * @return
     */
    public static Date getNextDayYYYYMMdd(Date date) {
        date = rollDay(date, 1);
        return getDateYYYYMMdd(dateStr2(date));
    }
    //wsl 2014-09-04 start

    /**
     * 获取当天 默认 格式 yyyy-MM-dd 00:00:00
     *
     * @param date
     * @return
     */
    public static long getDayYYYYMMdd(Date date) {
        return getDateYYYYMMdd(dateStr2(date)).getTime() / 1000;
    }

    //wsl 2014-09-04 end
    public static String getYYYYMMddHHmmss(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (date == null) {
            return null;
        }
        try {
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            return null;
        }
    }

    //v1.8.0.4_u3 TGPROJECT-333 qinjun 2014-06-10 start
    public static boolean valid(String str) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = formatter.parse(str);
            return str.equals(formatter.format(date));
        } catch (Exception e) {
            return false;
        }
    }
    //v1.8.0.4_u3 TGPROJECT-333 qinjun 2014-06-10 end

    public static int getSubTime(Date nowDate, Date date) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        String sNow = fmt.format(nowDate);
        String sdate = fmt.format(date);
        Date dNow = null;
        Date d = null;

        try {
            dNow = fmt.parse(sNow);
            d = fmt.parse(sdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long time = dNow.getTime() - d.getTime();
        long day = time / (24 * 60 * 60 * 1000);
        return (int) day;
    }

    public static int daysBetween(Date date1, Date date2) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        long time1 = cal.getTimeInMillis();
        cal.setTime(date2);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));

    }

    /**
     * 获得两个时间相差的分钟数
     *
     * @param nowTime
     * @param time
     * @return
     */
    public static long getSubMinutes(Long nowTime, Long time) {
        long subMillions = nowTime - time;
        long subMinutes = (subMillions / (1000 * 60) );
        System.out.println("--------------相差的分钟数为" + subMinutes + "---------------------");
        return subMinutes;
    }

    /**
     * 获得日期是星期几
     *
     * @param date
     * @return
     */
    public static Integer getDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int intWeek = 0;
        if (intWeek == 1) {
            intWeek = 7;
        } else {
            intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return intWeek;
    }

    /**
     * 指定小时，分钟，在指定范围内生成日期集合
     * 例如，2017-06-01 到 2017-07-01范围中，获得每周三 12:30 的日期
     *
     * @return
     */
    public static List<Date> getDays(Date startDate, Date endDate, Time time, Integer dayInWeek) {
        List<Date> dateList = new ArrayList<Date>();

        Integer startDateDayInWeek = getDayOfWeek(startDate);
        Integer dayDifference = 0;
        Integer daySpan = 0;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, time.getHours());
        calendar.set(Calendar.MINUTE, time.getMinutes());

        if (startDateDayInWeek <= dayInWeek) {
            daySpan = 0;
            dayDifference = dayInWeek - startDateDayInWeek;
        } else {
            daySpan = 7;
            dayDifference = startDateDayInWeek - dayInWeek;
        }
        calendar.add(Calendar.DATE, dayDifference - 1);

        while (true) {
            calendar.add(Calendar.DAY_OF_MONTH, daySpan);
            if (calendar.getTime().getTime() > (endDate.getTime() + 86400000)) {
                break;
            } else {
                dateList.add(calendar.getTime());
            }
            if (daySpan == 0) {
                daySpan += 7;
            }
        }
        return dateList;
    }

    public static void main(String[] args) {
        Date startDate = new Date(System.currentTimeMillis());
        Date endDate = rollDay(startDate, 30);
        System.out.println(dateStr4(endDate));
        Time time = new Time(14, 30, 0);
        getDays(startDate, endDate, time, 3);
    }
}
