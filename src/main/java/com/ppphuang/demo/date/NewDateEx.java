package com.ppphuang.demo.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class NewDateEx {
    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static ThreadLocal<SimpleDateFormat> threadSafeSimpleDateFormat = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    public static void main(String[] args) throws InterruptedException {

        //一个时间表示
        String stringDate = "2020-01-02 22:00:00";
        //初始化三个时区
        ZoneId timeZoneSH = ZoneId.of("Asia/Shanghai");
        ZoneId timeZoneNY = ZoneId.of("America/New_York");
        ZoneId timeZoneJST = ZoneOffset.ofHours(9);
        //格式化器
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ZonedDateTime date = ZonedDateTime.of(LocalDateTime.parse(stringDate, dateTimeFormatter), timeZoneJST);
        //使用DateTimeFormatter格式化时间，可以通过withZone方法直接设置格式化使用的时区
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");
        System.out.println(timeZoneSH.getId() + outputFormat.withZone(timeZoneSH).format(date));
        System.out.println(timeZoneNY.getId() + outputFormat.withZone(timeZoneNY).format(date));
        System.out.println(timeZoneJST.getId() + outputFormat.withZone(timeZoneJST).format(date));
        //Asia/Shanghai 2020-01-02 21:00:00 +0800
        //America/New_York 2020-01-02 08:00:00 -0500
        //+09:00 2020-01-02 22:00:00 +0900


        //菜鸡写法
        Date today = new Date();
        Date nextMonth = new Date(today.getTime() + 30L * 1000 * 60 * 60 * 24);
//        Date nextMonth = new Date(today.getTime() + 30 * 1000 * 60 * 60 * 24); 其实是因为 int 发生了溢出
        System.out.println(today);
        System.out.println(nextMonth);

        //菜鸡写法Java8之前写法
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_MONTH, 30);
        System.out.println(c.getTime());

        //Java8写法

        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(localDateTime.plusDays(30));

        //可以使用各种 minus 和 plus 方法直接对日期进行加减操作，比如如下代码实现了减一天和加一天，以及减一个月和加一个月：
        System.out.println("//测试操作日期");
        System.out.println(LocalDate.now()
                .minus(Period.ofDays(1))
                .plus(1, ChronoUnit.DAYS)
                .minusMonths(1)
                .plus(Period.ofMonths(1)));


        System.out.println("//本月的第一天");
        System.out.println(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()));

        System.out.println("//今年的程序员日");
        System.out.println(LocalDate.now().with(TemporalAdjusters.firstDayOfYear()).plusDays(255));

        System.out.println("//今天之前的一个周六");
        System.out.println(LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.SATURDAY)));

        System.out.println("//本月最后一个工作日");
        System.out.println(LocalDate.now().with(TemporalAdjusters.lastInMonth(DayOfWeek.FRIDAY)));


        //线程池不允许使用Executors去创建，而是通过ThreadPoolExecutor的方式，这样的处理方式让写的同学更加明确线程池的运行规则
        ExecutorService threadPool = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 20; i++) {
            //提交20个并发解析时间的任务到线程池，模拟并发环境
            threadPool.execute(() -> {
                for (int j = 0; j < 10; j++) {
                    try {
//                        System.out.println(simpleDateFormat.parse("2020-01-01 11:12:13"));
                        //因此只能在同一个线程复用 SimpleDateFormat，比较好的解决方式是，通过 ThreadLocal 来存放 SimpleDateFormat：
                        System.out.println(threadSafeSimpleDateFormat.get().parse("2020-01-01 11:12:13"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        threadPool.shutdown();
        boolean b = threadPool.awaitTermination(1, TimeUnit.HOURS);

//        运行程序后大量报错，且没有报错的输出结果也不正常，比如 2020 年解析成了 1212 年：
//        SimpleDateFormat 的作用是定义解析和格式化日期时间的模式。这，看起来这是一次性的工作，应该复用，但它的解析和格式化操作是非线程安全的。
//        我们来分析一下相关源码：SimpleDateFormat 继承了 DateFormat，DateFormat 有一个字段 Calendar；SimpleDateFormat 的 parse 方法调用 CalendarBuilder 的 establish 方法，
//        来构建 Calendar；establish 方法内部先清空 Calendar 再构建 Calendar，整个操作没有加锁。显然，如果多线程池调用 parse 方法，也就意味着多线程在并发操作一个 Calendar，
//        可能会产生一个线程还没来得及处理 Calendar 就被另一个线程清空了的情况

//        因此只能在同一个线程复用 SimpleDateFormat，比较好的解决方式是，通过 ThreadLocal 来存放 SimpleDateFormat：


    }
}
