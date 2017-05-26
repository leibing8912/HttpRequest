package cn.jianke.sample.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Base64;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Set;
import cn.jianke.httprequest.utils.StringUtil;
import cn.jianke.sample.module.ThreadManager;

/**
 * @className: SpLocalCache
 * @classDescription: 存于xml文件中的本地缓存工具, 该缓存直接存储一个请求返回对象，
 * 该缓存归于UserSahrePreferenceUtil管理，当清除用户缓存方法执行时，此缓存下的数据应全部清除。
 * @author: leibing
 * @createTime: 2016/8/26
 */
public class SpLocalCache<T> {
	// 缓存Key
	private static final String KEY_DATA = "data";
	// 缓存名
	private String cacheName;
	// 自定义Handler,用于将子线程数据更新到UI线程
	private Handler mHandler = new Handler();

	/**
	 * 清除Set中的缓存
	 * @author leibing
	 * @createTime 2016/8/26
	 * @lastModify 2016/8/26
	 * @param context
	 * @param cacheSet 要删除的缓存
	 * @return
	 */
	public static void clear(final Context context, final Set<String> cacheSet){
		ThreadManager.getInstance().getNewCachedThreadPool().execute(new Runnable() {
			@Override
			public void run() {
				synchronized (SpLocalCache.class) {
					if (cacheSet == null)
						return;
					for (String cacheName : cacheSet) {
						SharedPreferences spLc = context.getSharedPreferences(
								cacheName,
								Context.MODE_PRIVATE
						);
						spLc.edit().clear().commit();
					}
				}
			}
		});
	}

	/**
	 * 构造函数(用于普通对象缓存)
	 * @author leibing
	 * @createTime 2016/08/26
	 * @lastModify 2016/08/26
	 * @param forWhich 类名 缓存对象类名
	 * @return
	 */
	public SpLocalCache(Class<T> forWhich){
		cacheName = forWhich.getName();
	}

	/**
	 * 构造函数(用于列表缓存)
	 * @author leibing
	 * @createTime 2016/08/26
	 * @lastModify 2016/08/26
	 * @param forWhich     缓存列表类名
	 * @param modelClass  列表中数据类名
	 * @return
	 */
	public SpLocalCache(Class<T> forWhich, Class modelClass){
		cacheName = forWhich.getName() + "_" + modelClass.getName();
	}

	/**
	 * Save the data to the local cache.
	 * @author leibing
	 * @createTime 2016/08/26
	 * @lastModify 2016/08/26
	 * @param context 上下文
	 * @param data 数据源
	 * @return
	 */
	public void save(final Context context, final T data){
		ThreadManager.getInstance().getNewCachedThreadPool().execute(new Runnable() {
			@Override
			public void run() {
				synchronized (SpLocalCache.class) {
					final SharedPreferences spLc = context.getSharedPreferences(
							cacheName,
							Context.MODE_PRIVATE
					);
					String strData = base64Encode(data);
					if (strData != null) {
						spLc.edit()
								.putString(KEY_DATA, base64Encode(data))
								.commit();
					}
					SharePreferenceUtil.getInstance(context).setSpLocalCache(cacheName);
				}
			}
		});
	}

	/**
	 * Read the data from the local cache
	 * @author leibing
	 * @createTime 2016/08/26
	 * @lastModify 2016/08/26
	 * @param context 上下文
	 * @param localCacheCallBack 回调
	 * @return
	 */
	public void read(final Context context, final LocalCacheCallBack localCacheCallBack){
		ThreadManager.getInstance().getNewCachedThreadPool().execute(new Runnable() {
			@Override
			public void run() {
				synchronized (SpLocalCache.class) {
					SharedPreferences spLc = context.getSharedPreferences(
							cacheName,
							Context.MODE_PRIVATE
					);
					final String strData = spLc.getString(KEY_DATA, null);
					if (!StringUtil.isEmpty(strData)) {
						final Object obj = base64Decode(strData);
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								if (localCacheCallBack != null)
									localCacheCallBack.readCacheComplete(obj);
							}
						});
					}else {
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								if (localCacheCallBack != null)
									localCacheCallBack.readCacheComplete(null);
							}
						});
					}
				}
			}
		});
	}

	/**
	 * 读取Cache的同步方法
	 * @author leibing
	 * @createTime 2016/4/1
	 * @lastModify 2016/4/1
	 * @param context
	 * @return
	 */
	public Object read(final Context context){
		SharedPreferences spLc = context.getSharedPreferences(
				cacheName,
				Context.MODE_PRIVATE
		);
		String strData = spLc.getString(KEY_DATA, null);
		Object obj = base64Decode(strData);
		return obj;
	}

	/**
	 * clear the data to the local cache
	 * @author leibing
	 * @createTime 2016/08/26
	 * @lastModify 2016/08/26
	 * @param context 上下文
	 * @return
	 */
	public void clear(final Context context) {
		ThreadManager.getInstance().getNewCachedThreadPool().execute(new Runnable() {
			@Override
			public void run() {
				synchronized (SpLocalCache.class) {
					SharedPreferences spLc = context.getSharedPreferences(
							cacheName,
							Context.MODE_PRIVATE
					);
					spLc.edit().putString(KEY_DATA, "").commit();
				}
			}
		});
	}

	/**
	 * 数据base64Encode
	 * @author leibing
	 * @createTime 2016/08/26
	 * @lastModify 2016/08/26
	 * @param data 数据
	 * @return
	 */
	private String base64Encode(T data){
		String result = null;
		try {
			byte[] dataByte = getBytesFromObject(data);
			if(dataByte != null)
				result = Base64.encodeToString(dataByte, Base64.DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 数据base64Decode
	 * @author leibing
	 * @createTime 2016/08/26
	 * @lastModify 2016/08/26
	 * @param strData 数据
	 * @return
	 */
	private static Object base64Decode(String strData){
		Object result = null;
		try {
			byte[] dataByte = Base64.decode(strData, Base64.DEFAULT);
			result = getObjectFromBytes(dataByte);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 对象转字节数组
	 * @author leibing
	 * @createTime 2016/08/26
	 * @lastModify 2016/08/26
	 * @param obj 对象
	 * @return
	 */
	private byte[] getBytesFromObject(T obj) throws Exception {
		if (obj == null){
			return null;
		}
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		ObjectOutputStream oo = new ObjectOutputStream(bo);
		oo.writeObject(obj);
		return bo.toByteArray();
	}

	/**
	 * 字符数组转对象
	 * @author leibing
	 * @createTime 2016/08/26
	 * @lastModify 2016/08/26
	 * @param objBytes 对象字节数组
	 * @return
	 */
	private static Object getObjectFromBytes(byte[] objBytes) throws Exception {
		if (objBytes == null || objBytes.length == 0) {
			return null;
		}
		ByteArrayInputStream bi = new ByteArrayInputStream(objBytes);
		ObjectInputStream oi = new ObjectInputStream(bi);
		return oi.readObject();
	}

	/**
	 * @interfaceName: LocalCacheCallBack
	 * @interfaceDescription: 本地缓存回调，用于从子线程中拿缓存更新到UI线程
	 * @author: leibing
	 * @createTime: 2016/08/26
	 */
	public interface LocalCacheCallBack{
		// 读取缓存完成
		void readCacheComplete(Object obj);
	}
}
