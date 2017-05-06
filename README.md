# HttpRequest
一个基于 retrofit + okhttp + Gson 封装的网络框架.

目的:简化代码,方便找bug.


## Usage

### retrofit架构
首先需要讲工程中httprequest和utils包整个目录都拷贝到需要集成的工程做为Base包.

接着下来我需要做只需在api和httpresponse包添加api类以及Gson解析后对应的数据类代码如下:

Api类:

```java
package cn.jianke.httprequest.httprequest.api;

import java.net.URLEncoder;
import cn.jianke.httprequest.httprequest.ApiCallback;
import cn.jianke.httprequest.httprequest.CommonApiRequest;
import cn.jianke.httprequest.httprequest.CommonRetrofitCallback;
import cn.jianke.sample.httprequest.httpresponse.LoginResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @className: ApiLogin
 * @classDescription: 登陆api
 * @author: leibing
 * @createTime: 2016/8/12
 */
public class ApiLogin {
    // api
    private ApiStore mApiStore;

    /**
     * Constructor
     * @author leibing
     * @createTime 2016/08/30
     * @lastModify 2016/08/30
     * @param
     * @return
     */
    public ApiLogin() {
        // 初始化api
        mApiStore = JkApiRequest.getInstance().create(ApiStore.class);
    }

    /**
     * 登录
     * @author leibing
     * @createTime 2016/08/30
     * @lastModify 2016/08/30
     * @param username 用户名
     * @param password 密码
     * @param callback 回调
     * @return
     */
    public void Login(String username, String password, ApiCallback<LoginResponse> callback){
        Call<LoginResponse> mCall =  mApiStore.login(URLEncoder.encode(username), password);
        mCall.enqueue(new JkApiCallback<LoginResponse>(callback));
    }

    /**
     * @interfaceName: ApiStore
     * @interfaceDescription: 登录模块api接口
     * @author: leibing
     * @createTime: 2016/08/30
     */
    private interface ApiStore {
        @GET("app/User/Login")
        Call<LoginResponse> login(
                @Query("username") String username,
                @Query("userpass") String userpass);
    }
}

```

对ApiLogin类用法分析:

ApiStore是作为登录模块集合接口,里面写了一个登录的方法login,有两个参数分别为用户名与密码.
回调返回数据类型为LoginResponse,这个接下来再讲,在ApiLogin构造函数中我们对Apistore接口做了
初始化工作,在调用登录方法Login使用了mApiLogin,mCall.enqueue做为异步调用方法,是在子线程中
操作的,收到callback回调后,我用JkApiCallback对此作了包装,为的是封装一些无需在使用时调用的操作,
比如判断收到数据是否为空,如果收到数据为空,此时会对此做为请求错误的回调.


LoginResponse类:

```java
package cn.jianke.httprequest.httprequest.httpresponse;

import java.io.Serializable;

/**
 * @className: LoginResponse
 * @classDescription: 获取登录返回的信息
 * @author: leibing
 * @createTime: 2016/08/30
 */
public class LoginResponse extends BaseResponse implements Serializable{
    // 序列化UID 用于反序列化
    private static final long serialVersionUID = 4863726647304575308L;
    // token
    public String accesstoken;
}


```

对LoginResponse类用法分析：

这个Response类主要用做Gson解析数据后映射到该实例对象.如果我们要做将此对象通过序列化方式缓存的话,我们需要
将此类序列号并且把它的序列化UID写出来.Android Studio如何自动生成序列化UID?

Android Studio自动生成序列化UID操作如下:

* File -> Settings... -> Editor -> Inspections -> Serialization issues -> Serializable class without ‘serialVersionUID‘（选中）.

* 进入实现了Serializable中的类，选中类名，Alt+Enter弹出提示，然后直接导入完成.

### okhttp架构
使用比较简单，请看代码：

```java

    /**
     * 通过okhttp请求数据
     * @author leibing
     * @createTime 2017/4/11
     * @lastModify 2017/4/11
     * @param
     * @return
     */
    private void requestByOkhttp(){
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("month", month);
        params.put("day", day);
        params.put("key", key);
        OkHttpRequestUtils.getInstance().requestByGet(RequestUrlManager.HISTORY_TODAY_REQUEST_URL,
                params, HistoryTodayResponse.class, JkOkHttpCallBack.REQUEST_ID_ONE, this,
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

```

### 2017.04.11
#### retrofit架构

* 将数据解析从JkApiConvertFactory迁移到JkApiCallback中处理；
* 增加对页面弱引用处理、加入Activity堆栈管理判断当前页再回调处理；
* 根据请求标识处理对应数据格式，提高代码可读性，维护性；

#### okhttp架构（新增）
* RequestUrlManager管理请求Url地址；
* OkHttpRequestUtils管理请求服务；
* JkOkHttpCallBack管理数据解析以及回调服务；

### 2017.05.06
* 将retrofit、okhttp基类封装到aar文件
* okhttp添加JkOkHttpUpDownFileUtils文件上传下载封装


### License
Copyright 2016 leibing

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

