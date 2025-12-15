package com.github.kuangcp.time;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

/**
 * Java 8 日期API主要优势展示
 * 
 * @author kuangcp
 */
@Slf4j
public class Java8DateApiAdvantagesTest {

    /**
     * 优势1: 更清晰的API设计
     */
    @Test
    public void testClearApiDesign() {
        log.info("=== 优势1: 更清晰的API设计 ===");
        
        // 传统API - 月份从0开始，容易出错
        Calendar calendar = Calendar.getInstance();
        calendar.set(2023, Calendar.DECEMBER, 25); // 注意：DECEMBER是11，不是12
        Date date = calendar.getTime();
        
        // Java 8 API - 月份从1开始，更直观
        LocalDate localDate = LocalDate.of(2023, 12, 25); // 直接使用12表示12月
        
        log.info("传统API - 设置12月25日: {}", date);
        log.info("Java 8 - 设置12月25日: {}", localDate);
        log.info("优势: Java 8 API月份从1开始，更符合人类习惯");
    }

    /**
     * 优势2: 不可变性
     */
    @Test
    public void testImmutability() {
        log.info("=== 优势2: 不可变性 ===");
        
        // 传统API - 可变对象，容易产生副作用
        Date originalDate = new Date();
        Date modifiedDate = originalDate;
        modifiedDate.setTime(originalDate.getTime() + 86400000); // 修改会影响原对象
        
        // Java 8 API - 不可变对象，每次操作返回新对象
        LocalDateTime originalDateTime = LocalDateTime.now();
        LocalDateTime modifiedDateTime = originalDateTime.plusDays(1); // 返回新对象，原对象不变
        
        log.info("传统API - 原始日期: {}", originalDate);
        log.info("传统API - 修改后日期: {}", modifiedDate);
        log.info("传统API - 原始日期是否被修改: {}", originalDate.equals(modifiedDate));
        
        log.info("Java 8 - 原始日期时间: {}", originalDateTime);
        log.info("Java 8 - 修改后日期时间: {}", modifiedDateTime);
        log.info("Java 8 - 原始日期时间是否被修改: {}", originalDateTime.equals(modifiedDateTime));
        log.info("优势: Java 8 API不可变，避免副作用，更适合函数式编程");
    }

    /**
     * 优势3: 线程安全
     */
    @Test
    public void testThreadSafety() {
        log.info("=== 优势3: 线程安全 ===");
        
        // 传统API - SimpleDateFormat不是线程安全的
        // 在多线程环境中需要为每个线程创建独立的实例或使用ThreadLocal
        
        // Java 8 API - DateTimeFormatter是线程安全的
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        
        // 可以在多个线程中安全使用同一个formatter
        String formatted = now.format(formatter);
        
        log.info("Java 8 - 格式化结果: {}", formatted);
        log.info("优势: DateTimeFormatter线程安全，可以在多线程环境中共享使用");
    }

    /**
     * 优势4: 更好的时区支持
     */
    @Test
    public void testBetterTimeZoneSupport() {
        log.info("=== 优势4: 更好的时区支持 ===");
        
        // Java 8 API - 丰富的时区支持
        ZonedDateTime shanghaiTime = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"));
        ZonedDateTime newYorkTime = ZonedDateTime.now(ZoneId.of("America/New_York"));
        ZonedDateTime utcTime = ZonedDateTime.now(ZoneId.of("UTC"));
        
        log.info("上海时间: {}", shanghaiTime);
        log.info("纽约时间: {}", newYorkTime);
        log.info("UTC时间: {}", utcTime);
        
        // 时区转换
        ZonedDateTime converted = shanghaiTime.withZoneSameInstant(ZoneId.of("UTC"));
        log.info("上海时间转换为UTC: {}", converted);
        
        log.info("优势: Java 8 API提供更丰富的时区支持和转换功能");
    }

    /**
     * 优势5: 更精确的时间表示
     */
    @Test
    public void testPrecision() {
        log.info("=== 优势5: 更精确的时间表示 ===");
        
        // Java 8 API - 支持纳秒精度
        LocalDateTime preciseTime = LocalDateTime.now();
        Instant instant = Instant.now();
        
        log.info("LocalDateTime: {}", preciseTime);
        log.info("Instant: {}", instant);
        log.info("纳秒: {}", preciseTime.getNano());
        
        // 传统API - 只能精确到毫秒
        Date date = new Date();
        log.info("传统Date: {}", date);
        
        log.info("优势: Java 8 API支持纳秒精度，提供更精确的时间表示");
    }

