package tech.ganyaozi.danmu.colloctor.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.ganyaozi.danmu.colloctor.bean.BiliBiliMessage;

import java.nio.charset.StandardCharsets;

/**
 * @author Derek
 */
public class BiliBiliMessageHandler extends SimpleChannelInboundHandler<BiliBiliMessage> {

    private static final Logger loggerException = LoggerFactory.getLogger(BiliBiliMessageHandler.class);
    private static final Logger loggerBusiness = LoggerFactory.getLogger("business");


    @SuppressWarnings("SpellCheckingInspection")
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BiliBiliMessage msg) {
        if (msg == null) {
            loggerException.error("receive message , but is null ");
            return;
        }

        byte[] msgBody = msg.getContent();
        String jsonStr = new String(msgBody, StandardCharsets.UTF_8);

        switch (msg.getAction()) {
            case VIEWER_COUNT:
                loggerBusiness.info("[人数] {}", jsonStr);
                break;
            case ENTER_ROOM_SUCCESS:
                loggerBusiness.info("[系统] 进入房间成功 ");
                break;
            case MESSAGE:
                JSONObject object = JSON.parseObject(jsonStr);
                String msgType = object.getString("cmd");
                switch (msgType) {
                    case "DANMU_MSG": {
                        JSONArray array = object.getJSONArray("info").getJSONArray(2);
                        String uname = array.getString(1);
                        String danmuku = object.getJSONArray("info").getString(1);
                        loggerBusiness.info("[弹幕] {}:{}", uname, danmuku);
                        break;
                    }
                    case "SEND_GIFT": {
                        JSONObject giftData = object.getJSONObject("data");
                        String giftName = giftData.getString("giftName");
                        int giftNum = giftData.getInteger("num");
                        String uname = giftData.getString("uname");
                        loggerBusiness.info("[礼物] {} 赠送 {} * {} ", uname, giftName, giftNum);
                        break;
                    }
                    case "WELCOME": {
                        JSONObject welcData = object.getJSONObject("data");
                        String uname = welcData.getString("uname");
                        loggerBusiness.info("[欢迎] 欢迎老爷 {} 进入直播间", uname);
                        break;
                    }
                    default:
                        break;
                }
                break;
            default:
                loggerException.info("[忽略] {} ", new String(msgBody));
        }


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        loggerException.error("", cause);
    }

}
