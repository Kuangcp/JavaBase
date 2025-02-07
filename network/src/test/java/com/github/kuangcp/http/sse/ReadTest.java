package com.github.kuangcp.http.sse;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Kuangcp
 * 2025-02-07 10:21
 */
@Slf4j
public class ReadTest {
    private MockWebServer server;
    private OkHttpClient client;

    ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() {
        server = new MockWebServer();
        try {
            server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 设置 OkHttpClient 超时参数
        client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS) // 连接超时5秒
                .writeTimeout(5, TimeUnit.SECONDS)   // 写入超时5秒
                .readTimeout(5, TimeUnit.SECONDS)    // 读取超时5秒
                .build();
    }

    @After
    public void tearDown() throws IOException {
        server.shutdown();
    }

    @Test
    public void testConnectTimeout() throws IOException {
        // 模拟服务器延迟响应
        server.enqueue(new MockResponse().setBodyDelay(6, TimeUnit.SECONDS));

        Request request = new Request.Builder()
                .url(server.url("/"))
                .build();

        try {
            client.newCall(request).execute();
            fail("Expected a SocketTimeoutException to be thrown for connect timeout");
        } catch (SocketTimeoutException e) {
            // 连接超时，测试通过
            assertTrue(true);
        }
    }

    @Test
    public void testWriteTimeout() throws IOException {
        // 这个测试依赖于服务器的配置，确保服务器不会立即发送响应头
        server.enqueue(new MockResponse().setBodyDelay(6, TimeUnit.SECONDS));

        Request request = new Request.Builder()
                .url(server.url("/"))
                .post(RequestBody.create(null, "This is the request body"))
                .build();

        try {
            client.newCall(request).execute();
            fail("Expected a SocketTimeoutException to be thrown for write timeout");
        } catch (SocketTimeoutException e) {
            // 写入超时，测试通过
            assertTrue(true);
        }
    }

    @Test
    public void testReadTimeout() throws IOException {
        // 模拟服务器立即发送响应头，但延迟发送响应体
        server.enqueue(new MockResponse()
                .setBody("Response body")
//                        .setHeadersDelay(10,TimeUnit.SECONDS)
                .setBodyDelay(10, TimeUnit.SECONDS));

        Request request = new Request.Builder()
                .url(server.url("/"))
                .build();
        try {
            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
//            System.out.println(responseBody);
            // 读取超时，测试通过
            fail("Expected a SocketTimeoutException to be thrown for read timeout");
        } catch (SocketTimeoutException e) {
            // 写入超时，测试通过
            assertTrue(true);
        }
    }

    @Test
    public void testReadTimeoutWithEventSource() throws IOException, InterruptedException {
        // 模拟服务器立即发送响应头，但延迟发送响应体
        server.enqueue(new MockResponse()
                .setBody(": keep-alive") // SSE需要发送特定的响应头
                .addHeader("Content-Type", "text/event-stream") // 设置正确的MIME类型
                .setBodyDelay(5, TimeUnit.SECONDS)
        ); // 延迟发送事件

        Request request = new Request.Builder()
                .url(server.url("/"))
                .build();

        EventSource.Factory factory = EventSources.createFactory(client);

        EventSourceListener eventSourceListener = new EventSourceListener() {
            @Override
            public void onClosed(@NotNull EventSource eventSource) {
                log.info("closed {}", eventSource);
                super.onClosed(eventSource);
            }

            @Override
            public void onEvent(@NotNull EventSource eventSource, @Nullable String id, @Nullable String type, @NotNull String data) {
                log.info("event");
                super.onEvent(eventSource, id, type, data);
            }

            @Override
            public void onFailure(@NotNull EventSource eventSource, @Nullable Throwable t, @Nullable Response response) {
                assert t != null;
                log.error("返回异常信息：{}", t.getMessage());

                try {
                    log.error("response：{}", mapper.writeValueAsString(response));
                } catch (Exception e) {
                    log.error("", e);
                }
//                super.onFailure(eventSource, t, response);
            }

            @Override
            public void onOpen(@NotNull EventSource eventSource, @NotNull Response response) {
                log.info("open {}", response);
                super.onOpen(eventSource, response);
            }
        };

        log.info("request");
        final EventSource eventSource = factory.newEventSource(request, eventSourceListener);

        // 等待足够的时间以触发超时
        Thread.sleep(15000); // 等待时间应大于服务器延迟时间

        // 测试结束，关闭EventSource
    }
}
