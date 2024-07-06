package com.github.kuangcp.stream;

import com.github.kuangcp.base.SpringBootTestStarter;
import com.github.kuangcp.sharding.manual.AuthUtil;
import com.github.kuangcp.simple.customer.dao.CustomerDao;
import com.github.kuangcp.simple.customer.domain.Customer;
import com.github.kuangcp.stream.dao.ReportDao;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2021-07-12 23:29
 */
@Slf4j
public class CursorSessionSpringBootTest extends SpringBootTestStarter {

    @Autowired
    private CustomerDao customerDao;
    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private ReportDao reportDao;

    @Test
    public void testStream() throws Exception {
        authUtil.completeAuth(3L);
        List<Customer> result = new ArrayList<>();
        customerDao.streamQueryAll(ctx -> {
            Customer val = ctx.getResultObject();
            result.add(val);
        });
        log.info("result={}", result);
    }

    @Test
    public void testInsertCache() throws Exception {
        List<String> strList = IntStream.range(0, 70000)
                .mapToObj(v -> UUID.randomUUID().toString()).collect(Collectors.toList());
        Random random = new Random();
        long userFrame = 1; // 构造不同数据段
        for (int i = 1; i < 120_000; i++) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime time = now.plusDays(-1 * (i / 2_000)).plusMinutes(i % 30).plusSeconds(i % 33).plusNanos(i % 4000);
            Date statisticDate = Date.from(time.atZone(ZoneId.systemDefault()).toInstant());

            Report report = Report.builder()
                    .userId((long) i % 6 + userFrame)
//                    .userId((long) i * new Random().nextInt(300))
                    .b(strList.get(random.nextInt(70000)))
                    .c(i % 7)
                    .d(i % 4)
                    .e(i % 2)
                    .statisticsTime(statisticDate)
                    .build();
            reportDao.insert(report);
        }
    }

    @Test
    public void testPageQueryOne() throws Exception {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("query");
        Date start = Date.from(LocalDateTime.now().plusDays(-500).atZone(ZoneId.systemDefault()).toInstant());
        List<Report> reports = reportDao.queryByPage(start, new Date(), 0, 1000);
        stopWatch.stop();
        log.info("reports.size()={}", reports.size());
        log.info("={}", stopWatch.prettyPrint());
    }

    // 90s = 30 * 3s
    @Test
    public void testPageQueryAll() throws Exception {
        StopWatch stopWatch = new StopWatch();
        for (int i = 0; i < 10; i++) {
            stopWatch.start("query" + i);
            Date start = Date.from(LocalDateTime.now().plusDays(-500).atZone(ZoneId.systemDefault()).toInstant());
            List<Report> reports = reportDao.queryByPage(start, new Date(), i * 1000, 1000);
            stopWatch.stop();
            log.info("reports.size()={}", reports.size());
        }
        log.info("={}", stopWatch.prettyPrint());
    }

    // 3s
    @Test
    public void testStreamQuery() throws Exception {
        Date start = Date.from(LocalDateTime.now().plusDays(-500).atZone(ZoneId.systemDefault()).toInstant());

        List<Report> result = new ArrayList<>();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("query");
        reportDao.selectAutoList(start, new Date(), ctx -> {
            Report val = ctx.getResultObject();
//            try {
//                TimeUnit.NANOSECONDS.sleep(100);
//            } catch (InterruptedException e) {
//                log.error("", e);
//            }
            result.add(val);
        });
        stopWatch.stop();
        log.info("result={} {}ms {}", result.size(), stopWatch.getTotalTimeMillis(), stopWatch.prettyPrint());

//        Path path = Paths.get("/tmp/report.scv");
//        String content = result.stream().map(report -> String.format("%d,%s,%d,%d,%d", report.getUserId(),
//                report.getInquiryCode(), report.getInquiryCount(), report.getHaveGoodsCount(), report.getPurchaseCount()))
//                .collect(Collectors.joining("\n"));
//        Files.write(path, content.getBytes());
    }
}
