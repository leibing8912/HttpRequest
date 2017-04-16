package cn.jianke.sample.httprequest.retrofit.api;

import android.app.Activity;
import cn.jianke.httprequest.BuildConfig;
import cn.jianke.httprequest.httprequest.ApiCallback;
import cn.jianke.httprequest.httprequest.CommonApiCallback;
import cn.jianke.httprequest.httprequest.CommonApiRequest;
import cn.jianke.httprequest.httprequest.httpresponse.BaseResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @className: APiTest
 * @classDescription: 测试
 * @author: leibing
 * @createTime: 2017/4/11
 */
public class APiTest {
    // api
    private ApiStore mApiStore;
    // request url
    private String REQUEST_HTTP_URL = BuildConfig.API_URL;

    /**
     * Constructor
     * @author leibing
     * @createTime 2016/08/30
     * @lastModify 2016/08/30
     * @param
     * @return
     */
    public APiTest() {
        // 初始化api
        mApiStore = CommonApiRequest.getInstance().create(ApiStore.class,
                REQUEST_HTTP_URL);
    }

    /**
     * call
     * @author leibing
     * @createTime 2017/4/11
     * @lastModify 2017/4/11
     * @param phone
     * @param loginName
     * @param timestamp
     * @param sign
     * @param activity
     * @param callback
     * @return
     */
    public void call(String phone, String loginName, String timestamp, String sign,
                     Activity activity, ApiCallback<BaseResponse> callback){
        Call<BaseResponse> mCall =  mApiStore.call(phone,loginName,timestamp,sign,"9","1");
        mCall.enqueue(new CommonApiCallback<BaseResponse>(callback, activity,
                BaseResponse.class, CommonApiCallback.REQUEST_ID_TWO));
    }

    /**
     * @interfaceName: ApiStore
     * @interfaceDescription: 登录模块api接口
     * @author: leibing
     * @createTime: 2016/08/30
     */
    private interface ApiStore {
        @FormUrlEncoded
        @POST("HomePage/CallSend")
        Call<BaseResponse> call(
                @Field("phone") String phone,
                @Field("loginName") String loginName,
                @Field("timestamp") String timestamp,
                @Field("sign") String sign,
                @Field("versionCode") String versionCode,
                @Field("deviceType") String deviceType);
    }
}
