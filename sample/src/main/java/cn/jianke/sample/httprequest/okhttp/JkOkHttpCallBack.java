package cn.jianke.sample.httprequest.okhttp;

import android.app.Activity;
import cn.jianke.httprequest.httprequest.ApiCallback;
import cn.jianke.httprequest.httprequest.okhttp.CommonOkHttpCallBack;

/**
 * @className:JkOkHttpCallBack
 * @classDescription:okhttp健客回调（用于兼容数据返回）
 * @author: leibing
 * @createTime: 2017/4/16
 */
public class JkOkHttpCallBack extends CommonOkHttpCallBack {

    /**
     * Constructor
     * @author leibing
     * @createTime 2017/4/16
     * @lastModify 2017/4/16
     * @param mCallback
     * @param activity
     * @param typeCls
     * @return
     */
    public JkOkHttpCallBack(ApiCallback mCallback, Activity activity, Class typeCls) {
        super(mCallback, activity, typeCls);
    }

    /**
     * Constructor
     * @author leibing
     * @createTime 2017/4/16
     * @lastModify 2017/4/16
     * @param mCallback
     * @param activity
     * @param typeCls
     * @param requestId
     * @return
     */
    public JkOkHttpCallBack(ApiCallback mCallback, Activity activity,
                            Class typeCls, String requestId) {
        super(mCallback, activity, typeCls, requestId);
    }

    @Override
    public void compatibleData() {

    }
}
