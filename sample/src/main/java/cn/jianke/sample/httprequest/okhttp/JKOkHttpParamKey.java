package cn.jianke.sample.httprequest.okhttp;

/**
 * @className: JKOkHttpParamKey
 * @classDescription: 参数key集合
 * @author: leibing
 * @createTime: 2017/4/11
 */
public class JKOkHttpParamKey {
    // 历史的今日
    public final static String[] HISTORY_TODAY_PARAM = {
            "month","day","key"};
    // 商务通提交评价参数
    public final static String[] JK_CHAT_COMMIT_PRAISE_PARAM = {
            "Type", "jsoncallback", "'level", "ReplyLevel", "content", "staffname", "staffid",
            "website", "fromtype", "CId", "CName", "Curl","apptype"
    };
    // 商务通获取历史消息参数
    public final static String[] JK_CHAT_HISTORY_MSG_PARAM = {
            "Type", "Id", "apptype", "count", "siteId"
    };
}
