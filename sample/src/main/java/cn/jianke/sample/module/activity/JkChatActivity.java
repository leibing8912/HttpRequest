package cn.jianke.sample.module.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import cn.jianke.httprequest.utils.StringUtil;
import cn.jianke.sample.R;
import cn.jianke.sample.httprequest.okhttp.websocket.JkWsManagerImpl;
import cn.jianke.sample.httprequest.okhttp.websocket.JkWsStatusListener;
import cn.jianke.sample.httprequest.okhttp.websocket.bean.JkChatMsgBean;
import cn.jianke.sample.httprequest.okhttp.websocket.bean.JkChatMsg;
import cn.jianke.sample.httprequest.okhttp.websocket.util.JsonUtil;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okio.ByteString;

/**
 * @className: JkChatActivity
 * @classDescription: 聊天页面
 * @author: leibing
 * @createTime: 2017/5/6
 */
public class JkChatActivity extends BaseActivity implements View.OnClickListener{
    // 日志标识
    private final static String TAG = "JkRequest@JkChatActivity";
    // mock web server
    private static final MockWebServer mockWebServer = new MockWebServer();
    // 消息显示
    private TextView msgTv;
    // 消息输入
    private EditText msgEdt;
    // websocket链接地址
    private String wsUrl = "ws://tw.sgz88.com:2019/area=kenny&staff=&psid=&utype=1&page=1&user=fc2f0224-a0b8-4d27-ad5c-934a578a0591&website=tw&number=3&eng=0&ftype=android&act=1&refurl=m.jianke.com";
    // 消息内容
    private String msgContent = "";
    // jk websocket manager implement
    private JkWsManagerImpl mJkWsManagerImpl;
    // ui thread handler
    private Handler uiHandler = new Handler(Looper.getMainLooper());
    // jk chat msg model
    private JkChatMsgBean mJkChatMsgBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jk_chat);
        // findView
        msgTv = (TextView) findViewById(R.id.tv_msg);
        msgEdt = (EditText) findViewById(R.id.edt_msg);
        // onClick
        findViewById(R.id.btn_send).setOnClickListener(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                // init mock server
//                initMockServer();
                // init wsUrl
//                wsUrl = "ws://121.40.165.18:8088";
//                wsUrl = "ws://" + mockWebServer.getHostName() + ":" + mockWebServer.getPort() + "/";
//                wsUrl = "ws://172.17.30.48:2019";
//                wsUrl = "ws://172.17.30.76:2019/area=kenny&staff=&psid=&utype=1&page=1&user=fc2f0224-a0b8-4d27-ad5c-934a578a0591&website=tw&number=3&eng=0&ftype=android&act=1&refurl=m.jianke.com";
//                wsUrl = "ws://172.17.30.76:2019/area=%E5%B9%BF%E4%B8%9C%E7%9C%81%E5%B9%BF%E5%B7%9E%E5%B8%82%E8%AE%BF%E5%AE%A2&staff=69c9dab3-4628-46db-b073-c5c0d03b743b&psid=69c9dab3-4628-46db-b073-c5c0d03b743b&utype=1&page=1&user=a8fb7d7b-bc6c-2a08-d3b3-17033778bd41&website=tw&number=2&eng=0&rtype=1&ftype=2&act=1&refurl=tmswt.jianke.com/testgochat3.html";
                // init jk websocket manager
                initJkWsManager();
            }
        }).start();
    }

    /**
     * init jk websocket manager
     * @author leibing
     * @createTime 2017/5/8
     * @lastModify 2017/5/8
     * @param
     * @return
     */
    private void initJkWsManager(){
        Log.e(TAG, "#wsUrl=" + wsUrl);
        // new JkWsManagerImpl instance
        mJkWsManagerImpl = new JkWsManagerImpl.Builder(this)
                .setWsUrl(wsUrl)
                .build();
        // start connect websocket
        mJkWsManagerImpl.startConnect();
        // set status listener
        mJkWsManagerImpl.setJkWsStatusListener(new JkWsStatusListener() {
            @Override
            public void onOpen(Response response) {

            }

            @Override
            public void onMessage(String text) {
                // 接收消息
                updateReceiveMsgUi(text);
            }

            @Override
            public void onMessage(ByteString bytes) {

            }

            @Override
            public void onReconnect() {

            }

            @Override
            public void onClosing(int code, String reason) {

            }

            @Override
            public void onClosed(int code, String reason) {

            }

            @Override
            public void onFailure(Throwable t, Response response) {

            }
        });
    }

    /**
      * 更新接收数据ui
      * @author leibing
      * @createTime 2017/5/15
      * @lastModify 2017/5/15
      * @param
      * @return
      */
    private void updateReceiveMsgUi(String text) {
        if (StringUtil.isNotEmpty(text)) {
            // 转json obj
            mJkChatMsgBean = JsonUtil.fromJson(text, JkChatMsgBean.class);
            if (StringUtil.isNotEmpty(mJkChatMsgBean.Msg)) {
                String receiveMsg = JkChatMsg.receiveMsg(mJkChatMsgBean.Msg);
                if (StringUtil.isNotEmpty(receiveMsg)) {
                    msgContent = msgContent + "服务器：" + receiveMsg + "\n";
                    // 更新ui
                    updateUi(msgContent);
                }
            }
        }
    }

    /**
     * 更新ui
     * @author leibing
     * @createTime 2017/5/8
     * @lastModify 2017/5/8
     * @param msgContent 消息内容
     * @return
     */
    private void updateUi(final String msgContent){
        if (StringUtil.isNotEmpty(msgContent)
                && msgTv != null
                && uiHandler != null){
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    msgTv.setText(msgContent);
                }
            });
        }
    }

    /**
     * init mock server
     * @author leibing
     * @createTime 2017/5/6
     * @lastModify 2017/5/6
     * @param
     * @return
     */
    private void initMockServer() {
        if (mockWebServer == null)
            return;
        mockWebServer.enqueue(new MockResponse().withWebSocketUpgrade(new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
            }

            @Override
            public void onMessage(WebSocket webSocket, String string) {
                webSocket.send(string);
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {

            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {

            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {

            }
        }));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_send:
                if (mJkWsManagerImpl != null){
                    msgContent = msgContent + "客户端：" + msgEdt.getText().toString() + "\n";
                    // 更新ui
                    updateUi(msgContent);
                    // 发送文本消息
//                    sendTxtMsg(msgEdt.getText().toString());
                    // 发送链接消息
//                    sendLinkMsg("http://tw.sgz88.com");
                    // 发送图片消息
                    sendImgMsg("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494842480987&di=211f7c40d8e336692bcd393dc38e5174&imgtype=0&src=http%3A%2F%2Fimg2015.zdface.com%2F20160711%2F28af37919541e977a4434f6e10719a0f.jpg");
                    // 清空消息
                    msgEdt.setText("");
                }
                break;
            default:
                break;
        }
    }

    /**
      * 发送文本消息
      * @author leibing
      * @createTime 2017/5/15
      * @lastModify 2017/5/15
      * @param txt 文本
      * @return
      */
    private void sendTxtMsg(String txt){
        String sendMsg = JkChatMsg.sendTxtMsg(mJkChatMsgBean,
                txt);
        System.out.println("ddddddddddddddd sendMsg = " + sendMsg);
        if (StringUtil.isNotEmpty(sendMsg))
            mJkWsManagerImpl.sendMessage(sendMsg);
    }

    /**
      * 发送链接消息
      * @author leibing
      * @createTime 2017/5/15
      * @lastModify 2017/5/15
      * @param link 链接
      * @return
      */
    private void sendLinkMsg(String link){
        String sendMsg = JkChatMsg.sendLinkMsg(mJkChatMsgBean,link);
        System.out.println("ddddddddddddddd sendMsg = " + sendMsg);
        if (StringUtil.isNotEmpty(sendMsg))
            mJkWsManagerImpl.sendMessage(sendMsg);
    }

    /**
     * 发送链接消息
     * @author leibing
     * @createTime 2017/5/15
     * @lastModify 2017/5/15
     * @param imgUrl 图片链接
     * @return
     */
    private void sendImgMsg(String imgUrl){
        String sendMsg = JkChatMsg.sendImgMsg(mJkChatMsgBean,imgUrl);
        System.out.println("ddddddddddddddd sendMsg = " + sendMsg);
        if (StringUtil.isNotEmpty(sendMsg))
            mJkWsManagerImpl.sendMessage(sendMsg);
    }

    @Override
    protected void onDestroy() {
        if (mJkWsManagerImpl != null) {
            mJkWsManagerImpl.stopConnect();
        }
        super.onDestroy();
    }
}
