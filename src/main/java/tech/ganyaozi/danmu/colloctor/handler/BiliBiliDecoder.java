package tech.ganyaozi.danmu.colloctor.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.ganyaozi.danmu.colloctor.bean.BiliBiliMessage;

import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

import static tech.ganyaozi.danmu.colloctor.bean.BiliBiliMessage.Action.VIEWER_COUNT;

/**
 * B站数据解码
 **/
public class BiliBiliDecoder extends LengthFieldBasedFrameDecoder {


    private static final Logger loggerException = LoggerFactory.getLogger(BiliBiliDecoder.class);

    public BiliBiliDecoder() {
        this(ByteOrder.BIG_ENDIAN, 1000000000, 0, 4, -4, 4, true);
    }

    private BiliBiliDecoder(ByteOrder byteOrder, int maxFrameLength,
                            int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment,
                            int initialBytesToStrip, boolean failFast) {
        super(byteOrder, maxFrameLength, lengthFieldOffset, lengthFieldLength,
                lengthAdjustment, initialBytesToStrip, failFast);

    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = (ByteBuf) super.decode(ctx, in);

        if (frame == null) {
            loggerException.error("unable to decode frame {} , ", in);
            return null;
        }
        try {
            int totalLength = frame.readableBytes();
            short headLength = frame.readShort();
            short version = frame.readShort();
            int actionInt = frame.readInt();
            int extraParam = frame.readInt();

            BiliBiliMessage.DeviceType shortDeviceType = BiliBiliMessage.DeviceType.valueOf(version);
            BiliBiliMessage.Action action = BiliBiliMessage.Action.valueOf(actionInt);
            BiliBiliMessage.DeviceType longDeviceType = BiliBiliMessage.DeviceType.valueOf(extraParam);

            String content;
            if (action == VIEWER_COUNT) {
                content = frame.readInt() + "";
            } else {
                byte[] data = new byte[frame.readableBytes()];
                frame.readBytes(data);
                content = new String(data, StandardCharsets.UTF_8);
            }
            return new BiliBiliMessage(totalLength, headLength, shortDeviceType, action, longDeviceType, content.getBytes());
        } finally {
            //Trying to fix OOM of ByteBuf Unreleased.
            frame.release();
        }
    }

}
