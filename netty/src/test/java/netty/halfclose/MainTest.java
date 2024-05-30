package netty.halfclose;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author Kuangcp
 * 2024-05-30 16:20
 */
@Slf4j
public class MainTest {

    private final Client timeClient = new Client();

    @Test
    public void testServer() throws Exception {
        Server server = new Server();
        server.start();
    }

    @Test
    public void testClient() throws Exception {
        startClient();
    }


    void startClient() {
        if (timeClient.isReady()) {
            return;
        }

        Thread thread = new Thread(() -> {
            try {
                timeClient.connectLocal(Server.port);
            } catch (Exception e) {
                log.error("", e);
            }
        });

        thread.start();

        // 当循环体为空时会陷入死循环，即使已经准备好也没有退出
        while (!timeClient.isReady()) {
        }
        log.info("client ready");
    }
}
