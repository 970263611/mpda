package com.dahuaboke.mpda.bot.utils;


import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @Title:日期工具类
 * @Description:
 * @author: 雷大鹏
 * @date: 2020-05-10 10:19:37
 */
// TODO 整体替换，不要使用Date
public class DateUtil {
    public static final String yyyyMMdd = "yyyyMMdd";
    public static final String HHmmss = "HHmmss";
    public static int[][] day_tbl = {{0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31},
            {0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31}};

    // 获取今日日期
    public static String getToday() {
        return getToday("yyyy-MM-dd");
    }

    public static String getYearMonth() {
        return getYearMonth("yyyyMM");
    }

    // 获取无-day日期格式
    public static String getBusinessToday() {
        return getToday("yyyyMMdd");
    }

    // 获取无-day日期格式
    public static String getBusinessTime() {
        return getToday("HHmmss");
    }

    /**
     * 取得主机日期时间
     * yyyyMMddHHmmss
     *
     * @return
     * @date: 2020-12-01 10:30:29
     */
    public static String getFullToday() {
        return getToday("yyyyMMddHHmmss");
    }

    public static String getLastStamp() {
        return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
    }

    /**
     * 获取当前时间
     * yyyy-MM-dd HH：mm：ss
     *
     * @return
     * @date: 2020-12-01 10:30:18
     */

    public static String getNowDate() {
        return getToday("yyyy-MM-dd HH:mm:ss");
    }

    // 根据格式获取今日日期
    public static String getToday(String formatStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        return sdf.format(new Date());
    }

    public static String getYearMonth(String formatStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        return sdf.format(new Date());
    }

    // 判断传入日期是否为今日
    public static boolean isToday(Date date) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DATE, c.get(Calendar.DATE));
        Date currentDay = c.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(currentDay).equals(format.format(date));
    }

    // 根据格式获取昨天日期
    public static String getYesterDay(String formatStr) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);
        Date yesterDay = c.getTime();
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        return format.format(yesterDay);
    }

    // 获取昨天日期
    public static String getYesterDay() {
        return getYesterDay("yyyy-MM-dd");
    }

    // 根据格式获取明天日期
    public static String getTomorrowDay(String formatStr) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DATE, c.get(Calendar.DATE) + 1);
        Date yesterDay = c.getTime();
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        return format.format(yesterDay);
    }

    // 获取昨天日期
    public static String getTomorrowDay() {
        return getTomorrowDay("yyyy-MM-dd");
    }

    // 获取日期上个月月底
