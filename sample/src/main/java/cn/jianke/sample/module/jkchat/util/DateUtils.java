package cn.jianke.sample.module.jkchat.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @className: DateUtils
 * @classDescription: 时间工具类
 * @author: leibing
 * @createTime: 2017/5/25
 */
public class DateUtils {
	// 时间格式
	public static String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/**
	  *  时间转date
	  * @author leibing
	  * @createTime 2017/5/18
	  * @lastModify 2017/5/18
	  * @param strTime
	  * @param formatType
	  * @return
	  */
	public static Date stringToDate(String strTime, String formatType)
			throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(formatType);
		Date date = null;
		date = formatter.parse(strTime);
		return date;
	}

	/**
	  * 时间字符串转yyyy-MM-dd HH:mm:ss格式
	  * @author leibing
	  * @createTime 2017/5/22
	  * @lastModify 2017/5/22
	  * @param strTime
	  * @return
	  */
	public static String stringToStr(String strTime) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(TIME_FORMAT);
		Date date = formatter.parse(strTime);
		return dateToString(date, TIME_FORMAT);
	}

	/**
	  * string转long
	  * @author leibing
	  * @createTime 2017/5/18
	  * @lastModify 2017/5/18
	  * @param strTime
	  * @param formatType
	  * @return
	  */
	public static long stringToLong(String strTime, String formatType)
			throws ParseException {
		Date date = stringToDate(strTime, formatType); // String类型转成date类型
		if (date == null) {
			return 0;
		} else {
			long currentTime = dateToLong(date); // date类型转成long类型
			return currentTime;
		}
	}

	/**
	  * date转string
	  * @author leibing
	  * @createTime 2017/5/18
	  * @lastModify 2017/5/18
	  * @param data
	  * @param formatType
	  * @return
	  */
	public static String dateToString(Date data, String formatType) {
		return new SimpleDateFormat(formatType).format(data);
	}

	/**
	  * long转date
	  * @author leibing
	  * @createTime 2017/5/18
	  * @lastModify 2017/5/18
	  * @param currentTime
	  * @param formatType
	  * @return
	  */
	public static Date longToDate(long currentTime, String formatType)
			throws ParseException {
		Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
		String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
		Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
		return date;
	}

	/**
	  * date转long
	  * @author leibing
	  * @createTime 2017/5/18
	  * @lastModify 2017/5/18
	  * @param date
	  * @return
	  */
	public static long dateToLong(Date date) {
		return date.getTime();
	}
}