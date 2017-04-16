package cn.jianke.httprequest.httprequest;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @className:CommonApiRequest
 * @classDescription: retrofit api request
 * @author: leibing
 * @createTime: 2017/4/16
 */
public class CommonApiRequest {
    // sington
    private static CommonApiRequest instance;
    // Retrofit object
    private Retrofit retrofit;

    /**
     * Constructor
     * @author leibing
     * @createTime 2016/08/30
     * @lastModify 2016/08/30
     * @param
     * @return
     */
    private CommonApiRequest(){
    }

    /**
     * sington
     * @author leibing
     * @createTime 2016/08/30
     * @lastModify 2016/08/30
     * @param
     * @return
     */
    public static CommonApiRequest getInstance(){
        if (instance == null){
            instance = new CommonApiRequest();
        }
        return instance;
    }

    /**
     * create api instance
     * @author leibing
     * @createTime 2016/08/30
     * @lastModify 2016/08/30
     * @param service api class
     * @return
     */
    public <T> T create(Class<T> service, String baseUrl) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new OkHttpInterceptor())
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(CommonApiConvertFactory.create())
                .client(client)
                .build();
        return retrofit.create(service);
    }
}
