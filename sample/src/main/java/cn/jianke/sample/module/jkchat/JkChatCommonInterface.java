package cn.jianke.sample.module.jkchat;

import java.util.ArrayList;

/**
 * @interfaceName: JkChatCommonInterface
 * @interfaceDescription: 健客用药咨询（通用接口）
 * @author: leibing
 * @createTime: 2017/5/4
 */
public interface JkChatCommonInterface {
    // 更新ui
    void updateUI(Object object);
    // 评价
    void jkChatPraise(boolean isSuccess);
    // set data
    void setData(ArrayList data);
}
