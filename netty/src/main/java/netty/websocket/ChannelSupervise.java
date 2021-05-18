package netty.websocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author https://github.com/kuangcp on 2021-05-18 08:34
 */
public class ChannelSupervise {

  private static final ChannelGroup GlobalGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
  private static final ConcurrentMap<String, ChannelId> ChannelMap = new ConcurrentHashMap<>();

  public static void addChannel(Channel channel) {
    GlobalGroup.add(channel);
    ChannelMap.put(channel.id().asShortText(), channel.id());
  }

  public static void removeChannel(Channel channel) {
    GlobalGroup.remove(channel);
    ChannelMap.remove(channel.id().asShortText());
  }

  public static Channel findChannel(String id) {
    return GlobalGroup.find(ChannelMap.get(id));
  }

  public static void send2All(TextWebSocketFrame tws) {
    GlobalGroup.writeAndFlush(tws);
  }
}
