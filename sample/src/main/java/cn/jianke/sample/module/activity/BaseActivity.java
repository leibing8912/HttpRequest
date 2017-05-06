package cn.jianke.sample.module.activity;

import android.app.Activity;
import android.content.Intent;
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

    /**
     * 启动目标页
     * @author leibing
     * @createTime 2017/5/6
     * @lastModify 2017/5/6
     * @param targetCls 目标页
     * @return
     */
    public void startTargetActivity(Class targetCls){
        startTargetActivity(targetCls, null);
    }

    /**
     * 启动目标页
     * @author leibing
     * @createTime 2017/5/6
     * @lastModify 2017/5/6
     * @param targetCls 目标页
     * @param bundle 数据
     * @return
     */
    public void startTargetActivity(Class targetCls, Bundle bundle){
        Intent intent = new Intent(this, targetCls);
        if (bundle != null)
            intent.putExtras(bundle);
        startActivity(intent);
    }
}
