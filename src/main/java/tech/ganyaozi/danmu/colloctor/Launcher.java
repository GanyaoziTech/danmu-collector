package tech.ganyaozi.danmu.colloctor;

import com.alibaba.fastjson.JSONObject;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import tech.ganyaozi.danmu.colloctor.utils.ConsoleTool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 弹幕收集器，定向收集B站直播间的弹幕内容
 *
 * @author Derek.p.dai@qq.com
 */
public class Launcher {

    @SuppressWarnings("SpellCheckingInspection")
    private static final String BILIBILI_DEFAULT_COMMENT_HOST = "livecmt-1.bilibili.com";

    private static final int BILIBILI_DEFAULT_COMMENT_PORT = 788;

    private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(1, 10, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

    public static void main(String[] args) {
        ConsoleTool.displayOneLine("请输入需要B站的直播间的房间号: ");
        Integer roomId = ConsoleTool.readIntUntilNotNull();

        NettyClient client = new NettyClient(BILIBILI_DEFAULT_COMMENT_HOST, BILIBILI_DEFAULT_COMMENT_PORT, getRealRoomID(roomId));

        THREAD_POOL_EXECUTOR.submit(client);
    }

    private static Integer getRealRoomID(Integer roomID) {
        String url = "https://api.live.bilibili.com/room/v1/Room/room_init?id=" + roomID;

        OkHttpClient client = new OkHttpClient.Builder().build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            assert response.body() !=null;
            String responseBody = response.body().string();
            roomID = JSONObject.parseObject(responseBody).getJSONObject("data").getInteger("room_id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return roomID;
    }
}
