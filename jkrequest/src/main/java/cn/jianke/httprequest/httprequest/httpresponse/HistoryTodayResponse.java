package cn.jianke.httprequest.httprequest.httpresponse;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * @className: HistoryTodayResponse
 * @classDescription: 历史上的今天响应数据
 * @author: leibing
 * @createTime: 2016/11/10
 */
public class HistoryTodayResponse extends BaseResponse implements Serializable{
    // 序列化UID 用于反序列化
    private static final long serialVersionUID = 234513596098152097L;
    // error_code
    public String error_code = "";
    // reason
    public String reason = "";
    // 结果列表
    public ArrayList<ResultList> result;

    public static class ResultList implements Serializable{
        // id
        public String id = "";
        // day
        public String day = "";
        // des
        public String des = "";
        // lunar
        public String lunar = "";
        // month
        public String month = "";
        // pic
        public String pic = "";
        // title
        public String title = "";
        // year
        public String year = "";
    }
}
