package com.github.kuangcp.tank.ws;

import com.github.kuangcp.tank.mgr.PlayStageMgr;
import com.github.kuangcp.tank.util.HoldingKeyStateMgr;
import com.github.kuangcp.tank.util.Json;
import com.github.kuangcp.tank.ws.msg.MsgPack;
import com.github.kuangcp.websocket.WsServerConfig;
import com.github.kuangcp.websocket.handler.AbstractBizHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2024-07-13 00:40
 */
@Slf4j
@ChannelHandler.Sharable
public class WsBizHandler extends AbstractBizHandler {

    public WsBizHandler(WsServerConfig config) {
        super(config);
    }

    @Override
    public void connectSuccess(Long userId) {
        log.info("CONN userId={}", userId);
    }

    @Override
    protected void textWebSocketFrameHandler(ChannelHandlerContext ctx, TextWebSocketFrame frame) {
        final String text = frame.text();
        final MsgPack pack = Json.fromJson(text, MsgPack.class);

        HoldingKeyStateMgr.instance.handleJoy(pack.getDirect());
        if (BooleanUtils.isTrue(pack.getShot())) {
            PlayStageMgr.instance.hero.shotEnemy();
        }

        ctx.channel().writeAndFlush("OK");
    }
}
