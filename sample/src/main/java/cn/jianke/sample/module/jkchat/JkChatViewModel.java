package cn.jianke.sample.module.jkchat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import cn.jianke.httprequest.httprequest.okhttp.websocket.JkWsManagerImpl;
import cn.jianke.httprequest.httprequest.okhttp.websocket.JkWsStatusListener;
import cn.jianke.httprequest.utils.StringUtil;
import cn.jianke.sample.R;
import cn.jianke.sample.httprequest.httpresponse.JkChatUploadPicResponse;
import cn.jianke.sample.httprequest.okhttp.JkOkHttpUpDownFileUtils;
import cn.jianke.sample.httprequest.okhttp.RequestUrlManager;
import cn.jianke.sample.httprequest.okhttp.UpLoadFileCallBack;
import cn.jianke.sample.module.ImageLoader;
import cn.jianke.sample.module.ShareApplication;
import cn.jianke.sample.module.ThreadManager;
import cn.jianke.sample.module.jkchat.bean.JkChatBean;
import cn.jianke.sample.module.jkchat.bean.JkChatMsg;
import cn.jianke.sample.module.jkchat.bean.JkChatMsgBean;
import cn.jianke.sample.module.jkchat.util.FileUtils;
import cn.jianke.sample.module.jkchat.util.JsonUtil;
import okhttp3.Response;
import okio.ByteString;
import static cn.jianke.sample.module.jkchat.JkChatConstant.MSG_DIRECTION_RECEIVE;
import static cn.jianke.sample.module.jkchat.JkChatConstant.MSG_TYPE_IMG;
import static cn.jianke.sample.module.jkchat.JkChatConstant.MSG_TYPE_LINK;
import static cn.jianke.sample.module.jkchat.JkChatConstant.MSG_TYPE_TXT;

/**
 * @className: JkChatViewModel
 * @classDescription: 健客商务通（逻辑层）
 * @author: leibing
 * @createTime: 2017/5/4
 */
public class JkChatViewModel {
    // activity weak refer
    private WeakReference<JkChatActivity> mActivityWeakRef;
    // jk chat logical processing listener
    private ViewModelListener mViewModelListener;
    // jk chat data instance
    private JkChatModel mJkChatModel;
    // jk websocket manager implement
    private JkWsManagerImpl mJkWsManagerImpl;
    // jk chat msg model
    private JkChatMsgBean mJkChatMsgBean;
    // ui thread handler
    private Handler uiHandler = new Handler(Looper.getMainLooper());
    // jk chat data listener
    private JkChatModel.ModelListener mModelListener = new JkChatModel.ModelListener() {
        @Override
        public void updateUI(Object object) {
            if (mViewModelListener != null)
                mViewModelListener.updateUI(object);
        }

        @Override
        public void jkChatPraise(boolean isSuccess) {
            if (mViewModelListener != null)
                mViewModelListener.jkChatPraise(isSuccess);
        }

        @Override
        public void setData(ArrayList data) {
            if (mViewModelListener != null)
                mViewModelListener.setData(data);
        }
    };

    /**
     * Constructor
     * @author leibing
     * @createTime 2017/5/4
     * @lastModify 2017/5/4
     * @param mJkChatActivity activity refer
     * @param mViewModelListener jk chat logical processing listener
     * @return
     */
    public JkChatViewModel(JkChatActivity mJkChatActivity, ViewModelListener mViewModelListener){
        // init activity weak refer instance
        this.mActivityWeakRef = new WeakReference<JkChatActivity>(mJkChatActivity);
        // init jk chat logical processing listener
        this.mViewModelListener = mViewModelListener;
        // init jk chat data
        initJkChatModel(mJkChatActivity);
    }

    /**
     * init jk chat data
     * @author leibing
     * @createTime 2017/5/4
     * @lastModify 2017/5/4
     * @param mJkChatActivity
     * @return
     */
    private void initJkChatModel(JkChatActivity mJkChatActivity) {
        // init jk chat data instance
        mJkChatModel = new JkChatModel(mJkChatActivity, mModelListener);
        // init jk websocket manager
        initJkWsManager();
    }

