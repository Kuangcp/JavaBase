package time;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjusters;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

/**
 * @author kuangcp on 18-9-12-下午7:05
 */
@Slf4j
public class LocalDateTest {

    @Test
    public void testFromLocalDateTime() {
        LocalDate localDate = LocalDateTime.now().toLocalDate();
        LocalDate last = LocalDateTime.now().plusDays(-1).toLocalDate();

        log.info("{}", localDate);
        log.info("{}", last);
    }

    @Test
    public void testFormatParse() throws Exception {
        // 注意接口使用差异
        {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd");
            TemporalAccessor ta = format.parse("20230804");
            System.out.println(ta);

            LocalDate parse = LocalDate.parse("20171018", format);
            System.out.println(parse);
            System.out.println(format.format(parse));
        }
        {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MMdd");
            TemporalAccessor ta = format.parse("2023-0804");
            System.out.println(ta);

            LocalDate parse = LocalDate.parse("2017-1018", format);
            System.out.println(parse);
            System.out.println(format.format(parse));
        }

        {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyyMM");
            Date xx = fmt.parse("202407");
            System.out.println(xx);

            DateTimeFormatter monthFMT = new DateTimeFormatterBuilder()
                    .appendPattern("yyyyMM")
                    .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                    .toFormatter();
            LocalDate parse = LocalDate.parse("201707", monthFMT);
            Assert.assertEquals(LocalDate.of(2017, 7, 1), parse);
            Assert.assertEquals("201707", monthFMT.format(parse));
        }
        {
            DateTimeFormatter monthFMT = new DateTimeFormatterBuilder()
                    .appendPattern("yyyy")
                    .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
                    .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                    .toFormatter();
            LocalDate parse = LocalDate.parse("2017", monthFMT);

            Assert.assertEquals(LocalDate.of(2017, 1, 1), parse);
            Assert.assertEquals("2017", monthFMT.format(parse));
        }
    }

    @Test(expected = DateTimeParseException.class)
    public void testParseYM() throws Exception {
        // 违反直觉，纯浪费时间
        DateTimeFormatter monthFMT = DateTimeFormatter.ofPattern("yyyyMM");
        LocalDate parse = LocalDate.parse("201707", monthFMT);
        System.out.println(parse);
        System.out.println(monthFMT.format(parse));
    }

    @Test(expected = DateTimeParseException.class)
    public void testParseYear() throws Exception {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy");
        LocalDate parse = LocalDate.parse("2017", format);
        System.out.println(parse);
    }

    @Test(expected = DateTimeParseException.class)
    public void testParseYearDay() throws Exception {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-dd");
        LocalDate parse = LocalDate.parse("2017-21", format);
        System.out.println(parse);
    }

    @Test
    public void testDateCalculation() {
        LocalDate today = LocalDate.of(2024, 3, 15);
        
        // 日期加减
        LocalDate tomorrow = today.plusDays(1);
        Assert.assertEquals("2024-03-16", tomorrow.toString());
        
        LocalDate nextWeek = today.plusWeeks(1);
        Assert.assertEquals("2024-03-22", nextWeek.toString());
        
        LocalDate lastMonth = today.minusMonths(1);
        Assert.assertEquals("2024-02-15", lastMonth.toString());
        
        LocalDate nextYear = today.plusYears(1);
        Assert.assertEquals("2025-03-15", nextYear.toString());
    }

    @Test
    public void testDateComparison() {
        LocalDate date1 = LocalDate.of(2024, 3, 15);
        LocalDate date2 = LocalDate.of(2024, 3, 16);
        
        Assert.assertTrue(date1.isBefore(date2));
        Assert.assertTrue(date2.isAfter(date1));
        Assert.assertFalse(date1.isEqual(date2));
        
        // 计算日期间隔
        Period period = Period.between(date1, date2);
        Assert.assertEquals(1, period.getDays());
        
        long daysBetween = ChronoUnit.DAYS.between(date1, date2);
        Assert.assertEquals(1, daysBetween);
    }

    @Test
    public void testSpecialDates() {
        LocalDate date = LocalDate.of(2024, 3, 15);
        
        // 获取当月第一天
        LocalDate firstDayOfMonth = date.with(TemporalAdjusters.firstDayOfMonth());
        Assert.assertEquals("2024-03-01", firstDayOfMonth.toString());
        
        // 获取当月最后一天
        LocalDate lastDayOfMonth = date.with(TemporalAdjusters.lastDayOfMonth());
        Assert.assertEquals("2024-03-31", lastDayOfMonth.toString());
        
        // 获取下一个周一
        LocalDate nextMonday = date.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        Assert.assertEquals("2024-03-18", nextMonday.toString());
        
        // 获取本月第一个周日
        LocalDate firstSundayOfMonth = date.with(TemporalAdjusters.firstInMonth(DayOfWeek.SUNDAY));
        Assert.assertEquals("2024-03-03", firstSundayOfMonth.toString());
    }

    @Test
    public void testDateProperties() {
        LocalDate date = LocalDate.of(2024, 3, 15);
        
        Assert.assertEquals(2024, date.getYear());
        Assert.assertEquals(3, date.getMonthValue());
        Assert.assertEquals(15, date.getDayOfMonth());
        Assert.assertEquals(DayOfWeek.FRIDAY, date.getDayOfWeek());
        Assert.assertEquals(75, date.getDayOfYear());
        
        Assert.assertTrue(date.isLeapYear());  // 2024是闰年
        Assert.assertEquals(31, date.lengthOfMonth());
        Assert.assertEquals(366, date.lengthOfYear());  // 闰年
    }

    @Test
    public void testDateCreation() {
        // 从年月日创建
        LocalDate date1 = LocalDate.of(2024, 3, 15);
        Assert.assertEquals("2024-03-15", date1.toString());
        
        // 从纪元日创建
        LocalDate date2 = LocalDate.ofEpochDay(19797); // 2024-03-15
        Assert.assertEquals("2024-03-15", date2.toString());
        
        // 从年份中的日子创建
        LocalDate date3 = LocalDate.ofYearDay(2024, 75); // 2024年第75天
        Assert.assertEquals("2024-03-15", date3.toString());
    }
}
