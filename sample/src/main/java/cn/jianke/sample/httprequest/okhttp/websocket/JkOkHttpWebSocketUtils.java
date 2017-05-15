package cn.jianke.sample.httprequest.okhttp.websocket;

import android.util.Log;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import cn.jianke.sample.httprequest.okhttp.OkHttpRequestUtils;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocketListener;

/**
 * @className: JkOkHttpWebSocketUtils
 * @classDescription: okhttp websocket
 * @author: leibing
 * @createTime: 2017/5/6
 */
public class JkOkHttpWebSocketUtils {
    // 日志标识
    public final static String TAG = "JkRequest@JkOkHttpWebSocketUtils";
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
    // 锁
    private Lock mLock;

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
                .addInterceptor(new AddHeaderInterceptor())
                .retryOnConnectionFailure(true)
                .build();
        this.mLock = new ReentrantLock();
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
        Request mRequest = new Request.Builder()
                .url(wsUrl)
                .build();
        // create websocket communicate
        if (mOkHttpClient != null
                && mLock != null
                && mWebSocketListener != null){
            // cancel all request
            cancalAllRequest();
            try {
                // 上锁
                mLock.lockInterruptibly();
                try {
                    mOkHttpClient.newWebSocket(mRequest, mWebSocketListener);
                } finally {
                    // 解锁
                    mLock.unlock();
                }
            } catch (InterruptedException e) {
            }
        }
    }

    /**
     * 取消所有请求
     * @author leibing
     * @createTime 2017/5/8
     * @lastModify 2017/5/8
     * @param
     * @return
     */
    public void cancalAllRequest(){
        if (mOkHttpClient != null)
            mOkHttpClient.dispatcher().cancelAll();
    }

    /**
     * @className: AddHeaderInterceptor
     * @classDescription: add header interceptor
     * @author: leibing
     * @createTime: 2017/5/10
     */
    static class AddHeaderInterceptor implements Interceptor{

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request original = chain.request();
            // Request customization: add request headers
            Log.e(TAG, "#AddHeaderInterceptor");
            Request.Builder requestBuilder
                    = original.newBuilder().addHeader("Origin", "http://tw.sgz88.com");
            Request request = requestBuilder.build();
            long t1 = System.nanoTime();
            Log.e(TAG, String.format("Sending request %s%s",
                    request.url(),request.headers()));
            Response response = chain.proceed(request);
            long t2 = System.nanoTime();
            Log.e(TAG, String.format("Received response for %s in %.1fms%n%s",
                    request.url(), (t2 - t1) / 1e6d, response.headers()));
            return response;
        }
    }
}
