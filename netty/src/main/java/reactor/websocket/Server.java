package reactor.websocket;

import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;

import java.io.IOException;

/**
 *
 * @author Kuangcp 
 * 2024-02-28 20:27
 */
public class Server {
    public static void main(String[] args) throws IOException {
        DisposableServer server = HttpServer.create()
                .route(routes -> routes.get("/hello", (request, response) -> response.sendString(Mono.just("Hello World!")))
                        .post("/echo", (request, response) -> response.send(request.receive().retain()))
                        .get("/path/{param}", (request, response) -> response.sendString(Mono.just(request.param("param"))))
                        .ws("/ws", (wsInbound, wsOutbound) -> {
                                    return wsOutbound.send(wsInbound.receive().retain());
                                }
                        ))
                .port(40032)
                .bindNow();

        server.onDispose().block();
    }
}
