package cn.jianke.httprequest.utils;

/**
 * @className: StringUtil
 * @classDescription: 字符串操作
 * @author: leibing
 * @createTime: 2016/08/30
 */
public class StringUtil {

	/**
	 * 判断是否为null或空字符串
	 * @author leibing
	 * @createTime 2016/08/30
	 * @lastModify 2016/08/30
	 * @param str
	 * @return
	 */
    public static boolean isEmpty(String str) {
        if (str == null || "".equals(str.trim())) {
            return true;
        }
        return false;
    }

	/**
	 * 判断是否不为null或不是空字符串
	 * @author leibing
	 * @createTime 2016/08/30
	 * @lastModify 2016/08/30
	 * @param str
	 * @return
	 */
    public static boolean isNotEmpty(String str){
		if (str == null || str.trim().equals(""))
			return false;
		return true;
    }

	/**
	 * 根据类名获取对象实例
	 * @author leibing
	 * @createTime 2016/08/30
	 * @lastModify 2016/08/30
	 * @param className 类名
	 * @return
	 */
	public static Object getObject(String className){
		Object object = null;
		if(StringUtil.isNotEmpty(className)){
			try {
				object = Class.forName(className).newInstance();
			}catch(ClassNotFoundException cnf) {
			}
			catch(InstantiationException ie) {
			}
			catch(IllegalAccessException ia) {
			}
		}
		return object;
	}
}