    /**
     * 优势6: 更丰富的日期时间操作
     */
    @Test
    public void testRichOperations() {
        log.info("=== 优势6: 更丰富的日期时间操作 ===");
        
        LocalDateTime now = LocalDateTime.now();
        
        // 获取月份第一天
        LocalDateTime firstDayOfMonth = now.with(TemporalAdjusters.firstDayOfMonth());
        
        // 获取下个工作日
        LocalDateTime nextWorkingDay = now.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        
        // 获取本月的最后一个工作日
        LocalDateTime lastWorkingDayOfMonth = now.with(TemporalAdjusters.lastDayOfMonth())
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.FRIDAY));
        
        // 计算两个日期之间的差值
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        long monthsBetween = ChronoUnit.MONTHS.between(startDate, endDate);
        
        log.info("月份第一天: {}", firstDayOfMonth);
        log.info("下个工作日: {}", nextWorkingDay);
        log.info("本月最后一个工作日: {}", lastWorkingDayOfMonth);
        log.info("2023年总天数: {}", daysBetween);
        log.info("2023年总月数: {}", monthsBetween);
        
        log.info("优势: Java 8 API提供丰富的日期时间操作和计算功能");
    }

    /**
     * 优势7: 更好的格式化支持
     */
    @Test
    public void testBetterFormatting() {
        log.info("=== 优势7: 更好的格式化支持 ===");
        
        LocalDateTime now = LocalDateTime.now();
        
        // 预定义格式
        String isoFormat = now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        
        // 自定义格式
        String customFormat = now.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH时mm分ss秒"));
        
        // 本地化格式
        String localizedFormat = now.format(DateTimeFormatter.ofLocalizedDateTime(java.time.format.FormatStyle.MEDIUM));
        
        log.info("ISO格式: {}", isoFormat);
        log.info("自定义格式: {}", customFormat);
        log.info("本地化格式: {}", localizedFormat);
        
        log.info("优势: Java 8 API提供更丰富的格式化选项和本地化支持");
    }

    /**
     * 优势8: 更好的错误处理
     */
    @Test
    public void testBetterErrorHandling() {
        log.info("=== 优势8: 更好的错误处理 ===");
        
        // Java 8 API - 明确的异常处理
        try {
            LocalDate invalidDate = LocalDate.of(2023, 2, 30); // 2月没有30日
        } catch (DateTimeException e) {
            log.info("Java 8 API捕获到无效日期异常: {}", e.getMessage());
        }
        
        // 传统API - 可能静默失败或产生意外结果
        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(false);
        try {
            calendar.set(2023, Calendar.FEBRUARY, 30);
            calendar.getTime();
        } catch (Exception e) {
            log.info("传统API捕获到异常: {}", e.getMessage());
        }
        
        log.info("优势: Java 8 API提供更明确的错误处理和异常信息");
    }

    /**
     * 优势9: 更好的性能
     */
    @Test
    public void testBetterPerformance() {
        log.info("=== 优势9: 更好的性能 ===");
        
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
        log.info("优势: Java 8 API通常具有更好的性能");
    }

    /**
     * 优势10: 更好的可读性和维护性
     */
    @Test
    public void testBetterReadability() {
        log.info("=== 优势10: 更好的可读性和维护性 ===");
        
        // 传统API - 代码冗长，可读性差
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.HOUR_OF_DAY, 2);
        calendar.add(Calendar.MINUTE, 30);
        Date result1 = calendar.getTime();
        
        // Java 8 API - 代码简洁，可读性好
        LocalDateTime result2 = LocalDateTime.now()
                .plusDays(1)
                .plusHours(2)
                .plusMinutes(30);
        
        log.info("传统API结果: {}", result1);
        log.info("Java 8 API结果: {}", result2);
        log.info("优势: Java 8 API代码更简洁、更易读、更易维护");
    }

    /**
     * 总结Java 8日期API的主要优势
     */
    @Test
    public void testSummary() {
        log.info("=== Java 8日期API主要优势总结 ===");
        log.info("1. 更清晰的API设计 - 月份从1开始，更符合人类习惯");
        log.info("2. 不可变性 - 避免副作用，适合函数式编程");
        log.info("3. 线程安全 - DateTimeFormatter等类线程安全");
        log.info("4. 更好的时区支持 - 丰富的时区操作和转换");
        log.info("5. 更精确的时间表示 - 支持纳秒精度");
        log.info("6. 更丰富的日期时间操作 - TemporalAdjusters、ChronoUnit等");
        log.info("7. 更好的格式化支持 - 预定义格式、本地化等");
        log.info("8. 更好的错误处理 - 明确的异常信息");
        log.info("9. 更好的性能 - 通常比传统API更快");
        log.info("10. 更好的可读性和维护性 - 代码更简洁清晰");
        log.info("建议: 在新项目中使用Java 8日期API，逐步迁移现有代码");
    }
}

