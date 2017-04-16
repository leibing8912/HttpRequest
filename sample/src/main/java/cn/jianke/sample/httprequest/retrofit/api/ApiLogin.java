package cn.jianke.sample.httprequest.retrofit.api;

import android.app.Activity;
import java.net.URLEncoder;
import cn.jianke.httprequest.BuildConfig;
import cn.jianke.httprequest.httprequest.ApiCallback;
import cn.jianke.httprequest.httprequest.JkApiCallback;
import cn.jianke.httprequest.httprequest.JkApiRequest;
import cn.jianke.sample.httprequest.httpresponse.LoginResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @className: ApiLogin
 * @classDescription: 登陆api
 * @author: leibing
 * @createTime: 2016/8/12
 */
public class ApiLogin {
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
    public ApiLogin() {
        // 初始化api
        mApiStore = JkApiRequest.getInstance().create(ApiStore.class, REQUEST_HTTP_URL);
    }

    /**
     * 登录
     * @author leibing
     * @createTime 2016/08/30
     * @lastModify 2016/08/30
     * @param username 用户名
     * @param password 密码
     * @param activity 页面实例
     * @param callback 回调
     * @return
     */
    public void login(String username, String password,
                      Activity activity, ApiCallback<LoginResponse> callback){
        Call<LoginResponse> mCall =  mApiStore.login(URLEncoder.encode(username), password);
        mCall.enqueue(new JkApiCallback<LoginResponse>(callback, activity,
                LoginResponse.class, JkApiCallback.REQUEST_ID_THREE));
    }

    /**
     * @interfaceName: ApiStore
     * @interfaceDescription: 登录模块api接口
     * @author: leibing
     * @createTime: 2016/08/30
     */
    private interface ApiStore {
        @GET("app/User/login")
        Call<LoginResponse> login(
                @Query("username") String username,
                @Query("userpass") String userpass);
    }
}
