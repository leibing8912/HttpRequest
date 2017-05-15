package cn.jianke.sample.httprequest.okhttp.websocket.util;

import com.google.gson.Gson;
import java.lang.reflect.Type;

/**
 * @className: JsonUtil
 * @classDescription: json util
 * @author: leibing
 * @createTime: 2017/5/15
 */
public class JsonUtil {

    /**
      * 对象转换成json字符串
      * @author leibing
      * @createTime 2017/5/15
      * @lastModify 2017/5/15
      * @param obj
      * @return
      */
    public static String toJson(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    /**
      * json字符串转成对象
      * @author leibing
      * @createTime 2017/5/15
      * @lastModify 2017/5/15
      * @param str
     *  @param type
      * @return
      */
    public static <T> T fromJson(String str, Type type) {
        Gson gson = new Gson();
        return gson.fromJson(str, type);
    }

    /**
      * json字符串转成对象
      * @author leibing
      * @createTime 2017/5/15
      * @lastModify 2017/5/15
      * @param str
      * @param type
      * @return
      */
    public static <T> T fromJson(String str, Class<T> type) {
        Gson gson = new Gson();
        return gson.fromJson(str, type);
    }
}
