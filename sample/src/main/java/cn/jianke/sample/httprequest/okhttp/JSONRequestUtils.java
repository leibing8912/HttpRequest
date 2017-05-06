package cn.jianke.sample.httprequest.okhttp;

import android.text.TextUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @className: JSONRequestUtils
 * @classDescription: json格式请求
 * @author: leibing
 * @createTime: 2017/5/6
 */
public class JSONRequestUtils {
    /**
     * json格式请求数据
     * @author leibing
     * @createTime 2017/5/6
     * @lastModify 2017/5/6
     * @param url 请求地址
     * @param params 请求参数
     * @return
     */
    public static Request jsonRequet(String url, HashMap<String,Object> params){
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(url).post(jsonBody(params)).build();
        return request;
    }

    /**
     * get json body
     * @author leibing
     * @createTime 2017/5/6
     * @lastModify 2017/5/6
     * @param params 请求参数
     * @return
     */
    private static RequestBody jsonBody(HashMap<String, Object> params) {
        String json = "";
        JSONObject jsonObj = new JSONObject();
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                if (!TextUtils.isEmpty(key)) {
                    Object value = params.get(key);
                    if (value instanceof String) {
                        String val = (String) value;
                        if (TextUtils.isEmpty(val)) {
                            value = "";
                        }
                    } else if (value instanceof JSONObject) {
                        JSONObject val = (JSONObject) value;
                        if (val == null) {
                            value = new JSONObject();
                        }
                    } else if (value instanceof JSONArray) {
                        JSONArray val = (JSONArray) value;
                        if (val == null) {
                            value = new JSONArray();
                        }
                    }
                    if (value == null) {
                        value = "";
                    }
                    jsonPutValue(jsonObj, key, value);
                }
            }
        }
        if (jsonObj.length() > 0) {
            json = jsonObj.toString();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
        return requestBody;
    }

    /**
     * 往json对象存值
     * @author leibing
     * @createTime 2017/5/6
     * @lastModify 2017/5/6
     * @param jsonObj json对象
     * @param key 键
     * @param value 值
     * @return
     */
    private static void jsonPutValue(JSONObject jsonObj, String key, Object value) {
        try {
            jsonObj.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
