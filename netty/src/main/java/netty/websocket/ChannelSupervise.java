package netty.websocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author https://github.com/kuangcp on 2021-05-18 08:34
 */
@Slf4j
public class ChannelSupervise {

    private static final ChannelGroup GLOBAL_GROUP = new DefaultChannelGroup(
            GlobalEventExecutor.INSTANCE);
    private static final ConcurrentMap<String, ChannelId> CHANNEL_MAP = new ConcurrentHashMap<>();

    public static void addChannel(Channel channel) {
        GLOBAL_GROUP.add(channel);
        CHANNEL_MAP.put(channel.id().asShortText(), channel.id());
        printState();
    }

    public static void removeChannel(Channel channel) {
        GLOBAL_GROUP.remove(channel);
        CHANNEL_MAP.remove(channel.id().asShortText());
        printState();
    }

    public static Channel findChannel(String id) {
        return GLOBAL_GROUP.find(CHANNEL_MAP.get(id));
    }

    public static void send2All(TextWebSocketFrame tws) {
        GLOBAL_GROUP.writeAndFlush(tws);
    }

    private static void printState() {
        log.debug("channelSize={} global={}", CHANNEL_MAP.size(), GLOBAL_GROUP.size());
    }

    public static void watchState() {
        log.info("online {}", CHANNEL_MAP.size());
    }
}
