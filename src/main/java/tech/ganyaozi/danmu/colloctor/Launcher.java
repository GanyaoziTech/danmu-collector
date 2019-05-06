package tech.ganyaozi.danmu.colloctor;

import com.alibaba.fastjson.JSONObject;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import tech.ganyaozi.danmu.colloctor.utils.ConsoleTool;

/**
 * 弹幕收集器，定向收集暴雪游戏频道在各平台的弹幕内容
 **/
public class Launcher {

    private static final String BILIBILI_DEFAULT_COMMENT_HOST = "livecmt-1.bilibili.com";

    private static final int BILIBILI_DEFAULT_COMMENT_PORT = 788;

    public static void main(String[] args) throws InterruptedException {

        NettyClient client = new NettyClient(BILIBILI_DEFAULT_COMMENT_HOST, BILIBILI_DEFAULT_COMMENT_PORT);


        ConsoleTool.displayOneLine("请输入需要B站的直播间的房间号: ");
        Integer roomId = ConsoleTool.readInt();

        client.start(getRealRoomID(roomId));
    }


    private static String getRealRoomID(Integer roomID) {

        String url = "https://api.live.bilibili.com/room/v1/Room/room_init?id=" + roomID;

        OkHttpClient client = new OkHttpClient.Builder().build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            String responseBody = response.body().string();
            roomID = JSONObject.parseObject(responseBody).getJSONObject("data").getInteger("room_id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return roomID + "";
    }
}
