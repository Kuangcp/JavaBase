package reactor.proxy;


import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.NettyOutbound;
import reactor.netty.http.client.HttpClient;
import reactor.netty.http.server.HttpServer;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

/**
 * https://projectreactor.io/docs/netty/release/reference/index.html
 *
 * @author <a href="https://github.com/kuangcp">Github</a>
 * 2023-10-10 09:52
 */
@Slf4j
public class ProxyServer {

    private static final HttpClient client = HttpClient.create();

    public static void main(String[] args) {
        DisposableServer server = HttpServer.create()
                .handle(ProxyServer::handle)
                .port(32990)
                .bindNow();

        server.onDispose().block();
    }

    private static NettyOutbound handle(HttpServerRequest r, HttpServerResponse w) {
//        Mono<HttpClientResponse> response = client.request(r.method()).uri(r.uri()).send(r.receive())
//                .response();
//        response.subscribe(v -> {
//            log.info("v={}", v);
//        });

        Mono<ByteBuf> body = client.request(r.method()).uri(r.uri()).send(r.receive())
                .responseSingle((resp, bytes) -> {
                    w.status(resp.status());
                    w.headers(resp.responseHeaders());
                    return bytes;
                });
        return w.send(body);

//                    Mono<String> body = client.request(r.method()).uri(r.uri()).send(r.receive()).response()
//                            .handle((a, b) -> {
//                                HttpHeaders header = a.responseHeaders();
//                                header.add("proxy", "xxx");
//                                w.status(a.status()).headers(header);
//                                log.info("={} {}", a.toString(), a.getClass());
//                                w.headers(header);
//
//                                b.next("xtt");
//                                b.complete();
//                            });
//                    return w.sendString(body);


        // Bug
//                    Mono<Resp> respMono = client.request(r.method()).uri(r.uri()).send(r.receive())
//                            .responseSingle((resp, bytes) -> {
//                                return Mono.just(new Resp(resp.status(), resp.responseHeaders(), bytes));
//                            });
//                    respMono.subscribe(resp -> {
//                        w.status(resp.getStatus()).headers(resp.getResponseHeaders()).send(resp.getDataStream());
//                    });
//                    return respMono.then();
    }
}
