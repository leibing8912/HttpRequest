package cn.jianke.httprequest.httprequest.api;

import android.app.Activity;
import cn.jianke.httprequest.httprequest.ApiCallback;
import cn.jianke.httprequest.httprequest.JkApiCallback;
import cn.jianke.httprequest.httprequest.JkApiRequest;
import cn.jianke.httprequest.httprequest.httpresponse.HistoryTodayResponse;
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
                                    Activity activity,
                      ApiCallback<HistoryTodayResponse> callback){
        Call<HistoryTodayResponse> mCall =  mApiStore.getHistoryTodayData(month, day, key);
        mCall.enqueue(new JkApiCallback<HistoryTodayResponse>(callback, activity,
                HistoryTodayResponse.class, JkApiCallback.REQUEST_ID_ONE));
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
