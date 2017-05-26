package cn.jianke.sample.httprequest;

import android.util.Log;

import cn.jianke.sample.BuildConfig;

/**
 * @className: JkRequestLog
 * @classDescription: 健客请求log
 * @author: leibing
 * @createTime: 2017/5/3
 */
public class JkRequestLog {

    /**
     * 日志打印
     * @author leibing
     * @createTime 2017/5/3
     * @lastModify 2017/5/3
     * @param tag
     * @param msg
     * @return
     */
    public static void printLogs(String tag, String msg){
        if (BuildConfig.IS_PRINT_REQUEST_LOG){
            Log.e(tag, msg);
        }
    }
}