//    public static String getBeforeMonthEnd(String dayStr) {
//        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
//        Date date = formatDate(dayStr, "yyyyMMdd");
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//        calendar.add(Calendar.MONTH, -1);
//        DateUtil.endOfMonth(calendar);
//        date = calendar.getTime();
//        return format.format(date);
//    }

    // 根据格式获取指定天数之前的日期
    public static String getBeforeDay(int days, String formatStr) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DATE, c.get(Calendar.DATE) - days);
        Date yesterDay = c.getTime();
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        return format.format(yesterDay);
    }

    // 根据格式获取指定天数之后的日期
    public static String getAfterDay(int days, String formatStr) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DATE, c.get(Calendar.DATE) + days);
        Date yesterDay = c.getTime();
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        return format.format(yesterDay);
    }

    // 获取当前处于第几季度
    public static int getQuarter() {
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH) + 1;
        int quarter = 0;
        if (month >= 1 && month <= 3) {
            quarter = 1;
        } else if (month >= 4 && month <= 6) {
            quarter = 2;
        } else if (month >= 7 && month <= 9) {
            quarter = 3;
        } else {
            quarter = 4;
        }
        return quarter;
    }

    // 获取量日期之间相隔天数
    public static int getBetweenDay(Date startDate, Date endDate) {
        Calendar d1 = new GregorianCalendar();
        d1.setTime(startDate);
        Calendar d2 = new GregorianCalendar();
        d2.setTime(endDate);
        int days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
        System.out.println("days=" + days);
        int y2 = d2.get(Calendar.YEAR);
        if (d1.get(Calendar.YEAR) != y2) {
            do {
                days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);
                d1.add(Calendar.YEAR, 1);
            } while (d1.get(Calendar.YEAR) != y2);
        }
        return days;
    }

    // 获取当前星期
    public static String getCurrentWeek() {
        return getWeekOfDate(new Date());
    }

    // 获取指定日期星期
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }

    public static Date getDate(Date date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.parse(sdf.format(date));
    }

    public static String getDateStr(Date date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public static String getDateStr(long currentTimeMillis) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(currentTimeMillis);
    }

    public static Date getDate(Date date, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(sdf.format(date));
    }

    public static Date getDate(String dateStr, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(dateStr);
    }

    public static Date getStringToDate(String dateStr) {
        String format = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getDateStr(Date date, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String getDateString(Date date) {
        String format = "yyyyMMdd";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String getDateNoYearStr(Date date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(date);
    }

    public static String getConsumingString(Long startTime, Long endTime) throws ParseException {
        Long consuming = endTime - startTime;
        return getConsumingString(consuming);
    }

    public static String getConsumingString(Long consuming) throws ParseException {
        int s = 1000;
        int m = s * 60;
        int h = m * 60;
        int d = h * 24;

        int d_ = 0;
        int h_ = 0;
        int m_ = 0;
        int s_ = 0;
        // 天
        if (consuming > d) {
            d_ = consuming.intValue() / d;
        }

        if (consuming - d_ * d > h) {
            h_ = (consuming.intValue() - d_ * d) / h;
        }

        if (consuming - d_ * d - h_ * h > m) {
            m_ = (consuming.intValue() - d_ * d - h_ * h) / m;
        }

        if (consuming - d_ * d - h_ * h - m_ * m > s) {
            s_ = (consuming.intValue() - d_ * d - h_ * h - m_ * m) / s;
        }

        String text = "";
        if (d_ != 0) {
            text += d_ + "天";
        }
        if (h_ != 0) {
            text += h_ + "小时";
        }
        if (m_ != 0) {
            text += m_ + "分钟";
        }
        if (s_ != 0) {
            text += s_ + "秒";
        }
        if (StringUtils.isBlank(text)) {
            text = consuming + "毫秒";
        }
        // System.out.println("耗时:" + text);
        return text;
    }

    // 根据格式获取指定天数之前的日期
    public static String getDayByDateStr(int num, String dateStr, String type) throws ParseException {
        return getDayByDateStr(num, dateStr, type, "yyyyMMdd HHmm");
    }

    public static String getDayByDateStr(int num, String dateStr, String type, String format) throws ParseException {
        Calendar c = Calendar.getInstance();
        c.setTime(getDate(dateStr, format));
        switch (type) {
            case "min":
                c.add(Calendar.MINUTE, num);
                break;
            case "hour":
                c.add(Calendar.HOUR, num);
                break;
            case "day":
                c.add(Calendar.DATE, num);
                break;
            case "month":
                c.add(Calendar.MONTH, num);
                break;
            case "quarter":
                c.add(Calendar.MONTH, num * 3);
                break;
            case "year":
                c.add(Calendar.YEAR, num);
                break;
            default:
                break;
        }
        SimpleDateFormat sd = new SimpleDateFormat(format);
        return sd.format(c.getTime());
    }

    public static String getDayByDateStr(int num, Date date, String type, String format) throws ParseException {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        switch (type) {
            case "min":
                c.add(Calendar.MINUTE, num);
                break;
            case "hour":
                c.add(Calendar.HOUR, num);
                break;
            case "day":
                c.add(Calendar.DATE, num);
                break;
            case "month":
                c.add(Calendar.MONTH, num);
                break;
            case "quarter":
                c.add(Calendar.MONTH, num * 3);
                break;
            case "year":
                c.add(Calendar.YEAR, num);
                break;
            default:
                break;
        }
        SimpleDateFormat sd = new SimpleDateFormat(format);
        return sd.format(c.getTime());
    }

    /**
     * 日期+分钟=新日期
     *
     * @param baseDateStr 14位
     * @param addMinStr   单位分钟
     * @return
     */
    public static String dateAdd(String baseDateStr, String addMinStr) {
        String retStr = "";
        Integer ssY = Integer.parseInt(baseDateStr.substring(0, 4));
        Integer ssM = Integer.parseInt(baseDateStr.substring(4, 6));
        Integer ssD = Integer.parseInt(baseDateStr.substring(6, 8));
        Integer ssH = Integer.parseInt(baseDateStr.substring(8, 10));
        Integer ssMi = Integer.parseInt(baseDateStr.substring(10, 12));
        Integer ssS = Integer.parseInt(baseDateStr.substring(12));
        Calendar calendar = Calendar.getInstance();
        calendar.set(ssY, ssM - 1, ssD, ssH, ssMi, ssS);
        calendar.add(Calendar.MINUTE, Integer.parseInt(addMinStr));
        retStr = new SimpleDateFormat("yyyyMMddHHmmss").format(calendar.getTime());
        return retStr;
    }

    /**
     * 获取下一天
     *
     * @param dateStr    日期字符串
     * @param dateFormat 日期格式化字符串
     */
    public static String getNextDay(String dateStr, String dateFormat) {
        return getNextDay(dateStr, dateFormat, 1);
    }

    /**
     * 获取下nextDays天
     *
     * @param dateStr    日期字符串
     * @param dateFormat 日期格式化字符串，默认值yyyyMMdd
     * @param nextDays   增加天数
     */
    public static String getNextDay(String dateStr, String dateFormat, int nextDays) {
        if (StringUtils.isBlank(dateFormat)) {
            dateFormat = "yyyyMMdd";
        }
        Date date = formatDate(dateStr, dateFormat);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, nextDays);
        return dateFormatStr(cal.getTime(), dateFormat);
    }

    /**
     * 将string类型数据转换为date格式
     *
     * @param date
     * @param format
     * @return
     */
    public static Date formatDate(String date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date parse = null;
        try {
            parse = simpleDateFormat.parse(date);
        } catch (ParseException e) {
//            log.error("日期转化错误：" + date + " 格式：" + format + "错误信息：" + e.getMessage());
            e.printStackTrace();
        }
        return parse;
    }

    /**
     * 将日期格式转换为String
     *
     * @param date
     * @param format
     * @return
     */
    public static String dateFormatStr(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    /**
     * 当前日期减一天
     *
     * @param date
     * @param format
     * @return
     */
    public static String getPrevDay(String date, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(formatDate(date, format));
        //-1代表日期减一天
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date time = calendar.getTime();
        return dateFormatStr(time, format);
    }

    /**
     * 判断两个日期时间差，以小时计算
     *
     * @param bDate
     * @param eDate
     * @return
     */
    public static long getHourDiff(String bDate, String eDate) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(formatDate(bDate, "yyyyMMddhhmmss"));
        long bTime = cd.getTimeInMillis();
        cd.setTime(formatDate(eDate, "yyyyMMddhhmmss"));
        long eTime = cd.getTimeInMillis();
        long Hour = ((eTime - bTime) / (1000L * 3600L));
        return Hour;
    }

    /**
     * 判断日期是否合法
     *
     * @param date
     * @param format
     * @return
     */
    public static Boolean isRightDate(String date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            Date parse = simpleDateFormat.parse(date);
        } catch (ParseException e) {
//            log.error("日期转化错误：" + date + " 格式：" + format + "错误信息：" + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 日期维度上的增加天数
     *
     * @param baseDateStr
     * @param dateNum
     * @return
     */
    public static String dateAddForDate(String baseDateStr, int dateNum) {
        String retStr = "";
        Integer ssY = Integer.parseInt(baseDateStr.substring(0, 4));
        Integer ssM = Integer.parseInt(baseDateStr.substring(4, 6));
        Integer ssD = Integer.parseInt(baseDateStr.substring(6, 8));
        Calendar calendar = Calendar.getInstance();
        calendar.set(ssY, ssM - 1, ssD);
        calendar.add(Calendar.DATE, dateNum);
        retStr = new SimpleDateFormat("yyyyMMdd").format(calendar.getTime());
        return retStr;
    }

    /**
     * Author         lv yz
     * Date           2021/10/29 14:58
     * Description    判断第一个日期是否小于第二个日期
     * param			第一个日期小 返回true
     * return
     */
    public static Boolean TimeCompare(String bDate, String eDate, String format) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(formatDate(bDate, format));
        long bTime = cd.getTimeInMillis();
        cd.setTime(formatDate(eDate, format));
        long eTime = cd.getTimeInMillis();
        return (bTime - eTime) < 0;
    }

    /**
     * @param dayStr 要修改的天数
     * @param month  支持正负数  负数  之前的月， 正数  之后的
     * @return ResponseDto        根据条件改变日期
     * @date: 2021/11/17 14:41
     */
    public static String getDayBy(String dayStr, Integer year, Integer month, Integer day) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date date = formatDate(dayStr, "yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (null != year) {
            calendar.add(Calendar.YEAR, year);
        }
        if (null != month) {
            calendar.add(Calendar.MONTH, month);
        }
        if (null != day) {
            calendar.add(Calendar.DAY_OF_MONTH, +day);
        }
        date = calendar.getTime();
        String accDate = format.format(date);
        return accDate;
    }

    // 根据格式获取当前时间
    public static String getTime(String formatStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        return sdf.format(new Date());
    }

    /**
     * 比较两个时间相差的毫秒数
     * @param
     */
//    public static Long getBetweenMs(String startTime, String endTime, String format) {
//        try {
//            SimpleDateFormat sdf = new SimpleDateFormat(format);
//            Date begin = sdf.parse(startTime);
//            Date end = sdf.parse(endTime);
//            return DateUtil.betweenMs(begin, end);
//        } catch (ParseException e) {
//            throw new BusinessException("", "时间输入错误");
//        }
//    }
//
//    /**
//     * 比较两个日期相差月份
//     * @param
//     */
//    public static Integer getDifferenceMonth(String beginDate, String endDate) {
//        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//            Date begin = sdf.parse(beginDate);
//            Date end = sdf.parse(endDate);
//            long l = DateUtil.betweenMonth(begin, end, true);
//            return Integer.valueOf(l + "");
//        } catch (ParseException e) {
//            throw new BusinessException("", "日期输入错误");
//        }
//    }

//    /**
//     * 比较两个日期相差天数
//     * @param
//     */
//    public static Integer getDifferenceDay(String beginDate, String endDate) {
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//        Date begin = null;
//        try {
//            begin = sdf.parse(beginDate);
//            Date end = sdf.parse(endDate);
//            long l = DateUtil.betweenDay(begin, end, true);
//            return Integer.valueOf(l + "");
//        } catch (ParseException e) {
//            e.printStackTrace();
//            throw new BusinessException("", "日期输入错误");
//        }
//    }
//
//    /**
//     * 时间区(比较当前时间是否在开始时间和结束时间区间中)
//     * @param beginDate
//     * @param endDate
//     */
//    public static void temporalInterval(String beginDate, String endDate) {
//        try {
//            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
//            Date currentTime = dateFormat.parse(dateFormat.format(new Date()));
//            Date beg = dateFormat.parse(beginDate);
//            Date end = dateFormat.parse(endDate);
//            Calendar dateTime = Calendar.getInstance();
//            dateTime.setTime(currentTime);
//            Calendar begTime = Calendar.getInstance();
//            begTime.setTime(beg);
//            Calendar endTime = Calendar.getInstance();
//            endTime.setTime(end);
//            if (dateTime.after(begTime) && dateTime.before(endTime)) {
//                return;
//            } else {
//                throw new BusinessException("", "不在交易时间内,请在每天的(" + beginDate + "-" + endDate + ")进行交易");
//            }
//        } catch (ParseException e) {
//            //log.error("不在交易时间内,请在每天的("+beginDate+"-"+endDate+")进行交易",e.getMessage());
//            e.printStackTrace();
//            throw new BusinessException("", "不在交易时间内,请在每天的(" + beginDate + "-" + endDate + ")进行交易");
//        }
//    }

    /**
     * 两个日期比较大小  startDate >=endDate true   小于 false
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static Boolean dateCompare(String startDate, String endDate) {
//        LogUtils.info("交易日期：{},生效日期：{}",startDate,endDate);
        Calendar cd = Calendar.getInstance();
        cd.setTime(formatDate(startDate, "yyyyMMdd"));
        long startTime = cd.getTimeInMillis();
        cd.setTime(formatDate(endDate, "yyyyMMdd"));
        long endTime = cd.getTimeInMillis();
//        LogUtils.info("计算后的交易日期:{},计算后的生效日期:{}",startTime,endTime);
        //            LogUtils.info("计算后的结果：{}",startTime - endTime);
        return startTime - endTime >= 0;
    }

    /**
     * @Description 获取上个月的第一天
     * @Author gengfei
     * @Date 2022/7/21 15:56
     * @Param date
     * @Return java.lang.String
     */
    public static String getLastMonthFirstDay(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(format.parse(date));
        cal.add(Calendar.MONTH, -1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return format.format(cal.getTime());
    }

    /**
     * @Description 获取上个月的最后一天
     * @Author gengfei
     * @Date 2022/7/21 15:56
     * @Param date
     * @Return java.lang.String
     */
    public static String getLastMonthEndDay(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(format.parse(date));
        cal.set(Calendar.DAY_OF_MONTH, 0);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return format.format(cal.getTime());
    }

    /**
     * @Description 获取上个月份
     * @Author gengfei
     * @Date 2022/7/21 15:55
     * @Param date
     * @Return java.lang.String
     */
    public static String getLastMonth(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(format1.parse(date));
        cal.add(Calendar.MONTH, -1);
        return format.format(cal.getTime());
    }

    /**
     * 根据传入的年数获取那年的天数 0为本年
     *
     * @param year
     * @return int
     * @Author sunzhiqiang
     * @Date 2022/7/5 15:56
     */
    public static int dayNum(int year) {
        if (year == 0) {
            return LocalDate.now().lengthOfYear();
        } else {
            return LocalDate.of(year, 1, 1).lengthOfYear();
        }
    }

    /**
     * @Description 获取本月的第一天
     * @Author gengfei
     * @Date 2022/7/21 15:55
     * @Param date
     * @Return java.lang.String
     */
    public static String getNowMonthFirstDay(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(format.parse(date));
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return format.format(cal.getTime());
    }

    /**
     * @Description 获取本月的最后一天
     * @Author gengfei
     * @Date 2022/7/21 15:55
     * @Param date
     * @Return java.lang.String
     */
    public static String getNowMonthEndDay(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(format.parse(date));
        cal.add(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, 0);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return format.format(cal.getTime());
    }

    /**
     * @Description 获取本月月份
     * @Author gengfei
     * @Date 2022/7/21 15:55
     * @Param date
     * @Return java.lang.String
     */
    public static String getNowMonth(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        return format.format(format1.parse(date));
    }

    /**
     * 校验YYYYMMDD格式的日期是否合法
     *
     * @param szBrDate 日期
     * @return ture 合法
     * @author Dyw
     */
    public static boolean checkDate(String szBrDate) {
        try {
            if (null == szBrDate || "".equals(szBrDate) || " ".equals(szBrDate)) {
                return false;
            }
            int yr = Integer.parseInt(szBrDate.substring(0, 4));
            int mon = Integer.parseInt(szBrDate.substring(4, 6));
            int day = Integer.parseInt(szBrDate.substring(6, 8));
            if (mon < 1 || mon > 12 || day < 1 || day > 31) {
                return false;
            }
            int leap = yr % 4 == 0 && yr % 100 != 0 || yr % 400 == 0 ? 1 : 0; //leap year
            if (day > day_tbl[leap][mon]) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 获取每个月的第一天
     *
     * @param date 格式：20240828
     * @return
     */
    public static String monthStartDay(String date) {
        String dd = date.substring(6, 8);
        String yearStarDay = String.valueOf(Integer.parseInt(date) - Integer.parseInt(dd) + 1);
        return yearStarDay;
    }

    /**
     * 求上个月的最后一天
     *
     * @return
     */
    public static String monthLastDay() {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        int lastdate = cal.getMinimum(Calendar.DATE);
        cal.set(Calendar.DAY_OF_MONTH, lastdate - 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(cal.getTime());
    }
}
