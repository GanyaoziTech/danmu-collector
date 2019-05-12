package tech.ganyaozi.danmu.colloctor;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import tech.ganyaozi.danmu.colloctor.handler.*;

import java.util.concurrent.TimeUnit;

/**
 * @author Derek
 */
public class BiliBiliChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final Integer roomId;

    public BiliBiliChannelInitializer(Integer roomId) {
        this.roomId = roomId;
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        ch.pipeline()
                .addLast("decoder", new BiliBiliDecoder())
                .addLast("encoder", new BiliBiliEncoder())
                .addLast("idleState", new IdleStateHandler(0, 15, 0, TimeUnit.SECONDS))
                .addLast("heartbeat", new BiliBiliHeartBeatHandler())
                .addLast("joinRoomHandler", new BiliBiliJoinRoomHandler(roomId))
                .addLast("messageHandler", new BiliBiliMessageHandler());
    }
}
