package cn.jianke.sample.httprequest.httpresponse;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @className: JkChatHistoryMsgResponse
 * @classDescription: 商务通历史消息数据
 * @author: leibing
 * @createTime: 2017/5/18
 */
public class JkChatHistoryMsgResponse implements Serializable {
    // 消息
    public String msg = "";
    // 数据
    public Data data;

    /**
     * @className: Data
     * @classDescription: 数据
     * @author: leibing
     * @createTime: 2017/5/18
     */
    public static class Data{
        // 结果
        public ArrayList<Result> result;

        /**
         * @className: Result
         * @classDescription: 结果
         * @author: leibing
         * @createTime: 2017/5/18
         */
        public static class Result{
            public String ID = "";
            public String TalkId = "";
            public String CustomId = "";
            public String StaffId = "";
            public String StaffName = "";
            public ArrayList<ContentCls> Content;

            /**
             * @className: ContentCls
             * @classDescription: 内容
             * @author: leibing
             * @createTime: 2017/5/19
             */
            public static class ContentCls{
                // 消息类型（1=文字；2=图片；3=链接）
                public String type = "";
                // 消息内容
                public String msg = "";
                // 消息图片大小（如果消息类型是图片类型）
                public SizeCls size;

                /**
                 * @className: SizeCls
                 * @classDescription: 消息图片大小
                 * @author: leibing
                 * @createTime: 2017/5/25
                 */
                public static class SizeCls{
                    // 宽度
                    public int width = 100;
                    // 高度
                    public int height = 100;
                }
            }
            // 消息方向(1是用户，其他均是客服)
            public String ChatType = "";
            // 消息时间
            public String ChatTime = "";
            public String Href = "";
            public String WebSiteId = "";
            public String MsgType = "";
            public String ProNumber = "";
        }
    }
}
