package cn.jianke.httprequest.httprequest.okhttp;

/**
 * @className: RequestUrlManager
 * @classDescription: 请求url管理
 * @author: leibing
 * @createTime: 2017/3/16
 */
public class RequestUrlManager {
    /**---------------------------------请求Host--------------------------------------------------
     -----------------------------------------------------------------------------------*/
    // 请求Host
    private final static String REQUEST_HOST = "http://apis.haoservice.com";

    /**---------------------------------请求api--------------------------------------------------
     -----------------------------------------------------------------------------------*/
    // 历史上的今天请求api
    private final static String HISTORY_TODAY_REQUEST_API = "/lifeservice/toh";

    /**---------------------------------请求url--------------------------------------------------
        -----------------------------------------------------------------------------------*/
    // 历史上的今天请求URL
    public final static String SUBMIT_ORDER_REQUEST_URL = REQUEST_HOST
            + HISTORY_TODAY_REQUEST_API;
}
