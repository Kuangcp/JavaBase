package reactor.proxy.domain;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.reactivestreams.Publisher;

/**
 *
 * @author Kuangcp 
 * 2024-03-05 12:03
 */
public class Resp {

    HttpResponseStatus status;
    HttpHeaders responseHeaders;
    Publisher<? extends ByteBuf> dataStream;

    public Resp(HttpResponseStatus status, HttpHeaders responseHeaders, Publisher<? extends ByteBuf> dataStream) {
        this.status = status;
        this.responseHeaders = responseHeaders;
        this.dataStream = dataStream;
    }

    public HttpResponseStatus getStatus() {
        return status;
    }

    public HttpHeaders getResponseHeaders() {
        return responseHeaders;
    }

    public Publisher<? extends ByteBuf> getDataStream() {
        return dataStream;
    }
}
