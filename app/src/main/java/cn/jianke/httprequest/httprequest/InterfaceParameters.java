package cn.jianke.httprequest.httprequest;

import cn.jianke.httprequest.BuildConfig;

/**
 * @className: InterfaceParameters
 * @classDescription: 参数配置
 * @author: leibing
 * @createTime: 2016/8/30
 */

public class InterfaceParameters {
    // 请求URL
    public final static String REQUEST_HTTP_URL = BuildConfig.API_URL;
    // 接口返回结果名称
    public final static String INFO = "info";
    // 接口返回错误码
    public final static String ERROR_CODE = "errorcode";
    // 接口返回错误信息
    public final static String ERROR_MSG = "errormsg";
    // 接口返回码 add by leibing 2016/11/10
    public final static String ERROR_CODE_NEW = "error_code";
    // 接口返回信息
    public final static String REASON = "reason";
}
