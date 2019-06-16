//package com.github.kuangcp.hi;
//
//import com.github.kuangcp.hi.util.SplitJobCommand;
//import com.baturu.ofc.constant.statistic.ProductStatisticJobCommand;
//import com.baturu.ofc.constant.statistic.ProductStatisticSpan;
//import com.baturu.ofc.util.JsonMapper;
//import org.junit.Test;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.client.RestTemplate;
//
//import java.nio.charset.StandardCharsets;
//import java.time.LocalDateTime;
//import java.util.Base64;
//import java.util.HashMap;
//import java.util.HashSet;
//
//import static com.github.kuangcp.hi.PartsSpuStatistic.calculateOrder;
//
///**
// * @author kuangchengping@qipeipu.com
// * @since 2019-05-21 14:59
// */
//public class PartsSpuStatisticTest {
//
//    @Test
//    public void testCalculate() throws Exception {
//        HashSet<ProductStatisticSpan> spans = new HashSet<>();
//        spans.add(ProductStatisticSpan.MONTH);
//
//        ProductStatisticJobCommand msg = ProductStatisticJobCommand.builder()
//                .productStatisticSpan(spans)
//                .startTime(SplitJobCommand.toDate(LocalDateTime.now().withMonth(1)))
//                .endTime(SplitJobCommand.toDate(LocalDateTime.now().withMonth(4)))
//                .build();
//
//        calculateOrder(msg);
//    }
//
//    @Test
//    public void testJson() {
//        HashSet<ProductStatisticSpan> spans = new HashSet<>();
//        spans.add(ProductStatisticSpan.MONTH);
//
//        ProductStatisticJobCommand msg = ProductStatisticJobCommand.builder()
//                .id("12")
//                .productStatisticSpan(spans)
//                .startTime(SplitJobCommand.toDate(LocalDateTime.now().withMonth(1)))
//                .endTime(SplitJobCommand.toDate(LocalDateTime.now().withMonth(3)))
//                .build();
//        String json = JsonMapper.obj2String(msg);
//
//        byte[] result = Base64.getEncoder().encode(json.getBytes());
//        System.out.println(new String(result, StandardCharsets.UTF_8));
//
//        byte[] origin = Base64.getDecoder().decode(result);
//        System.out.println(new String(origin, StandardCharsets.UTF_8));
//    }
//
//    @Test
//    public void testRest() {
//        String BASE_URL = "http://127.0.0.1:8081/jars/";
//        String JAR_NAME = "6b74c981-ed07-4ad3-97e0-5e47f570ca49_btr-ofc-batch-test32-jar-with-dependencies.jar";
//
//        String msg = "eyJpZCI6IjEyIiwicHJvZHVjdFN0YXRpc3RpY1NwYW4iOlsiTU9OVEgiXSwic3RhcnRUaW1lIjoxNTQ4OTE0MTY3NzcxLCJlbmRUaW1lIjoxNTU0MDExNzY3ODExfQ==";
//        String url = BASE_URL + JAR_NAME + "/run?entry-class=com.baturu.ofc.batch.PartsSpuStatistic&program-args=" + msg;
////        String url = BASE_URL + JAR_NAME + "/run?entry-class=com.baturu.ofc.batch.word.WordCount";
//
//        RestTemplate restTemplate = new RestTemplate();
//        HttpEntity httpEntity = new HttpEntity<>(new HashMap<>());
//
//        ResponseEntity<String> request = restTemplate.postForEntity(url, httpEntity, String.class);
//        System.out.println(request.getBody());
//    }
//}