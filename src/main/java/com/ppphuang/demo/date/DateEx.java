package com.ppphuang.demo.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateEx {

    public static void main(String[] args) throws ParseException {

        Date date = new Date(2019, 12, 31, 11, 12, 13);
        System.out.println(date);
        //Sat Jan 31 11:12:13 CST 3920
        //年应该是和 1900 的差值，月应该是从 0 到 11 而不是从 1 到 12
        date = new Date(2019 - 1900, 11, 31, 11, 12, 13);
        System.out.println(date);
        //Tue Dec 31 11:12:13 CST 2019

        Calendar calendar = Calendar.getInstance();
        calendar.set(2019,11,31,11,12,13);
        System.out.println(calendar.getTime());
        //Tue Dec 31 11:12:13 CST 2019
        Calendar calendar2 = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
        calendar2.set(2019, Calendar.DECEMBER,31,11,12,13);
        System.out.println(calendar2.getTime());
        //Wed Jan 01 00:12:13 CST 2020


        System.out.println(new Date(0));
        //Thu Jan 01 08:00:00 CST 1970
        System.out.println(TimeZone.getDefault().getID() + ":" + TimeZone.getDefault().getRawOffset()/3600000);
        //Asia/Shanghai:8
        //我得到的是 1970 年 1 月 1 日 8 点。因为我机器当前的时区是中国上海，相比 UTC 时差 +8 小时：

        String stringDate = "2020-01-02 22:00:00";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //默认时区解析时间表示
        Date parse = simpleDateFormat.parse(stringDate);
        System.out.println(parse + ":" + parse.getTime());
        //Thu Jan 02 22:00:00 CST 2020:1577973600000
        //纽约时区解析时间表示
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        Date parse1 = simpleDateFormat.parse(stringDate);
        System.out.println(parse1 + ":" + parse1.getTime());
        //Fri Jan 03 11:00:00 CST 2020:1578020400000


        String stringDate1 = "2020-01-02 22:00:00";
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //同一Date
        Date date2 = inputFormat.parse(stringDate1);
        //默认时区格式化输出：
        System.out.println(new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss Z]").format(date2));
        //[2020-01-02 22:00:00 +0800]
        //纽约时区格式化输出
        TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
        System.out.println(new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss Z]").format(date2));
        //[2020-01-02 09:00:00 -0500]

        inputFormat.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        //同一Date
        Date date3 = inputFormat.parse(stringDate1);
        System.out.println(new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss Z]").format(date3));
        //[2020-01-02 22:00:00 -0500]
        //序列化的时候按 SimpleDateFormat 时区来，没显式设置会按当前时区


    }
}
