package cn.jianke.sample.module.jkchat;

import com.google.gson.Gson;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import cn.jianke.httprequest.httprequest.ApiCallback;
import cn.jianke.httprequest.utils.StringUtil;
import cn.jianke.sample.cache.ListCache;
import cn.jianke.sample.cache.SpLocalCache;
import cn.jianke.sample.httprequest.httpresponse.JkChatHistoryMsgResponse;
import cn.jianke.sample.httprequest.httpresponse.JkChatPraiseResponse;
import cn.jianke.sample.httprequest.okhttp.JKOkHttpParamKey;
import cn.jianke.sample.httprequest.okhttp.OkHttpRequestUtils;
import cn.jianke.sample.httprequest.okhttp.RequestUrlManager;
import cn.jianke.sample.module.ShareApplication;
import cn.jianke.sample.module.jkchat.bean.JkChatBean;
import cn.jianke.sample.module.jkchat.bean.JkChatMsgBean;
import static cn.jianke.sample.module.jkchat.JkChatConstant.JK_CHAT_WEBSITE_ID;

/**
 * @className: JkChatModel
 * @classDescription: 健客商务通（数据层）
 * @author: leibing
 * @createTime: 2017/5/4
 */
public class JkChatModel {
    // activity weak refer
    private WeakReference<JkChatActivity> mActivityWeakRef;
    // jk chat data listener
    private ModelListener mModelListener;
    // 聊天缓存
    private SpLocalCache<ListCache> lSpLocalCache;

    /**
     * Constructor
     * @author leibing
     * @createTime 2017/5/4
     * @lastModify 2017/5/4
     * @param mJkChatActivity activity refer
     * @param mModelListener jk chat data listener
     * @return
     */
    public JkChatModel(JkChatActivity mJkChatActivity, ModelListener mModelListener){
        this.mActivityWeakRef = new WeakReference<JkChatActivity>(mJkChatActivity);
        this.mModelListener = mModelListener;
        // 读取缓存数据
        readCacheData();
    }

    /**
      * 读取缓存数据
      * @author leibing
      * @createTime 2017/5/18
      * @lastModify 2017/5/18
      * @param
      * @return
      */
    public void readCacheData(){
        lSpLocalCache = new SpLocalCache<>(ListCache.class, JkChatBean.class);
        lSpLocalCache.read(ShareApplication.getInstance(), new SpLocalCache.LocalCacheCallBack() {
            @Override
            public void readCacheComplete(Object obj) {
                if (obj != null){
                    ListCache<JkChatBean> mReadCache = (ListCache<JkChatBean>) obj;
                    if (mReadCache != null){
                        ArrayList<JkChatBean> modelArrayList = mReadCache.getObjList();
                        if (modelArrayList != null
                                && modelArrayList.size() != 0){
                            if (mModelListener != null)
                                mModelListener.setData(modelArrayList);
                        }
                    }
                }else {
                    // 无缓存，此时需要请求服务端加载数据
                    requestHistoryMsgData();
                }
            }
        });
    }

