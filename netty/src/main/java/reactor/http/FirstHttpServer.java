package reactor.http;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * https://projectreactor.io/docs/netty/release/reference/index.html
 *
 * @author <a href="https://github.com/kuangcp">Github</a>
 * 2023-10-10 09:52
 */
public class FirstHttpServer {

    public static void main(String[] args) {
        DisposableServer server = HttpServer.create()
                .route(routes -> routes
                        .get("/hello",
                                (request, response) -> response.sendString(Mono.just("Hello World!")))
                        .post("/echo",
                                (request, response) -> response.send(request.receive().retain()))
                        .get("/path/{param}",
                                (request, response) -> response.sendString(Mono.just(request.param("param"))))
                        .get("/cal", (request, response) -> {
                            // 和传统SpringBoot项目对比
                            // 内存配置 -Xmx500m hey -c 1200 -n 120000 http://127.0.0.1:32990/cal
                            // reactor-netty: 耗时 80s 90%RT为1s
                            // springboot-tomcat: 耗时 160s 90%RT为2.3s
                            long start = System.nanoTime();
                            for (int i = 0; i < 1000; i++) {
                                Supplier<Integer> random = () -> new SecureRandom().nextInt(10000000) + 100;
                                Math.tanh(Math.log(Math.sqrt(random.get()) + random.get()));
                            }
                            long end = System.nanoTime();
                            return response.sendString(Mono.just((end - start) + ""));
                        })
                        .ws("/ws",
                                (wsInbound, wsOutbound) -> wsOutbound.send(wsInbound.receive().retain()))
                        .get("/sse", serveSse())
                )
                .port(32990)
                .bindNow();

        server.onDispose().block();
    }

    /**
     * Prepares SSE response
     * The "Content-Type" is "text/event-stream"
     * The flushing strategy is "flush after every element" emitted by the provided Publisher
     */
    private static BiFunction<HttpServerRequest, HttpServerResponse, Publisher<Void>> serveSse() {
        Flux<Long> flux = Flux.interval(Duration.ofSeconds(3));
        return (request, response) ->
                response.sse()
                        .send(flux.map(FirstHttpServer::toByteBuf), b -> true);
    }

    /**
     * Transforms the Object to ByteBuf following the expected SSE format.
     */
    private static ByteBuf toByteBuf(Object any) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            out.write("data: ".getBytes(Charset.defaultCharset()));
            MAPPER.writeValue(out, any);
            out.write("\n\n".getBytes(Charset.defaultCharset()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ByteBufAllocator.DEFAULT
                .buffer()
                .writeBytes(out.toByteArray());
    }

    private static final ObjectMapper MAPPER = new ObjectMapper();
}
