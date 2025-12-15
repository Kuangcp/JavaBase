package com.github.kuangcp.time;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

/**
 * Java 8 新日期API vs 传统日期API 对比测试
 * 
 * @author kuangcp
 */
@Slf4j
public class DateApiComparisonTest {

    /**
     * 获取当前时间对比
     */
    @Test
    public void testGetCurrentTime() {
        log.info("=== 获取当前时间对比 ===");
        
        // 传统API
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        
        // Java 8 API
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();
        Instant instant = Instant.now();
        
        log.info("传统API - Date: {}", now);
        log.info("传统API - Calendar: {}", calendar.getTime());
        log.info("Java 8 - LocalDateTime: {}", localDateTime);
        log.info("Java 8 - LocalDate: {}", localDate);
        log.info("Java 8 - LocalTime: {}", localTime);
        log.info("Java 8 - Instant: {}", instant);
    }

    /**
     * 创建指定日期时间对比
     */
    @Test
    public void testCreateSpecificDateTime() {
        log.info("=== 创建指定日期时间对比 ===");
        
        // 传统API - 创建2023年12月25日 15:30:45
        Calendar calendar = Calendar.getInstance();
        calendar.set(2023, Calendar.DECEMBER, 25, 15, 30, 45);
        calendar.set(Calendar.MILLISECOND, 0);
        Date specificDate = calendar.getTime();
        
        // Java 8 API
        LocalDateTime specificDateTime = LocalDateTime.of(2023, 12, 25, 15, 30, 45);
        LocalDate specificDateOnly = LocalDate.of(2023, 12, 25);
        LocalTime specificTimeOnly = LocalTime.of(15, 30, 45);
        
        log.info("传统API - 指定日期时间: {}", specificDate);
        log.info("Java 8 - LocalDateTime: {}", specificDateTime);
        log.info("Java 8 - LocalDate: {}", specificDateOnly);
        log.info("Java 8 - LocalTime: {}", specificTimeOnly);
    }

    /**
     * 日期时间计算对比
     */
    @Test
    public void testDateTimeCalculation() {
        log.info("=== 日期时间计算对比 ===");
        
        // 传统API - 加一天
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date tomorrow = calendar.getTime();
        
        // 传统API - 减一个月
        calendar.add(Calendar.MONTH, -1);
        Date lastMonth = calendar.getTime();
        
        // Java 8 API - 加一天
        LocalDateTime tomorrow8 = LocalDateTime.now().plusDays(1);
        
        // Java 8 API - 减一个月
        LocalDateTime lastMonth8 = LocalDateTime.now().minusMonths(1);
        
        // Java 8 API - 更灵活的计算
        LocalDateTime nextWeek = LocalDateTime.now().plus(1, ChronoUnit.WEEKS);
        LocalDateTime nextYear = LocalDateTime.now().plus(1, ChronoUnit.YEARS);
        
        log.info("传统API - 明天: {}", tomorrow);
        log.info("传统API - 上个月: {}", lastMonth);
        log.info("Java 8 - 明天: {}", tomorrow8);
        log.info("Java 8 - 上个月: {}", lastMonth8);
        log.info("Java 8 - 下周: {}", nextWeek);
        log.info("Java 8 - 明年: {}", nextYear);
    }

    /**
     * 日期时间比较对比
     */
    @Test
    public void testDateTimeComparison() {
        log.info("=== 日期时间比较对比 ===");
        
        // 传统API
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal2.add(Calendar.DAY_OF_MONTH, 1);
        
        boolean isBefore = cal1.before(cal2);
        boolean isAfter = cal1.after(cal2);
        int comparison = cal1.compareTo(cal2);
        
        // Java 8 API
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = now.plusDays(1);
        
        boolean isBefore8 = now.isBefore(tomorrow);
        boolean isAfter8 = now.isAfter(tomorrow);
        boolean isEqual8 = now.isEqual(tomorrow);
        
        log.info("传统API - isBefore: {}, isAfter: {}, comparison: {}", isBefore, isAfter, comparison);
        log.info("Java 8 - isBefore: {}, isAfter: {}, isEqual: {}", isBefore8, isAfter8, isEqual8);
    }

    /**
     * 格式化对比
     */
    @Test
    public void testFormatting() {
        log.info("=== 格式化对比 ===");
        
        // 传统API
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf.format(now);
        
        // Java 8 API
        LocalDateTime now8 = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate8 = now8.format(formatter);
        
        // Java 8 预定义格式
        String isoDate = now8.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String customFormat = now8.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH时mm分ss秒"));
        
        log.info("传统API - 格式化: {}", formattedDate);
        log.info("Java 8 - 格式化: {}", formattedDate8);
        log.info("Java 8 - ISO格式: {}", isoDate);
        log.info("Java 8 - 中文格式: {}", customFormat);
    }

