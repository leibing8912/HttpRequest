package cn.jianke.sample.module.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import cn.jianke.httprequest.utils.StringUtil;
import cn.jianke.sample.R;
import cn.jianke.sample.httprequest.okhttp.JkOkHttpWebSocketUtils;
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
    // mock web server
    private static final MockWebServer mockWebServer = new MockWebServer();
    // 消息显示
    private TextView msgTv;
    // 消息输入
    private EditText msgEdt;
    // webSocket
    private WebSocket mWebSocket = null;
    // websocket链接地址
    private String wsUrl = "";
    // 消息内容
    private String msgContent = "";

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
                initMockServer();
                // init wsUrl
                wsUrl = "ws://" + mockWebServer.getHostName() + ":" + mockWebServer.getPort() + "/";
                // init websocket client
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initWebSocket();
                    }
                });
            }
        }).start();
    }

    /**
     * init websocket
     * @author leibing
     * @createTime 2017/5/6
     * @lastModify 2017/5/6
     * @param
     * @return
     */
    private void initWebSocket() {
        System.out.println("ddddddddddd wsUrl = " + wsUrl);
        JkOkHttpWebSocketUtils.getInstance().initWsClient(wsUrl, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                mWebSocket = webSocket;
                System.out.println("ddddddddddddd client onOpen");
                System.out.println("ddddddddddddd response:" + response);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                System.out.println("ddddddddddddd client onFailure");
                System.out.println("ddddddddddddd response:" + response);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                System.out.println("ddddddddddddd client onClosed");
                System.out.println("ddddddddddddd code:" + code + " reason:" + reason);
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                System.out.println("ddddddddddddd client onClosing");
                System.out.println("ddddddddddddd code:" + code + " reason:" + reason);
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                System.out.println("ddddddddddddd client onMessage");
                System.out.println("ddddddddddddd bytes:" + bytes.toString());
                msgContent = msgContent + "服务端：" + bytes.toString() + "\n";
                if (msgTv != null)
                    msgTv.setText(msgContent);
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                System.out.println("ddddddddddddd client onMessage");
                System.out.println("ddddddddddddd text:" + text);
                msgContent = msgContent + "服务端：" + text + "\n";
                if (msgTv != null)
                    msgTv.setText(msgContent);
            }
        });
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
                System.out.println("ddddddddddddd server onOpen");
                System.out.println("ddddddddddddd response:" + response);
            }

            @Override
            public void onMessage(WebSocket webSocket, String string) {
                System.out.println("ddddddddddddd server onMessage");
                System.out.println("ddddddddddddd string" + string);
                webSocket.send("response-" + string);
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                System.out.println("ddddddddddddd server onClosing");
                System.out.println("ddddddddddddd code:" + code + " reason:" + reason);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                System.out.println("ddddddddddddd server onClosed");
                System.out.println("ddddddddddddd code:" + code + " reason:" + reason);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                System.out.println("ddddddddddddd server onFailure");
                System.out.println("ddddddddddddd response:" + response);
            }
        }));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_send:
                sendMsg();
                break;
            default:
                break;
        }
    }

    /**
     * 发送数据
     * @author leibing
     * @createTime 2017/5/6
     * @lastModify 2017/5/6
     * @param
     * @return
     */
    private void sendMsg() {
        if (msgEdt != null
                && StringUtil.isNotEmpty(msgEdt.getText().toString())
                && mWebSocket != null){
            System.out.println("ddddddddddddd send msg");
            mWebSocket.send(msgEdt.getText().toString());
            msgContent = msgContent + "客户端：" + msgEdt.getText().toString() + "\n";
            if (msgTv != null)
                msgTv.setText(msgContent);
            msgEdt.setText("");
        }
    }
}
