package com.github.kuangcp.time;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期时间操作场景对比测试
 * 
 * @author kuangcp
 */
@Slf4j
public class DateTimeOperationsComparisonTest {

    /**
     * 获取月份第一天和最后一天对比
     */
    @Test
    public void testMonthBoundaries() {
        log.info("=== 获取月份边界对比 ===");
        
        // 传统API - 获取当前月份第一天
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date firstDayOfMonth = calendar.getTime();
        
        // 传统API - 获取当前月份最后一天
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Date lastDayOfMonth = calendar.getTime();
        
        // Java 8 API - 获取当前月份第一天
        LocalDateTime firstDay8 = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        
        // Java 8 API - 获取当前月份最后一天
        LocalDateTime lastDay8 = LocalDateTime.now().with(TemporalAdjusters.lastDayOfMonth())
                .withHour(23).withMinute(59).withSecond(59).withNano(999999999);
        
        log.info("传统API - 月份第一天: {}", firstDayOfMonth);
        log.info("传统API - 月份最后一天: {}", lastDayOfMonth);
        log.info("Java 8 - 月份第一天: {}", firstDay8);
        log.info("Java 8 - 月份最后一天: {}", lastDay8);
    }

    /**
     * 计算两个日期之间的差值对比
     */
    @Test
    public void testDateDifference() {
        log.info("=== 计算日期差值对比 ===");
        
        // 传统API - 计算两个日期之间的天数差
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal2.add(Calendar.DAY_OF_MONTH, 10);
        
        long diffInMillis = cal2.getTimeInMillis() - cal1.getTimeInMillis();
        long diffInDays = diffInMillis / (24 * 60 * 60 * 1000);
        
        // Java 8 API - 计算两个日期之间的差值
        LocalDate date1 = LocalDate.now();
        LocalDate date2 = date1.plusDays(10);
        
        long diffInDays8 = ChronoUnit.DAYS.between(date1, date2);
        long diffInWeeks8 = ChronoUnit.WEEKS.between(date1, date2);
        long diffInMonths8 = ChronoUnit.MONTHS.between(date1, date2);
        
        log.info("传统API - 天数差: {}", diffInDays);
        log.info("Java 8 - 天数差: {}", diffInDays8);
        log.info("Java 8 - 周数差: {}", diffInWeeks8);
        log.info("Java 8 - 月数差: {}", diffInMonths8);
    }

    /**
     * 工作日计算对比
     */
    @Test
    public void testWorkingDays() {
        log.info("=== 工作日计算对比 ===");
        
        // 传统API - 计算下一个工作日
        Calendar calendar = Calendar.getInstance();
        do {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        } while (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || 
                 calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY);
        Date nextWorkingDay = calendar.getTime();
        
        // Java 8 API - 计算下一个工作日
        LocalDate nextWorkingDay8 = LocalDate.now();
        do {
            nextWorkingDay8 = nextWorkingDay8.plusDays(1);
        } while (nextWorkingDay8.getDayOfWeek() == DayOfWeek.SATURDAY || 
                 nextWorkingDay8.getDayOfWeek() == DayOfWeek.SUNDAY);
        
        log.info("传统API - 下一个工作日: {}", nextWorkingDay);
        log.info("Java 8 - 下一个工作日: {}", nextWorkingDay8);
    }

    /**
     * 季度计算对比
     */
    @Test
    public void testQuarterCalculation() {
        log.info("=== 季度计算对比 ===");
        
        // 传统API - 获取当前季度
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int quarter = (month / 3) + 1;
        
        // 传统API - 获取季度第一天
        calendar.set(Calendar.MONTH, (quarter - 1) * 3);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date quarterStart = calendar.getTime();
        
        // Java 8 API - 获取当前季度
        LocalDate now = LocalDate.now();
        int quarter8 = (now.getMonthValue() - 1) / 3 + 1;
        
        // Java 8 API - 获取季度第一天
        LocalDate quarterStart8 = now.withMonth((quarter8 - 1) * 3 + 1).withDayOfMonth(1);
        
        log.info("传统API - 当前季度: Q{}", quarter);
        log.info("传统API - 季度第一天: {}", quarterStart);
        log.info("Java 8 - 当前季度: Q{}", quarter8);
        log.info("Java 8 - 季度第一天: {}", quarterStart8);
    }

    /**
     * 年龄计算对比
     */
    @Test
    public void testAgeCalculation() {
        log.info("=== 年龄计算对比 ===");
        
        // 传统API - 计算年龄
        Calendar birthDate = Calendar.getInstance();
        birthDate.set(1990, Calendar.JANUARY, 15);
        
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < birthDate.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        
        // Java 8 API - 计算年龄
        LocalDate birthDate8 = LocalDate.of(1990, 1, 15);
        LocalDate today8 = LocalDate.now();
        long age8 = ChronoUnit.YEARS.between(birthDate8, today8);
        
        log.info("传统API - 年龄: {}岁", age);
        log.info("Java 8 - 年龄: {}岁", age8);
    }