    /**
      * 请求服务端数据
      * @author leibing
      * @createTime 2017/5/18
      * @lastModify 2017/5/18
      * @param
      * @return
      */
    private void requestHistoryMsgData() {
        if (mActivityWeakRef == null || mActivityWeakRef.get() == null)
            return;
        HashMap paramMap = OkHttpRequestUtils.JkRequestParameters(
                JKOkHttpParamKey.JK_CHAT_HISTORY_MSG_PARAM,
                "GetNextChatList",
                "1234567890",
                "android",
                "15",
                JK_CHAT_WEBSITE_ID);
        OkHttpRequestUtils.getInstance().requestByGet(
                RequestUrlManager.JK_CHAT_GET_HISTORY_MSG_URL,
                paramMap, String.class,
                mActivityWeakRef.get(),
                new ApiCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        if (response != null) {
                            String body = (String) response;
                            JkChatHistoryMsgResponse mJkChatHistoryMsgResponse
                                    = new Gson().fromJson(body, JkChatHistoryMsgResponse.class);
                            if (mJkChatHistoryMsgResponse != null
                                    && mJkChatHistoryMsgResponse.data != null
                                    && mJkChatHistoryMsgResponse.data.result != null
                                    && mJkChatHistoryMsgResponse.data.result.size() != 0){
                                ArrayList<JkChatBean> mData = new ArrayList<JkChatBean>();
                                for (int i=0;i<mJkChatHistoryMsgResponse.data.result.size();i++){
                                    JkChatHistoryMsgResponse.Data.Result result
                                            = mJkChatHistoryMsgResponse.data.result.get(i);
                                    if (result != null){
                                        if (StringUtil.isNotEmpty(result.ChatType)
                                                && "1".equals(result.ChatType)){
                                            // 用户
                                            if (result.Content != null
                                                    && result.Content.size() != 0){
                                                for (int j=0;j<result.Content.size();j++){
                                                    JkChatHistoryMsgResponse.Data.Result.ContentCls
                                                            content = result.Content.get(j);
                                                    if (content != null){
                                                        JkChatBean mJkChatBean = new JkChatBean();
                                                        // 判断数据是否可以添加到数据源
                                                        boolean isCouldAdd = true;
                                                        mJkChatBean.msgDirection = JkChatConstant.MSG_DIRECTION_SEND;
                                                        mJkChatBean.msgStatus = JkChatConstant.MSG_STATUS_SEND_SUCCESS;
                                                        switch (content.type){
                                                            case "1":
                                                                //文字
                                                                mJkChatBean.msgType = JkChatConstant.MSG_TYPE_TXT;
                                                                JkChatBean.SendTxtMsg mSendTxtMsg
                                                                        = new JkChatBean.SendTxtMsg();
                                                                if (StringUtil.isNotEmpty(result.ChatTime))
                                                                    mSendTxtMsg.timeStr = result.ChatTime;
                                                                else
                                                                    isCouldAdd = false;

                                                                if (StringUtil.isNotEmpty(content.msg))
                                                                    mSendTxtMsg.msgTxt = content.msg;
                                                                else
                                                                    isCouldAdd = false;

                                                                mSendTxtMsg.headPortraitUrl = "";
                                                                mJkChatBean.mSendTxtMsg = mSendTxtMsg;
                                                                break;
                                                            case "2":
                                                                // 图片
                                                                mJkChatBean.msgType = JkChatConstant.MSG_TYPE_IMG;
                                                                JkChatBean.SendImgMsg mSendImgMsg
                                                                        = new JkChatBean.SendImgMsg();
                                                                if (StringUtil.isNotEmpty(result.ChatTime))
                                                                    mSendImgMsg.timeStr = result.ChatTime;
                                                                else
                                                                    isCouldAdd = false;

                                                                if (StringUtil.isNotEmpty(content.msg))
                                                                    mSendImgMsg.msgImgUrl = content.msg;
                                                                else
                                                                    isCouldAdd = false;

                                                                if (content.size != null){
                                                                    mSendImgMsg.width = content.size.width;
                                                                    mSendImgMsg.height = content.size.height;
                                                                }

                                                                mSendImgMsg.headPortraitUrl = "";
                                                                mJkChatBean.mSendImgMsg = mSendImgMsg;
                                                                break;
                                                            case "3":
                                                                // 链接
                                                                mJkChatBean.msgType = JkChatConstant.MSG_TYPE_LINK;
                                                                JkChatBean.SendTxtMsg nSendTxtMsg
                                                                        = new JkChatBean.SendTxtMsg();
                                                                if (StringUtil.isNotEmpty(result.ChatTime))
                                                                    nSendTxtMsg.timeStr = result.ChatTime;
                                                                else
                                                                    isCouldAdd = false;

                                                                if (StringUtil.isNotEmpty(content.msg))
                                                                    nSendTxtMsg.msgTxt = content.msg;
                                                                else
                                                                    isCouldAdd = false;

                                                                nSendTxtMsg.headPortraitUrl = "";
                                                                mJkChatBean.mSendTxtMsg = nSendTxtMsg;
                                                                break;
                                                            default:
                                                                isCouldAdd = false;
                                                                break;
                                                        }
                                                        if (isCouldAdd)
                                                            mData.add(mJkChatBean);
                                                    }
                                                }
                                            }
                                        }else {
                                            // 客服
                                            if (result.Content != null
                                                    && result.Content.size() != 0){
                                                for (int j=0;j<result.Content.size();j++){
                                                    JkChatHistoryMsgResponse.Data.Result.ContentCls
                                                            content = result.Content.get(j);
                                                    if (content != null){
                                                        JkChatBean mJkChatBean = new JkChatBean();
                                                        // 判断数据是否可以添加到数据源
                                                        boolean isCouldAdd = true;
                                                        mJkChatBean.msgDirection = JkChatConstant.MSG_DIRECTION_RECEIVE;
                                                        switch (content.type){
                                                            case "1":
                                                                //文字
                                                                mJkChatBean.msgType = JkChatConstant.MSG_TYPE_TXT;
                                                                JkChatBean.ReceiveTxtMsg mReceiveTxtMsg
                                                                        = new JkChatBean.ReceiveTxtMsg();
                                                                if (StringUtil.isNotEmpty(result.ChatTime))
                                                                    mReceiveTxtMsg.timeStr = result.ChatTime;
                                                                else
                                                                    isCouldAdd = false;

                                                                if (StringUtil.isNotEmpty(content.msg))
                                                                    mReceiveTxtMsg.msgTxt = content.msg;
                                                                else
                                                                    isCouldAdd = false;

                                                                mReceiveTxtMsg.headPortraitUrl = "";
                                                                mJkChatBean.mReceiveTxtMsg = mReceiveTxtMsg;
                                                                break;
                                                            case "2":
                                                                // 图片
                                                                mJkChatBean.msgType = JkChatConstant.MSG_TYPE_IMG;
                                                                JkChatBean.ReceiveImgMsg mReceiveImgMsg
                                                                        = new JkChatBean.ReceiveImgMsg();
                                                                if (StringUtil.isNotEmpty(result.ChatTime))
                                                                    mReceiveImgMsg.timeStr = result.ChatTime;
                                                                else
                                                                    isCouldAdd = false;

                                                                if (StringUtil.isNotEmpty(content.msg))
                                                                    mReceiveImgMsg.msgImgUrl = content.msg;
                                                                else
                                                                    isCouldAdd = false;

                                                                if (content.size != null){
                                                                    mReceiveImgMsg.width = content.size.width;
                                                                    mReceiveImgMsg.height = content.size.height;
                                                                }

                                                                mReceiveImgMsg.headPortraitUrl = "";
                                                                mJkChatBean.mReceiveImgMsg = mReceiveImgMsg;
                                                                break;
                                                            case "3":
                                                                // 链接
                                                                mJkChatBean.msgType = JkChatConstant.MSG_TYPE_LINK;
                                                                JkChatBean.ReceiveTxtMsg nReceiveTxtMsg
                                                                        = new JkChatBean.ReceiveTxtMsg();
                                                                if (StringUtil.isNotEmpty(result.ChatTime))
                                                                    nReceiveTxtMsg.timeStr = result.ChatTime;
                                                                else
                                                                    isCouldAdd = false;

                                                                if (StringUtil.isNotEmpty(content.msg))
                                                                    nReceiveTxtMsg.msgTxt = content.msg;
                                                                else
                                                                    isCouldAdd = false;

                                                                nReceiveTxtMsg.headPortraitUrl = "";
                                                                mJkChatBean.mReceiveTxtMsg = nReceiveTxtMsg;
                                                                break;
                                                            default:
                                                                isCouldAdd = false;
                                                                break;
                                                        }
                                                        if (isCouldAdd)
                                                            mData.add(mJkChatBean);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                if (mModelListener != null
                                        && mData != null
                                        && mData.size() != 0)
                                    mModelListener.setData(mData);
                            }
                        }
                    }

                    @Override
                    public void onError(String err_msg) {

                    }

                    @Override
                    public void onFailure() {

                    }
                });
    }

    /**
      * 提交评价
      * @author leibing
      * @createTime 2017/5/16
      * @lastModify 2017/5/16
      * @param serviceLevel 服务评价星级
     *  @param replyLevel 回复速度星级
     *  @param content 评级内容
     *  @param mJkChatMsgBean 聊天消息
      * @return
      */
    public void commitPraise(int serviceLevel, int replyLevel,
                             String content, JkChatMsgBean mJkChatMsgBean){
        if (mActivityWeakRef == null || mActivityWeakRef.get() == null)
            return;
        if (mJkChatMsgBean == null)
            return;
        HashMap paramMap = OkHttpRequestUtils.JkRequestParameters(
                JKOkHttpParamKey.JK_CHAT_COMMIT_PRAISE_PARAM,
                "Appraise",
                "",
                serviceLevel + "",
                replyLevel + "",
                content,
                mJkChatMsgBean.StaffName,
                mJkChatMsgBean.StaffSessionID,
                mJkChatMsgBean.WebSiteId,
                "1",
                "1234567890",
                "john",
                "m.jianke.com",
                "android"
                );
        OkHttpRequestUtils.getInstance().requestByGet(
                RequestUrlManager.JK_CHAT_COMMIT_PRAISE_URL,
                paramMap, String.class,
                mActivityWeakRef.get(),
                new ApiCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        if (response != null){
                            String body = (String) response;
                            JkChatPraiseResponse mJkChatPraiseResponse
                                    = new Gson().fromJson(body, JkChatPraiseResponse.class);
                            if (mJkChatPraiseResponse != null
                                    && mJkChatPraiseResponse.data != null
                                    && "1".equals(mJkChatPraiseResponse.data.result)){
                                // 评价成功
                                if (mModelListener != null)
                                    mModelListener.jkChatPraise(true);
                            }else {
                                // 评价失败
                                if (mModelListener != null)
                                    mModelListener.jkChatPraise(false);
                            }
                        }
                    }

                    @Override
                    public void onError(String err_msg) {
                        // 评价失败
                        if (mModelListener != null)
                            mModelListener.jkChatPraise(false);
                    }

                    @Override
                    public void onFailure() {
                        // 评价失败
                        if (mModelListener != null)
                            mModelListener.jkChatPraise(false);
                    }
                }
        );
    }
    
    /**
     * @interfaceName: ModelListener
     * @interfaceDescription: jk chat data listener
     * @author: leibing
     * @createTime: 2017/5/4
     */
    public interface ModelListener extends JkChatCommonInterface{
    }
}
