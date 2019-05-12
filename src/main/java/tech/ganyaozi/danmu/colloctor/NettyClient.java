package tech.ganyaozi.danmu.colloctor;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Derek
 */
public class NettyClient implements Runnable {

    private static final Logger loggerException = LoggerFactory.getLogger(NettyClient.class);

    private final String host;
    private final int port;
    private final Integer roomId;


    public NettyClient(String host, int port, Integer roomId) {
        this.host = host;
        this.port = port;
        this.roomId = roomId;
    }

    /**
     * 初始化B站弹幕链接
     */
    @Override
    public void run() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootStrap = new Bootstrap();
            bootStrap
                    .group(group)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .channel(NioSocketChannel.class)
                    .handler(new BiliBiliChannelInitializer(roomId));

            ChannelFuture channelFuture = bootStrap.connect(host, port).sync();
            channelFuture.channel().closeFuture().sync();

        } catch (Exception e) {
            loggerException.error("", e);
        } finally {
            try {
                group.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                loggerException.error("", e);
            }
        }
    }
}
