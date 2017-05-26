package cn.jianke.sample.module.jkchat.util;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

/**
 * @className: ToastUtils
 * @classDescription: toast utils
 * @author: leibing
 * @createTime: 2017/5/25
 */
public class ToastUtils {

    /**
      * show toast
      * @author leibing
      * @createTime 2017/5/25
      * @lastModify 2017/5/25
      * @param context
      * @param resId
      * @return
      */
    public static void show(Context context, int resId) {
        show(context, context.getResources().getText(resId), Toast.LENGTH_SHORT);
    }

    /**
     * show toast
     * @author leibing
     * @createTime 2017/5/25
     * @lastModify 2017/5/25
     * @param context
     * @param resId
     * @param duration
     * @return
     */
    public static void show(Context context, int resId, int duration) {
        show(context, context.getResources().getText(resId), duration);
    }

    /**
     * show toast
     * @author leibing
     * @createTime 2017/5/25
     * @lastModify 2017/5/25
     * @param context
     * @param text
     * @return
     */
    public static void show(Context context, CharSequence text) {
        show(context, text, Toast.LENGTH_SHORT);
    }

    /**
     * show toast
     * @author leibing
     * @createTime 2017/5/25
     * @lastModify 2017/5/25
     * @param context
     * @param text
     * @param duration
     * @return
     */
    public static void show(Context context, CharSequence text, int duration) {
        Toast.makeText(context, text, duration).show();
    }

    /**
     * show toast
     * @author leibing
     * @createTime 2017/5/25
     * @lastModify 2017/5/25
     * @param context
     * @param resId
     * @param args
     * @return
     */
    public static void show(Context context, int resId, Object... args) {
        show(context, String.format(context.getResources().getString(resId), args), Toast.LENGTH_SHORT);
    }

    /**
     * show toast
     * @author leibing
     * @createTime 2017/5/25
     * @lastModify 2017/5/25
     * @param context
     * @param format
     * @param args
     * @return
     */
    public static void show(Context context, String format, Object... args) {
        show(context, String.format(format, args), Toast.LENGTH_SHORT);
    }

    /**
     * show toast
     * @author leibing
     * @createTime 2017/5/25
     * @lastModify 2017/5/25
     * @param context
     * @param resId
     * @param duration
     * @param args
     * @return
     */
    public static void show(Context context, int resId, int duration, Object... args) {
        show(context, String.format(context.getResources().getString(resId), args), duration);
    }

    /**
     * show toast
     * @author leibing
     * @createTime 2017/5/25
     * @lastModify 2017/5/25
     * @param context
     * @param format
     * @param duration
     * @param args
     * @return
     */
    public static void show(Context context, String format, int duration, Object... args) {
        show(context, String.format(format, args), duration);
    }

    /**
     * show toast
     * @author leibing
     * @createTime 2017/5/25
     * @lastModify 2017/5/25
     * @param context
     * @param view
     * @return
     */
	public static void show(Context context, View view) {
		Toast toast = new Toast(context);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setView(view);
		toast.show();
	}
}
