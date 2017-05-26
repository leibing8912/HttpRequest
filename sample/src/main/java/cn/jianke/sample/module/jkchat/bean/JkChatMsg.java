package cn.jianke.sample.module.jkchat.bean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.text.SimpleDateFormat;
import cn.jianke.httprequest.utils.StringUtil;
import cn.jianke.sample.module.jkchat.JkChatConstant;
import cn.jianke.sample.module.jkchat.util.JsonUtil;

/**
 * @className: JkChatMsg
 * @classDescription: 构造符合jk商务通聊天消息的数据类型
 * @author: leibing
 * @createTime: 2017/5/15
 */
public class JkChatMsg {

    /**
      * 获取当前时间
      * @author leibing
      * @createTime 2017/5/15
      * @lastModify 2017/5/15
      * @param
      * @return
      */
    public static String getCurrentDate(){
        SimpleDateFormat sDateFormat   =   new SimpleDateFormat("yyyy-MM-dd   hh:mm:ss");
        String date   =   sDateFormat.format(new   java.util.Date());
        return date;
    }

    /**
      * 发送消息--文本
      * @author leibing
      * @createTime 2017/5/15
      * @lastModify 2017/5/15
      * @param mJkChatMsgBean
      * @param txt
      * @return
      */
    public static String sendTxtMsg(JkChatMsgBean mJkChatMsgBean, String txt){
        if (StringUtil.isNotEmpty(txt)
                && mJkChatMsgBean != null){
            String msg = "<FONT size=2 face=宋体>" + txt + "</FONT>";
            mJkChatMsgBean.Msg = msg;
            mJkChatMsgBean.MsgFrom = "1";
            mJkChatMsgBean.Type = "正常消息";
            mJkChatMsgBean.NowDate = getCurrentDate();
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
    public static String sendLinkMsg(JkChatMsgBean mJkChatMsgBean, String link){
        if (StringUtil.isNotEmpty(link)
                && mJkChatMsgBean != null){
            String msg = "<FONT size=2 face=宋体><a  href=" +
                    link + "  target=_blank>"+ link +"</a></FONT><FONT size=2 face=宋体></FONT>";
            mJkChatMsgBean.Msg = msg;
            mJkChatMsgBean.MsgFrom = "1";
            mJkChatMsgBean.Type = "正常消息";
            mJkChatMsgBean.NowDate = getCurrentDate();
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
      * @param width
      * @param height
      * @return
      */
    public static String sendImgMsg(JkChatMsgBean mJkChatMsgBean, String imgUrl,
                                    int width, int height){
        if (StringUtil.isNotEmpty(imgUrl)
                && mJkChatMsgBean != null){
            String msg = "<IMG style=heigth: 24px: border:0px src="
                    + imgUrl + " width="+ width
                    +" height="+ height +">";
            mJkChatMsgBean.Msg = msg;
            mJkChatMsgBean.MsgFrom = "1";
            mJkChatMsgBean.Type = "正常消息";
            mJkChatMsgBean.NowDate = getCurrentDate();
            return JsonUtil.toJson(mJkChatMsgBean);
        }
        return null;
    }

    /**
      * 接收消息
      * @author leibing
      * @createTime 2017/5/15
      * @lastModify 2017/5/15
      * @param mListener 消息回调
      * @return
      */
    public static void receiveMsg(String msg, JkChatMsgListener mListener){
        if (mListener == null)
            return;
        Document content = Jsoup.parse(msg);
        // 解析文本消息
        Elements fontElement = content.getElementsByTag("font");
        if (fontElement != null
                && StringUtil.isNotEmpty(fontElement.text())){
            // 解析链接消息
            for(Element links : fontElement){
                String link  = links.select("a").attr("href").trim();
                String title = links.getElementsByTag("a").text();
                if (StringUtil.isNotEmpty(title)
                        && StringUtil.isNotEmpty(link)){
                    // 链接
                    mListener.msgCallBack(link, JkChatConstant.MSG_TYPE_LINK,0,0);
                    return;
                }
            }
            // 文本
            mListener.msgCallBack(fontElement.text(), JkChatConstant.MSG_TYPE_TXT, 0, 0);
            return;
        }
        // 解析链接消息
        Elements linkElement = content.getElementsByTag("a");
        String link  = linkElement.attr("href").trim();
        String title = linkElement.text();
        if (StringUtil.isNotEmpty(title)
                && StringUtil.isNotEmpty(link)){
            // 链接
            mListener.msgCallBack(link, JkChatConstant.MSG_TYPE_LINK,0,0);
            return;
        }
        // 解析图片消息
        Elements imgElement = content.getElementsByTag("img");
        if (imgElement != null
                && StringUtil.isNotEmpty(imgElement.attr("src"))){
            int width = 0;
            int height = 0;
            if (StringUtil.strIsNum(imgElement.attr("width")))
                width = Integer.parseInt(imgElement.attr("width"));
            if (StringUtil.strIsNum(imgElement.attr("height")))
                height = Integer.parseInt(imgElement.attr("height"));
            // 图片
            mListener.msgCallBack(imgElement.attr("src"), JkChatConstant.MSG_TYPE_IMG, width, height);
            return;
        }
    }

    /**
     * @interfaceName: JkChatMsgListener
     * @interfaceDescription: 消息回调
     * @author: leibing
     * @createTime: 2017/5/15
     */
    public interface JkChatMsgListener{
        void msgCallBack(String msg, int msgType, int width, int height);
    }
}
