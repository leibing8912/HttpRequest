package cn.jianke.sample.httprequest.okhttp;

import java.security.MessageDigest;
/**
 * @className: MD5Util
 * @classDescription: md5工具
 * @author: leibing
 * @createTime: 2017/5/6
 */
public class MD5Util {
	// 字符数组
	private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	/**
	 * 字节数组转字符串
	 * @author leibing
	 * @createTime 2017/5/6
	 * @lastModify 2017/5/6
	 * @param b 字节数组
	 * @return
	 */
	private static String byteArrayToHexString(byte b[]) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++)
			resultSb.append(byteToHexString(b[i]));
		return resultSb.toString();
	}

	/**
	 * 字节转字符串
	 * @author leibing
	 * @createTime 2017/5/6
	 * @lastModify 2017/5/6
	 * @param b 字节
	 * @return
	 */
	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n += 256;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	/**
	 * md5 encode
	 * @author leibing
	 * @createTime 2017/5/6
	 * @lastModify 2017/5/6
	 * @param origin 原字符串
	 * @param charsetname 字符格式
	 * @return
	 */
	public static String md5Encode(String origin, String charsetname) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			if (charsetname == null || "".equals(charsetname))
				resultString = byteArrayToHexString(md.digest(resultString
						.getBytes()));
			else
				resultString = byteArrayToHexString(md.digest(resultString
						.getBytes(charsetname)));
		} catch (Exception exception) {
		}
		return resultString;
	}
}
