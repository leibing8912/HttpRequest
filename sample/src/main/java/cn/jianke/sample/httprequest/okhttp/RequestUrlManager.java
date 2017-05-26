package cn.jianke.sample.httprequest.okhttp;

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
    public final static String JK_CHAT_HOST_URL = "http://tw.sgz88.com";

    /**---------------------------------请求api--------------------------------------------------
     -----------------------------------------------------------------------------------*/
    // 历史上的今天请求api
    private final static String HISTORY_TODAY_REQUEST_API = "/lifeservice/toh";
    // 上传图片api
    private final static String JK_CHAT_UPLOAD_IMG_API
            = "/XYAjax/MsgChange.ashx?Type=UploadFile&secureuri=false&fileElementId=fup&dataType=json";
    // 提交评价api
    private final static String JK_CHAT_COMMIT_PRAISE_API = "/XYAjax/MsgChange.ashx";
    // 商务通获取历史消息api
    private final static String JK_CHAT_GET_HISTORY_MSG_API = "/XYAjax/InfoHandler.ashx";

    /**---------------------------------请求url--------------------------------------------------
        -----------------------------------------------------------------------------------*/
    // 历史上的今天请求URL
    public final static String HISTORY_TODAY_REQUEST_URL = REQUEST_HOST
            + HISTORY_TODAY_REQUEST_API;
    // 上传图片url
    public final static String JK_CHAT_UPLOAD_IMG_URL = JK_CHAT_HOST_URL
            + JK_CHAT_UPLOAD_IMG_API;
    // 提交评价url
    public final static String JK_CHAT_COMMIT_PRAISE_URL = JK_CHAT_HOST_URL
            + JK_CHAT_COMMIT_PRAISE_API;
    // 商务通获取历史消息url
    public final static String JK_CHAT_GET_HISTORY_MSG_URL = JK_CHAT_HOST_URL
            + JK_CHAT_GET_HISTORY_MSG_API;

}
