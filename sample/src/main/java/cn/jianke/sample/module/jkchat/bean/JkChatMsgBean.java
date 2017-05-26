package cn.jianke.sample.module.jkchat.bean;

/**
 * @className:  JkChatMsgBean
 * @classDescription: jk chat msg model
 * @author: leibing
 * @createTime: 2017/5/15
 */
public class JkChatMsgBean {
    // 会话类型
    public String Type;
    // 当前消息id
    public String TID;
    // 医生id
    public String StaffSessionID;
    // 用户id
    public String CustomSessionID;
    // 医生名称
    public String StaffName;
    // 消息内容
    public String Msg;
    // web site id
    public String WebSiteId;
    // token
    public String token;
    // open id
    public String openid;
    // ser object
    public String serObject;
    // wx server id
    public String wxServerId;
    // 消息（0表示接收消息，1表示发送消息）
    public String MsgFrom;
    // 消息时间
    public String NowDate;
}
