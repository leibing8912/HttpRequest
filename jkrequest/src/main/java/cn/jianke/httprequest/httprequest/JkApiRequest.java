package cn.jianke.httprequest.httprequest;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @className:JkApiRequest
 * @classDescription:网络请求
 * @author: leibing
 * @createTime: 2016/8/30
 */
public class JkApiRequest {
    // sington
    private static JkApiRequest instance;
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
    private JkApiRequest(){
    }

    /**
     * sington
     * @author leibing
     * @createTime 2016/08/30
     * @lastModify 2016/08/30
     * @param
     * @return
     */
    public static JkApiRequest getInstance(){
        if (instance == null){
            instance = new JkApiRequest();
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
                .addConverterFactory(JkApiConvertFactory.create())
                .client(client)
                .build();
        return retrofit.create(service);
    }
}