    /**
     * 时间间隔计算对比
     */
    @Test
    public void testTimeInterval() {
        log.info("=== 时间间隔计算对比 ===");
        
        // 传统API - 计算两个时间点之间的间隔
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 9);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.SECOND, 0);
        
        Calendar endTime = Calendar.getInstance();
        endTime.set(Calendar.HOUR_OF_DAY, 17);
        endTime.set(Calendar.MINUTE, 30);
        endTime.set(Calendar.SECOND, 0);
        
        long diffInMillis = endTime.getTimeInMillis() - startTime.getTimeInMillis();
        long hours = diffInMillis / (60 * 60 * 1000);
        long minutes = (diffInMillis % (60 * 60 * 1000)) / (60 * 1000);
        
        // Java 8 API - 计算时间间隔
        LocalTime startTime8 = LocalTime.of(9, 0);
        LocalTime endTime8 = LocalTime.of(17, 30);
        
        Duration duration = Duration.between(startTime8, endTime8);
        long hours8 = duration.toHours();
        long minutes8 = duration.toMinutes() % 60;
        
        log.info("传统API - 工作时间: {}小时{}分钟", hours, minutes);
        log.info("Java 8 - 工作时间: {}小时{}分钟", hours8, minutes8);
    }

    /**
     * 日期验证对比
     */
    @Test
    public void testDateValidation() {
        log.info("=== 日期验证对比 ===");
        
        // 传统API - 验证日期有效性
        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(false);
        boolean isValidTraditional = true;
        try {
            calendar.set(2023, Calendar.FEBRUARY, 30); // 无效日期
            calendar.getTime();
        } catch (Exception e) {
            isValidTraditional = false;
        }
        
        // Java 8 API - 验证日期有效性
        boolean isValidJava8 = true;
        try {
            LocalDate.of(2023, 2, 30); // 无效日期
        } catch (DateTimeException e) {
            isValidJava8 = false;
        }
        
        log.info("传统API - 2023年2月30日是否有效: {}", isValidTraditional);
        log.info("Java 8 - 2023年2月30日是否有效: {}", isValidJava8);
    }

    /**
     * 夏令时处理对比
     */
    @Test
    public void testDaylightSavingTime() {
        log.info("=== 夏令时处理对比 ===");
        
        // 传统API - 夏令时处理
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(java.util.TimeZone.getTimeZone("America/New_York"));
        calendar.set(2023, Calendar.MARCH, 12, 2, 30, 0); // 夏令时开始时间
        Date dstStart = calendar.getTime();
        
        // Java 8 API - 夏令时处理
        ZonedDateTime dstStart8 = ZonedDateTime.of(2023, 3, 12, 2, 30, 0, 0, 
                ZoneId.of("America/New_York"));
        
        log.info("传统API - 夏令时开始: {}", dstStart);
        log.info("Java 8 - 夏令时开始: {}", dstStart8);
        log.info("Java 8 - 是否夏令时: {}", dstStart8.getZone().getRules().isDaylightSavings(dstStart8.toInstant()));
    }

    /**
     * 日期范围检查对比
     */
    @Test
    public void testDateRangeCheck() {
        log.info("=== 日期范围检查对比 ===");
        
        // 传统API - 检查日期是否在范围内
        Calendar startDate = Calendar.getInstance();
        startDate.set(2023, Calendar.JANUARY, 1);
        
        Calendar endDate = Calendar.getInstance();
        endDate.set(2023, Calendar.DECEMBER, 31);
        
        Calendar checkDate = Calendar.getInstance();
        checkDate.set(2023, Calendar.JUNE, 15);
        
        boolean isInRange = checkDate.after(startDate) && checkDate.before(endDate);
        
        // Java 8 API - 检查日期是否在范围内
        LocalDate startDate8 = LocalDate.of(2023, 1, 1);
        LocalDate endDate8 = LocalDate.of(2023, 12, 31);
        LocalDate checkDate8 = LocalDate.of(2023, 6, 15);
        
        boolean isInRange8 = checkDate8.isAfter(startDate8) && checkDate8.isBefore(endDate8);
        
        log.info("传统API - 日期是否在范围内: {}", isInRange);
        log.info("Java 8 - 日期是否在范围内: {}", isInRange8);
    }

    /**
     * 线程安全性对比
     */
    @Test
    public void testThreadSafety() {
        log.info("=== 线程安全性对比 ===");
        
        // 传统API - SimpleDateFormat不是线程安全的
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        // Java 8 API - DateTimeFormatter是线程安全的
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        log.info("传统API - SimpleDateFormat线程安全: false");
        log.info("Java 8 - DateTimeFormatter线程安全: true");
        log.info("建议: 在多线程环境中使用Java 8的DateTimeFormatter");
    }
}

