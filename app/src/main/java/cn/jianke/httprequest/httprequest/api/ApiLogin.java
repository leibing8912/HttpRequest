package cn.jianke.httprequest.httprequest.api;

import java.net.URLEncoder;
import cn.jianke.httprequest.httprequest.ApiCallback;
import cn.jianke.httprequest.httprequest.JkApiRequest;
import cn.jianke.httprequest.httprequest.JkApiCallback;
import cn.jianke.httprequest.httprequest.httpresponse.LoginResponse;
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
        mApiStore = JkApiRequest.getInstance().create(ApiStore.class);
    }

    /**
     * 登录
     * @author leibing
     * @createTime 2016/08/30
     * @lastModify 2016/08/30
     * @param username 用户名
     * @param password 密码
     * @param callback 回调
     * @return
     */
    public void Login(String username, String password, ApiCallback<LoginResponse> callback){
        Call<LoginResponse> mCall =  mApiStore.login(URLEncoder.encode(username), password);
        mCall.enqueue(new JkApiCallback<LoginResponse>(callback));
    }

    /**
     * @interfaceName: ApiStore
     * @interfaceDescription: 登录模块api接口
     * @author: leibing
     * @createTime: 2016/08/30
     */
    private interface ApiStore {
        @GET("app/User/Login")
        Call<LoginResponse> login(
                @Query("username") String username,
                @Query("userpass") String userpass);
    }
}
