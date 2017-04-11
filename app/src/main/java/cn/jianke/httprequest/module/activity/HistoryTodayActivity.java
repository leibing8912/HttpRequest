package cn.jianke.httprequest.module.activity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import cn.jianke.httprequest.R;
import cn.jianke.httprequest.httprequest.ApiCallback;
import cn.jianke.httprequest.httprequest.api.ApiHistoryToday;
import cn.jianke.httprequest.httprequest.httpresponse.HistoryTodayResponse;
import cn.jianke.httprequest.module.adapter.HistoryTodayAdapter;

/**
 * @className: HistoryTodayActivity
 * @classDescription: 历史上的今天页面
 * @author: leibing
 * @createTime: 2016/11/10
 */
public class HistoryTodayActivity extends BaseActivity {
    // listView
    private ListView historyTodayLv;
    // 数据源
    private ArrayList<HistoryTodayResponse.ResultList> mData;
    // 适配器
    private HistoryTodayAdapter mAdapter;
    // Api
    private ApiHistoryToday mApiHistoryToday;

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
        // 初始化api
        mApiHistoryToday = new ApiHistoryToday();
        // 请求数据
        mApiHistoryToday.getHistoryTodayData("10", "1", "2df3bf9577484943b20a59321de0c707",this,
                new ApiCallback<HistoryTodayResponse>() {
            @Override
            public void onSuccess(HistoryTodayResponse response) {
                // 更新Ui
                updateUI(response);
            }

            @Override
            public void onError(String err_msg) {
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
