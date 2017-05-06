package cn.jianke.sample.httprequest.okhttp;

import android.app.Activity;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import cn.jianke.httprequest.httprequest.ApiCallback;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @className: OkHttpRequestUtils
 * @classDescription: okhttp request
 * @author: leibing
 * @createTime: 2017/3/16
 */
public class OkHttpRequestUtils {
    // 连接超时时间
    public final static int CONNECT_TIMEOUT =60;
    // 读取超时时间
    public final static int READ_TIMEOUT=100;
    // 写的超时时间
    public final static int WRITE_TIMEOUT=60;
    // sington
    private static OkHttpRequestUtils instance;
    // ok http client
    private OkHttpClient mOkHttpClient;

    private OkHttpRequestUtils(){
        mOkHttpClient = new OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT,TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT,TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

    /**
     * get sington
     * @author leibing
     * @createTime 2017/3/16
     * @lastModify 2017/3/16
     * @param
     * @return
     */
    public static OkHttpRequestUtils getInstance(){
        if (instance == null){
            synchronized (OkHttpRequestUtils.class){
                if (instance == null)
                    instance = new OkHttpRequestUtils();
            }
        }
        return  instance;
    }

    /**
     * request by post
     * @author leibing
     * @createTime 2017/3/16
     * @lastModify 2017/3/16
     * @param url
     * @param map
     * @param responseCls
     * @param activity
     * @param callBack
     * @return
     */
    public void requestByPost(String url,
                              HashMap<String, String> map,
                              Class responseCls,
                              Activity activity,
                              ApiCallback callBack){
        // 创建请求的参数body
        FormBody.Builder builder = new FormBody.Builder();
        // 遍历key
        if (null != map) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                builder.add(entry.getKey(), entry.getValue().toString());
            }
        }

        // create request instance
        RequestBody mRequestBody = builder.build();
        Request mRequest = new Request.Builder().url(url).post(mRequestBody).build();

        // create call instance
        Call mCall = mOkHttpClient.newCall(mRequest);

        // start request
        mCall.enqueue(new JkOkHttpCallBack(callBack, activity, responseCls));
    }

    /**
     * request by post
     * @author leibing
     * @createTime 2017/3/16
     * @lastModify 2017/3/16
     * @param url
     * @param map
     * @param responseCls
     * @param requestId
     * @param activity
     * @param callBack
     * @return
     */
    public void requestByPost(String url,
                              HashMap<String, String> map,
                              Class responseCls,
                              String requestId,
                              Activity activity,
                              ApiCallback callBack){
        // 创建请求的参数body
        FormBody.Builder builder = new FormBody.Builder();
        // 遍历key
        if (null != map) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                builder.add(entry.getKey(), entry.getValue().toString());
            }
        }

        // create request instance
        RequestBody mRequestBody = builder.build();
        Request mRequest = new Request.Builder().url(url).post(mRequestBody).build();

        // create call instance
        Call mCall = mOkHttpClient.newCall(mRequest);

        // start request
        mCall.enqueue(new JkOkHttpCallBack(callBack, activity, responseCls, requestId));
    }

    /**
     * request by post json
     * @author leibing
     * @createTime 2017/3/16
     * @lastModify 2017/3/16
     * @param url
     * @param map
     * @param responseCls
     * @param requestId
     * @param activity
     * @param callBack
     * @return
     */
    public void requestByPostJson(String url,
                                  HashMap<String, Object> map,
                                  Class responseCls,
                                  String requestId,
                                  Activity activity,
                                  ApiCallback callBack){
        // get request
        Request mRequest = JSONRequestUtils.jsonRequet(url, map);
        // create call instance
        Call mCall = mOkHttpClient.newCall(mRequest);
        // start request
        mCall.enqueue(new JkOkHttpCallBack(callBack, activity, responseCls, requestId));
    }

    /**
     * request by get
     * @author leibing
     * @createTime 2017/3/16
     * @lastModify 2017/3/16
     * @param url
     * @param map
     * @param responseCls
     * @param activity
     * @param callBack
     * @return
     */
    public void requestByGet(String url,
                             HashMap<String, String> map,
                             Class responseCls,
                             Activity activity,
                             ApiCallback callBack){
        // 拼接参数
        if (map != null)
            url = getRequestUrl(url, map);
        System.out.println("xxxxxxxxxxxxxx url = " + url);
        // request builder
        Request.Builder requestBuilder = new Request.Builder().url(url);
        // 可以省略，默认是GET请求
        requestBuilder.method("GET",null);
        // create request instance
        final Request request = requestBuilder.build();
        // create call instance
        Call mCall= mOkHttpClient.newCall(request);
        // start request
        mCall.enqueue(new JkOkHttpCallBack(callBack, activity, responseCls));
    }

    /**
     * request by get
     * @author leibing
     * @createTime 2017/3/16
     * @lastModify 2017/3/16
     * @param url
     * @param map
     * @param responseCls
     * @param requestId
     * @param activity
     * @param callBack
     * @return
     */
    public void requestByGet(String url,
                             HashMap<String, String> map,
                             Class responseCls,
                             String requestId,
                             Activity activity,
                             ApiCallback callBack){
        // 拼接参数
        if (map != null)
            url = getRequestUrl(url, map);
        System.out.println("xxxxxxxxxxxxxx url = " + url);
        // request builder
        Request.Builder requestBuilder = new Request.Builder().url(url);
        // 可以省略，默认是GET请求
        requestBuilder.method("GET",null);
        // create request instance
        final Request request = requestBuilder.build();
        // create call instance
        Call mCall= mOkHttpClient.newCall(request);
        // start request
        mCall.enqueue(new JkOkHttpCallBack(callBack, activity, responseCls, requestId));
    }

    /**
     * 拼接参数
     * @author leibing
     * @createTime 2017/3/16
     * @lastModify 2017/3/16
     * @param url
     * @param params
     * @return
     */
    private String getRequestUrl(String url,HashMap<String, String> params) {
        if (params != null) {
            Iterator<String> it = params.keySet().iterator();
            StringBuffer sb = null;
            while (it.hasNext()) {
                String key = it.next();
                String value = params.get(key);
                if (sb == null) {
                    sb = new StringBuffer();
                    sb.append("?");
                } else {
                    sb.append("&");
                }
                sb.append(key);
                sb.append("=");
                sb.append(value);
            }
            url += sb.toString();
        }
        return url;
    }

    /**
     * 构建map对象
     * @author leibing
     * @createTime 2017/4/11
     * @lastModify 2017/4/11
     * @param paramKey key
     * @param paramValue value
     * @return
     */
    public static HashMap<String,String> JkRequestParameters(String[] paramKey, String... paramValue){
        HashMap paramMap = new HashMap<String, String>();
        for(int i=0; paramKey!=null && i<paramKey.length && i<paramValue.length; i++){
            paramMap.put(paramKey[i], paramValue[i]);
        }
        return paramMap;
    }
}