    /**
     * init jk websocket manager
     * @author leibing
     * @createTime 2017/5/8
     * @lastModify 2017/5/8
     * @param
     * @return
     */
    private void initJkWsManager(){
        if (mActivityWeakRef != null
                && mActivityWeakRef.get() != null) {
            // new JkWsManagerImpl instance
            mJkWsManagerImpl = new JkWsManagerImpl.Builder(mActivityWeakRef.get())
                    .setWsUrl(JkChatConstant.WS_URL)
                    .build();
            // start connect websocket
            startConnect();
            // set status listener
            mJkWsManagerImpl.setJkWsStatusListener(new JkWsStatusListener() {
                @Override
                public void onOpen(Response response) {
                    // 回调连接上状态
                    if (mViewModelListener != null)
                        mViewModelListener.connect();
                }

                @Override
                public void onMessage(String text) {
                    // 接收消息
                    updateReceiveMsgUi(text);
                }

                @Override
                public void onMessage(ByteString bytes) {

                }

                @Override
                public void onReconnect() {

                }

                @Override
                public void onClosing(int code, String reason) {

                }

                @Override
                public void onClosed(int code, String reason) {
                    if (mViewModelListener != null) {
                        // 回调关闭状态
                        mViewModelListener.closed();
                    }
                }

                @Override
                public void onFailure(Throwable t, Response response) {
                    if (mViewModelListener != null) {
                        // 回调断开连接状态
                        mViewModelListener.disConnect();
                    }
                }

                @Override
                public void onNoNetWork() {
                    if (mViewModelListener != null) {
                        // 回调无网状态
                        mViewModelListener.noNetWork();
                    }
                }
            });
        }
    }

    /**
     *  提交评价
     * @author leibing
     * @createTime 2017/5/16
     * @lastModify 2017/5/16
     * @param serviceLevel 服务评价星级
     * @param replyLevel 回复速度星级
     * @param content 评价内容
     * @return
     */
    public void commitPraise(int serviceLevel, int replyLevel, String content){
        if (mJkChatModel != null)
            mJkChatModel.commitPraise(serviceLevel, replyLevel, content, mJkChatMsgBean);
    }

    /**
     * 预发图片消息(先将图片消息发出去再上传图片再回调更新图片发送状态)
     * @author leibing
     * @createTime 2017/5/17
     * @lastModify 2017/5/17
     * @param filePath
     * @param mListener
     * @return
     */
    public void preSendImgMsg(final String filePath, final CallBackListener mListener){
        ImageLoader.getInstance().getImgWidthHeight(ShareApplication.getInstance(),
                ImageLoader.getSDSource(filePath), new ImageLoader.ImageLoaderCallBack() {
                    @Override
                    public void getImgWidthHeight(int width, int height) {
                        // 发送图片消息
                        JkChatBean mJkChatBean = updateSendImgMsgUi(filePath, width,
                                height, JkChatConstant.MSG_STATUS_SENDING);
                        if (mJkChatBean != null
                                && mListener != null){
                            mListener.dataCallBack(mJkChatBean);
                        }
                    }
                });
    }

    /**
     * 重发图片消息
     * @author leibing
     * @createTime 2017/5/17
     * @lastModify 2017/5/17
     * @param msgCount
     * @param mJkChatBean
     * @return
     */
    public void retrySendImgMsg(final int msgCount, final JkChatBean mJkChatBean){
        if (mJkChatBean == null)
            return;
        if (mViewModelListener != null){
            // 更改状态为发送中
            mViewModelListener.updatePanelView(msgCount,
                    mJkChatBean, JkChatConstant.MSG_STATUS_SENDING);
            // 上传图片
            upLoadPic(msgCount, mJkChatBean);
        }
    }

