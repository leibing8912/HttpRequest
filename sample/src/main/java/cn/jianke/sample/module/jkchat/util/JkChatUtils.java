package cn.jianke.sample.module.jkchat.util;

import java.text.DecimalFormat;

/**
 * @className: JkChatUtils
 * @classDescription: 健客聊天工具
 * @author: leibing
 * @createTime: 2017/5/11
 */
public class JkChatUtils {

    /**
     * 保留两位小数
     * @author leibing
     * @createTime 2017/3/13
     * @lastModify 2017/3/13
     * @param num
     * @return
     */
    public static String doubleTwoDecimal(Double num){
        DecimalFormat df  = new DecimalFormat("######0.00");
        return df.format(num);
    }
}
