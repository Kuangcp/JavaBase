package time;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

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
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd");
        TemporalAccessor ta = format.parse("20230804");
        System.out.println(ta);

        LocalDate parse = LocalDate.parse("20171018", format);
        System.out.println(parse);
        System.out.println(format.format(parse));
    }
}
