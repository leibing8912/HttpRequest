package cn.jianke.httprequest.module.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import cn.jianke.httprequest.R;
import cn.jianke.httprequest.httprequest.ApiCallback;
import cn.jianke.httprequest.httprequest.api.ApiHistoryToday;
import cn.jianke.httprequest.httprequest.httpresponse.HistoryTodayResponse;
import cn.jianke.httprequest.httprequest.okhttp.JKOkHttpParamKey;
import cn.jianke.httprequest.httprequest.okhttp.JkOkHttpCallBack;
import cn.jianke.httprequest.httprequest.okhttp.OkHttpRequestUtils;
import cn.jianke.httprequest.httprequest.okhttp.RequestUrlManager;
import cn.jianke.httprequest.module.adapter.HistoryTodayAdapter;
import cn.jianke.httprequest.utils.StringUtil;
import static cn.jianke.httprequest.module.activity.MainActivity.NETWORK_FRAMEWORK_TYPE;
import static cn.jianke.httprequest.module.activity.MainActivity.NETWORK_FRAMEWORK_TYPE_OKHTTP;
import static cn.jianke.httprequest.module.activity.MainActivity.NETWORK_FRAMEWORK_TYPE_RETROFIT;

/**
 * @className: HistoryTodayActivity
 * @classDescription: 历史上的今天页面
 * @author: leibing
 * @createTime: 2016/11/10
 */
public class HistoryTodayActivity extends BaseActivity {
    // 日志标识
    private final static String TAG = "HistoryTodayActivity";
    // listView
    private ListView historyTodayLv;
    // 数据源
    private ArrayList<HistoryTodayResponse.ResultList> mData;
    // 适配器
    private HistoryTodayAdapter mAdapter;
    // Api
    private ApiHistoryToday mApiHistoryToday;
    // month
    private String month = "10";
    // day
    private String day = "1";
    // key
    private String key = "2df3bf9577484943b20a59321de0c707";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_today);
        // findview
        historyTodayLv = (ListView) findViewById(R.id.lv_history_today);
        // 初始化数据源
        mData = new ArrayList<>();
        // 适配
        mAdapter = new HistoryTodayAdapter(this, mData);
        historyTodayLv.setAdapter(mAdapter);
        // 根据意图传值请求数据
        requestDataByIntent();
    }

    /**
     * 根据意图传值请求数据
     * @author leibing
     * @createTime 2017/4/11
     * @lastModify 2017/4/11
     * @param
     * @return
     */
    private void requestDataByIntent() {
        String type = getIntent().getStringExtra(NETWORK_FRAMEWORK_TYPE);
        switch (type){
            case NETWORK_FRAMEWORK_TYPE_RETROFIT:
                // retrofit
                requestByRetrofit();
                break;
            case NETWORK_FRAMEWORK_TYPE_OKHTTP:
                // okhttp
                requestByOkhttp();
                break;
            default:
                break;
        }
    }

    /**
     * 通过okhttp请求数据
     * @author leibing
     * @createTime 2017/4/11
     * @lastModify 2017/4/11
     * @param
     * @return
     */
    private void requestByOkhttp(){
        Log.e(TAG, "#okhttp request");
        OkHttpRequestUtils.getInstance().requestByGet(RequestUrlManager.HISTORY_TODAY_REQUEST_URL,
                OkHttpRequestUtils.getInstance().JkRequestParameters(
                        JKOkHttpParamKey.HISTORY_TODAY_PARAM, month, day, key),
                HistoryTodayResponse.class, JkOkHttpCallBack.REQUEST_ID_ONE, this,
                new ApiCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        // 更新Ui
                        updateUI((HistoryTodayResponse) response);
                    }

                    @Override
                    public void onError(String err_msg) {
                        if (StringUtil.isNotEmpty(err_msg))
                        Toast.makeText(HistoryTodayActivity.this, err_msg, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(HistoryTodayActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 通过retrofit请求数据
     * @author leibing
     * @createTime 2017/4/11
     * @lastModify 2017/4/11
     * @param
     * @return
     */
    private void requestByRetrofit(){
        Log.e(TAG, "#retrofit request");
        // 初始化api
        mApiHistoryToday = new ApiHistoryToday();
        // 请求数据
        mApiHistoryToday.getHistoryTodayData(month, day, key,this,
                new ApiCallback<HistoryTodayResponse>() {
                    @Override
                    public void onSuccess(HistoryTodayResponse response) {
                        // 更新Ui
                        updateUI(response);
                    }

                    @Override
                    public void onError(String err_msg) {
                        if (StringUtil.isNotEmpty(err_msg))
                            Toast.makeText(HistoryTodayActivity.this, err_msg, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(HistoryTodayActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 更新UI
     * @author leibing
     * @createTime 2016/11/10
     * @lastModify 2016/11/10
     * @param response
     * @return
     */
    private void updateUI(HistoryTodayResponse response) {
        if (response == null)
            return;
        if (response.result == null || response.result.size() == 0)
            return;
        if (mAdapter == null)
            return;
        // 更新数据源
        mAdapter.updateData(response.result);
    }
}
