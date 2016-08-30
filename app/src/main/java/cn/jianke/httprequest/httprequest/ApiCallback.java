package cn.jianke.httprequest.httprequest;

/**
 * @className: ApiCallback
 * @classDescription: Api回调
 * @author: leibing
 * @createTime: 2016/08/30
 */
public interface ApiCallback<T> {
    // 请求数据成功
    void onSuccess(T response);
    // 请求数据错误
    void onError(String err_msg);
    // 网络请求失败
    void onFailure();
}
