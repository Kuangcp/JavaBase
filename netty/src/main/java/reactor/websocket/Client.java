package reactor.websocket;

import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import reactor.core.publisher.Flux;
import reactor.netty.http.client.HttpClient;

/**
 *
 * @author Kuangcp
 * 2024-02-28 20:29
 */
public class Client {
    public static void main(String[] args) {
        HttpClient client = HttpClient.create();

        client.websocket()
                .uri("ws://localhost:40032/ws")
                .handle((inbound, outbound) -> {
                    inbound.receive()
                            .asString()
                            .take(1)
                            .subscribe(System.out::println);

                    final byte[] msgBytes = "hello".getBytes(CharsetUtil.ISO_8859_1);
                    return outbound.send(Flux.just(Unpooled.wrappedBuffer(msgBytes), Unpooled.wrappedBuffer(msgBytes)))
                            .neverComplete();
                })
                .blockLast();
    }
}
