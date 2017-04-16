package cn.jianke.sample.httprequest.retrofit.api;

import android.app.Activity;
import cn.jianke.httprequest.httprequest.ApiCallback;
import cn.jianke.httprequest.httprequest.CommonApiCallback;

/**
 * @className: JkRetrofitCallBack
 * @classDescription: retrofit健客回调（用于兼容数据返回）
 * @author: leibing
 * @createTime: 2017/4/16
 */
public class JkRetrofitCallBack extends CommonApiCallback{

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
    public JkRetrofitCallBack(ApiCallback mCallback, Activity activity,
                              Class typeCls, String requestId) {
        super(mCallback, activity, typeCls, requestId);
    }

    @Override
    public void compatibleData() {
        System.out.println("xxxxxxxxxxxx compatibleData ing ");
    }
}
