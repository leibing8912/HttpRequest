package cn.jianke.sample.httprequest.okhttp.websocket.bean;

import cn.jianke.httprequest.utils.StringUtil;
import cn.jianke.sample.httprequest.okhttp.websocket.util.JsonUtil;

/**
 * @className: JkChatSendMsg
 * @classDescription: 构造符合jk商务通聊天消息的数据类型
 * @author: leibing
 * @createTime: 2017/5/15
 */
public class JkChatSendMsg {

    /**
      * 发送消息--文本
      * @author leibing
      * @createTime 2017/5/15
      * @lastModify 2017/5/15
      * @param mJkChatMsgBean
      * @param txt
      * @return
      */
    public static String sendTxtMsg(JkChatMsgBean mJkChatMsgBean,String txt){
        if (StringUtil.isNotEmpty(txt)
                && mJkChatMsgBean != null){
            String msg = "<FONT size=2 face=宋体>" + txt + "</FONT>";
            mJkChatMsgBean.Msg = msg;
            mJkChatMsgBean.MsgFrom = "1";
            mJkChatMsgBean.Type = "正常消息";
            return JsonUtil.toJson(mJkChatMsgBean);
        }
        return null;
    }

    /**
     * 发送消息--链接
     * @author leibing
     * @createTime 2017/5/15
     * @lastModify 2017/5/15
     * @param mJkChatMsgBean
     * @param link
     * @return
     */
    public static String sendLinkMsg(JkChatMsgBean mJkChatMsgBean,String link){
        if (StringUtil.isNotEmpty(link)
                && mJkChatMsgBean != null){
            String msg = "<FONT size=2 face=宋体><a  href=" +
                    link + "  target=_blank>"+ link +"</a></FONT><FONT size=2 face=宋体></FONT>";
            mJkChatMsgBean.Msg = msg;
            mJkChatMsgBean.MsgFrom = "1";
            mJkChatMsgBean.Type = "正常消息";
            return JsonUtil.toJson(mJkChatMsgBean);
        }
        return null;
    }

    /**
      * 发送消息--图片
      * @author leibing
      * @createTime 2017/5/15
      * @lastModify 2017/5/15
      * @param mJkChatMsgBean
      * @param imgUrl
      * @return
      */
    public static String sendImgMsg(JkChatMsgBean mJkChatMsgBean,String imgUrl){
        if (StringUtil.isNotEmpty(imgUrl)
                && mJkChatMsgBean != null){
            String msg = "<IMG style=heigth: 24px: border:0px src="
                    + imgUrl + " width=120 height=120>";
            mJkChatMsgBean.Msg = msg;
            mJkChatMsgBean.MsgFrom = "1";
            mJkChatMsgBean.Type = "正常消息";
            return JsonUtil.toJson(mJkChatMsgBean);
        }
        return null;
    }
}
