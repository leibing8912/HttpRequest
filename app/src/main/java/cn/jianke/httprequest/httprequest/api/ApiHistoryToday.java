package cn.jianke.httprequest.httprequest.api;

import cn.jianke.httprequest.httprequest.ApiCallback;
import cn.jianke.httprequest.httprequest.JkApiCallback;
import cn.jianke.httprequest.httprequest.JkApiRequest;
import cn.jianke.httprequest.httprequest.httpresponse.HistoryTodayResponse;
import cn.jianke.httprequest.utils.TransUtil;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @className: ApiHistoryToday
 * @classDescription: 历史上的今天api
 * @author: leibing
 * @createTime: 2016/11/10
 */
public class ApiHistoryToday {
    // BaseUrl
    private final static String BASE_URL = "http://apis.haoservice.com";
    // api
    private ApiStore mApiStore;

    /**
     * Constructor
     * @author leibing
     * @createTime 2016/08/30
     * @lastModify 2016/08/30
     * @param
     * @return
     */
    public ApiHistoryToday() {
        // 初始化api
        mApiStore = JkApiRequest.getInstance().create(ApiStore.class, BASE_URL);
    }

    /**
     * 获取历史上的今天数据
     * @author leibing
     * @createTime 2016/11/10
     * @lastModify 2016/11/10
     * @param month 月
     * @param day 日
     * @param key 密钥值
     * @param callback 回调
     * @return
     */
    public void getHistoryTodayData(String month, String day, String key,
                      ApiCallback<HistoryTodayResponse> callback){
        // 当前接口返回数据第一层数据中存在json数组 add by leibing 2016/11/10
        TransUtil.isJsonListData = true;

        Call<HistoryTodayResponse> mCall =  mApiStore.getHistoryTodayData(month, day, key);
        mCall.enqueue(new JkApiCallback<HistoryTodayResponse>(callback));
    }

    /**
     * @interfaceName: ApiStore
     * @interfaceDescription: 历史上的今天api接口
     * @author: leibing
     * @createTime: 2016/11/10
     */
    private interface ApiStore {
        @GET("/lifeservice/toh")
        Call<HistoryTodayResponse> getHistoryTodayData(
                @Query("month") String month,
                @Query("day") String day,
                @Query("key") String key);
    }
}
