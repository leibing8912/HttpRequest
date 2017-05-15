package cn.jianke.sample.httprequest.okhttp.websocket.bean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.text.SimpleDateFormat;
import cn.jianke.httprequest.utils.StringUtil;
import cn.jianke.sample.httprequest.okhttp.websocket.util.JsonUtil;

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
    private static String getCurrentDate(){
        SimpleDateFormat   sDateFormat   =   new SimpleDateFormat("yyyy-MM-dd   hh:mm:ss");
        String   date   =   sDateFormat.format(new   java.util.Date());
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
    public static String sendTxtMsg(JkChatMsgBean mJkChatMsgBean,String txt){
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
    public static String sendLinkMsg(JkChatMsgBean mJkChatMsgBean,String link){
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
      * @param msg
      * @return
      */
    public static String receiveMsg(String msg){
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
                    return link;
                }
            }
            return fontElement.text();
        }
        // 解析图片消息
        Elements imgElement = content.getElementsByTag("img");
        if (imgElement != null
                && StringUtil.isNotEmpty(imgElement.attr("src"))){
            return imgElement.attr("src");
        }
        return null;
    }
}
