package time;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
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
}
