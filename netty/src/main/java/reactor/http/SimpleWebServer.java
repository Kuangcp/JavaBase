package reactor.http;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;
import reactor.netty.http.server.HttpServerRoutes;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Kuangcp
 * 2024-05-28 21:27
 */
@Slf4j
public class SimpleWebServer {
    public static void main(String[] args) {
        DisposableServer server = HttpServer.create()
                .route(SimpleWebServer::buildRouter)
                .port(9033)
                .bindNow();

        server.onDispose().block();
    }

    private static void buildRouter(HttpServerRoutes routes) {
        Consumer<Throwable> onError = (Throwable ex) -> log.error("", ex);


        routes
                .get("/ping",
                        (request, response) -> response.sendString(Mono.just("pong")))
                // body echo
                .post("/echo",
                        (request, response) -> response.send(request.receive().retain()))
                // url 路径参数 echo
                .get("/echo/pv/{param}",
                        (request, response) -> response.sendString(Mono.justOrEmpty(request.param("param"))))
                // query参数
                .get("/echo/p", (request, response) -> {
                    String uri = request.uri();
                    URI tmp = URI.create(uri);
                    try {
                        Map<String, String> params = splitQuery(tmp);
                        return response.sendString(Mono.just(params.get("id")));
                    } catch (Exception e) {
                        log.error("", e);
                    }
                    return response.sendString(Mono.just("Error"));
                })
                // 读取post body json参数，返回结果
                .post("/act/task", (request, response) -> response.sendString(request.receive()
                        .aggregate().asString()
                        // 注意使用的是map类似于Optional 无参不会调用，即body无内容时不会调用 handleTask
                        .map(SimpleWebServer::handleTask).doOnError(onError).flatMap(Mono::just)))
        // TODO form表单 https://projectreactor.io/docs/netty/release/reference/index.html#_reading_post_form_or_multipart_data
        ;
    }

    /**
     * @param param JSON字符串
     * @return 序列化后 JSON字符串
     */
    public static String handleTask(String param) {
        log.info("param={}", param);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error("", e);
        }
        return "Task";
    }

    public static Map<String, String> splitQuery(URI url) throws UnsupportedEncodingException {
        Map<String, String> query_pairs = new LinkedHashMap<>();
        String query = url.getQuery();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
        return query_pairs;
    }
}
