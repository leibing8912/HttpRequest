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
import cn.jianke.httprequest.httprequest.httpresponse.BaseResponse;
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
    // 添加json标签名称
    public final static String JK_JSON_NAME = "jk_json_name";
    // 请求标识--数据格式一({error_code：0，reason：成功，result：array})
    public static final String REQUEST_ID_ONE = "request_id_one";
    // 请求标识--数据格式二（无需解析数据，直接回调）
    public static final String REQUEST_ID_TWO = "request_id_two";
    // 请求标识--数据格式三（{"errorcode":"0","errormsg":"成功","info"：obj}）
    public static final String REQUEST_ID_THREE = "request_id_three";
    // 数据解析标识--result
    public final static String DATA_ANALYSIS_IDENTIFY_RESULT = "result";
    // 数据解析标识--data
    public final static String DATA_ANALYSIS_IDENTIFY_DATA = "data";
    // 数据解析标识--status
    public final static String DATA_ANALYSIS_IDENTIFY_STATUS = "status";
    // 数据解析标识--info
    public final static String DATA_ANALYSIS_IDENTIFY_INFO = "info";
    // 数据解析标识--errorcode
    public final static String DATA_ANALYSIS_IDENTIFY_ERRORCODE = "errorcode";
    // 数据解析标识--errormsg
    public final static String DATA_ANALYSIS_IDENTIFY_ERRORMSG = "errormsg";
    // 数据解析标识--msg
    public final static String DATA_ANALYSIS_IDENTIFY_MSG = "msg";
    // 数据解析标识--code
    public final static String DATA_ANALYSIS_IDENTIFY_CODE = "code";
    // 数据解析标识--success
    public final static String DATA_ANALYSIS_IDENTIFY_SUCCESS = "success";
    // 数据解析标识--error_code
    public final static String DATA_ANALYSIS_IDENTIFY_ERROR_CODE = "error_code";
    // 数据解析标识--reason
    public final static String DATA_ANALYSIS_IDENTIFY_REASON = "reason";
    // 状态标识--0
    public final static String STATUS_IDENTIFY_ZORE = "0";
    // 状态标识--1
    public final static String STATUS_IDENTIFY_ONE = "1";
    // 状态标识--true
    public final static String STATUS_IDENTIFY_TRUE = "true";
    // 异常
    public final static String EXCEPTION = "exception";
    // 空数据
    public final static String NULL_DATA  = "nodata";
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
        if (activityWeakRef == null
                || activityWeakRef.get() == null)
            return;
        // 失败回调
        failCallBack();
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (activityWeakRef == null
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
                case REQUEST_ID_THREE:
                    // 数据格式三处理
                    requestIdThreeDeal(body);
                    break;
                default:
                    break;
            }
            return;
        }
    }

    /**
     * 数据格式三处理
     * @author leibing
     * @createTime 2017/4/11
     * @lastModify 2017/4/11
     * @param body
     * @return
     */
    private void requestIdThreeDeal(String body) {
        try {
            JSONObject mJson = new JSONObject(body);
            // 响应码
            String errorCode = mJson.optString(DATA_ANALYSIS_IDENTIFY_ERRORCODE);
            // 消息
            String errormsg = mJson.optString(DATA_ANALYSIS_IDENTIFY_ERRORMSG);
            // 结果信息
            String info = mJson.optString(DATA_ANALYSIS_IDENTIFY_INFO);
            BaseResponse baseResponse;
            // 请求成功
            if (STATUS_IDENTIFY_ZORE.equals(errorCode)
                    && StringUtil.isNotEmpty(info)){
                // Gson解析数据，回调数据
                baseResponse = (BaseResponse) new Gson().fromJson(info, typeCls);
                baseResponse.setSuccess(true);
                baseResponse.setErrormsg(errormsg);
                baseResponse.setInfo(info);
                successCallBack(baseResponse);
            }else {
                // 请求错误（回调错误信息）
                errorCallBack(info);
            }
        } catch (JSONException e) {
            // 异常
            errorCallBack(EXCEPTION);
            e.printStackTrace();
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
     * @createTime 2017/4/11
     * @lastModify 2017/4/11
     * @param body 数据字符串
     * @return
     */
    private void requestIdOneDeal(String body) {
        try {
            JSONObject mJson = new JSONObject(body);
            // 响应码
            String errorCode = mJson.optString(DATA_ANALYSIS_IDENTIFY_ERROR_CODE);
            // 消息
            String reason = mJson.optString(DATA_ANALYSIS_IDENTIFY_REASON);
            // 结果信息
            String result = mJson.optString(DATA_ANALYSIS_IDENTIFY_RESULT);
            // 改造后json字符串
            String remakeStr = remakeJsonData(body);
            // 获取需解析数据
            JSONObject remakeJsonObject = new JSONObject(remakeStr);
            String data = remakeJsonObject.optString(JK_JSON_NAME);
            // 请求成功
            if (STATUS_IDENTIFY_ZORE.equals(errorCode)
                    && StringUtil.isNotEmpty(result)){
                // Gson解析数据，回调数据
                BaseResponse baseResponse;
                baseResponse = (BaseResponse) new Gson().fromJson(data, typeCls);
                baseResponse.setSuccess(true);
                baseResponse.setErrormsg(reason);
                baseResponse.setInfo(data);
                successCallBack(baseResponse);
            }else {
                // 请求错误（回调错误信息）
                errorCallBack(reason);
            }
        } catch (JSONException e) {
            // 异常
            errorCallBack(EXCEPTION);
            e.printStackTrace();
        }
    }

    /**
     * 改造json数据，增加一层标签
     * @author leibing
     * @createTime 2017/4/11
     * @lastModify 2017/4/11
     * @param jsonStr 改造前json字符串
     * @return jsonObject.toString 改造后json字符串
     */
    public static String remakeJsonData(String jsonStr) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JK_JSON_NAME, new JSONObject(jsonStr));
        return jsonObject.toString();
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