    /**
     * 解析字符串对比
     */
    @Test
    public void testParsing() {
        log.info("=== 解析字符串对比 ===");
        
        String dateStr = "2023-12-25 15:30:45";
        
        try {
            // 传统API
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date parsedDate = sdf.parse(dateStr);
            
            // Java 8 API
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime parsedDateTime = LocalDateTime.parse(dateStr, formatter);
            
            log.info("传统API - 解析结果: {}", parsedDate);
            log.info("Java 8 - 解析结果: {}", parsedDateTime);
            
        } catch (Exception e) {
            log.error("解析失败", e);
        }
    }

    /**
     * 获取日期时间组件对比
     */
    @Test
    public void testGetComponents() {
        log.info("=== 获取日期时间组件对比 ===");
        
        // 传统API
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // 注意：Calendar月份从0开始
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        
        // Java 8 API
        LocalDateTime now = LocalDateTime.now();
        int year8 = now.getYear();
        int month8 = now.getMonthValue();
        int day8 = now.getDayOfMonth();
        int hour8 = now.getHour();
        int minute8 = now.getMinute();
        int second8 = now.getSecond();
        
        log.info("传统API - 年:{}, 月:{}, 日:{}, 时:{}, 分:{}, 秒:{}", 
                year, month, day, hour, minute, second);
        log.info("Java 8 - 年:{}, 月:{}, 日:{}, 时:{}, 分:{}, 秒:{}", 
                year8, month8, day8, hour8, minute8, second8);
    }

    /**
     * 时区处理对比
     */
    @Test
    public void testTimeZoneHandling() {
        log.info("=== 时区处理对比 ===");
        
        // 传统API
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(java.util.TimeZone.getTimeZone("Asia/Shanghai"));
        Date shanghaiTime = calendar.getTime();
        
        // Java 8 API
        ZonedDateTime shanghaiDateTime = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"));
        ZonedDateTime utcDateTime = ZonedDateTime.now(ZoneId.of("UTC"));
        
        log.info("传统API - 上海时间: {}", shanghaiTime);
        log.info("Java 8 - 上海时间: {}", shanghaiDateTime);
        log.info("Java 8 - UTC时间: {}", utcDateTime);
        log.info("Java 8 - 时区转换: {}", shanghaiDateTime.withZoneSameInstant(ZoneId.of("UTC")));
    }

    /**
     * 不可变性对比
     */
    @Test
    public void testImmutability() {
        log.info("=== 不可变性对比 ===");
        
        // 传统API - Date是可变的
        Date originalDate = new Date();
        Date modifiedDate = originalDate;
        modifiedDate.setTime(originalDate.getTime() + 86400000); // 加一天
        
        log.info("传统API - 原始日期: {}", originalDate);
        log.info("传统API - 修改后日期: {}", modifiedDate);
        log.info("传统API - 原始日期是否被修改: {}", originalDate.equals(modifiedDate));
        
        // Java 8 API - 不可变对象
        LocalDateTime originalDateTime = LocalDateTime.now();
        LocalDateTime modifiedDateTime = originalDateTime.plusDays(1);
        
        log.info("Java 8 - 原始日期时间: {}", originalDateTime);
        log.info("Java 8 - 修改后日期时间: {}", modifiedDateTime);
        log.info("Java 8 - 原始日期时间是否被修改: {}", originalDateTime.equals(modifiedDateTime));
    }

    /**
     * 性能对比测试
     */
    @Test
    public void testPerformance() {
        log.info("=== 性能对比测试 ===");
        
        int iterations = 100000;
        
        // 传统API性能测试
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, i);
            Date date = calendar.getTime();
        }
        long traditionalTime = System.currentTimeMillis() - startTime;
        
        // Java 8 API性能测试
        startTime = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            LocalDateTime dateTime = LocalDateTime.now().plusDays(i);
        }
        long java8Time = System.currentTimeMillis() - startTime;
        
        log.info("传统API - {}次操作耗时: {}ms", iterations, traditionalTime);
        log.info("Java 8 API - {}次操作耗时: {}ms", iterations, java8Time);
        log.info("性能提升: {}%", ((double)(traditionalTime - java8Time) / traditionalTime) * 100);
    }
}

