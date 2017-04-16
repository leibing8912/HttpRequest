package cn.jianke.httprequest.module.activity;

import android.app.Activity;
import android.os.Bundle;
import cn.jianke.httprequest.module.AppManager;

/**
 * @className: BaseActivity
 * @classDescription: Activity基类
 * @author: leibing
 * @createTime: 2017/4/10
 */
public class BaseActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // activity压栈
        AppManager.getInstance().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        // activity出栈
        AppManager.getInstance().removeActivity(this);
        super.onDestroy();
    }
}
