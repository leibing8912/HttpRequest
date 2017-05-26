package cn.jianke.sample.module.jkchat.bean;

import java.io.Serializable;

/**
 * @className: JkChatBean
 * @classDescription: 聊天
 * @author: leibing
 * @createTime: 2017/5/11
 */
public class JkChatBean implements Serializable {
    private static final long serialVersionUID = 4187201656825123209L;
    // 消息类型
    public int msgType;
    // 消息方向
    public int msgDirection;
    // 消息发送状态
    public int msgStatus;
    // 药品信息
    public Drug mDrug;
    /**
     * @className: Drug
     * @classDescription: 药品信息
     * @author: leibing
     * @createTime: 2017/5/11
     */
    public static class Drug implements Serializable {
        private static final long serialVersionUID = -4738745632141694144L;
        // 药品名称
        public String drugName = "";
        // 药品图片
        public String drugImgUrl = "";
        // 药品id
        public String drugId = "";
        // 药品链接
        public String drugLink = "";
        // 药品描述
        public String drugDescription = "";
        // 药品价格
        public String drugPrice = "";
    }
    // 接收图片消息
    public ReceiveImgMsg mReceiveImgMsg;

    /**
     * @className: ReceiveImgMsg
     * @classDescription: 接收图片消息
     * @author: leibing
     * @createTime: 2017/5/11
     */
    public static class ReceiveImgMsg implements Serializable {
        private static final long serialVersionUID = -2670233971065293066L;
        // 时间
        public String timeStr = "";
        // 头像url
        public String headPortraitUrl = "";
        // 消息图片
        public String msgImgUrl = "";
        // 宽度
        public int width = 100;
        // 高度
        public int height = 100;
        // 是否需要显示时间
        public boolean isNeedShowTime = false;
    }

    // 接收文本消息
    public ReceiveTxtMsg mReceiveTxtMsg;

    /**
     * @className: ReceiveTxtMsg
     * @classDescription: 接收文本消息
     * @author: leibing
     * @createTime: 2017/5/11
     */
    public static class ReceiveTxtMsg implements Serializable {
        private static final long serialVersionUID = 4728647314567203269L;
        // 时间
        public String timeStr = "";
        // 头像url
        public String headPortraitUrl = "";
        // 消息文本
        public String msgTxt = "";
        // 是否需要显示时间
        public boolean isNeedShowTime = false;
    }

    // 发送药品信息
    public SendDrugMsg mSendDrugMsg;

    /**
     * @className: SendDrugMsg
     * @classDescription: 发送药品消息
     * @author: leibing
     * @createTime: 2017/5/11
     */
    public static class SendDrugMsg implements Serializable {
        private static final long serialVersionUID = -6480488043983515528L;
        // 时间
        public String timeStr = "";
        // 头像url
        public String headPortraitUrl = "";
        // 药品图片
        public String drugImgUrl = "";
        // 药品名称
        public String drugName = "";
        // 药品id
        public String drugId = "";
        // 药品描述
        public String drugDescription = "";
        // 药品链接
        public String drugLink = "";
        // 是否需要显示时间
        public boolean isNeedShowTime = false;
    }

    // 发送图片消息
    public SendImgMsg mSendImgMsg;

    /**
     * @interfaceName: SendImgMsg
     * @interfaceDescription: 发送图片消息
     * @author: leibing
     * @createTime: 2017/5/11
     */
    public static class SendImgMsg implements Serializable {
        private static final long serialVersionUID = -7310033699620980243L;
        // 时间
        public String timeStr = "";
        // 头像url
        public String headPortraitUrl = "";
        // 消息图片
        public String msgImgUrl = "";
        // 宽度
        public int width = 100;
        // 高度
        public int height = 100;
        // 是否需要显示时间
        public boolean isNeedShowTime = false;
    }

    // 发送文本消息
    public SendTxtMsg mSendTxtMsg;

    /**
     * @interfaceName: SendTxtMsg
     * @interfaceDescription: 发送文本消息
     * @author: leibing
     * @createTime: 2017/5/11
     */
    public static class SendTxtMsg implements Serializable {
        private static final long serialVersionUID = 9195757083247813815L;
        // 时间
        public String timeStr = "";
        // 头像url
        public String headPortraitUrl = "";
        // 消息文本
        public String msgTxt = "";
        // 是否需要显示时间
        public boolean isNeedShowTime = false;
    }
}
