package time;


import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

/**
 * @author kuangcp on 18-9-11-下午3:52
 * LocalDateTime 本身是和时区没有关联的,
 * 如果是采用 now() 得到LocalDateTime的实例 就会采用操作系统默认时区
 * 如果是采用 of() 得到LocalDateTime的实例 就不会和时区有关系, 采用默认的UTC
 * <p>
 * LocalDateTime 就是 主要由 LocalDate LocalTime 组成的对象, 唯一的私有构造器就说明了这一点
 */
@Slf4j
public class LocalDateTimeTest {

    @Test
    public void testNow() {
        // 这里默认使用了当前时区 计算得到时间
        LocalDateTime now = LocalDateTime.now();

        // 这里将时区的影响消除
        long milli = now.atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli();

        log.info(": milli={}", milli);
        log.info(": milli={}", System.currentTimeMillis());

        // 这个差就是以上转换消耗的时间
        assertThat(System.currentTimeMillis() - milli, lessThan(10L));
    }

    @Test
    public void testOf() {
        LocalDateTime of = LocalDateTime.of(2018, 1, 23, 22, 22);
        log.info(": of={}", of.toString());
        String instant = of.atZone(ZoneOffset.systemDefault()).toInstant().toString();
        log.info(": instant={}", instant);
    }

    @Test
    public void testDate() throws Exception {
        final Date date = new Date(585327600000L);
        System.out.println(date);
    }

    @Test
    public void testFormat() throws Exception {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        String str = format.format(LocalDateTime.now());
        System.out.println(str);

        LocalDateTime last = LocalDateTime.parse(str, format);
        System.out.println(last);
        System.out.println(format.format(last));

        format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.println(format.format(LocalDate.now()));
    }

    @Test(expected = DateTimeParseException.class)
    public void testParseError() throws Exception {
        // 字符串内不含时间，解析失败
        LocalDateTime yyyyMMdd = LocalDateTime.parse("20230804", DateTimeFormatter.ofPattern("yyyyMMdd"));
        System.out.println(yyyyMMdd);
    }

}
