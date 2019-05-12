package tech.ganyaozi.danmu.colloctor.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.ganyaozi.danmu.colloctor.bean.BiliBiliMessage;

import java.security.SecureRandom;


/**
 * @author Derek
 */
public class BiliBiliJoinRoomHandler extends SimpleChannelInboundHandler<BiliBiliMessage> {

    private static final Logger loggerBusiness = LoggerFactory.getLogger("business");

    private final SecureRandom random = new SecureRandom();

    private final Integer roomId;

    public BiliBiliJoinRoomHandler(Integer roomId) {
        this.roomId = roomId;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        //链接建立时，发送进入房间指令
        loggerBusiness.info("[系统] 正在进入直播间 {}...", roomId);
        ctx.writeAndFlush(buildJoinRoomMessage(roomId));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BiliBiliMessage msg) {
        //不处理任何信息，往后抛
        ctx.fireChannelRead(msg);
    }

    /**
     * 发送进入房间指令
     * @param roomID 房间id
     * @return message
     */
    private BiliBiliMessage buildJoinRoomMessage(Integer roomID) {
        long uid = 1000000000 + (long) (2000000000 * random.nextDouble());
        String jsonBody = "{\"roomid\": " + roomID + ", \"uid\": " + uid + "}";
        return new BiliBiliMessage(BiliBiliMessage.Action.ENTER_ROOM,jsonBody.getBytes());
    }
}
