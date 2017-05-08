package cn.jianke.sample.module.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import cn.jianke.httprequest.utils.StringUtil;
import cn.jianke.sample.R;
import cn.jianke.sample.httprequest.okhttp.websocket.JkWsManagerImpl;
import cn.jianke.sample.httprequest.okhttp.websocket.JkWsStatusListener;
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
    // websocket链接地址
    private String wsUrl = "ws://tw.sgz88.com:2019/area=上海&staff=&psid=&utype=1&page=1&user=fc2f0224-a0b8-4d27-ad5c-934a578a0591&website=tw&number=3&eng=0&ftype=ios&act=1&refurl=m.jianke.com";
    // 消息内容
    private String msgContent = "";
    // jk websocket manager implement
    private JkWsManagerImpl mJkWsManagerImpl;
    // ui thread handler
    private Handler uiHandler = new Handler(Looper.getMainLooper());

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
                if (StringUtil.isNotEmpty(text)){
                    msgContent = msgContent + "服务器：" + text + "\n";
                    // 更新ui
                    updateUi(msgContent);
                }
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
                    // 发送消息
                    mJkWsManagerImpl.sendMessage(msgEdt.getText().toString());
                }
                break;
            default:
                break;
        }
    }
}
