package cn.jianke.sample.module.jkchat;

/**
 * @className: JkChatConstant
 * @classDescription: 健客用药咨询常量
 * @author: leibing
 * @createTime: 2017/5/4
 */
public class JkChatConstant {
    // ws url
    public static String WS_URL = "ws://tw.sgz88.com:2019/area=john&staff=&psid=&utype=1&page=1&user=1234567890&website=tw&number=3&eng=0&ftype=android&act=1&refurl=m.jianke.com";
    // 提示栏--提示1
    public final static String PROMPT_LINE_TIP_ONE = "正在为您转接......";
    // 提示栏--提示2
    public final static String PROMPT_LINE_TIP_TWO = "对话已结束，感谢您的咨询！";
    // 提示栏--提示3上半部
    public final static String PROMPT_LINE_TIP_THREE_UPPER_HALF = "对话已建立，您正在和";
    // 提示栏--提示3下半部
    public final static String PROMPT_LINE_TIP_THREE_LOWER_HALF = "药师对话";
    // 提示栏状态--"正在为您转接......"
    public final static int PROMPT_LINE_TIP_ONE_STATUS = 0x20;
    // 提示栏状态--"对话已建立，您正在与xx药师对话"
    public final static int PROMPT_LINE_TIP_TWO_STATUS = 0x21;
    // 提示栏状态--"网络状况不好，请重新尝试！"
    public final static int PROMPT_LINE_TIP_THREE_STATUS = 0x22;
    // 提示栏状态--"对话已结束，感谢您的咨询！"
    public final static int PROMPT_LINE_TIP_FOUR_STATUS = 0x23;
    // 提示栏状态--不需要显示状态栏
    public final static int PROMPT_LINE_TIP_FIVE_STATUS = 0x24;
    // 提示栏布局选择--对话状态提示
    public final static int PROMPT_LINE_SELECT_DIALOG_PROMPT = 0x31;
    // 提示栏布局选择--网络状态差提示
    public final static int PROMPT_LINE_SELECT_POOR_NETWORK = 0x32;
    // 消息操作--删除
    public final static String MSG_ACTION_DEL = "删除";
    // 消息操作--重发
    public final static String MSG_ACTION_RESEND = "重发";
    // 消息类型--文本
    public final static int MSG_TYPE_TXT = 0x10;
    // 消息类型--图片
    public final static int MSG_TYPE_IMG = 0x11;
    // 消息类型--链接
    public final static int MSG_TYPE_LINK = 0x12;
    // 消息类型--药品信息
    public final static int MSG_TYPE_DRUG = 0x13;
    // 消息类型--发送药品消息
    public final static int MSG_TYPE_SEND_DRUG = 0x14;
    // 消息方向--发送消息
    public final static int MSG_DIRECTION_SEND = 0x36;
    // 消息方向--接收消息
    public final static int MSG_DIRECTION_RECEIVE = 0x37;
    // 消息发送状态--正在发送
    public final static int MSG_STATUS_SENDING = 0x50;
    // 消息发送状态--发送成功
    public final static int MSG_STATUS_SEND_SUCCESS = 0x51;
    // 消息发送状态--发送失败
    public final static int MSG_STATUS_SEND_FAIL = 0x52;
    // 评价等级--未评价
    public final static int PRAISE_LEVEL_NO_PRAISE = -1;
    // 评价等级--一星
    public final static int PRAISE_LEVEL_ONE_STAR = 0;
    // 评价等级--二星
    public final static int PRAISE_LEVEL_TWO_STAR = 1;
    // 评价等级--三星
    public final static int PRAISE_LEVEL_THREE_STAR = 2;
    // 评价等级--四星
    public final static int PRAISE_LEVEL_FOUR_STAR = 3;
    // 评价等级--五星
    public final static int PRAISE_LEVEL_FIVE_STAR = 4;
    // 评价等级未选择提示
    public final static String PRAISE_NO_SELECT_TIP = "选择服务星级后才能提交哦~";
    // 底部布局显示--显示聊天输入框
    public final static int BOTTOM_VIEW_SHOW_INPUT = 0x60;
    // 底部布局显示--显示评价页面
    public final static int BOTTOM_VIEW_SHOW_PRAISE = 0x61;
    // 底部布局显示--显示继续咨询
    public final static int BOTTOM_VIEW_SHOW_CONTINUE_CONSULTING = 0x62;
    // 药品详情传值
    public final static String INTENT_DRUG_INFO = "intent_drug_info";
    // 评价成功
    public final static String PRAISE_SUCCESS = "感谢您的评价";
    // 评价失败
    public final static String PRAISE_FAIL = "评价失败";
    // 商务通标题
    public final static String JK_CHAT_TITLE = "jk_chat_title";
    // 请求站点
    public final static String JK_CHAT_WEBSITE_ID = "tw";
    // 未连接提示
    public final static String JK_CHAT_DISCONNECT_TIP = "请连接后再操作";
    // 内存卡不存在
    public final static String SDCARD_NO_EXIST = "内存卡不存在";
    // 输入框为空时请输入文字提示
    public final static String PLEASE_INPUT_TXT_TIP = "请输入文字";
    // 图片选择失败
    public final static String PIC_SELECT_FAIL = "图片选择失败";
    // 使用相册中的图片
    public static final int SELECT_PIC_CODE = 0x10;
    // 使用照相机拍照获取图片
    public static final int TAKE_PHOTO_CODE = 0x11;
    // 继续咨询无网提示
    public static final String CONTINUE_ASK_NO_NETWORK_TIP = "请检查网络后重试~";
    // 进入商品详情页请求码
    public static final int GOTO_GOODS_DETAILS_REQUEST_CODE  = 0x89;
    // 跳转到商品详情页
    public static final String GOTO_GOODS_DETAILS_PAGE = "跳转商品详情页";
}
