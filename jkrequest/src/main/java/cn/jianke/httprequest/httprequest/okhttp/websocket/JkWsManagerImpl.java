package cn.jianke.httprequest.httprequest.okhttp.websocket;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import cn.jianke.httprequest.utils.StringUtil;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * @className: JkWsManagerImpl
 * @classDescription: jk websocket manager implement
 * @author: leibing
 * @createTime: 2017/5/8
 */
public class JkWsManagerImpl implements IJkWsManager{
    // 日志标识
    public final static String TAG = "JkRequest@JkWsManagerImpl";
    // 重连间隔时间
    private final static int RECONNECT_INTERVAL_TIME = 10 * 1000;
    // 重连最大间隔时间
    private final static long RECONNECT_MAX_INTERVAL_TIME = 120 * 1000;
    // 当前连接状态(默认为断开连接)
    private int mCurrentStatus = JkWsStatus.ConnectStatus.DISCONNECTED;
    // 是否需要重新连接
    private boolean isNeedReconnect = true;
    // 上下文
    private Context mContext;
    // ws链接
    private String wsUrl;
    // websocket instance
    private WebSocket mWebSocket;
    // jk websocket listener
    private JkWsStatusListener mJkWsStatusListener;
    // 重连次数
    private int reconnectCount = 1;
    // websocket handler(用于重连websocket)
    private Handler wsHandler = new Handler(Looper.getMainLooper());
    // 重连runnable
    private Runnable reconnectRunnable = new Runnable() {
        @Override
        public void run() {
            if (mJkWsStatusListener != null)
                mJkWsStatusListener.onReconnect();
            buildConnect();
        }
    };
    // websocket listener
    private WebSocketListener mWebSocketListener = new WebSocketListener() {
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            // assignin websocket
            mWebSocket = webSocket;
            // set currentStatus as connected
            mCurrentStatus = JkWsStatus.ConnectStatus.CONNECTED;
            // 取消重连websocket
            cancelReconnect();
            // open status callback
            if (mJkWsStatusListener != null) {
                Log.e(TAG, "#onOpen#response=" + response);
                mJkWsStatusListener.onOpen(response);
            }
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            // message text callback
            if (mJkWsStatusListener != null) {
                Log.e(TAG, "#onMessage#text=" + text);
                mJkWsStatusListener.onMessage(text);
            }
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            // message bytes callback
            if (mJkWsStatusListener != null) {
                if (bytes != null) {
                    Log.e(TAG, "#onMessage#bytes=" + bytes.toString());
                }else {
                    Log.e(TAG, "#onMessage#bytes");
                }
                mJkWsStatusListener.onMessage(bytes);
            }
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            // set currentStatus as disConnected
            mCurrentStatus = JkWsStatus.ConnectStatus.DISCONNECTED;
            // closed status callback
            if (mJkWsStatusListener != null) {
                Log.e(TAG, "#onClosed#code=" + code+"#reason="+ reason);
                mJkWsStatusListener.onClosed(code, reason);
            }
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            // closing status callback
            if (mJkWsStatusListener != null) {
                Log.e(TAG, "#onClosing#code=" + code+"#reason="+ reason);
                mJkWsStatusListener.onClosing(code, reason);
            }
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            // set currentStatus as disConnected
            mCurrentStatus = JkWsStatus.ConnectStatus.DISCONNECTED;
            // failure status callback
            if (mJkWsStatusListener != null) {
                Log.e(TAG, "#onFailure#t=" + t+"#response="+ response);
                if (t.toString().contains("Socket closed")){
                    mJkWsStatusListener.onClosed(JkWsStatus.StatusCode.NORMAL_CLOSE,
                            JkWsStatus.StatusTip.NORMAL_CLOSE);
                }else {
                    mJkWsStatusListener.onFailure(t, response);
                }
            }
        }
    };

    /**
     * 关闭websocket
     * @author leibing
     * @createTime 2017/5/11
     * @lastModify 2017/5/11
     * @param code 关闭码
     * @param reason 关闭原因
     * @return
     */
    public boolean closeWebSocket(int code,String reason){
        if (mWebSocket != null){
            return mWebSocket.close(code, reason);
        }
        return false;
    }

    /**
     * 重连websocket
     * @author leibing
     * @createTime 2017/5/8
     * @lastModify 2017/5/8
     * @param
     * @return
     */
    private void tryReconnect() {
        // 若不需重连或无网则返回
        if (!isNeedReconnect || !isNetworkConnected(mContext)) return;
        // set currentStatus as reconnect
        mCurrentStatus = JkWsStatus.ConnectStatus.RECONNECT;
        Log.e(TAG, "#tryReconnect#reconnectCount=" + reconnectCount);
        // 重连延迟
        long delay = reconnectCount * RECONNECT_INTERVAL_TIME;
        Log.e(TAG, "#tryReconnect#delay=" + delay);
        // 开始重连
        if (wsHandler != null
                && reconnectRunnable != null) {
            wsHandler.removeCallbacks(reconnectRunnable);
            reconnectCount++;
            wsHandler.postDelayed(reconnectRunnable,
                    delay > RECONNECT_MAX_INTERVAL_TIME ? RECONNECT_INTERVAL_TIME : delay);
            if (delay > RECONNECT_MAX_INTERVAL_TIME){
                Log.e(TAG, "#tryReconnect#reset reconnectCount");
                reconnectCount = 1;
            }
        }
    }

    /**
     * 取消重连websocket
     * @author leibing
     * @createTime 2017/5/8
     * @lastModify 2017/5/8
     * @param
     * @return
     */
    public void cancelReconnect() {
        if (wsHandler != null
                && reconnectRunnable != null) {
            wsHandler.removeCallbacks(reconnectRunnable);
            reconnectCount = 1;
        }
    }

    /**
     * set jk websocket listener
     * @author leibing
     * @createTime 2017/5/8
     * @lastModify 2017/5/8
     * @param mJkWsStatusListener jk websocket listener
     * @return
     */
    public void setJkWsStatusListener(JkWsStatusListener mJkWsStatusListener) {
        this.mJkWsStatusListener = mJkWsStatusListener;
    }

    /**
     * Constructor
     * @author leibing
     * @createTime 2017/5/8
     * @lastModify 2017/5/8
     * @param builder
     * @return
     */
    public JkWsManagerImpl(Builder builder) {
        mContext = builder.mContext;
        wsUrl = builder.wsUrl;
    }

    /**
     * 检查网络是否连接
     * @author leibing
     * @createTime 2017/5/8
     * @lastModify 2017/5/8
     * @param context
     * @return
     */
    private boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 创建websocket连接
     * @author leibing
     * @createTime 2017/5/8
     * @lastModify 2017/5/8
     * @param
     * @return
     */
    private void buildConnect() {
        // 若当前状态为已连接、正在连接中、无网状态则无需创建websocket连接
        if (mCurrentStatus == JkWsStatus.ConnectStatus.CONNECTED
                | mCurrentStatus == JkWsStatus.ConnectStatus.CONNECTING)
            return;
        if (!isNetworkConnected(mContext)){
            if (mJkWsStatusListener != null){
                mJkWsStatusListener.onNoNetWork();
            }
            return;
        }
        // 设置当前状态为正在连接中
        mCurrentStatus = JkWsStatus.ConnectStatus.CONNECTING;
        // 初始化websocket
        initWebSocket();
    }

    /**
     * 初始化websocket
     * @author leibing
     * @createTime 2017/5/8
     * @lastModify 2017/5/8
     * @param
     * @return
     */
    private void initWebSocket() {
        if (StringUtil.isNotEmpty(wsUrl)
                && mWebSocketListener != null) {
            JkOkHttpWebSocketUtils.getInstance().initWsClient(wsUrl, mWebSocketListener);
        }
    }

    /**
     * 发送消息
     * @author leibing
     * @createTime 2017/5/8
     * @lastModify 2017/5/8
     * @param msg 消息
     * @return
     */
    private boolean sendMsg(Object msg) {
        boolean isSend = false;
        if (mWebSocket != null
                && mCurrentStatus == JkWsStatus.ConnectStatus.CONNECTED) {
            if (msg instanceof String) {
                isSend = mWebSocket.send((String) msg);
            } else if (msg instanceof ByteString) {
                isSend = mWebSocket.send((ByteString) msg);
            }
            // 发送消息失败，尝试重连
            if (!isSend) {
                tryReconnect();
            }
        }
        return isSend;
    }

    /**
     * 断开websocket连接
     * @author leibing
     * @createTime 2017/5/8
     * @lastModify 2017/5/8
     * @param
     * @return
     */
    private void disconnect() {
        // 若当前状态为断开连接则返回
        if (mCurrentStatus == JkWsStatus.ConnectStatus.DISCONNECTED)
            return;
        // 取消重连websocket
        cancelReconnect();
        // 取消所有请求
        JkOkHttpWebSocketUtils.getInstance().cancalAllRequest();
        if (mWebSocket != null) {
            boolean isClosed = closeWebSocket(JkWsStatus.StatusCode.NORMAL_CLOSE,
                    JkWsStatus.StatusTip.NORMAL_CLOSE);
            // 非正常关闭连接
            if (!isClosed) {
                // 回调关闭状态
                if (mJkWsStatusListener != null)
                    mJkWsStatusListener.onClosed(JkWsStatus.StatusCode.ABNORMAL_CLOSE,
                            JkWsStatus.StatusTip.ABNORMAL_CLOSE);
            }
        }
        // set currentStatus as disConnected
        mCurrentStatus = JkWsStatus.ConnectStatus.DISCONNECTED;
    }

    @Override
    public WebSocket getWebSocket() {
        return mWebSocket;
    }

    @Override
    public void startConnect() {
        isNeedReconnect = true;
        // 创建websocket连接
        buildConnect();
    }

    @Override
    public void stopConnect() {
        isNeedReconnect = false;
        // 断开websocket连接
        disconnect();
    }

    @Override
    public boolean isWsConnected() {
        return mCurrentStatus == JkWsStatus.ConnectStatus.CONNECTED;
    }

    @Override
    public int getCurrentStatus() {
        return mCurrentStatus;
    }

    @Override
    public boolean sendMessage(String msg) {
        return sendMsg(msg);
    }

    @Override
    public boolean sendMessage(ByteString byteString) {
        return sendMsg(byteString);
    }

    /**
     * @className: Builder
     * @classDescription: 建造者模式构建
     * @author: leibing
     * @createTime: 2017/5/8
     */
    public static final class Builder {
        // 上下文
        private Context mContext;
        // ws链接地址
        private String wsUrl;

        /**
         * ConStructor
         * @author leibing
         * @createTime 2017/5/8
         * @lastModify 2017/5/8
         * @param mContext 上下文
         * @return
         */
        public Builder(Context mContext) {
            this.mContext = mContext;
        }

        /**
         * ws链接地址配置
         * @author leibing
         * @createTime 2017/5/8
         * @lastModify 2017/5/8
         * @param wsUrl ws链接地址
         * @return
         */
        public Builder setWsUrl(String wsUrl) {
            this.wsUrl = wsUrl;
            return this;
        }

        /**
         * 构建JkWsManagerImpl对象
         * @author leibing
         * @createTime 2017/5/8
         * @lastModify 2017/5/8
         * @param
         * @return
         */
        public JkWsManagerImpl build() {
            return new JkWsManagerImpl(this);
        }
    }
}
