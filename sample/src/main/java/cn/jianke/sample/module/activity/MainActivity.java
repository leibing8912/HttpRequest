package cn.jianke.sample.module.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import cn.jianke.httprequest.httprequest.ApiCallback;
import cn.jianke.sample.R;
import cn.jianke.sample.httprequest.okhttp.JkOkHttpUpDownFileUtils;
import cn.jianke.sample.httprequest.okhttp.UpLoadFileCallBack;
import cn.jianke.sample.httprequest.retrofit.api.ApiLogin;
import cn.jianke.sample.httprequest.httpresponse.LoginResponse;

/**
 * @className: MainActivity
 * @classDescription: 首页
 * @author: leibing
 * @createTime: 2016/08/30
 */
public class MainActivity extends BaseActivity implements View.OnClickListener{
    // 网络框架类型
    public final static String NETWORK_FRAMEWORK_TYPE = "network_framework_type";
    // 网络框架类型--retrofit
    public final static String NETWORK_FRAMEWORK_TYPE_RETROFIT = "network_framework_type_retrofit";
    // 网络框架类型--okhttp
    public final static String NETWORK_FRAMEWORK_TYPE_OKHTTP = "network_framework_type_okhttp";
    // 用户名
    private final static String USERNAME = "18818917198";
    // 密码
    private final static String PASSWORD = "123456";
    // 下载图片
    private final static String downloadUrl = "https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1494057730&di=9ffa30b6b828bd57c6005889d02bbfaf&src=http://img2015.zdface.com/20160711/28af37919541e977a4434f6e10719a0f.jpg";
    // 登录信息显示
    private TextView mLoginMsgTv;
    // Api
    private ApiLogin mApiLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // findView
        mLoginMsgTv = (TextView) findViewById(R.id.tv_show_login_msg);
        // 初始化ApiLogin
        mApiLogin = new ApiLogin();
        // onClick
        findViewById(R.id.btn_request_login_msg).setOnClickListener(this);
        // 历史上的今天(retrofit)
        findViewById(R.id.btn_history_today_retrofit).setOnClickListener(this);
        // 历史上的今天（okhttp）
        findViewById(R.id.btn_history_today_okhttp).setOnClickListener(this);
        // 下载文件
        findViewById(R.id.btn_download_file).setOnClickListener(this);
        // websocket chat
        findViewById(R.id.btn_websocket).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()){
            case R.id.btn_request_login_msg:
                // 请求登录信息
                mApiLogin.login(USERNAME, PASSWORD, MainActivity.this,
                        new ApiCallback<LoginResponse>() {
                            @Override
                            public void onSuccess(LoginResponse response) {
                                mLoginMsgTv.setText("login accesstoken : \n" + response.accesstoken);
                            }

                            @Override
                            public void onError(String err_msg) {
                                mLoginMsgTv.setText("error msg : " + err_msg);
                            }

                            @Override
                            public void onFailure() {
                                Toast.makeText(MainActivity.this, "网络不给力,请检查!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                break;
            case R.id.btn_history_today_retrofit:
                // 历史上的今天(retrofit)
                bundle.putString(NETWORK_FRAMEWORK_TYPE, NETWORK_FRAMEWORK_TYPE_RETROFIT);
                startTargetActivity(HistoryTodayActivity.class, bundle);
                break;
            case R.id.btn_history_today_okhttp:
                // 历史上的今天（okhttp）
                bundle.putString(NETWORK_FRAMEWORK_TYPE, NETWORK_FRAMEWORK_TYPE_OKHTTP);
                startTargetActivity(HistoryTodayActivity.class, bundle);
                break;
            case R.id.btn_download_file:
                // 下载文件
                JkOkHttpUpDownFileUtils.getInstance().downLoadFile(downloadUrl,
                        JkOkHttpUpDownFileUtils.getInstance().getSDPath(), new UpLoadFileCallBack() {
                            @Override
                            public void onSuccess(String data) {
                                System.out.println("ddddddd onSuccess data = " + data);
                            }

                            @Override
                            public void onFail(String error) {
                                System.out.println("ddddddd onFail error = " + error);
                            }

                            @Override
                            public void onException(Exception e) {
                                System.out.println("ddddddd onException e = " + e.getMessage());
                            }

                            @Override
                            public void onProgress(long total, long current) {
                                System.out.println("dddddddd onProgress total = "
                                        + total + "#current = " + current);
                            }
                        });
                break;
            case R.id.btn_websocket:
                // websocket chat
                startTargetActivity(JkChatActivity.class, null);
                break;
            default:
                break;
        }
    }
}
