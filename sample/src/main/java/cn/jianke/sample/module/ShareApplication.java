package cn.jianke.sample.module;

import android.app.Application;

/**
 * @className: ShareApplication
 * @classDescription: 应用实例
 * @author: leibing
 * @createTime: 2017/5/25
 */
public class ShareApplication extends Application {
	// sington
	private static Application mInstance;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
	}

	/**
	  * get sington
	  * @author leibing
	  * @createTime 2017/5/25
	  * @lastModify 2017/5/25
	  * @param
	  * @return
	  */
	public static Application getInstance(){
		if (mInstance == null){
			synchronized (ShareApplication.class){
				if (mInstance == null)
					mInstance = new ShareApplication();
			}
		}
		return mInstance;
	}
}
