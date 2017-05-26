package cn.jianke.sample.httprequest.httpresponse;

/**
 * @className: JkChatPraiseResponse
 * @classDescription: 健客聊天评价数据
 * @author: leibing
 * @createTime: 2017/5/17
 */
public class JkChatPraiseResponse {
    // 消息
    public String msg = "";
    // 数据
    public Data data;

    /**
     * @className: Data
     * @classDescription: 数据
     * @author: leibing
     * @createTime: 2017/5/17
     */
    public static class Data{
        // 结果
        public String result = "";
    }
}
