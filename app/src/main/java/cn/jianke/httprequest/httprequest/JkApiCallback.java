package cn.jianke.httprequest.httprequest;

import cn.jianke.httprequest.httprequest.httpresponse.BaseResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @className: JkApiCallback
 * @classDescription: 健客网统一Api回调
 * @author: leibing
 * @createTime: 2016/08/30
 */
public class JkApiCallback<T> implements Callback <T>{
    // 回调
    private ApiCallback<T> mCallback;

    /**
     * Constructor
     * @author leibing
     * @createTime 2016/08/30
     * @lastModify 2016/08/30
     * @param mCallback 回调
     * @return
     */
    public JkApiCallback(ApiCallback<T> mCallback){
        this.mCallback = mCallback;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (mCallback == null){
            throw new NullPointerException("mCallback == null");
        }
        if (response != null && response.body() != null){
            if (((BaseResponse)response.body()).isSuccess()){
                mCallback.onSuccess((T)response.body());
            }else {
                mCallback.onError(((BaseResponse) response.body()).getErrormsg());
            }
        }else {
            mCallback.onFailure();
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (mCallback == null){
            throw new NullPointerException("mCallback == null");
        }
        mCallback.onFailure();
    }
}
