package tech.ganyaozi.danmu.colloctor.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import tech.ganyaozi.danmu.colloctor.bean.BiliBiliMessage;

import java.nio.charset.StandardCharsets;

import static tech.ganyaozi.danmu.colloctor.bean.BiliBiliMessage.Action.VIEWER_COUNT;

/**
 * B站数据解码
 **/
public class BiliBiliDecoder extends LengthFieldBasedFrameDecoder {

    public BiliBiliDecoder() {
        super(Integer.MAX_VALUE, 0, 4, -4, 4);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = (ByteBuf) super.decode(ctx, in);

        short headLength;
        short version;
        int actionInt;
        int extraParam;
        String content;

        if (frame == null) {
            return null;
        } else {
            headLength = frame.readShort();
            version = frame.readShort();
            actionInt = frame.readInt();
            extraParam = frame.readInt();

            if (actionInt == VIEWER_COUNT.getValue()) {
                content = frame.readInt() + "";
            } else {
                byte[] data = new byte[frame.readableBytes()];
                frame.readBytes(data);
                content = new String(data, StandardCharsets.UTF_8);
            }
        }

        try {
            BiliBiliMessage.DeviceType shortDeviceType = BiliBiliMessage.DeviceType.valueOf(version);
            BiliBiliMessage.Action action = BiliBiliMessage.Action.valueOf(actionInt);
            BiliBiliMessage.DeviceType longDeviceType = BiliBiliMessage.DeviceType.valueOf(extraParam);
            return new BiliBiliMessage(headLength, shortDeviceType, action, longDeviceType, content.getBytes());
        } finally {
            //Trying to fix OOM of ByteBuf Unreleased.
            frame.release();
        }
    }

}
