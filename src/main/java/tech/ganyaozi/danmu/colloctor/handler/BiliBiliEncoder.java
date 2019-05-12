package tech.ganyaozi.danmu.colloctor.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.ganyaozi.danmu.colloctor.bean.BiliBiliMessage;

/**
 * @author Derek
 */
public class BiliBiliEncoder extends MessageToByteEncoder<BiliBiliMessage> {

    private static final Logger loggerException = LoggerFactory.getLogger(BiliBiliEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, BiliBiliMessage msg, ByteBuf out) {

        int dataLength = msg.getContent() == null ? 0 : msg.getContent().length;
        //   int short short int int bytes[]
        int totalLength = dataLength + BiliBiliMessage.PROTOCOL_HEAD_LENGTH;

        out.ensureWritable(totalLength);
        out.writeInt(totalLength);
        out.writeShort(msg.getProtocolHeadLength());
        out.writeShort((short) msg.getShortDeviceType().getValue());
        out.writeInt(msg.getAction().getValue());
        out.writeInt(msg.getLongDeviceType().getValue());
        if (dataLength > 0) {
            out.writeBytes(msg.getContent());
        }
        loggerException.debug("send msg {} ", msg);
    }
}
