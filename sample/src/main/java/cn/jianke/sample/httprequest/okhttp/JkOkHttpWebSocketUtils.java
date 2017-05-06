package cn.jianke.sample.httprequest.okhttp;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocketListener;

/**
 * @className: JkOkHttpWebSocketUtils
 * @classDescription: okhttp websocket
 * @author: leibing
 * @createTime: 2017/5/6
 */
public class JkOkHttpWebSocketUtils {
    // 连接超时时间
    public final static int CONNECT_TIMEOUT =60;
    // 读取超时时间
    public final static int READ_TIMEOUT=100;
    // 写的超时时间
    public final static int WRITE_TIMEOUT=60;
    // sington
    private static JkOkHttpWebSocketUtils instance;
    // ok http client
    private OkHttpClient mOkHttpClient;

    /**
     * Constructor
     * @author leibing
     * @createTime 2017/5/6
     * @lastModify 2017/5/6
     * @param
     * @return
     */
    private JkOkHttpWebSocketUtils(){
        mOkHttpClient = new OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

    /**
     * get sington
     * @author leibing
     * @createTime 2017/5/6
     * @lastModify 2017/5/6
     * @param
     * @return
     */
    public static JkOkHttpWebSocketUtils getInstance(){
        if (instance == null){
            synchronized (OkHttpRequestUtils.class){
                if (instance == null)
                    instance = new JkOkHttpWebSocketUtils();
            }
        }
        return  instance;
    }

    /**
     * init ws client
     * @author leibing
     * @createTime 2017/5/6
     * @lastModify 2017/5/6
     * @param wsUrl websocket链接地址
     * @param mWebSocketListener websocket listner
     * @return
     */
    public void initWsClient(String wsUrl, WebSocketListener mWebSocketListener){
        // create request
        Request request = new Request.Builder()
                .url(wsUrl)
                .build();
        // create websocket communicate
        if (mOkHttpClient != null){
            mOkHttpClient.newWebSocket(request, mWebSocketListener);
        }
    }
}
