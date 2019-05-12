package tech.ganyaozi.danmu.colloctor.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.ganyaozi.danmu.colloctor.bean.BiliBiliMessage;


/**
 * @author Derek
 */
public class BiliBiliHeartBeatHandler extends SimpleChannelInboundHandler<BiliBiliMessage> {

    private static final Logger loggerBusiness = LoggerFactory.getLogger("business");

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        loggerBusiness.info("[系统] 获取直播间人数...");
        ctx.writeAndFlush(new BiliBiliMessage(BiliBiliMessage.Action.HEART_BEAT, StringUtils.EMPTY.getBytes()));
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            ctx.writeAndFlush(new BiliBiliMessage(BiliBiliMessage.Action.HEART_BEAT, StringUtils.EMPTY.getBytes()));
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BiliBiliMessage msg) {
        ctx.fireChannelRead(msg);
    }


}