    /**
     * 上传图片
     * @author leibing
     * @createTime 2017/5/16
     * @lastModify 2017/5/16
     * @param msgCount 消息数量
     * @param mJkChatBean
     * @return
     */
    public void upLoadPic(final int msgCount, final JkChatBean mJkChatBean){
        if (mJkChatBean == null)
            return;
        final String filePath = mJkChatBean.mSendImgMsg.msgImgUrl;
        if (StringUtil.isEmpty(filePath))
            return;
        ThreadManager.getInstance().getNewCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                int sampleSize = 2;
                File file = null;
                boolean isBreak = true;
                while (isBreak){
                    Bitmap bitmap = FileUtils.getSmallBitmap(filePath, sampleSize);
                    file = FileUtils.compressImage(bitmap);
                    String fileSize = FileUtils.getFileSize(file);
                    if (StringUtil.isEmpty(fileSize)
                            || "0BT".equals(fileSize)){
                        file = null;
                        isBreak = false;
                    }else if (fileSize.contains("KB")){
                        String size = fileSize.replace("KB", "").trim();
                        if (Double.parseDouble(size) < 200){
                            isBreak = false;
                        }else {
                            sampleSize ++;
                        }
                    }else {
                        sampleSize ++;
                    }
                }
                if (file == null)
                    return;
                if (uiHandler != null){
                    final HashMap fileMap = new HashMap();
                    fileMap.put(file.getName(), file);
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            JkOkHttpUpDownFileUtils.getInstance().upLoadFile(RequestUrlManager.JK_CHAT_UPLOAD_IMG_URL,
                                    fileMap,JkOkHttpUpDownFileUtils.MEDIA_TYPE_PNG, new UpLoadFileCallBack() {
                                        @Override
                                        public void onSuccess(String data) {
                                            // 解析上传图片成功数据
                                            ResolveUploadData(data, msgCount, mJkChatBean);
                                        }

                                        @Override
                                        public void onFail(String error) {
                                            if (mViewModelListener != null)
                                                mViewModelListener.updatePanelView(msgCount,
                                                        mJkChatBean, JkChatConstant.MSG_STATUS_SEND_FAIL);
                                        }

                                        @Override
                                        public void onException(Exception e) {
                                            if (mViewModelListener != null)
                                                mViewModelListener.updatePanelView(msgCount,
                                                        mJkChatBean, JkChatConstant.MSG_STATUS_SEND_FAIL);
                                        }

                                        @Override
                                        public void onProgress(long total, long current) {
                                        }
                                    });
                        }
                    });
                }
            }
        });
    }

    /**
     * 解析上传图片成功数据
     * @author leibing
     * @createTime 2017/5/16
     * @lastModify 2017/5/16
     * @param data
     * @param msgCount 消息数量
     * @param mJkChatBean
     * @return
     */
    private void ResolveUploadData(String data, int msgCount,JkChatBean mJkChatBean) {
        try {
            JSONObject mJson = new JSONObject(data);
            String status = mJson.optString("Success");
            String datas = mJson.optString("Data");
            if ("true".equals(status)
                    && StringUtil.isNotEmpty(datas)){
                JkChatUploadPicResponse mJkChatUploadPicResponse
                        = new Gson().fromJson(datas, JkChatUploadPicResponse.class);
                if (StringUtil.isNotEmpty(mJkChatUploadPicResponse.msg)){
                    String imgUrl = RequestUrlManager.JK_CHAT_HOST_URL
                            + mJkChatUploadPicResponse.msg;
                    // 发送图片消息
                    boolean isSendSuccess = sendImgMsg(imgUrl,
                            mJkChatUploadPicResponse.width, mJkChatUploadPicResponse.height);
                    if (isSendSuccess){
                        if (mViewModelListener != null)
                            mViewModelListener.updatePanelView(msgCount, mJkChatBean,
                                    JkChatConstant.MSG_STATUS_SEND_SUCCESS);
                    }else {
                        if (mViewModelListener != null)
                            mViewModelListener.updatePanelView(msgCount, mJkChatBean,
                                    JkChatConstant.MSG_STATUS_SEND_FAIL);
                    }
                }
            }else {
                if (mViewModelListener != null)
                    mViewModelListener.updatePanelView(msgCount, mJkChatBean,
                            JkChatConstant.MSG_STATUS_SEND_FAIL);
            }
        } catch (JSONException e) {
            if (mViewModelListener != null)
                mViewModelListener.updatePanelView(msgCount, mJkChatBean,
                        JkChatConstant.MSG_STATUS_SEND_FAIL);
            e.printStackTrace();
        }
    }

    /**
     * 跳转到网页
     * @author leibing
     * @createTime 2017/5/23
     * @lastModify 2017/5/23
     * @param url 链接
     * @return
     */
    public void turnToHtmlPage(String url){
        try {
            if (mActivityWeakRef == null || mActivityWeakRef.get() == null)
                return;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            mActivityWeakRef.get().startActivity(intent);
        }catch (Exception ex){
        }
    }

    /**
     * 更新接收数据ui
     * @author leibing
     * @createTime 2017/5/15
     * @lastModify 2017/5/15
     * @param
     * @return
     */
    private void updateReceiveMsgUi(String text) {
        if (StringUtil.isNotEmpty(text)) {
            // 转json obj
            mJkChatMsgBean = JsonUtil.fromJson(text, JkChatMsgBean.class);
            if (mJkChatMsgBean == null)
                return;
            // 回调连接上时医生名称
            if (StringUtil.isNotEmpty(mJkChatMsgBean.StaffName)
                    && mViewModelListener != null){
                mViewModelListener.connected(mJkChatMsgBean.StaffName);
            }
            if (StringUtil.isNotEmpty(mJkChatMsgBean.Msg)) {
                JkChatMsg.receiveMsg(mJkChatMsgBean.Msg, new JkChatMsg.JkChatMsgListener() {
                    @Override
                    public void msgCallBack(String msg, int msgType, int width, int height) {
                        JkChatBean mJkChatBean = new JkChatBean();
                        mJkChatBean.msgType = msgType;
                        mJkChatBean.msgDirection = MSG_DIRECTION_RECEIVE;
                        // 构建消息实体
                        switch (msgType){
                            case MSG_TYPE_TXT:
                            case MSG_TYPE_LINK:
                                // 文本消息
                                // 链接消息
                                JkChatBean.ReceiveTxtMsg mReceiveTxtMsg
                                        = new JkChatBean.ReceiveTxtMsg();
                                mReceiveTxtMsg.headPortraitUrl = ImageLoader.getDrawableSource(
                                        ShareApplication.getInstance(), R.drawable.doctor_head_portrait);
                                mReceiveTxtMsg.msgTxt = msg;
                                mReceiveTxtMsg.timeStr = JkChatMsg.getCurrentDate();
                                mJkChatBean.mReceiveTxtMsg = mReceiveTxtMsg;
                                break;
                            case MSG_TYPE_IMG:
                                // 图片消息
                                JkChatBean.ReceiveImgMsg mReceiveImgMsg
                                        = new JkChatBean.ReceiveImgMsg();
                                mReceiveImgMsg.headPortraitUrl = ImageLoader.getDrawableSource(
                                        ShareApplication.getInstance(), R.drawable.doctor_head_portrait);
                                mReceiveImgMsg.msgImgUrl = msg;
                                mReceiveImgMsg.width = width;
                                mReceiveImgMsg.height = height;
                                mReceiveImgMsg.timeStr = JkChatMsg.getCurrentDate();
                                mJkChatBean.mReceiveImgMsg = mReceiveImgMsg;
                                break;
                            default:
                                break;
                        }
                        // 回调更新ui
                        if (mViewModelListener != null)
                            mViewModelListener.updateUI(mJkChatBean);
                    }
                });
            }
        }
    }

    /**
     * 药品消息
     * @author leibing
     * @createTime 2017/5/15
     * @lastModify 2017/5/15
     * @param mDrug
     * @return
     */
    public void showDrugMsg(JkChatBean.Drug mDrug){
        if (mDrug == null)
            return;
        JkChatBean mJkChatBean = new JkChatBean();
        mJkChatBean.msgType = JkChatConstant.MSG_TYPE_DRUG;
        mJkChatBean.msgDirection = JkChatConstant.MSG_DIRECTION_SEND;
        mJkChatBean.mDrug = mDrug;
        // 回调更新ui
        if (mViewModelListener != null)
            mViewModelListener.updateUI(mJkChatBean);
    }

    /**
     * 发送药品消息
     * @author leibing
     * @createTime 2017/5/15
     * @lastModify 2017/5/15
     * @param urlLink
     * @param msgCount
     * @param mJkChatBean
     * @return
     */
    public void sendDrugMsg(String urlLink, int msgCount, JkChatBean mJkChatBean){
        // 发送药品信息实际是给商务通发送链接消息
        sendTxtMsg(urlLink,msgCount, mJkChatBean,true,true);
    }

    /**
     * 重发药品消息
     * @author leibing
     * @createTime 2017/5/17
     * @lastModify 2017/5/17
     * @param msgCount
     * @param mJkChatBean
     * @return
     */
    public void retrySendDrugMsg(final int msgCount, final JkChatBean mJkChatBean){
        if (mJkChatBean == null)
            return;
        if (mViewModelListener != null){
            // 更改状态为发送中
            mViewModelListener.updatePanelView(msgCount,
                    mJkChatBean, JkChatConstant.MSG_STATUS_SENDING);
            // 发送文本消息
            sendTxtMsg(mJkChatBean.mSendDrugMsg.drugLink, msgCount, mJkChatBean, true,true);
        }
    }

    /**
     * 更新发送药品消息ui
     * @author leibing
     * @createTime 2017/5/17
     * @lastModify 2017/5/17
     * @param mSendDrugMsg
     * @param status
     * @return
     */
    public JkChatBean updatesendDrugMsgUi(JkChatBean.SendDrugMsg mSendDrugMsg, int status){
        if (mSendDrugMsg == null)
            return null;
        JkChatBean mJkChatBean = new JkChatBean();
        mJkChatBean.msgType = JkChatConstant.MSG_TYPE_SEND_DRUG;
        mJkChatBean.msgDirection = JkChatConstant.MSG_DIRECTION_SEND;
        mJkChatBean.msgStatus = status;
        mSendDrugMsg.timeStr = JkChatMsg.getCurrentDate();
        mSendDrugMsg.headPortraitUrl = "";
        mJkChatBean.mSendDrugMsg = mSendDrugMsg;
        // 回调更新ui
        if (mViewModelListener != null)
            mViewModelListener.updateUI(mJkChatBean);
        return mJkChatBean;
    }

    /**
     * 重发文本消息
     * @author leibing
     * @createTime 2017/5/17
     * @lastModify 2017/5/17
     * @param msgCount
     * @param mJkChatBean
     * @param isLink
     * @param isDrug
     * @return
     */
    public void retrySendTxtMsg(final int msgCount, final JkChatBean mJkChatBean,
                                boolean isLink, boolean isDrug){
        if (mJkChatBean == null)
            return;
        if (mViewModelListener != null){
            // 更改状态为发送中
            mViewModelListener.updatePanelView(msgCount,
                    mJkChatBean, JkChatConstant.MSG_STATUS_SENDING);
            // 发送文本消息
            sendTxtMsg(mJkChatBean.mSendTxtMsg.msgTxt, msgCount, mJkChatBean, isLink, isDrug);
        }
    }

    /**
     * 发送文本消息
     * @author leibing
     * @createTime 2017/5/15
     * @lastModify 2017/5/15
     * @param txt 文本
     * @param msgCount 消息数目
     * @param mJkChatBean
     * @param isLink 是否链接
     * @return
     */
    public void sendTxtMsg(String txt, int msgCount,
                           JkChatBean mJkChatBean, boolean isLink, boolean isDrug){
        String sendMsg = "";
        if (isLink)
            sendMsg = JkChatMsg.sendLinkMsg(mJkChatMsgBean,txt);
        else
            sendMsg = JkChatMsg.sendTxtMsg(mJkChatMsgBean, txt);
        if (StringUtil.isNotEmpty(sendMsg)
                && mJkWsManagerImpl != null) {
            boolean isSendSuccess  = mJkWsManagerImpl.sendMessage(sendMsg);
            if (isSendSuccess){
                // 发送成功
                if (mViewModelListener != null) {
                    mViewModelListener.updatePanelView(msgCount,
                            mJkChatBean, JkChatConstant.MSG_STATUS_SEND_SUCCESS);
                    // 删除药品信息item
                    if (isDrug)
                        mViewModelListener.removeDrugMsgItem();
                }
            }else {
                // 发送失败
                if (mViewModelListener != null) {
                    mViewModelListener.updatePanelView(msgCount,
                            mJkChatBean, JkChatConstant.MSG_STATUS_SEND_FAIL);
                    // 删除药品信息item
                    if (isDrug)
                        mViewModelListener.removeDrugMsgItem();
                }
            }
        }
    }

    /**
     * 更新发送文本消息ui
     * @author leibing
     * @createTime 2017/5/17
     * @lastModify 2017/5/17
     * @param txt 文本
     * @param status 状态
     * @param isLink 是否链接
     * @return
     */
    public JkChatBean updateSendTxtMsgUi(String txt, int status, boolean isLink){
        JkChatBean mJkChatBean = new JkChatBean();
        if (isLink)
            mJkChatBean.msgType = JkChatConstant.MSG_TYPE_LINK;
        else
            mJkChatBean.msgType = JkChatConstant.MSG_TYPE_TXT;
        mJkChatBean.msgDirection = JkChatConstant.MSG_DIRECTION_SEND;
        mJkChatBean.msgStatus = status;
        JkChatBean.SendTxtMsg mSendTxtMsg = new JkChatBean.SendTxtMsg();
        mSendTxtMsg.headPortraitUrl = "";
        mSendTxtMsg.msgTxt = txt;
        mSendTxtMsg.timeStr = JkChatMsg.getCurrentDate();
        mJkChatBean.mSendTxtMsg = mSendTxtMsg;
        // 回调更新ui
        if (mViewModelListener != null)
            mViewModelListener.updateUI(mJkChatBean);
        return mJkChatBean;
    }

    /**
     * 发送图片消息
     * @author leibing
     * @createTime 2017/5/15
     * @lastModify 2017/5/15
     * @param imgUrl 图片链接
     * @param width
     * @param height
     * @return
     */
    public boolean sendImgMsg(String imgUrl, int width, int height){
        String sendMsg = JkChatMsg.sendImgMsg(mJkChatMsgBean,imgUrl, width, height);
        if (StringUtil.isNotEmpty(sendMsg)
                && mJkWsManagerImpl != null) {
            return mJkWsManagerImpl.sendMessage(sendMsg);
        }
        return false;
    }

    /**
     * 更新发送图片消息ui
     * @author leibing
     * @createTime 2017/5/17
     * @lastModify 2017/5/17
     * @param imgUrl
     * @param width
     * @param height
     * @param status
     * @return
     */
    public JkChatBean updateSendImgMsgUi(String imgUrl, int width, int height, int status){
        JkChatBean mJkChatBean = new JkChatBean();
        mJkChatBean.msgType = JkChatConstant.MSG_TYPE_IMG;
        mJkChatBean.msgDirection = JkChatConstant.MSG_DIRECTION_SEND;
        mJkChatBean.msgStatus = status;
        JkChatBean.SendImgMsg mSendImgMsg = new JkChatBean.SendImgMsg();
        mSendImgMsg.headPortraitUrl = "";
        mSendImgMsg.msgImgUrl = imgUrl;
        mSendImgMsg.width = width;
        mSendImgMsg.height = height;
        mSendImgMsg.timeStr = JkChatMsg.getCurrentDate();
        mJkChatBean.mSendImgMsg = mSendImgMsg;
        // 回调更新ui
        if (mViewModelListener != null)
            mViewModelListener.updateUI(mJkChatBean);
        return mJkChatBean;
    }

    /**
     * 读取缓存数据
     * @author leibing
     * @createTime 2017/5/25
     * @lastModify 2017/5/25
     * @param
     * @return
     */
    public void readCacheData(){
        if (mJkChatModel != null)
            mJkChatModel.readCacheData();
    }

    /**
     * 断开websocket连接
     * @author leibing
     * @createTime 2017/5/16
     * @lastModify 2017/5/16
     * @param
     * @return
     */
    public void stopConnect(){
        if (mJkWsManagerImpl != null) {
            mJkWsManagerImpl.stopConnect();
        }
    }

    /**
     * 启动websocket连接
     * @author leibing
     * @createTime 2017/5/16
     * @lastModify 2017/5/16
     * @param
     * @return
     */
    public void startConnect(){
        if (mJkWsManagerImpl != null) {
            mJkWsManagerImpl.startConnect();
        }
    }

    /**
     * @interfaceName: ViewModelListener
     * @interfaceDescription: jk chat logical processing listener
     * @author: leibing
     * @createTime: 2017/5/4
     */
    public interface ViewModelListener extends JkChatCommonInterface{
        // 对话连接上
        void connect();
        // 对话连接上并接收到医生名称
        void connected(String doctorName);
        // 对话关闭
        void closed();
        // 对话断开
        void disConnect();
        // 无网
        void noNetWork();
        // 更新局部view
        void updatePanelView(int count,JkChatBean mJkChatBean, int msgType);
        // 删除药品信息item
        void removeDrugMsgItem();
    }

    /**
     * @interfaceName: CallBackListener
     * @interfaceDescription: 回调监听
     * @author: leibing
     * @createTime: 2017/5/17
     */
    public interface CallBackListener{
        void dataCallBack(Object object);
    }
}
