package cn.jianke.httprequest.httprequest.okhttp;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.lang.ref.WeakReference;
import cn.jianke.httprequest.httprequest.ApiCallback;
import cn.jianke.httprequest.module.AppManager;
import cn.jianke.httprequest.utils.StringUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @className:
 * @classDescription:
 * @author: leibing
 * @createTime: 2017/3/16
 */
public class JkOkHttpCallBack<T> implements Callback {
    // 请求标识--数据格式一(msg: {info: "",code: 0,success: true},data: null})
    public static final String REQUEST_ID_ONE = "request_id_one";
    // 请求标识--数据格式二（无需解析数据，直接回调）
    public static final String REQUEST_ID_TWO = "request_id_two";
    // 数据解析标识--result
    public final static String DATA_ANALYSIS_IDENTIFY_RESULT = "result";
    // 数据解析标识--data
    public final static String DATA_ANALYSIS_IDENTIFY_DATA = "data";
    // 数据解析标识--status
    public final static String DATA_ANALYSIS_IDENTIFY_STATUS = "status";
    // 数据解析标识--info
    public final static String DATA_ANALYSIS_IDENTIFY_INFO = "info";
    // 数据解析标识--msg
    public final static String DATA_ANALYSIS_IDENTIFY_MSG = "msg";
    // 数据解析标识--code
    public final static String DATA_ANALYSIS_IDENTIFY_CODE = "code";
    // 数据解析标识--success
    public final static String DATA_ANALYSIS_IDENTIFY_SUCCESS = "success";
    // 未知错误
    public final static String UNKNOWN_ERROR = "unknown_error";
    // 空数据错误
    public final static String NULL_DATA  = "no_data";
    // 数据格式错误
    public final static String DATA_FORMAT_ERROR = "数据格式错误";
    // 回调
    private ApiCallback<T> mCallback;
    // 页面弱引用
    private WeakReference<Activity> activityWeakRef;
    // type cls
    public Class<T> typeCls;
    // ui thread handler
    public Handler mHandler;
    // 请求标识
    private String requestId;

    /**
     * Constructor
     * @author leibing
     * @createTime 2017/3/16
     * @lastModify 2017/3/16
     * @param mCallback 回调
     * @param activity 页面实例
     * @return
     */
    public JkOkHttpCallBack(ApiCallback<T> mCallback, Activity activity, Class<T> typeCls){
        this.mCallback = mCallback;
        activityWeakRef = new WeakReference<Activity>(activity);
        this.typeCls = typeCls;
        mHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * Constructor
     * @author leibing
     * @createTime 2017/3/16
     * @lastModify 2017/3/16
     * @param mCallback 回调
     * @param activity 页面实例
     * @param requestId 请求标识
     * @return
     */
    public JkOkHttpCallBack(ApiCallback<T> mCallback, Activity activity,
                            Class<T> typeCls, String requestId){
        this.mCallback = mCallback;
        activityWeakRef = new WeakReference<Activity>(activity);
        this.typeCls = typeCls;
        mHandler = new Handler(Looper.getMainLooper());
        this.requestId = requestId;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        if (mCallback == null
                || activityWeakRef == null
                || activityWeakRef.get() == null)
            return;
        mCallback.onFailure();
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (mCallback == null
                || activityWeakRef == null
                || activityWeakRef.get() == null)
            return;
        // 处理是否当前页，如果非当前页则无需回调更新UI
        if (!AppManager.getInstance().isCurrent(activityWeakRef.get())){
            return;
        }
        // 若数据为空则回调返回
        if (response == null || response.body() == null) {
            mCallback.onError(NULL_DATA);
            return;
        }
        String body = response.body().string();
        // 无数据回调处理
        if (body == null){
            mCallback.onError(NULL_DATA);
            return;
        }
        // 根据请求标识解析数据
        if (StringUtil.isNotEmpty(requestId)){
            switch (requestId){
                case REQUEST_ID_ONE:
                    // 数据格式一处理
                    requestIdOneDeal(body);
                    break;
                case REQUEST_ID_TWO:
                    // 数据格式二处理
                    requestIdTwoDeal(body);
                    break;
                default:
                    break;
            }
            return;
        }
    }

    /**
     * 数据格式二处理
     * @author leibing
     * @createTime 2017/4/10
     * @lastModify 2017/4/10
     * @param body
     * @return
     */
    private void requestIdTwoDeal(String body) {
        if (StringUtil.isNotEmpty(body)){
            successCallBack(body);
        }else {
            errorCallBack(NULL_DATA);
        }
    }

    /**
     * 数据格式一处理
     * @author leibing
     * @createTime 2017/4/10
     * @lastModify 2017/4/10
     * @param body
     * @return
     */
    private void requestIdOneDeal(String body) {
        final JSONObject result;
        try {
            result = new JSONObject(JSONTokener(body));
            JSONObject msg = result.optJSONObject(DATA_ANALYSIS_IDENTIFY_MSG);
            String code = msg.optString(DATA_ANALYSIS_IDENTIFY_CODE);
            String success = msg.optString(DATA_ANALYSIS_IDENTIFY_SUCCESS);
            String info = msg.optString(DATA_ANALYSIS_IDENTIFY_INFO);
            String data = result.optJSONObject(DATA_ANALYSIS_IDENTIFY_DATA).toString();
            // 成功
            if ("0".equals(code)
                    && "true".equals(success)){
                // 若数据为非空则gson解析数据回调
                if (StringUtil.isNotEmpty(data)){
                    successCallBack(new Gson().fromJson(data, typeCls));
                }else {
                    successCallBack(null);
                }
            }else {
                // 失败
                errorCallBack(info);
            }
        } catch (JSONException e) {
            errorCallBack(DATA_FORMAT_ERROR);
            e.printStackTrace();
        }
    }

    /**
     * 成功回调
     * @author leibing
     * @createTime 2017/4/10
     * @lastModify 2017/4/10
     * @param object
     * @return
     */
    private void successCallBack(final Object object){
        if (mCallback != null && mHandler != null){
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (object != null){
                        mCallback.onSuccess((T) object);
                    }else {
                        mCallback.onSuccess(null);
                    }
                }
            });
        }
    }

    /**
     * 请求错误回调
     * @author leibing
     * @createTime 2017/4/10
     * @lastModify 2017/4/10
     * @param msg 错误消息
     * @return
     */
    private void errorCallBack(final String msg){
        if (mCallback != null && mHandler != null){
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mCallback.onError(msg);
                }
            });
        }
    }

    /**
     * 请求失败回调
     * @author leibing
     * @createTime 2017/4/10
     * @lastModify 2017/4/10
     * @param
     * @return
     */
    private void failCallBack(){
        if (mCallback != null && mHandler != null){
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mCallback.onFailure();
                }
            });
        }
    }

    /**
     * 解决json解析问题
     * @author leibing
     * @createTime 2017/3/16
     * @lastModify 2017/3/16
     * @param in
     * @return
     */
    private String JSONTokener(String in) {
        // consume an optional byte order mark (BOM) if it exists
        if (in != null && in.startsWith("\ufeff")) {
            in = in.substring(1);
        }
        return in;
    }
}
