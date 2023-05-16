package taozi.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    public static String getDate(){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String time = simpleDateFormat.format(date);
        return time;
    }
    /***带时分秒*/
    public static String getDateYmdHms(){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String time = simpleDateFormat.format(date);
        return time;
    }

    public static String getLastMonth(){
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date); // 设置为当前时间
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1); // 设置为上1个月
        date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
        String time = sdf.format(date);
        return time;
    }
    public static String getYear(String dateStr) throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM").parse(dateStr );
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);

       // int day = ca.get(Calendar.DAY_OF_YEAR);//一年中的第几天
       // int week = ca.get(Calendar.WEEK_OF_YEAR);//一年中的第几周
        int month = ca.get(Calendar.MONTH)+1;//第几个月 8
        int year = ca.get(Calendar.YEAR);//年份数值
///2023 年
        return String.valueOf(year);
    }
    public static String getMonth(String dateStr) throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM").parse(dateStr );
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);

        // int day = ca.get(Calendar.DAY_OF_YEAR);//一年中的第几天
        // int week = ca.get(Calendar.WEEK_OF_YEAR);//一年中的第几周
        int month = ca.get(Calendar.MONTH)+1;//第几个月 8
       // int year = ca.get(Calendar.YEAR);//年份数值
        return numToStr(String.valueOf(month))+"月";
    }


    /**
     * @param nun 0~9 ==>零~九
     * @return
     */
    public static String numToStr(String nun) {
        if (nun.equals("0")) {
            return "零";
        } else if (nun.equals("1")) {
            return "一";
        } else if (nun.equals("2")) {
            return "二";
        } else if (nun.equals("3")) {
            return "三";
        } else if (nun.equals("4")) {
            return "四";
        } else if (nun.equals("5")) {
            return "五";
        } else if (nun.equals("6")) {
            return "六";
        } else if (nun.equals("7")) {
            return "七";
        } else if (nun.equals("8")) {
            return "八";
        } else if (nun.equals("9")) {
            return "九";
        } else if (nun.equals("10")) {
            return "十";
        } else if (nun.equals("11")) {
            return "十一";
        } else if (nun.equals("12")) {
            return "十二";
        }
        return "";
    }
    //获取这个月最后一天
    public static String getLastOfMonth(String dateStr){
        Calendar cal = Calendar.getInstance();
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = new SimpleDateFormat("yyyy-MM").parse(dateStr );
            Calendar ca = Calendar.getInstance();
            ca.setTime(date);
            // int day = ca.get(Calendar.DAY_OF_YEAR);//一年中的第几天
            // int week = ca.get(Calendar.WEEK_OF_YEAR);//一年中的第几周
            int month = ca.get(Calendar.MONTH)+1;//第几个月 8
            int year = ca.get(Calendar.YEAR);//年份数值
            //设置年份
            cal.set(Calendar.YEAR,year);
            //设置月份
            cal.set(Calendar.MONTH, month);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //获取当月最小值
        int lastDay = cal.getMinimum(Calendar.DAY_OF_MONTH);
        //设置日历中的月份，当月+1月-1天=当月最后一天
        cal.set(Calendar.DAY_OF_MONTH, lastDay-1);

        String lastDayOfMonth = sdf.format(cal.getTime());
        return lastDayOfMonth;
    }
    public static void main(String[] args) {
        System.out.println(getLastOfMonth("2022-12"));
    }
}
